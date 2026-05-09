package org.mtis.serviciostockred;

import conexionDB.ConexionesDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ServicioStockRedSkeleton {

    public org.mtis.serviciostockred.ConsultarStockRedResponse consultarStockRed(
        org.mtis.serviciostockred.ConsultarStockRedRequest req) {

        org.mtis.serviciostockred.ConsultarStockRedResponse respuesta =
            new org.mtis.serviciostockred.ConsultarStockRedResponse();

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
                respuesta.setMensaje("Error: WSKey invalida");
                cn.close();
                return respuesta;
            }

            int idModelo = req.getIdModelo();

            // Cuenta stock en todos los concesionarios de la red
            PreparedStatement ps = cn.prepareStatement(
                "SELECT COUNT(*) AS total FROM InventarioVehiculo " +
                "WHERE id_modelo = ? AND estado = 'STOCK_CONCESIONARIO'");
            ps.setInt(1, idModelo);
            ResultSet rs = ps.executeQuery();

            int total = 0;
            if (rs.next()) total = rs.getInt("total");
            cn.close();

            respuesta.setExito(true);
            respuesta.setMensaje("Consulta de stock en red realizada correctamente");
            respuesta.setDisponible(total > 0);
            respuesta.setCantidad(total);

        } catch (Exception e) {
            respuesta.setExito(false);
            respuesta.setMensaje("Error: " + e.getMessage());
            e.printStackTrace();
        }

        return respuesta;
    }
}