package org.mtis.serviciostockfabricante;

import conexionDB.ConexionesDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ServicioStockFabricanteSkeleton {

    public org.mtis.serviciostockfabricante.ConsultarStockFabricanteResponse consultarStockFabricante(
        org.mtis.serviciostockfabricante.ConsultarStockFabricanteRequest req) {

        org.mtis.serviciostockfabricante.ConsultarStockFabricanteResponse respuesta =
            new org.mtis.serviciostockfabricante.ConsultarStockFabricanteResponse();

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

            // Consulta de stock
            int idModelo = req.getIdModelo();
            PreparedStatement ps = cn.prepareStatement(
                "SELECT COUNT(*) AS total FROM InventarioVehiculo " +
                "WHERE id_modelo = ? AND estado = 'STOCK_FABRICANTE'");
            ps.setInt(1, idModelo);
            ResultSet rs = ps.executeQuery();

            int stockEncontrado = 0;
            if (rs.next()) stockEncontrado = rs.getInt("total");

            cn.close();

            respuesta.setExito(true);
            respuesta.setMensaje("Consulta realizada correctamente");
            respuesta.setDisponible(stockEncontrado > 0);
            respuesta.setCantidad(stockEncontrado);

        } catch (Exception e) {
            respuesta.setExito(false);
            respuesta.setMensaje("Error: " + e.getMessage());
            e.printStackTrace();
        }

        return respuesta;
    }
}