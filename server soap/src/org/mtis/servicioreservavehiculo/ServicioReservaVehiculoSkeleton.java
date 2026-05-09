package org.mtis.servicioreservavehiculo;

import conexionDB.ConexionesDB;
import java.sql.*;
import java.util.UUID;

public class ServicioReservaVehiculoSkeleton {
    public org.mtis.servicioreservavehiculo.BloquearVehiculoResponse bloquearVehiculo(
            org.mtis.servicioreservavehiculo.BloquearVehiculoRequest req) {
        
        org.mtis.servicioreservavehiculo.BloquearVehiculoResponse res = 
            new org.mtis.servicioreservavehiculo.BloquearVehiculoResponse();
        ConexionesDB db = new ConexionesDB();
        Connection cn = db.conectar();
        
        try {
            // Buscamos un vehículo disponible para ese modelo 
            PreparedStatement ps = cn.prepareStatement(
                "SELECT vin FROM InventarioVehiculo WHERE id_modelo = ? " +
                "AND estado IN ('STOCK_FABRICANTE', 'STOCK_CONCESIONARIO') LIMIT 1");
            ps.setInt(1, req.getIdModelo());
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String vin = rs.getString("vin");
                String code = "LOCK-" + UUID.randomUUID().toString().substring(0,8);
                
                // Actualizamos a 'RESERVADO' (ya existente en tu ENUM) 
                PreparedStatement up = cn.prepareStatement(
                    "UPDATE InventarioVehiculo SET estado = 'RESERVADO' WHERE vin = ?");
                up.setString(1, vin);
                up.executeUpdate();

                res.setExito(true);
                res.setBloqueado(true);
                res.setCodigoBloqueo(code);
                res.setMensaje("Vehículo reservado correctamente.");
            } else {
                res.setExito(false);
                res.setBloqueado(false);
                res.setMensaje("No hay stock disponible de este modelo.");
            }
            cn.close();
        } catch (Exception e) {
            res.setExito(false);
            res.setMensaje("Error en base de datos: " + e.getMessage());
        }
        return res;
    }
}