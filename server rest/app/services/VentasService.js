/**
 * Servicio para gestión de Ventas (Proceso 2.5)
 */

const { queryWithParams, query } = require('../db');
const logger = require('../logger');
const Service = require('./Service');

/**
 * Registrar venta del vehículo
 */
const registrarVenta = ({ ventaRequest }) => new Promise(
  async (resolve, reject) => {
    try {
      logger.info('🔍 registrarVenta iniciado con:', JSON.stringify(ventaRequest));
      
      // Validar parámetros requeridos
      if (!ventaRequest.vehiculoId) {
        logger.warn('❌ vehiculoId es requerido');
        return reject({ mensaje: 'vehiculoId es requerido' });
      }
      if (!ventaRequest.clienteId) {
        logger.warn('❌ clienteId es requerido');
        return reject({ mensaje: 'clienteId es requerido' });
      }
      if (!ventaRequest.precioTotal) {
        logger.warn('❌ precioTotal es requerido');
        return reject({ mensaje: 'precioTotal es requerido' });
      }
      
      logger.info('✓ Validaciones pasadas. Consultando vehículo...');
      
      // Verificar que el vehículo existe y obtener concesionario
      const vehiculoSql = `SELECT id_modelo, id_concesionario FROM InventarioVehiculo WHERE vin = ?`;
      logger.info('📝 SQL vehículo:', vehiculoSql, 'Params:', [ventaRequest.vehiculoId]);
      
      const vehiculoResult = await queryWithParams(vehiculoSql, [ventaRequest.vehiculoId]);
      logger.info('✓ Resultado vehículo:', JSON.stringify(vehiculoResult));
      
      if (vehiculoResult.length === 0) {
        logger.warn('❌ Vehículo no encontrado:', ventaRequest.vehiculoId);
        return reject({ mensaje: 'Vehículo no encontrado' });
      }
      
      const concesionarioId = vehiculoResult[0].id_concesionario || 1;
      logger.info('💼 Concesionario ID:', concesionarioId);

      const sql = `
        INSERT INTO Venta 
        (id_cliente, id_concesionario, vin, precio_final, estado_venta)
        VALUES (?, ?, ?, ?, 'PENDIENTE_PAGO')
      `;

      const params = [
        ventaRequest.clienteId,
        concesionarioId,
        ventaRequest.vehiculoId,
        ventaRequest.precioTotal,
      ];

      logger.info('🔍 SQL params antes de ejecutar:', JSON.stringify(params));
      logger.info('📝 SQL INSERT:', sql.trim());
      
      const result = await queryWithParams(sql, params);
      logger.info('✓ INSERT exitoso. insertId:', result.insertId);
      
      // Actualizar estado del inventario a VENDIDO
      const updateSql = `UPDATE InventarioVehiculo SET estado = 'VENDIDO' WHERE vin = ?`;
      logger.info('📝 SQL UPDATE:', updateSql, 'Params:', [ventaRequest.vehiculoId]);
      
      await queryWithParams(updateSql, [ventaRequest.vehiculoId]);
      logger.info('✓ Estado vehículo actualizado a VENDIDO');
      
      resolve({
        ventaId: result.insertId.toString(),
        estado: 'PENDIENTE_PAGO',
      });
    } catch (e) {
      logger.error('❌ Error al registrar venta:', e.message);
      logger.error('   Stack:', e.stack);
      reject({
        mensaje: e.message || 'Error al registrar venta',
      });
    }
  },
);

/**
 * Consultar venta
 */
const consultarVenta = ({ id }) => new Promise(
  async (resolve, reject) => {
    try {
      const sql = `
        SELECT 
          id_venta as ventaId,
          id_cliente as clienteId,
          vin as vehiculoId,
          precio_final as precioFinal,
          estado_venta as estado,
          fecha_venta as fechaVenta
        FROM Venta
        WHERE id_venta = ?
      `;

      const result = await queryWithParams(sql, [id]);
      
      if (result.length === 0) {
        return reject({
          mensaje: `Venta ${id} no encontrada`,
        });
      }

      resolve(result[0]);
    } catch (e) {
      logger.error('Error al consultar venta:', e);
      reject({
        mensaje: e.message || 'Error al consultar venta',
      });
    }
  },
);

/**
 * Registrar venta (endpoint)
 */
const ventasRegistrarPOST = (params) => new Promise(
  async (resolve, reject) => {
    try {
      logger.info('🔍 ventasRegistrarPOST params recibidos:', JSON.stringify(params));
      
      // Extraer el body correctamente
      let ventaRequest = params.ventaRequest || params.VentaRequest || params.body;
      
      logger.info('✓ ventaRequest extraído:', JSON.stringify(ventaRequest));
      
      // Validar que tengamos los campos requeridos
      if (!ventaRequest) {
        logger.warn('❌ No se encontró ventaRequest');
        return reject(Service.rejectResponse('Cuerpo de solicitud requerido', 400));
      }
      
      if (!ventaRequest.vehiculoId) {
        logger.warn('❌ vehiculoId no proporcionado');
        return reject(Service.rejectResponse('vehiculoId es requerido', 400));
      }
      
      if (!ventaRequest.clienteId) {
        logger.warn('❌ clienteId no proporcionado');
        return reject(Service.rejectResponse('clienteId es requerido', 400));
      }
      
      if (!ventaRequest.precioTotal) {
        logger.warn('❌ precioTotal no proporcionado');
        return reject(Service.rejectResponse('precioTotal es requerido', 400));
      }
      
      logger.info('✓ Validaciones en endpoint pasadas, llamando registrarVenta...');
      
      const result = await registrarVenta({ ventaRequest });
      logger.info('✓ Resultado de registrarVenta:', JSON.stringify(result));
      
      resolve(Service.successResponse(result, 201));
    } catch (e) {
      logger.error('❌ Error en ventasRegistrarPOST:', e.message);
      logger.error('   Stack:', e.stack);
      
      // Si ya es una respuesta de Service, devolverla tal cual
      if (e.code) {
        reject(e);
      } else {
        reject(Service.rejectResponse(e.mensaje || e.message || 'Error al registrar venta', 400));
      }
    }
  },
);

/**
 * Consultar venta (endpoint)
 */
const ventasIdGET = ({ id }) => new Promise(
  async (resolve, reject) => {
    try {
      logger.info('🔍 ventasIdGET params recibidos - id:', id);
      
      const result = await consultarVenta({ id });
      logger.info('✓ Venta consultada:', JSON.stringify(result));
      
      resolve(Service.successResponse(result, 200));
    } catch (e) {
      logger.error('❌ Error en ventasIdGET:', e.message);
      reject(Service.rejectResponse(
        e.mensaje || 'Venta no encontrada',
        404,
      ));
    }
  },
);

module.exports = {
  ventasIdGET,
  ventasRegistrarPOST,
};
