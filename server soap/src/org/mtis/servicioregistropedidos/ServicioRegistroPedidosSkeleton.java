package org.mtis.servicioregistropedidos;

import conexionDB.ConexionesDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ServicioRegistroPedidosSkeleton {

    public org.mtis.servicioregistropedidos.RegistrarPedidoFabricacionResponse registrarPedidoFabricacion(
        org.mtis.servicioregistropedidos.RegistrarPedidoFabricacionRequest req) {

        org.mtis.servicioregistropedidos.RegistrarPedidoFabricacionResponse respuesta =
            new org.mtis.servicioregistropedidos.RegistrarPedidoFabricacionResponse();

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
            int idOrden         = req.getIdOrden();

            // Verificar que el pedido existe y pertenece al concesionario
            PreparedStatement psCheck = cn.prepareStatement(
                "SELECT estado_pedido FROM PedidoFabricacion " +
                "WHERE id_pedido = ? AND id_concesionario = ?");
            psCheck.setInt(1, idOrden);
            psCheck.setInt(2, idConcesionario);
            ResultSet rsCheck = psCheck.executeQuery();

            if (!rsCheck.next()) {
                respuesta.setExito(false);
                respuesta.setMensaje("Error: Pedido " + idOrden +
                    " no encontrado para el concesionario " + idConcesionario);
                cn.close();
                return respuesta;
            }

            String estadoActual = rsCheck.getString("estado_pedido");
            if (!"EN_PRODUCCION".equals(estadoActual)) {
                respuesta.setExito(false);
                respuesta.setMensaje("Error: El pedido " + idOrden +
                    " ya está en estado '" + estadoActual + "' y no puede re-registrarse");
                cn.close();
                return respuesta;
            }

            // Actualizar estado a ACEPTADO
            PreparedStatement psUpdate = cn.prepareStatement(
                "UPDATE PedidoFabricacion SET estado_pedido = 'ACEPTADO' WHERE id_pedido = ?");
            psUpdate.setInt(1, idOrden);
            psUpdate.executeUpdate();

            respuesta.setExito(true);
            respuesta.setIdPedido(idOrden);
            respuesta.setMensaje("Pedido registrado correctamente. ID pedido: " +
                idOrden + ". Estado: ACEPTADO");

            cn.close();

        } catch (Exception e) {
            respuesta.setExito(false);
            respuesta.setMensaje("Error: " + e.getMessage());
            e.printStackTrace();
        }

        return respuesta;
    }
}