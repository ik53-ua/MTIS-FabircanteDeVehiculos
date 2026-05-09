package org.mtis.servicioregistroreservas;

import conexionDB.ConexionesDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class ServicioRegistroReservasSkeleton {

    public org.mtis.servicioregistroreservas.RegistrarReservaResponse registrarReserva(
        org.mtis.servicioregistroreservas.RegistrarReservaRequest req) {

        org.mtis.servicioregistroreservas.RegistrarReservaResponse respuesta =
            new org.mtis.servicioregistroreservas.RegistrarReservaResponse();

        ConexionesDB db = new ConexionesDB();
        Connection cn = db.conectar();

        if (cn == null) {
            respuesta.setExito(false);
            respuesta.setMensaje("Error: No se pudo conectar a la base de datos");
            return respuesta;
        }

        try {
            // 1. Validar WSKey
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
            String codigoBloqueo = req.getCodigoBloqueo();

            // 2. Encontrar el VIN del vehículo que acabamos de bloquear (estado RESERVADO)
            PreparedStatement psFind = cn.prepareStatement(
                "SELECT vin FROM InventarioVehiculo WHERE id_modelo = ? AND estado = 'RESERVADO' LIMIT 1");
            psFind.setInt(1, idModelo);
            ResultSet rsFind = psFind.executeQuery();

            if (rsFind.next()) {
                String vin = rsFind.getString("vin");

                // 3. INSERTAR la reserva oficial en la base de datos
                // Nota: Usamos id_cliente = 1 por defecto porque el WSDL no lo pide, 
                // pero la BD exige que no sea nulo.
                PreparedStatement psInsert = cn.prepareStatement(
                    "INSERT INTO Reserva (id_cliente, id_concesionario, vin, estado_reserva, codigo_bloqueo) " +
                    "VALUES (1, ?, ?, 'ACTIVA', ?)", Statement.RETURN_GENERATED_KEYS);
                
                psInsert.setInt(1, idConcesionario);
                psInsert.setString(2, vin);
                psInsert.setString(3, codigoBloqueo);
                psInsert.executeUpdate();

                // 4. Obtener el ID de la reserva generada
                ResultSet rsKeys = psInsert.getGeneratedKeys();
                int idReserva = 0;
                if (rsKeys.next()) {
                    idReserva = rsKeys.getInt(1);
                }

                respuesta.setExito(true);
                respuesta.setMensaje("Reserva #" + idReserva + " registrada correctamente.");
                respuesta.setIdReserva(idReserva);

            } else {
                respuesta.setExito(false);
                respuesta.setMensaje("No se encontró ningún vehículo bloqueado para este modelo.");
            }

            cn.close();

        } catch (Exception e) {
            respuesta.setExito(false);
            respuesta.setMensaje("Error al registrar: " + e.getMessage());
            e.printStackTrace();
        }

        return respuesta;
    }
}