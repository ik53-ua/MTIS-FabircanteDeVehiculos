package org.mtis.servicioconfiguracionesvehiculo;

import conexionDB.ConexionesDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ServicioConfiguracionesVehiculoSkeleton {

    public org.mtis.servicioconfiguracionesvehiculo.ValidarConfiguracionResponse validarConfiguracion(
        org.mtis.servicioconfiguracionesvehiculo.ValidarConfiguracionRequest req) {

        org.mtis.servicioconfiguracionesvehiculo.ValidarConfiguracionResponse respuesta =
            new org.mtis.servicioconfiguracionesvehiculo.ValidarConfiguracionResponse();

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

            int idModelo = req.getIdModelo();
            String configuracion = req.getConfiguracion();

            // Buscar la configuracion en ConfiguracionModelo
            PreparedStatement psConfig = cn.prepareStatement(
                "SELECT es_viable, sobrecoste " +
                "FROM ConfiguracionModelo " +
                "WHERE id_modelo = ? AND nombre_config = ?");
            psConfig.setInt(1, idModelo);
            psConfig.setString(2, configuracion);
            ResultSet rsConfig = psConfig.executeQuery();

            if (!rsConfig.next()) {
                respuesta.setExito(false);
                respuesta.setEsViable(false);
                respuesta.setMensaje("Error: Configuración '" + configuracion +
                    "' no encontrada para el modelo " + idModelo);
                cn.close();
                return respuesta;
            }

            boolean esViable = rsConfig.getBoolean("es_viable");
            double sobrecoste = rsConfig.getDouble("sobrecoste");

            respuesta.setExito(true);
            respuesta.setEsViable(esViable);

            if (esViable) {
                respuesta.setMensaje("Configuración válida y viable. Sobrecoste: " + sobrecoste + " €");
            } else {
                respuesta.setMensaje("Configuración encontrada pero no viable para producción");
            }

            cn.close();

        } catch (Exception e) {
            respuesta.setExito(false);
            respuesta.setMensaje("Error: " + e.getMessage());
            e.printStackTrace();
        }

        return respuesta;
    }
}