package org.mtis.serviciocalculocostes;

import conexionDB.ConexionesDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ServicioCalculoCostesSkeleton {

    public org.mtis.serviciocalculocostes.CalcularCosteProduccionResponse calcularCosteProduccion(
        org.mtis.serviciocalculocostes.CalcularCosteProduccionRequest req) {

        org.mtis.serviciocalculocostes.CalcularCosteProduccionResponse respuesta =
            new org.mtis.serviciocalculocostes.CalcularCosteProduccionResponse();

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

            // Obtener coste_produccion del modelo base
            PreparedStatement psModelo = cn.prepareStatement(
                "SELECT coste_produccion FROM ModeloVehiculo WHERE id_modelo = ?");
            psModelo.setInt(1, idModelo);
            ResultSet rsModelo = psModelo.executeQuery();

            if (!rsModelo.next()) {
                respuesta.setExito(false);
                respuesta.setMensaje("Error: Modelo con id " + idModelo + " no encontrado");
                cn.close();
                return respuesta;
            }

            double costeBase = rsModelo.getDouble("coste_produccion");

            // Obtener sobrecoste de la configuracion elegida
            PreparedStatement psConfig = cn.prepareStatement(
                "SELECT sobrecoste, es_viable FROM ConfiguracionModelo " +
                "WHERE id_modelo = ? AND nombre_config = ?");
            psConfig.setInt(1, idModelo);
            psConfig.setString(2, configuracion);
            ResultSet rsConfig = psConfig.executeQuery();

            if (!rsConfig.next()) {
                respuesta.setExito(false);
                respuesta.setMensaje("Error: Configuración '" + configuracion +
                    "' no encontrada para el modelo " + idModelo);
                cn.close();
                return respuesta;
            }

            if (!rsConfig.getBoolean("es_viable")) {
                respuesta.setExito(false);
                respuesta.setMensaje("Error: La configuración no es viable para producción");
                cn.close();
                return respuesta;
            }

            double sobrecoste  = rsConfig.getDouble("sobrecoste");
            double costeTotal  = costeBase + sobrecoste;

            respuesta.setExito(true);
            respuesta.setCosteTotal(costeTotal);
            respuesta.setMensaje("Coste calculado correctamente. Base: " + costeBase +
                " € | Sobrecoste config: " + sobrecoste + " € | Total: " + costeTotal + " €");

            cn.close();

        } catch (Exception e) {
            respuesta.setExito(false);
            respuesta.setMensaje("Error: " + e.getMessage());
            e.printStackTrace();
        }

        return respuesta;
    }
}