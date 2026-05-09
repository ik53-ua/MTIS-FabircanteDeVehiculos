package org.mtis.serviciopedidosrepuestos;

import conexionDB.ConexionesDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class ServicioPedidosRepuestosSkeleton {

    public org.mtis.serviciopedidosrepuestos.GenerarPedidoRepuestosResponse generarPedidoRepuestos(
        org.mtis.serviciopedidosrepuestos.GenerarPedidoRepuestosRequest req) {

        org.mtis.serviciopedidosrepuestos.GenerarPedidoRepuestosResponse respuesta = 
            new org.mtis.serviciopedidosrepuestos.GenerarPedidoRepuestosResponse();

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
            int idPieza = req.getIdPieza();
            int cantidad = req.getCantidad();

            // Iniciar transacción
            cn.setAutoCommit(false);

            // 1. Insertar la cabecera del pedido
            PreparedStatement psPedido = cn.prepareStatement(
                "INSERT INTO PedidoRepuesto (id_concesionario, estado) VALUES (?, 'PENDIENTE')",
                Statement.RETURN_GENERATED_KEYS);
            psPedido.setInt(1, idConcesionario);
            psPedido.executeUpdate();

            // Obtener el ID generado para el pedido
            ResultSet rsGenerado = psPedido.getGeneratedKeys();
            int idPedidoGenerado = 0;
            if (rsGenerado.next()) {
                idPedidoGenerado = rsGenerado.getInt(1);
            } else {
                throw new Exception("No se pudo obtener el ID del pedido generado.");
            }

            // 2. Insertar el detalle del pedido
            PreparedStatement psDetalle = cn.prepareStatement(
                "INSERT INTO DetallePedidoRepuesto (id_pedido_repuesto, id_pieza, cantidad) VALUES (?, ?, ?)");
            psDetalle.setInt(1, idPedidoGenerado);
            psDetalle.setInt(2, idPieza);
            psDetalle.setInt(3, cantidad);
            psDetalle.executeUpdate();

            // Confirmar transacción
            cn.commit();

            respuesta.setExito(true);
            respuesta.setMensaje("Pedido generado correctamente");
            respuesta.setIdPedido(idPedidoGenerado);

            cn.close();

        } catch (Exception e) {
            try {
                if (cn != null && !cn.getAutoCommit()) {
                    cn.rollback(); // Deshacer cambios si hubo error
                }
            } catch (Exception rollbackEx) {
                rollbackEx.printStackTrace();
            }
            respuesta.setExito(false);
            respuesta.setMensaje("Error al generar el pedido: " + e.getMessage());
            e.printStackTrace();
        }

        return respuesta;
    }
}