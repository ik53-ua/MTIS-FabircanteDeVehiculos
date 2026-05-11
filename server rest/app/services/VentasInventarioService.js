/* eslint-disable no-unused-vars */
const { queryWithParams } = require('../db');
const logger = require('../logger');
const Service = require('./Service');

/**
 * Consultar estado del inventario del vehículo
 */
const obtenerEstadoVehiculo = ({ vehiculoId }) => new Promise(
  async (resolve, reject) => {
    try {
      logger.info('🔍 obtenerEstadoVehiculo iniciado - vehiculoId:', vehiculoId);
      
      const sql = `
        SELECT 
          vin as vehiculoId,
          id_modelo as modeloId,
          id_concesionario as concesionarioId,
          estado,
          color,
          motor,
          fecha_fabricacion as fechaFabricacion
        FROM InventarioVehiculo
        WHERE vin = ?
      `;

      logger.info('📝 SQL SELECT:', sql.trim());
      logger.info('🔍 SQL params:', JSON.stringify([vehiculoId]));
      
      const result = await queryWithParams(sql, [vehiculoId]);
      logger.info('✓ Resultado:', JSON.stringify(result));
      
      if (result.length === 0) {
        logger.warn('❌ Vehículo no encontrado:', vehiculoId);
        return reject({
          mensaje: `Vehículo ${vehiculoId} no encontrado`,
        });
      }

      resolve({
        mensaje: `Estado del vehículo: ${result[0].estado}`,
        datos: result[0],
      });
    } catch (e) {
      logger.error('❌ Error al consultar inventario:', e.message);
      logger.error('   Stack:', e.stack);
      reject({
        mensaje: e.message || 'Error al consultar inventario',
      });
    }
  },
);

/**
 * Actualizar estado del vehículo
 */
const actualizarEstadoVehiculo = ({ vehiculoId, inventarioUpdateRequest }) => new Promise(
  async (resolve, reject) => {
    try {
      logger.info('🔍 actualizarEstadoVehiculo iniciado - vehiculoId:', vehiculoId, 'con:', JSON.stringify(inventarioUpdateRequest));
      
      // Validar parámetro requerido
      if (!inventarioUpdateRequest.estado) {
        logger.warn('❌ estado es requerido');
        return reject({ mensaje: 'estado es requerido' });
      }
      
      logger.info('✓ Validaciones pasadas. Consultando vehículo...');
      
      // Validar estado válido
      const estadosValidos = ['EN_FABRICACION', 'STOCK_FABRICANTE', 'STOCK_CONCESIONARIO', 'RESERVADO', 'VENDIDO'];
      if (!estadosValidos.includes(inventarioUpdateRequest.estado)) {
        logger.warn('❌ Estado inválido:', inventarioUpdateRequest.estado);
        return reject({
          mensaje: `Estado inválido: ${inventarioUpdateRequest.estado}`,
        });
      }

      const sql = `
        UPDATE InventarioVehiculo
        SET estado = ?
        WHERE vin = ?
      `;

      logger.info('📝 SQL UPDATE:', sql.trim());
      logger.info('🔍 SQL params:', JSON.stringify([inventarioUpdateRequest.estado, vehiculoId]));
      
      const result = await queryWithParams(sql, [inventarioUpdateRequest.estado, vehiculoId]);
      logger.info('✓ UPDATE exitoso. affectedRows:', result.affectedRows);
      
      if (result.affectedRows === 0) {
        logger.warn('❌ Vehículo no encontrado:', vehiculoId);
        return reject({
          mensaje: `Vehículo ${vehiculoId} no encontrado`,
        });
      }

      resolve({
        mensaje: `Vehículo actualizado a estado: ${inventarioUpdateRequest.estado}`,
      });
    } catch (e) {
      logger.error('❌ Error al actualizar inventario:', e.message);
      logger.error('   Stack:', e.stack);
      reject({
        mensaje: e.message || 'Error al actualizar inventario',
      });
    }
  },
);

/**
 * Consultar inventario del vehículo (endpoint)
 */
const ventasInventarioVehiculoIdGET = ({ vehiculoId }) => new Promise(
  async (resolve, reject) => {
    try {
      logger.info('🔍 ventasInventarioVehiculoIdGET params recibidos - vehiculoId:', vehiculoId);
      
      const result = await obtenerEstadoVehiculo({ vehiculoId });
      logger.info('✓ Estado vehículo consultado:', JSON.stringify(result));
      
      resolve(Service.successResponse(result, 200));
    } catch (e) {
      logger.error('❌ Error en ventasInventarioVehiculoIdGET:', e.message);
      reject(Service.rejectResponse(
        e.mensaje || 'Inventario no encontrado',
        404,
      ));
    }
  },
);

/**
 * Actualizar estado del vehículo (endpoint)
 */
const ventasInventarioVehiculoIdPUT = (params) => new Promise(
  async (resolve, reject) => {
    try {
      logger.info('🔍 ventasInventarioVehiculoIdPUT params recibidos:', JSON.stringify(params));
      
      // Extraer parámetros
      const vehiculoId = params.vehiculoId;
      let inventarioUpdateRequest = params.inventarioUpdateRequest || params.InventarioUpdateRequest || params.body;
      
      logger.info('📝 vehiculoId:', vehiculoId);
      logger.info('✓ inventarioUpdateRequest extraído:', JSON.stringify(inventarioUpdateRequest));
      
      // Validar que tengamos los parámetros requeridos
      if (!vehiculoId) {
        logger.warn('❌ vehiculoId no proporcionado');
        return reject(Service.rejectResponse('vehiculoId es requerido', 400));
      }
      
      if (!inventarioUpdateRequest) {
        logger.warn('❌ No se encontró inventarioUpdateRequest');
        return reject(Service.rejectResponse('Cuerpo de solicitud requerido', 400));
      }
      
      if (!inventarioUpdateRequest.estado) {
        logger.warn('❌ estado no proporcionado');
        return reject(Service.rejectResponse('estado es requerido', 400));
      }
      
      logger.info('✓ Validaciones en endpoint pasadas, llamando actualizarEstadoVehiculo...');
      
      const result = await actualizarEstadoVehiculo({ vehiculoId, inventarioUpdateRequest });
      logger.info('✓ Resultado de actualizarEstadoVehiculo:', JSON.stringify(result));
      
      resolve(Service.successResponse(result, 200));
    } catch (e) {
      logger.error('❌ Error en ventasInventarioVehiculoIdPUT:', e.message);
      logger.error('   Stack:', e.stack);
      
      if (e.code) {
        reject(e);
      } else {
        reject(Service.rejectResponse(e.mensaje || e.message || 'Error al actualizar inventario', 400));
      }
    }
  },
);

module.exports = {
  ventasInventarioVehiculoIdGET,
  ventasInventarioVehiculoIdPUT,
};
