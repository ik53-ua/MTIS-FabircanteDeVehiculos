/* eslint-disable no-unused-vars */
const { queryWithParams } = require('../db');
const logger = require('../logger');
const Service = require('./Service');

/**
 * Actualizar planificación de producción
 */
const actualizarPlanificacion = ({ id, planificacionUpdateRequest }) => new Promise(
  async (resolve, reject) => {
    try {
      logger.info('🔍 actualizarPlanificacion iniciado - id:', id, 'con:', JSON.stringify(planificacionUpdateRequest));
      
      // Validar parámetros requeridos
      if (!planificacionUpdateRequest.estadoProduccion && !planificacionUpdateRequest.nuevaFechaReanudacion) {
        logger.warn('❌ Al menos un campo para actualizar es requerido');
        return reject({ mensaje: 'Al menos un campo para actualizar es requerido' });
      }
      
      logger.info('✓ Validaciones pasadas. Consultando planificación...');
      
      // Verificar que existe la planificación
      const existsSql = `SELECT id_planificacion FROM PlanificacionProduccion WHERE id_planificacion = ?`;
      logger.info('📝 SQL verificación:', existsSql, 'Params:', [id]);
      
      const existsResult = await queryWithParams(existsSql, [id]);
      logger.info('✓ Resultado verificación:', JSON.stringify(existsResult));
      
      if (existsResult.length === 0) {
        logger.warn('❌ Planificación no encontrada:', id);
        return reject({ mensaje: `Planificación ${id} no encontrada` });
      }

      // Construir actualización dinámica
      let updateFields = [];
      let updateParams = [];
      
      if (planificacionUpdateRequest.estadoProduccion) {
        logger.info('📝 Actualizando estado a:', planificacionUpdateRequest.estadoProduccion);
        updateFields.push('estado = ?');
        updateParams.push(planificacionUpdateRequest.estadoProduccion);
      }
      
      if (planificacionUpdateRequest.nuevaFechaReanudacion) {
        logger.info('📝 Actualizando fecha a:', planificacionUpdateRequest.nuevaFechaReanudacion);
        updateFields.push('fecha_inicio = ?');
        updateParams.push(planificacionUpdateRequest.nuevaFechaReanudacion);
      }

      updateParams.push(id);
      const sql = `UPDATE PlanificacionProduccion SET ${updateFields.join(', ')} WHERE id_planificacion = ?`;
      
      logger.info('📝 SQL UPDATE:', sql.trim());
      logger.info('🔍 SQL params:', JSON.stringify(updateParams));
      
      await queryWithParams(sql, updateParams);
      logger.info('✓ UPDATE exitoso');
      
      resolve({
        mensaje: 'Planificación actualizada correctamente',
      });
    } catch (e) {
      logger.error('❌ Error al actualizar planificación:', e.message);
      logger.error('   Stack:', e.stack);
      reject({
        mensaje: e.message || 'Error al actualizar planificación',
      });
    }
  },
);

/**
 * Actualizar planificación (endpoint)
 */
const produccionPlanificacionProduccionIdPUT = (params) => new Promise(
  async (resolve, reject) => {
    try {
      logger.info('🔍 produccionPlanificacionProduccionIdPUT params recibidos:', JSON.stringify(params));
      
      // Extraer parámetros
      const id = params.id;
      let planificacionUpdateRequest = params.planificacionUpdateRequest || params.PlanificacionUpdateRequest || params.body;
      
      logger.info('📝 id:', id);
      logger.info('✓ planificacionUpdateRequest extraído:', JSON.stringify(planificacionUpdateRequest));
      
      // Validar que tengamos los campos requeridos
      if (!id) {
        logger.warn('❌ id no proporcionado');
        return reject(Service.rejectResponse('id es requerido', 400));
      }
      
      if (!planificacionUpdateRequest) {
        logger.warn('❌ No se encontró planificacionUpdateRequest');
        return reject(Service.rejectResponse('Cuerpo de solicitud requerido', 400));
      }
      
      if (!planificacionUpdateRequest.estadoProduccion && !planificacionUpdateRequest.nuevaFechaReanudacion) {
        logger.warn('❌ Al menos un campo para actualizar es requerido');
        return reject(Service.rejectResponse('Al menos un campo para actualizar es requerido', 400));
      }
      
      logger.info('✓ Validaciones en endpoint pasadas, llamando actualizarPlanificacion...');
      
      const result = await actualizarPlanificacion({ id, planificacionUpdateRequest });
      logger.info('✓ Resultado de actualizarPlanificacion:', JSON.stringify(result));
      
      resolve(Service.successResponse(result, 200));
    } catch (e) {
      logger.error('❌ Error en produccionPlanificacionProduccionIdPUT:', e.message);
      logger.error('   Stack:', e.stack);
      
      if (e.code) {
        reject(e);
      } else {
        reject(Service.rejectResponse(e.mensaje || e.message || 'Error al actualizar planificación', 400));
      }
    }
  },
);

module.exports = {
  produccionPlanificacionProduccionIdPUT,
};
