package org.mtis.serviciocatalogopiezas;

import conexionDB.ConexionesDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ServicioCatalogoPiezasSkeleton {

    public org.mtis.serviciocatalogopiezas.ConsultarPiezaResponse consultarPieza(
        org.mtis.serviciocatalogopiezas.ConsultarPiezaRequest req) {

        org.mtis.serviciocatalogopiezas.ConsultarPiezaResponse respuesta = 
            new org.mtis.serviciocatalogopiezas.ConsultarPiezaResponse();

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

            // Consultar datos de la pieza
            int idPieza = req.getIdPieza();
            PreparedStatement psPieza = cn.prepareStatement(
                "SELECT nombre, precio, tiempo_suministro_dias FROM Pieza WHERE id_pieza = ?");
            psPieza.setInt(1, idPieza);
            ResultSet rsPieza = psPieza.executeQuery();

            if (rsPieza.next()) {
                respuesta.setExito(true);
                respuesta.setMensaje("Pieza encontrada con éxito");
                respuesta.setNombrePieza(rsPieza.getString("nombre"));
                respuesta.setPrecio(rsPieza.getDouble("precio"));
                respuesta.setTiempoSuministroDias(rsPieza.getInt("tiempo_suministro_dias"));
            } else {
                respuesta.setExito(false);
                respuesta.setMensaje("Error: La pieza con ID " + idPieza + " no existe en el catálogo.");
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