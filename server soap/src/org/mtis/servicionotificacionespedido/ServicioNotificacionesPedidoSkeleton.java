package org.mtis.servicionotificacionespedido;

import conexionDB.ConexionesDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

public class ServicioNotificacionesPedidoSkeleton {

	public org.mtis.servicionotificacionespedido.EnviarNotificacionPedidoResponse enviarNotificacionPedido(
	        org.mtis.servicionotificacionespedido.EnviarNotificacionPedidoRequest req) {

	        org.mtis.servicionotificacionespedido.EnviarNotificacionPedidoResponse respuesta =
	            new org.mtis.servicionotificacionespedido.EnviarNotificacionPedidoResponse();

	        ConexionesDB db = new ConexionesDB();
	        Connection cn = db.conectar();

	        if (cn == null) {
	            respuesta.setExito(false);
	            respuesta.setMensaje("Error: No se pudo conectar a la base de datos");
	            // NUEVO
	            enviarEmail("sistema@mtis.org", "admin@mtis.org", "Error Crítico: BD", "No se pudo conectar a la BD en Notificaciones de Pedido.");
	            return respuesta;
	        }

	        try {
	            // Validar WSKey
	            PreparedStatement psKey = cn.prepareStatement(
	                "SELECT COUNT(*) FROM SoapKey WHERE soap_key = ?");
	            psKey.setString(1, req.getWSKey());
	            ResultSet rsKey = psKey.executeQuery();
	            rsKey.next();
	            if (rsKey.getInt(1) == 0) {
	                respuesta.setExito(false);
	                respuesta.setMensaje("Error: WSKey inválida");
	                // NUEVO
	                enviarEmail("sistema@mtis.org", "admin@mtis.org", "Alerta de Seguridad", "Intento de acceso con WSKey inválida en Notificaciones de Pedido.");
	                cn.close();
	                return respuesta;
	            }

	            int idConcesionario  = req.getIdConcesionario();
	            int idPedido         = req.getIdPedido();
	            String mensajeRecibido = req.getMensajeAEnviar();

	            boolean esExito = !mensajeRecibido.toLowerCase().contains("error")
	                           && !mensajeRecibido.toLowerCase().contains("cancelado")
	                           && !mensajeRecibido.toLowerCase().contains("no se pudo");

	            // Obtener nombre del concesionario
	            String emailDestino        = "concesionario@gmail.com";
	            String nombreConcesionario = "Concesionario";
	            PreparedStatement psConc = cn.prepareStatement(
	                "SELECT nombre FROM Concesionario WHERE id_concesionario = ?");
	            psConc.setInt(1, idConcesionario);
	            ResultSet rsConc = psConc.executeQuery();
	            if (rsConc.next()) {
	                nombreConcesionario = rsConc.getString("nombre");
	                emailDestino = nombreConcesionario.toLowerCase().replace(" ", "") + "@gmail.com";
	            } else {
	                // NUEVO: Manejo si el concesionario no existe
	                enviarEmail("sistema@mtis.org", "admin@mtis.org", "Aviso", "No se encontró el concesionario " + idConcesionario + ". Se usará el email por defecto.");
	            }

	            // Obtener detalles del pedido
	            String fechaEntrega    = "pendiente de calcular";
	            String costeProduccion = "pendiente de calcular";
	            String configuracion   = "";
	            PreparedStatement psPedido = cn.prepareStatement(
	                "SELECT pf.configuracion_detalles, pf.coste_produccion, pf.fecha_entrega_estimada, " +
	                "mv.nombre AS nombre_modelo " +
	                "FROM PedidoFabricacion pf " +
	                "JOIN ModeloVehiculo mv ON pf.id_modelo = mv.id_modelo " +
	                "WHERE pf.id_pedido = ? AND pf.id_concesionario = ?");
	            psPedido.setInt(1, idPedido);
	            psPedido.setInt(2, idConcesionario);
	            ResultSet rsPedido = psPedido.executeQuery();
	            if (rsPedido.next()) {
	                configuracion   = rsPedido.getString("configuracion_detalles");
	                costeProduccion = rsPedido.getObject("coste_produccion") != null
	                                ? rsPedido.getDouble("coste_produccion") + " €"
	                                : "pendiente de calcular";
	                fechaEntrega    = rsPedido.getObject("fecha_entrega_estimada") != null
	                                ? rsPedido.getString("fecha_entrega_estimada")
	                                : "pendiente de calcular";
	                configuracion   = configuracion != null ? configuracion : "no especificada";
	            }

	            // Guardar en BD
	            String tipo = esExito ? "PEDIDO_FABRICACION" : "PEDIDO_CANCELADO";
	            PreparedStatement psInsert = cn.prepareStatement(
	                "INSERT INTO Notificacion (id_concesionario, tipo, mensaje) VALUES (?, ?, ?)");
	            psInsert.setInt(1, idConcesionario);
	            psInsert.setString(2, tipo);
	            psInsert.setString(3, mensajeRecibido);
	            psInsert.executeUpdate();

	            // Construir email
	            String asunto, cuerpo;
	            if (esExito) {
	                asunto = "Pedido de fabricacion #" + idPedido + " aceptado";
	                cuerpo = "Estimado " + nombreConcesionario + ",\n\n"
	                       + "Su pedido de fabricacion ha sido aceptado y esta en proceso.\n\n"
	                       + "Detalles del pedido:\n"
	                       + "   - ID Pedido:          " + idPedido + "\n"
	                       + "   - Configuracion:      " + configuracion + "\n"
	                       + "   - Coste produccion:   " + costeProduccion + "\n"
	                       + "   - Fecha entrega est.: " + fechaEntrega + "\n"
	                       + "   - Estado:             EN PRODUCCION\n\n"
	                       + "Le notificaremos cuando el vehiculo este listo para entrega.\n"
	                       + "Un saludo,\nFabricante de Vehiculos MTIS";
	            } else {
	                asunto = "Pedido de fabricacion #" + idPedido + " cancelado";
	                cuerpo = "Estimado " + nombreConcesionario + ",\n\n"
	                       + "Lamentamos informarle que su pedido de fabricacion no ha podido procesarse.\n\n"
	                       + "Detalles del pedido:\n"
	                       + "   - ID Pedido:    " + idPedido + "\n"
	                       + "   - Configuracion:" + configuracion + "\n"
	                       + "   - Estado:       CANCELADO\n"
	                       + "   - Motivo:       " + mensajeRecibido + "\n\n"
	                       + "Por favor, contacte con nosotros para mas informacion.\n"
	                       + "Un saludo,\nFabricante de Vehiculos MTIS";
	            }

	            enviarEmail("fabricante@mtis.org", emailDestino, asunto, cuerpo);

	            cn.close();
	            respuesta.setExito(true);
	            respuesta.setMensaje("Notificacion de pedido enviada a " + emailDestino);

	        } catch (Exception e) {
	            respuesta.setExito(false);
	            respuesta.setMensaje("Error: " + e.getMessage());
	            // NUEVO
	            enviarEmail("sistema@mtis.org", "admin@mtis.org", "Excepción en Servicio", "Error en Notificaciones de Pedido: " + e.getMessage());
	            e.printStackTrace();
	        }

	        return respuesta;
	    }
	private void enviarEmail(String from, String to, String asunto, String cuerpo) {
        try {
            Properties props = new Properties();
            props.put("mail.smtp.host", "localhost");
            props.put("mail.smtp.port", "25");
            props.put("mail.smtp.auth", "false");
            
            // NUEVO: Evita que se quede colgado si FakeSMTP no responde
            props.put("mail.smtp.connectiontimeout", "5000"); 
            props.put("mail.smtp.timeout", "5000");

            Session session = Session.getInstance(props);
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(from));
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            msg.setSubject(asunto);
            msg.setText(cuerpo);
            Transport.send(msg);
            System.out.println("[EMAIL] Enviado a: " + to + " | Asunto: " + asunto);
        } catch (Exception e) {
            System.err.println("[EMAIL ERROR] No se pudo enviar: " + e.getMessage());
        }
    }
	
}