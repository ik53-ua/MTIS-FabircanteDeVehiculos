package org.mtis.serviciostockrepuestos;

import conexionDB.ConexionesDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ServicioStockRepuestosSkeleton {

    public org.mtis.serviciostockrepuestos.ConsultarStockRepuestosResponse consultarStockRepuestos(
        org.mtis.serviciostockrepuestos.ConsultarStockRepuestosRequest req) {

        org.mtis.serviciostockrepuestos.ConsultarStockRepuestosResponse respuesta = 
            new org.mtis.serviciostockrepuestos.ConsultarStockRepuestosResponse();

        ConexionesDB db = new ConexionesDB();
        Connection cn = db.conectar();

        if (cn == null) {
            respuesta.setExito(false);
            respuesta.setMensaje("Error: No se pudo conectar a la base de datos");
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
                cn.close();
                return respuesta;
            }

            int idConcesionario = req.getIdConcesionario();
            int idPieza = req.getIdPieza();

            // Consultar cantidad de stock en el concesionario
            PreparedStatement psStock = cn.prepareStatement(
                "SELECT cantidad FROM StockRepuesto WHERE id_concesionario = ? AND id_pieza = ?");
            psStock.setInt(1, idConcesionario);
            psStock.setInt(2, idPieza);
            
            ResultSet rsStock = psStock.executeQuery();

            if (rsStock.next()) {
                respuesta.setExito(true);
                respuesta.setMensaje("Stock consultado con éxito");
                respuesta.setCantidadDisponible(rsStock.getInt("cantidad"));
            } else {
                // Si no hay registro en la tabla, interpretamos que el stock es 0
                respuesta.setExito(true);
                respuesta.setMensaje("El concesionario no dispone de stock registrado para esta pieza");
                respuesta.setCantidadDisponible(0);
            }

            cn.close();

        } catch (Exception e) {
            respuesta.setExito(false);
            respuesta.setMensaje("Error interno del servidor: " + e.getMessage());
            e.printStackTrace();
        }

        return respuesta;
    }
}