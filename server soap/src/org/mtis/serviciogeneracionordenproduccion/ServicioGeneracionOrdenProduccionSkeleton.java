package org.mtis.serviciogeneracionordenproduccion;

import conexionDB.ConexionesDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ServicioGeneracionOrdenProduccionSkeleton {

    public org.mtis.serviciogeneracionordenproduccion.GenerarOrdenProduccionResponse generarOrdenProduccion(
        org.mtis.serviciogeneracionordenproduccion.GenerarOrdenProduccionRequest req) {

        org.mtis.serviciogeneracionordenproduccion.GenerarOrdenProduccionResponse respuesta =
            new org.mtis.serviciogeneracionordenproduccion.GenerarOrdenProduccionResponse();

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
            int idModelo        = req.getIdModelo();
            String configuracion = req.getConfiguracion();

            // Obtener id_configuracion verificando que es viable
            PreparedStatement psConfig = cn.prepareStatement(
                "SELECT id_configuracion FROM ConfiguracionModelo " +
                "WHERE id_modelo = ? AND nombre_config = ? AND es_viable = TRUE");
            psConfig.setInt(1, idModelo);
            psConfig.setString(2, configuracion);
            ResultSet rsConfig = psConfig.executeQuery();

            if (!rsConfig.next()) {
                respuesta.setExito(false);
                respuesta.setMensaje("Error: Configuración '" + configuracion +
                    "' no viable o inexistente para el modelo " + idModelo);
                cn.close();
                return respuesta;
            }

            int idConfiguracion = rsConfig.getInt("id_configuracion");

            // Insertar en PedidoFabricacion con estado EN_PRODUCCION
            PreparedStatement psInsert = cn.prepareStatement(
                "INSERT INTO PedidoFabricacion " +
                "(id_concesionario, id_modelo, id_configuracion, configuracion_detalles, estado_pedido) " +
                "VALUES (?, ?, ?, ?, 'EN_PRODUCCION')",
                PreparedStatement.RETURN_GENERATED_KEYS);
            psInsert.setInt(1, idConcesionario);
            psInsert.setInt(2, idModelo);
            psInsert.setInt(3, idConfiguracion);
            psInsert.setString(4, configuracion);
            psInsert.executeUpdate();

            ResultSet rsKeys = psInsert.getGeneratedKeys();
            int idOrden = -1;
            if (rsKeys.next()) {
                idOrden = rsKeys.getInt(1);
            }

            respuesta.setExito(true);
            respuesta.setIdOrden(idOrden);
            respuesta.setMensaje("Orden de producción generada correctamente. ID orden: " + idOrden);

            cn.close();

        } catch (Exception e) {
            respuesta.setExito(false);
            respuesta.setMensaje("Error: " + e.getMessage());
            e.printStackTrace();
        }

        return respuesta;
    }
}