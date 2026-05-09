package org.mtis.servicioprocesosolicitudrepuestos;

import org.mtis.serviciostockrepuestos.ServicioStockRepuestosSkeleton;
import org.mtis.serviciopedidosrepuestos.ServicioPedidosRepuestosSkeleton;
import org.mtis.serviciologisticaenvios.ServicioLogisticaEnviosSkeleton;
import org.mtis.servicionotificacionesentrega.ServicioNotificacionesEntregaSkeleton;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ServicioProcesoSolicitudRepuestosSkeleton {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/FabricanteVehiculosDB";
    private static final String DB_USER = "root";
    private static final String DB_PASS = ""; 

    public org.mtis.servicioprocesosolicitudrepuestos.ProcesarSolicitudRepuestosResponse procesarSolicitudRepuestos(
            org.mtis.servicioprocesosolicitudrepuestos.ProcesarSolicitudRepuestosRequest req) {
        
        org.mtis.servicioprocesosolicitudrepuestos.ProcesarSolicitudRepuestosResponse resF = 
            new org.mtis.servicioprocesosolicitudrepuestos.ProcesarSolicitudRepuestosResponse();
        
        String key = req.getWSKey();
        int conc = req.getIdConcesionario();
        int piezaId = Integer.parseInt(req.getListaPiezasRequeridas().split(",")[0].trim());

        try {
            // 0.1 Validar WSKey
            if (!validarWSKey(key)) {
                resF.setExito(false);
                resF.setMensaje("Error: Acceso denegado. WSKey inválida.");
                return resF;
            }

            // 0.2 Validar Concesionario y Pieza
            if (!existeConcesionarioYPieza(conc, piezaId)) {
                String mensajeError = "Error: El concesionario (" + conc + ") o la pieza (" + piezaId + ") no existen en BD.";
                resF.setExito(false);
                resF.setMensaje(mensajeError);
                // NOTIFICAR ERROR
                enviarAvisoNotificacion(key, conc, 0, mensajeError);
                return resF;
            }

            // 1. CONSULTAR STOCK LOCAL 
            ServicioStockRepuestosSkeleton sStock = new ServicioStockRepuestosSkeleton();
            org.mtis.serviciostockrepuestos.ConsultarStockRepuestosRequest reqS = 
                new org.mtis.serviciostockrepuestos.ConsultarStockRepuestosRequest();
            reqS.setWSKey(key); 
            reqS.setIdConcesionario(conc); 
            reqS.setIdPieza(piezaId);
            
            org.mtis.serviciostockrepuestos.ConsultarStockRepuestosResponse resS = sStock.consultarStockRepuestos(reqS);

            if (resS.getExito() && resS.getCantidadDisponible() < 1) {
                // 2. GENERAR PEDIDO 
                ServicioPedidosRepuestosSkeleton sPed = new ServicioPedidosRepuestosSkeleton();
                org.mtis.serviciopedidosrepuestos.GenerarPedidoRepuestosRequest reqP = new org.mtis.serviciopedidosrepuestos.GenerarPedidoRepuestosRequest();
                reqP.setWSKey(key); reqP.setIdConcesionario(conc); reqP.setIdPieza(piezaId); reqP.setCantidad(1);
                org.mtis.serviciopedidosrepuestos.GenerarPedidoRepuestosResponse resP = sPed.generarPedidoRepuestos(reqP);

                // 3. LOGÍSTICA 
                ServicioLogisticaEnviosSkeleton sLog = new ServicioLogisticaEnviosSkeleton();
                org.mtis.serviciologisticaenvios.GestionarEnvioRepuestosRequest reqL = new org.mtis.serviciologisticaenvios.GestionarEnvioRepuestosRequest();
                reqL.setWSKey(key); reqL.setIdPedido(resP.getIdPedido()); reqL.setIdConcesionario(conc);
                org.mtis.serviciologisticaenvios.GestionarEnvioRepuestosResponse resL = sLog.gestionarEnvioRepuestos(reqL);

                // 4. NOTIFICAR ENTREGA (Éxito)
                ServicioNotificacionesEntregaSkeleton sNot = new ServicioNotificacionesEntregaSkeleton();
                org.mtis.servicionotificacionesentrega.EnviarNotificacionEntregaRequest reqN = new org.mtis.servicionotificacionesentrega.EnviarNotificacionEntregaRequest();
                reqN.setWSKey(key); reqN.setIdConcesionario(conc); reqN.setIdEnvio(resL.getIdEnvio()); 
                reqN.setFechaRecepcionEstimada(resL.getFechaEstimada()); 
                sNot.enviarNotificacionEntrega(reqN);

                resF.setExito(true);
                resF.setMensaje("Pedido procesado y envío gestionado.");
                resF.setFechaRecepcionEsperada(resL.getFechaEstimada());

            } else {
                resF.setExito(true); 
                String mensajeAviso = "Aviso: Stock local suficiente para la pieza " + piezaId + ".";
                resF.setMensaje(mensajeAviso);
                // NOTIFICAR AVISO
                enviarAvisoNotificacion(key, conc, 0, mensajeAviso);
            }
        } catch (Exception e) { 
            String mensajeError = "Error cancelado en el proceso de repuestos: " + e.getMessage();
            resF.setExito(false); 
            resF.setMensaje(mensajeError);
            // NOTIFICAR EXCEPCIÓN
            enviarAvisoNotificacion(key, conc, 0, mensajeError);
        }
        return resF;
    }

    private boolean validarWSKey(String wsKey) {
        String query = "SELECT 1 FROM SoapKey WHERE soap_key = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, wsKey);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (Exception e) {
            return false; 
        }
    }

    private boolean existeConcesionarioYPieza(int idConcesionario, int idPieza) {
        String queryConc = "SELECT 1 FROM Concesionario WHERE id_concesionario = ?";
        String queryPieza = "SELECT 1 FROM Pieza WHERE id_pieza = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             PreparedStatement stmtConc = conn.prepareStatement(queryConc);
             PreparedStatement stmtPieza = conn.prepareStatement(queryPieza)) {
            
            stmtConc.setInt(1, idConcesionario);
            boolean concExiste;
            try (ResultSet rs = stmtConc.executeQuery()) { concExiste = rs.next(); }

            stmtPieza.setInt(1, idPieza);
            boolean piezaExiste;
            try (ResultSet rs = stmtPieza.executeQuery()) { piezaExiste = rs.next(); }

            return concExiste && piezaExiste;
        } catch (Exception e) {
            return false;
        }
    }

    private void enviarAvisoNotificacion(String key, int conc, int idEnvio, String mensaje) {
        try {
            ServicioNotificacionesEntregaSkeleton sNot = new ServicioNotificacionesEntregaSkeleton();
            org.mtis.servicionotificacionesentrega.EnviarNotificacionEntregaRequest reqN = 
                new org.mtis.servicionotificacionesentrega.EnviarNotificacionEntregaRequest();
            reqN.setWSKey(key); 
            reqN.setIdConcesionario(conc); 
            reqN.setIdEnvio(idEnvio); 
            reqN.setFechaRecepcionEstimada(mensaje); 
            sNot.enviarNotificacionEntrega(reqN);
        } catch (Exception ex) {
            System.err.println("Fallo al intentar notificar: " + ex.getMessage());
        }
    }
}