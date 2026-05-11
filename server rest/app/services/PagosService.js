/**
 * Servicio para gestión de Pagos (Proceso 2.5)
 */

const { queryWithParams, query } = require('../db');
const logger = require('../logger');
const Service = require('./Service');

/**
 * Registrar pago
 */
const registrarPago = ({ pagoRequest }) => new Promise(
  async (resolve, reject) => {
    try {
      logger.info('🔍 registrarPago iniciado con:', JSON.stringify(pagoRequest));
      
      // Validar parámetros requeridos
      if (!pagoRequest.facturaId) {
        logger.warn('❌ facturaId es requerido');
        return reject({ mensaje: 'facturaId es requerido' });
      }
      if (!pagoRequest.cantidad) {
        logger.warn('❌ cantidad es requerido');
        return reject({ mensaje: 'cantidad es requerido' });
      }
      
      logger.info('✓ Validaciones pasadas. Consultando factura...');
      
      // Verificar que la factura existe
      const facturaSql = `SELECT id_factura, total FROM Factura WHERE id_factura = ?`;
      logger.info('📝 SQL factura:', facturaSql, 'Params:', [pagoRequest.facturaId]);
      
      const facturaResult = await queryWithParams(facturaSql, [pagoRequest.facturaId]);
      logger.info('✓ Resultado factura:', JSON.stringify(facturaResult));
      
      if (facturaResult.length === 0) {
        logger.warn('❌ Factura no encontrada:', pagoRequest.facturaId);
        return reject({ mensaje: 'Factura no encontrada' });
      }

      const sql = `
        INSERT INTO Pago 
        (id_factura, monto, metodo_pago)
        VALUES (?, ?, ?)
      `;

      const params = [
        pagoRequest.facturaId,
        pagoRequest.cantidad,
        pagoRequest.metodoPago || 'TRANSFERENCIA',
      ];

      logger.info('🔍 SQL params antes de ejecutar:', JSON.stringify(params));
      logger.info('📝 SQL INSERT:', sql.trim());
      
      const result = await queryWithParams(sql, params);
      logger.info('✓ INSERT exitoso. insertId:', result.insertId);
      
      // Actualizar estado de factura si el pago es total
      const totalFactura = facturaResult[0].total;
      if (pagoRequest.cantidad >= totalFactura) {
        logger.info('💰 Pago total detectado. Actualizando factura...');
        await actualizarFacturaPagada(pagoRequest.facturaId);
      }
      
      resolve({
        pagoId: result.insertId.toString(),
        estado: 'COMPLETADO',
      });
    } catch (e) {
      logger.error('❌ Error al registrar pago:', e.message);
      logger.error('   Stack:', e.stack);
      reject({
        mensaje: e.message || 'Error al registrar pago',
      });
    }
  },
);

/**
 * Consultar pago
 */
const consultarPago = ({ id }) => new Promise(
  async (resolve, reject) => {
    try {
      const sql = `
        SELECT 
          id_pago as pagoId,
          id_factura as facturaId,
          monto,
          metodo_pago as metodoPago,
          'COMPLETADO' as estado,
          fecha_pago as fechaPago
        FROM Pago
        WHERE id_pago = ?
      `;

      const result = await queryWithParams(sql, [id]);
      
      if (result.length === 0) {
        return reject({
          mensaje: `Pago ${id} no encontrado`,
        });
      }

      resolve(result[0]);
    } catch (e) {
      logger.error('Error al consultar pago:', e);
      reject({
        mensaje: e.message || 'Error al consultar pago',
      });
    }
  },
);

/**
 * Actualizar factura como pagada
 */
const actualizarFacturaPagada = async (facturaId) => {
  try {
    const sql = `
      UPDATE Factura
      SET estado = 'PAGADA'
      WHERE id_factura = ?
    `;
    await queryWithParams(sql, [facturaId]);
  } catch (e) {
    logger.error('Error al actualizar factura:', e);
  }
};

/**
 * Registrar pago (endpoint)
 */
const ventasPagosPOST = (params) => new Promise(
  async (resolve, reject) => {
    try {
      logger.info('🔍 ventasPagosPOST params recibidos:', JSON.stringify(params));
      
      // Extraer el body correctamente
      let pagoRequest = params.pagoRequest || params.PagoRequest || params.body;
      
      logger.info('✓ pagoRequest extraído:', JSON.stringify(pagoRequest));
      
      // Validar que tengamos los campos requeridos
      if (!pagoRequest) {
        logger.warn('❌ No se encontró pagoRequest');
        return reject(Service.rejectResponse('Cuerpo de solicitud requerido', 400));
      }
      
      if (!pagoRequest.facturaId) {
        logger.warn('❌ facturaId no proporcionado');
        return reject(Service.rejectResponse('facturaId es requerido', 400));
      }
      
      if (!pagoRequest.cantidad) {
        logger.warn('❌ cantidad no proporcionado');
        return reject(Service.rejectResponse('cantidad es requerido', 400));
      }
      
      logger.info('✓ Validaciones en endpoint pasadas, llamando registrarPago...');
      
      const result = await registrarPago({ pagoRequest });
      logger.info('✓ Resultado de registrarPago:', JSON.stringify(result));
      
      resolve(Service.successResponse(result, 201));
    } catch (e) {
      logger.error('❌ Error en ventasPagosPOST:', e.message);
      logger.error('   Stack:', e.stack);
      
      // Si ya es una respuesta de Service, devolverla tal cual
      if (e.code) {
        reject(e);
      } else {
        reject(Service.rejectResponse(e.mensaje || e.message || 'Error al registrar pago', 400));
      }
    }
  },
);

/**
 * Consultar pago (endpoint)
 */
const ventasPagosIdGET = ({ id }) => new Promise(
  async (resolve, reject) => {
    try {
      logger.info('🔍 ventasPagosIdGET params recibidos - id:', id);
      
      const result = await consultarPago({ id });
      logger.info('✓ Pago consultado:', JSON.stringify(result));
      
      resolve(Service.successResponse(result, 200));
    } catch (e) {
      logger.error('❌ Error en ventasPagosIdGET:', e.message);
      reject(Service.rejectResponse(
        e.mensaje || 'Pago no encontrado',
        404,
      ));
    }
  },
);

module.exports = {
  ventasPagosIdGET,
  ventasPagosPOST,
};
