/**
 * Servicio para gestión de Facturas (Proceso 2.5)
 */

const { queryWithParams, query } = require('../db');
const logger = require('../logger');
const Service = require('./Service');

/**
 * Generar factura
 */
const generarFactura = ({ facturaRequest }) => new Promise(
  async (resolve, reject) => {
    try {
      logger.info('🔍 generarFactura iniciado con:', JSON.stringify(facturaRequest));
      
      // Validar parámetros requeridos
      if (!facturaRequest.ventaId) {
        logger.warn('❌ ventaId es requerido');
        return reject({ mensaje: 'ventaId es requerido' });
      }
      if (!facturaRequest.importe) {
        logger.warn('❌ importe es requerido');
        return reject({ mensaje: 'importe es requerido' });
      }
      
      logger.info('✓ Validaciones pasadas. Consultando venta...');
      
      // Verificar que la venta existe
      const ventaSql = `SELECT id_venta, precio_final FROM Venta WHERE id_venta = ?`;
      logger.info('📝 SQL venta:', ventaSql, 'Params:', [facturaRequest.ventaId]);
      
      const ventaResult = await queryWithParams(ventaSql, [facturaRequest.ventaId]);
      logger.info('✓ Resultado venta:', JSON.stringify(ventaResult));
      
      if (ventaResult.length === 0) {
        logger.warn('❌ Venta no encontrada:', facturaRequest.ventaId);
        return reject({ mensaje: 'Venta no encontrada' });
      }
      
      const importe = facturaRequest.importe || ventaResult[0].precio_final;
      logger.info('💰 Importe:', importe);

      const sql = `
        INSERT INTO Factura 
        (id_venta, total, estado)
        VALUES (?, ?, 'PENDIENTE')
      `;

      const params = [
        facturaRequest.ventaId,
        importe,
      ];

      logger.info('🔍 SQL params antes de ejecutar:', JSON.stringify(params));
      logger.info('📝 SQL INSERT:', sql.trim());
      
      const result = await queryWithParams(sql, params);
      logger.info('✓ INSERT exitoso. insertId:', result.insertId);
      
      // Actualizar estado de la venta
      const updateVentaSql = `UPDATE Venta SET estado_venta = 'COMPLETADA' WHERE id_venta = ?`;
      logger.info('📝 SQL UPDATE venta:', updateVentaSql, 'Params:', [facturaRequest.ventaId]);
      
      await queryWithParams(updateVentaSql, [facturaRequest.ventaId]);
      logger.info('✓ Estado venta actualizado a COMPLETADA');
      
      resolve({
        facturaId: result.insertId.toString(),
        estado: 'PENDIENTE',
      });
    } catch (e) {
      logger.error('❌ Error al generar factura:', e.message);
      logger.error('   Stack:', e.stack);
      reject({
        mensaje: e.message || 'Error al generar factura',
      });
    }
  },
);

/**
 * Consultar factura
 */
const consultarFactura = ({ id }) => new Promise(
  async (resolve, reject) => {
    try {
      const sql = `
        SELECT 
          id_factura as facturaId,
          id_venta as ventaId,
          total,
          estado,
          fecha_emision as fechaEmision
        FROM Factura
        WHERE id_factura = ?
      `;

      const result = await queryWithParams(sql, [id]);
      
      if (result.length === 0) {
        return reject({
          mensaje: `Factura ${id} no encontrada`,
        });
      }

      resolve(result[0]);
    } catch (e) {
      logger.error('Error al consultar factura:', e);
      reject({
        mensaje: e.message || 'Error al consultar factura',
      });
    }
  },
);

/**
 * Generar factura (endpoint)
 */
const ventasFacturasPOST = (params) => new Promise(
  async (resolve, reject) => {
    try {
      logger.info('🔍 ventasFacturasPOST params recibidos:', JSON.stringify(params));
      
      // Extraer el body correctamente
      let facturaRequest = params.facturaRequest || params.FacturaRequest || params.body;
      
      logger.info('✓ facturaRequest extraído:', JSON.stringify(facturaRequest));
      
      // Validar que tengamos los campos requeridos
      if (!facturaRequest) {
        logger.warn('❌ No se encontró facturaRequest');
        return reject(Service.rejectResponse('Cuerpo de solicitud requerido', 400));
      }
      
      if (!facturaRequest.ventaId) {
        logger.warn('❌ ventaId no proporcionado');
        return reject(Service.rejectResponse('ventaId es requerido', 400));
      }
      
      if (!facturaRequest.importe) {
        logger.warn('❌ importe no proporcionado');
        return reject(Service.rejectResponse('importe es requerido', 400));
      }
      
      logger.info('✓ Validaciones en endpoint pasadas, llamando generarFactura...');
      
      const result = await generarFactura({ facturaRequest });
      logger.info('✓ Resultado de generarFactura:', JSON.stringify(result));
      
      resolve(Service.successResponse(result, 201));
    } catch (e) {
      logger.error('❌ Error en ventasFacturasPOST:', e.message);
      logger.error('   Stack:', e.stack);
      
      // Si ya es una respuesta de Service, devolverla tal cual
      if (e.code) {
        reject(e);
      } else {
        reject(Service.rejectResponse(e.mensaje || e.message || 'Error al generar factura', 400));
      }
    }
  },
);

/**
 * Consultar factura (endpoint)
 */
const ventasFacturasIdGET = ({ id }) => new Promise(
  async (resolve, reject) => {
    try {
      logger.info('🔍 ventasFacturasIdGET params recibidos - id:', id);
      
      const result = await consultarFactura({ id });
      logger.info('✓ Factura consultada:', JSON.stringify(result));
      
      resolve(Service.successResponse(result, 200));
    } catch (e) {
      logger.error('❌ Error en ventasFacturasIdGET:', e.message);
      reject(Service.rejectResponse(
        e.mensaje || 'Factura no encontrada',
        404,
      ));
    }
  },
);

module.exports = {
  ventasFacturasIdGET,
  ventasFacturasPOST,
};
