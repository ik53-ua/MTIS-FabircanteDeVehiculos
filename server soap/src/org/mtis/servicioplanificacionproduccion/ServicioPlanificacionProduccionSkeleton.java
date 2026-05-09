package org.mtis.servicioplanificacionproduccion;

import conexionDB.ConexionesDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ServicioPlanificacionProduccionSkeleton {

    public org.mtis.servicioplanificacionproduccion.ConsultarCapacidadProduccionResponse consultarCapacidadProduccion(
        org.mtis.servicioplanificacionproduccion.ConsultarCapacidadProduccionRequest req) {

        org.mtis.servicioplanificacionproduccion.ConsultarCapacidadProduccionResponse respuesta =
            new org.mtis.servicioplanificacionproduccion.ConsultarCapacidadProduccionResponse();

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

            // Obtener dias_fabricacion del modelo
            PreparedStatement psModelo = cn.prepareStatement(
                "SELECT dias_fabricacion FROM ModeloVehiculo WHERE id_modelo = ?");
            psModelo.setInt(1, idModelo);
            ResultSet rsModelo = psModelo.executeQuery();

            if (!rsModelo.next()) {
                respuesta.setExito(false);
                respuesta.setMensaje("Error: Modelo con id " + idModelo + " no encontrado");
                cn.close();
                return respuesta;
            }

            int diasBase = rsModelo.getInt("dias_fabricacion");

            // Verificar que la configuracion es viable
            PreparedStatement psConfig = cn.prepareStatement(
                "SELECT es_viable FROM ConfiguracionModelo " +
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

            // Calcular fecha de entrega estimada
            LocalDate fechaEntrega = LocalDate.now().plusDays(diasBase);
            String fechaEntregaStr = fechaEntrega.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

            respuesta.setExito(true);
            respuesta.setDiasEstimados(diasBase);
            respuesta.setFechaEntrega(fechaEntregaStr);
            respuesta.setMensaje("Capacidad disponible. Entrega estimada en " +
                diasBase + " días (" + fechaEntregaStr + ")");

            cn.close();

        } catch (Exception e) {
            respuesta.setExito(false);
            respuesta.setMensaje("Error: " + e.getMessage());
            e.printStackTrace();
        }

        return respuesta;
    }
}