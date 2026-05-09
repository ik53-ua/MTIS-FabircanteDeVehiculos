package org.mtis.servicionotificaciones;

import conexionDB.ConexionesDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

public class ServicioNotificacionesSkeleton {

	public org.mtis.servicionotificaciones.EnviarConfirmacionResponse enviarConfirmacion(
	        org.mtis.servicionotificaciones.EnviarConfirmacionRequest req) {

	        org.mtis.servicionotificaciones.EnviarConfirmacionResponse respuesta =
	            new org.mtis.servicionotificaciones.EnviarConfirmacionResponse();

	        ConexionesDB db = new ConexionesDB();
	        Connection cn = db.conectar();

	        if (cn == null) {
	            respuesta.setExito(false);
	            respuesta.setMensaje("Error: No se pudo conectar a la base de datos");
	            // NUEVO
	            enviarEmail("sistema@mtis.org", "admin@mtis.org", "Error Crítico: BD", "No se pudo conectar a la BD en Servicio Confirmación.");
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
	                enviarEmail("sistema@mtis.org", "admin@mtis.org", "Alerta de Seguridad", "Intento de acceso con WSKey inválida en Confirmación.");
	                cn.close();
	                return respuesta;
	            }

	            int idConcesionario = req.getIdConcesionario();
	            int idReserva = req.getIdReserva();
	            String mensajeRecibido = req.getMensajeAEnviar();

	            boolean esExito = !mensajeRecibido.toLowerCase().contains("error")
	                           && !mensajeRecibido.toLowerCase().contains("no hay")
	                           && !mensajeRecibido.toLowerCase().contains("no se encontró");

	            // Obtener nombre concesionario para el email
	            String emailDestino = "concesionario@gmail.com";
	            String nombreConcesionario = "Concesionario";
	            PreparedStatement psConc = cn.prepareStatement(
	                "SELECT nombre FROM Concesionario WHERE id_concesionario = ?");
	            psConc.setInt(1, idConcesionario);
	            ResultSet rsConc = psConc.executeQuery();
	            if (rsConc.next()) {
	                nombreConcesionario = rsConc.getString("nombre");
	                emailDestino = nombreConcesionario.toLowerCase().replace(" ", "") + "@gmail.com";
	            } else {
	                // NUEVO
	                enviarEmail("sistema@mtis.org", "admin@mtis.org", "Aviso", "No se encontró el concesionario " + idConcesionario + " en BD para la reserva #" + idReserva);
	            }

	            // Guardar en BD
	            String tipo = esExito ? "RESERVA_CONFIRMADA" : "RESERVA_SIN_STOCK";
	            PreparedStatement psInsert = cn.prepareStatement(
	                "INSERT INTO Notificacion (id_concesionario, tipo, mensaje) VALUES (?, ?, ?)");
	            psInsert.setInt(1, idConcesionario);
	            psInsert.setString(2, tipo);
	            psInsert.setString(3, mensajeRecibido);
	            psInsert.executeUpdate();

	            // Email personalizado
	            String asunto, cuerpo;
	            if (esExito) {
	                asunto = "Reserva #" + idReserva + " confirmada";
	                cuerpo = "Estimado " + nombreConcesionario + ",\n\n"
	                       + "Su reserva ha sido procesada correctamente.\n\n"
	                       + "Detalles:\n"
	                       + "  - ID Reserva: " + idReserva + "\n"
	                       + "  - Estado: CONFIRMADA\n"
	                       + "  - Detalle: " + mensajeRecibido + "\n\n"
	                       + "Gracias por confiar en Fabricante de Vehiculos MTIS.\n"
	                       + "Un saludo.";
	            } else {
	                asunto = "Reserva #" + idReserva + " - Sin stock disponible";
	                cuerpo = "Estimado " + nombreConcesionario + ",\n\n"
	                       + "Lamentamos informarle que no ha sido posible completar su reserva.\n\n"
	                       + "Detalles:\n"
	                       + "  - ID Reserva: " + idReserva + "\n"
	                       + "  - Estado: SIN STOCK\n"
	                       + "  - Motivo: " + mensajeRecibido + "\n\n"
	                       + "Contacte con nosotros para buscar alternativas.\n"
	                       + "Un saludo.";
	            }

	            enviarEmail("fabricante@mtis.org", emailDestino, asunto, cuerpo);
	            cn.close();

	            respuesta.setExito(true);
	            respuesta.setMensaje("Notificacion enviada correctamente a " + emailDestino);

	        } catch (Exception e) {
	            respuesta.setExito(false);
	            respuesta.setMensaje("Error: " + e.getMessage());
	            // NUEVO
	            enviarEmail("sistema@mtis.org", "admin@mtis.org", "Excepción en Servicio", "Error en Confirmación de Reserva: " + e.getMessage());
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