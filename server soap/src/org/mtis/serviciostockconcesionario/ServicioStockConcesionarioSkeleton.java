package org.mtis.serviciostockconcesionario;

import conexionDB.ConexionesDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ServicioStockConcesionarioSkeleton {

    public org.mtis.serviciostockconcesionario.ConsultarStockConcesionarioResponse consultarStockConcesionario(
        org.mtis.serviciostockconcesionario.ConsultarStockConcesionarioRequest req) {

        org.mtis.serviciostockconcesionario.ConsultarStockConcesionarioResponse respuesta =
            new org.mtis.serviciostockconcesionario.ConsultarStockConcesionarioResponse();

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

            int idConcesionario = req.getIdConcesionario();
            int idModelo = req.getIdModelo();

            PreparedStatement ps = cn.prepareStatement(
                "SELECT COUNT(*) AS total FROM InventarioVehiculo " +
                "WHERE id_concesionario = ? AND id_modelo = ? AND estado = 'STOCK_CONCESIONARIO'");
            ps.setInt(1, idConcesionario);
            ps.setInt(2, idModelo);
            ResultSet rs = ps.executeQuery();

            int total = 0;
            if (rs.next()) total = rs.getInt("total");
            cn.close();

            respuesta.setExito(true);
            respuesta.setMensaje("Consulta realizada correctamente");
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