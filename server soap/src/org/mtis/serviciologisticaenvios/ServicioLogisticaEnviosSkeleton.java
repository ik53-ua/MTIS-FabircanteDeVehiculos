package org.mtis.serviciologisticaenvios;

import conexionDB.ConexionesDB;
import java.sql.*;
import java.time.LocalDate;

public class ServicioLogisticaEnviosSkeleton {
    public org.mtis.serviciologisticaenvios.GestionarEnvioRepuestosResponse gestionarEnvioRepuestos(
            org.mtis.serviciologisticaenvios.GestionarEnvioRepuestosRequest req) {
        
        org.mtis.serviciologisticaenvios.GestionarEnvioRepuestosResponse res = 
            new org.mtis.serviciologisticaenvios.GestionarEnvioRepuestosResponse();
        Connection cn = new ConexionesDB().conectar();
        
        try {
            // Consultamos el tiempo de suministro de la pieza asociada al pedido 
            PreparedStatement ps = cn.prepareStatement(
                "SELECT p.tiempo_suministro_dias FROM Pieza p " +
                "JOIN DetallePedidoRepuesto d ON p.id_pieza = d.id_pieza " +
                "WHERE d.id_pedido_repuesto = ?");
            ps.setInt(1, req.getIdPedido());
            ResultSet rs = ps.executeQuery();
            
            int dias = rs.next() ? rs.getInt(1) : 5; // 5 días por defecto
            String fecha = LocalDate.now().plusDays(dias).toString();

            // Insertamos el registro de envío 
            PreparedStatement in = cn.prepareStatement(
                "INSERT INTO EnvioLogistica (id_pedido_repuesto, id_concesionario_destino, " +
                "estado_envio, fecha_entrega_estimada) VALUES (?, ?, 'PREPARACION', ?)", 
                Statement.RETURN_GENERATED_KEYS);
            in.setInt(1, req.getIdPedido());
            in.setInt(2, req.getIdConcesionario());
            in.setString(3, fecha);
            in.executeUpdate();
            
            ResultSet keys = in.getGeneratedKeys();
            res.setExito(true);
            res.setMensaje("Envío logístico registrado.");
            res.setFechaEstimada(fecha);
            if (keys.next()) res.setIdEnvio(keys.getInt(1));
            
            cn.close();
        } catch (Exception e) { 
            res.setExito(false); 
            res.setMensaje("Error: " + e.getMessage());
        }
        return res;
    }
}