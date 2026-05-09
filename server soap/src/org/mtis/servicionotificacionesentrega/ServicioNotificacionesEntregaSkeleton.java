package org.mtis.servicionotificacionesentrega;

import conexionDB.ConexionesDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

public class ServicioNotificacionesEntregaSkeleton {

	public org.mtis.servicionotificacionesentrega.EnviarNotificacionEntregaResponse enviarNotificacionEntrega(
            org.mtis.servicionotificacionesentrega.EnviarNotificacionEntregaRequest req) {

        org.mtis.servicionotificacionesentrega.EnviarNotificacionEntregaResponse respuesta = 
            new org.mtis.servicionotificacionesentrega.EnviarNotificacionEntregaResponse();

        ConexionesDB db = new ConexionesDB();
        Connection cn = db.conectar();

        if (cn == null) {
            respuesta.setExito(false);
            respuesta.setMensaje("Error: No se pudo conectar a la base de datos");
            // NUEVO: Alerta por fallo de Base de Datos
            enviarEmail("sistema@mtis.org", "admin@mtis.org", "Error Crítico: BD", "No se pudo conectar a la base de datos en Notificaciones de Entrega.");
            return respuesta;
        }

        try {
            // 1. Validar WSKey
            PreparedStatement psKey = cn.prepareStatement("SELECT COUNT(*) FROM SoapKey WHERE soap_key = ?");
            psKey.setString(1, req.getWSKey());
            ResultSet rsKey = psKey.executeQuery();
            rsKey.next();
            if (rsKey.getInt(1) == 0) {
                respuesta.setExito(false);
                respuesta.setMensaje("Error: WSKey inválida");
                // NUEVO: Alerta por WSKey inválida
                enviarEmail("sistema@mtis.org", "admin@mtis.org", "Alerta de Seguridad", "Intento de acceso con WSKey inválida en Notificaciones de Entrega.");
                cn.close();
                return respuesta;
            }

            int idConcesionario = req.getIdConcesionario();
            int idEnvio = req.getIdEnvio();
            
            // Recoger el texto (fecha o error)
            String mensajeRecibido = req.getFechaRecepcionEstimada(); 
            boolean esExito = !mensajeRecibido.toLowerCase().contains("error")
                           && !mensajeRecibido.toLowerCase().contains("cancelado")
                           && !mensajeRecibido.toLowerCase().contains("aviso");

            // 2. Comprobar si el concesionario existe y obtener sus datos
            String emailDestino = "";
            String nombreConcesionario = "";
            
            PreparedStatement psConc = cn.prepareStatement("SELECT nombre FROM Concesionario WHERE id_concesionario = ?");
            psConc.setInt(1, idConcesionario);
            ResultSet rsConc = psConc.executeQuery();
            
            if (rsConc.next()) {
                nombreConcesionario = rsConc.getString("nombre");
                emailDestino = nombreConcesionario.toLowerCase().replace(" ", "") + "@gmail.com";
            } else {
                respuesta.setExito(false);
                respuesta.setMensaje("Error: El concesionario indicado (" + idConcesionario + ") no existe en la base de datos.");
                // NUEVO: Alerta por concesionario inexistente en lugar de solo cortar
                enviarEmail("sistema@mtis.org", "admin@mtis.org", "Error de Lógica", "Petición rechazada: El concesionario (" + idConcesionario + ") no existe en la base de datos.");
                cn.close();
                return respuesta; 
            }

            // 3. Registrar en la tabla Notificacion de la BD
            String tipo = esExito ? "REPUESTOS_ENVIADOS" : "OTRO";
            PreparedStatement psInsert = cn.prepareStatement(
                "INSERT INTO Notificacion (id_concesionario, tipo, mensaje) VALUES (?, ?, ?)");
            
            psInsert.setInt(1, idConcesionario);
            psInsert.setString(2, tipo);
            psInsert.setString(3, mensajeRecibido);
            psInsert.executeUpdate();

            // 4. Enviar Email vía FakeSMTP (Flujo normal)
            String asunto, cuerpo;
            if (esExito) {
                asunto = "Notificación de Envío de Repuestos - Pedido #" + idEnvio;
                cuerpo = "Estimado " + nombreConcesionario + ",\n\n"
                       + "Le informamos que los repuestos solicitados para su taller han sido procesados.\n"
                       + "Detalles del envío:\n"
                       + " - ID Envío: " + idEnvio + "\n"
                       + " - Fecha estimada de recepción: " + mensajeRecibido + "\n\n"
                       + "Por favor, prepare el espacio en su almacén para la descarga.\n"
                       + "Un saludo,\nDepartamento de Logística MTIS";
            } else {
                asunto = "Aviso: Problema con la solicitud de repuestos";
                cuerpo = "Estimado " + nombreConcesionario + ",\n\n"
                       + "Ha ocurrido un inconveniente al procesar la solicitud de repuestos.\n\n"
                       + "Detalles:\n"
                       + " - Motivo: " + mensajeRecibido + "\n\n"
                       + "Por favor, revise el stock local o los datos introducidos.\n"
                       + "Un saludo,\nSistema de Notificaciones Automáticas MTIS";
            }

            enviarEmail("logistica@mtis.org", emailDestino, asunto, cuerpo);

            cn.close();
            respuesta.setExito(true);
            respuesta.setMensaje("Notificación enviada correctamente a " + emailDestino);

        } catch (Exception e) {
            respuesta.setExito(false);
            respuesta.setMensaje("Error en notificaciones: " + e.getMessage());
            // NUEVO: Alerta por Excepción en el sistema
            enviarEmail("sistema@mtis.org", "admin@mtis.org", "Excepción en Servicio", "Error en Notificaciones de Entrega: " + e.getMessage());
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