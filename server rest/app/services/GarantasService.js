/* eslint-disable no-unused-vars */
const { queryWithParams } = require('../db');
const logger = require('../logger');
const Service = require('./Service');

/**
 * Activar garantía para un vehículo
 */
const activarGarantia = ({ garantiaRequest }) => new Promise(
  async (resolve, reject) => {
    try {
      logger.info('🔍 activarGarantia iniciado con:', JSON.stringify(garantiaRequest));
      
      // Validar parámetros requeridos
      if (!garantiaRequest.vehiculoId) {
        logger.warn('❌ vehiculoId es requerido');
        return reject({ mensaje: 'vehiculoId es requerido' });
      }
      
      logger.info('✓ Validaciones pasadas. Consultando vehículo...');
      
      // Verificar que el vehículo existe
      const vehiculoSql = `SELECT vin FROM InventarioVehiculo WHERE vin = ?`;
      logger.info('📝 SQL vehículo:', vehiculoSql, 'Params:', [garantiaRequest.vehiculoId]);
      
      const vehiculoResult = await queryWithParams(vehiculoSql, [garantiaRequest.vehiculoId]);
      logger.info('✓ Resultado vehículo:', JSON.stringify(vehiculoResult));
      
      if (vehiculoResult.length === 0) {
        logger.warn('❌ Vehículo no encontrado:', garantiaRequest.vehiculoId);
        return reject({ mensaje: 'Vehículo no encontrado' });
      }

      const duracionMeses = garantiaRequest.duracionMeses || 24;
      const fechaInicio = new Date();
      const fechaFin = new Date(fechaInicio.getFullYear(), fechaInicio.getMonth() + duracionMeses, fechaInicio.getDate());

      logger.info('📅 Fechas garantía - Inicio:', fechaInicio, 'Fin:', fechaFin);

      const sql = `
        INSERT INTO Garantia 
        (vin, fecha_inicio, fecha_fin, duracion_meses, tipo_cobertura, estado)
        VALUES (?, ?, ?, ?, ?, 'ACTIVA')
      `;

      const params = [
        garantiaRequest.vehiculoId,
        fechaInicio.toISOString().split('T')[0],
        fechaFin.toISOString().split('T')[0],
        duracionMeses,
        'Cobertura total de fábrica',
      ];

      logger.info('🔍 SQL params antes de ejecutar:', JSON.stringify(params));
      logger.info('📝 SQL INSERT:', sql.trim());
      
      const result = await queryWithParams(sql, params);
      logger.info('✓ INSERT exitoso. insertId:', result.insertId);
      
      resolve({
        garantiaId: result.insertId.toString(),
        estado: 'ACTIVA',
        duracionMeses,
        vehiculoId: garantiaRequest.vehiculoId,
      });
    } catch (e) {
      logger.error('❌ Error al activar garantía:', e.message);
      logger.error('   Stack:', e.stack);
      reject({
        mensaje: e.message || 'Error al activar garantía',
      });
    }
  },
);

/**
 * Consultar garantía de un vehículo
 */
const consultarGarantia = ({ vehiculoId }) => new Promise(
  async (resolve, reject) => {
    try {
      const sql = `
        SELECT 
          id_garantia as garantiaId,
          vin as vehiculoId,
          fecha_inicio as fechaInicio,
          fecha_fin as fechaFin,
          duracion_meses as duracionMeses,
          tipo_cobertura as tipoCobertura,
          estado
        FROM Garantia
        WHERE vin = ?
        ORDER BY fecha_inicio DESC
        LIMIT 1
      `;

      const result = await queryWithParams(sql, [vehiculoId]);
      
      if (result.length === 0) {
        return reject({
          mensaje: `No hay garantía registrada para el vehículo ${vehiculoId}`,
        });
      }

      resolve(result[0]);
    } catch (e) {
      logger.error('Error al consultar garantía:', e);
      reject({
        mensaje: e.message || 'Error al consultar garantía',
      });
    }
  },
);

/**
 * Activar garantía (endpoint)
 */
const ventasGarantiasPOST = (params) => new Promise(
  async (resolve, reject) => {
    try {
      logger.info('🔍 ventasGarantiasPOST params recibidos:', JSON.stringify(params));
      
      // Extraer el body correctamente
      let garantiaRequest = params.garantiaRequest || params.GarantiaRequest || params.body;
      
      logger.info('✓ garantiaRequest extraído:', JSON.stringify(garantiaRequest));
      
      // Validar que tengamos los campos requeridos
      if (!garantiaRequest) {
        logger.warn('❌ No se encontró garantiaRequest');
        return reject(Service.rejectResponse('Cuerpo de solicitud requerido', 400));
      }
      
      if (!garantiaRequest.vehiculoId) {
        logger.warn('❌ vehiculoId no proporcionado');
        return reject(Service.rejectResponse('vehiculoId es requerido', 400));
      }
      
      logger.info('✓ Validaciones en endpoint pasadas, llamando activarGarantia...');
      
      const result = await activarGarantia({ garantiaRequest });
      logger.info('✓ Resultado de activarGarantia:', JSON.stringify(result));
      
      resolve(Service.successResponse(result, 201));
    } catch (e) {
      logger.error('❌ Error en ventasGarantiasPOST:', e.message);
      logger.error('   Stack:', e.stack);
      
      // Si ya es una respuesta de Service, devolverla tal cual
      if (e.code) {
        reject(e);
      } else {
        reject(Service.rejectResponse(e.mensaje || e.message || 'Error al activar garantía', 400));
      }
    }
  },
);

/**
 * Consultar garantía (endpoint)
 */
const ventasGarantiasVehiculoIdGET = ({ vehiculoId }) => new Promise(
  async (resolve, reject) => {
    try {
      logger.info('🔍 ventasGarantiasVehiculoIdGET params recibidos - vehiculoId:', vehiculoId);
      
      const result = await consultarGarantia({ vehiculoId });
      logger.info('✓ Garantía consultada:', JSON.stringify(result));
      
      resolve(Service.successResponse(result, 200));
    } catch (e) {
      logger.error('❌ Error en ventasGarantiasVehiculoIdGET:', e.message);
      reject(Service.rejectResponse(
        e.mensaje || 'Garantía no encontrada',
        404,
      ));
    }
  },
);

module.exports = {
  ventasGarantiasPOST,
  ventasGarantiasVehiculoIdGET,
};
