/**
 * Servicio para gestión de Pedidos de Compra (Proceso 2.4)
 */

const { queryWithParams, query } = require('../db');
const logger = require('../logger');
const Service = require('./Service');

/**
 * Crear orden de compra automática
 */
const crearPedidoCompra = ({ pedidoCompraRequest }) => new Promise(
  async (resolve, reject) => {
    try {
      logger.info('🔍 crearPedidoCompra iniciado con:', JSON.stringify(pedidoCompraRequest));
      
      // Validar parámetros
      if (!pedidoCompraRequest.componenteId) {
        logger.warn('❌ componenteId es requerido');
        return reject({ mensaje: 'componenteId es requerido' });
      }
      if (!pedidoCompraRequest.cantidadRequerida) {
        logger.warn('❌ cantidadRequerida es requerido');
        return reject({ mensaje: 'cantidadRequerida es requerido' });
      }

      logger.info('✓ Validaciones pasadas. Consultando componente...');
      
      // Obtener info del componente para calcular el coste
      const componenteSql = `SELECT precio_unitario FROM ComponenteFabrica WHERE id_componente = ?`;
      logger.info('📝 SQL componente:', componenteSql, 'Params:', [pedidoCompraRequest.componenteId]);
      
      const componenteResult = await queryWithParams(componenteSql, [pedidoCompraRequest.componenteId]);
      logger.info('✓ Resultado componente:', JSON.stringify(componenteResult));
      
      if (componenteResult.length === 0) {
        logger.warn('❌ Componente no encontrado con ID:', pedidoCompraRequest.componenteId);
        return reject({ mensaje: 'Componente no encontrado' });
      }
      
      const precioUnitario = componenteResult[0].precio_unitario || 0;
      const costoTotal = precioUnitario * pedidoCompraRequest.cantidadRequerida;
      logger.info('💰 Precio unitario:', precioUnitario, 'Costo total:', costoTotal);

      const sql = `
        INSERT INTO PedidoCompra 
        (id_componente, id_proveedor, cantidad_solicitada, coste_total_estimado, estado)
        VALUES (?, ?, ?, ?, 'PENDIENTE')
      `;

      const proveedorId = pedidoCompraRequest.proveedorId !== undefined ? pedidoCompraRequest.proveedorId : 1;

      const params = [
        pedidoCompraRequest.componenteId,
        proveedorId,
        pedidoCompraRequest.cantidadRequerida,
        costoTotal,
      ];

      logger.info('🔍 SQL params antes de ejecutar:', JSON.stringify(params));
      logger.info('📝 SQL INSERT:', sql.trim());
      
      const result = await queryWithParams(sql, params);
      logger.info('✓ INSERT exitoso. insertId:', result.insertId);
      
      resolve({
        pedidoId: result.insertId.toString(),
        estado: 'PENDIENTE',
      });
    } catch (e) {
      logger.error('❌ Error al crear pedido de compra:', e.message);
      logger.error('   Stack:', e.stack);
      reject({
        mensaje: e.message || 'Error al crear pedido de compra',
      });
    }
  },
);

/**
 * Consultar orden de compra
 */
const consultarPedidoCompra = ({ id }) => new Promise(
  async (resolve, reject) => {
    try {
      const sql = `
        SELECT 
          id_pedido_compra as pedidoId,
          id_componente as componenteId,
          cantidad_solicitada as cantidadSolicitada,
          coste_total_estimado as costoTotalEstimado,
          estado,
          fecha_pedido as fechaPedido
        FROM PedidoCompra
        WHERE id_pedido_compra = ?
      `;

      const result = await queryWithParams(sql, [id]);
      
      if (result.length === 0) {
        return reject({
          mensaje: `Pedido ${id} no encontrado`,
        });
      }

      resolve(result[0]);
    } catch (e) {
      logger.error('Error al consultar pedido:', e);
      reject({
        mensaje: e.message || 'Error al consultar pedido',
      });
    }
  },
);

/**
 * Crear pedido de compra (endpoint)
 */
const produccionPedidosCompraPOST = (params) => new Promise(
  async (resolve, reject) => {
    try {
      logger.info('🔍 produccionPedidosCompraPOST params recibidos:', JSON.stringify(params));
      
      // Extraer el body correctamente
      let pedidoCompraRequest = params.pedidoCompraRequest || params.PedidoCompraRequest || params.body;
      
      // Si no existe pedidoCompraRequest, intentar extraer directamente de params
      if (!pedidoCompraRequest) {
        logger.warn('⚠️ No se encontró pedidoCompraRequest, intentando extraer de params directamente');
        pedidoCompraRequest = {
          componenteId: params.componenteId,
          cantidadRequerida: params.cantidadRequerida,
          proveedorId: params.proveedorId,
        };
      }
      
      logger.info('✓ pedidoCompraRequest extraído:', JSON.stringify(pedidoCompraRequest));
      
      // Validar que tengamos los campos requeridos
      if (!pedidoCompraRequest.componenteId) {
        logger.warn('❌ componenteId no proporcionado');
        return reject(Service.rejectResponse('componenteId es requerido', 400));
      }
      if (!pedidoCompraRequest.cantidadRequerida) {
        logger.warn('❌ cantidadRequerida no proporcionado');
        return reject(Service.rejectResponse('cantidadRequerida es requerido', 400));
      }
      
      logger.info('✓ Validaciones en endpoint pasadas, llamando crearPedidoCompra...');
      
      const result = await crearPedidoCompra({ pedidoCompraRequest });
      logger.info('✓ Resultado de crearPedidoCompra:', JSON.stringify(result));
      
      resolve(Service.successResponse(result, 201));
    } catch (e) {
      logger.error('❌ Error en produccionPedidosCompraPOST:', e.message);
      logger.error('   Stack:', e.stack);
      
      // Si ya es una respuesta de Service, devolverla tal cual
      if (e.code) {
        reject(e);
      } else {
        reject(Service.rejectResponse(e.mensaje || e.message || 'Error al crear pedido de compra', 400));
      }
    }
  },
);

/**
 * Consultar pedido de compra (endpoint)
 */
const produccionPedidosCompraIdGET = ({ id }) => new Promise(
  async (resolve, reject) => {
    try {
      const result = await consultarPedidoCompra({ id });
      resolve(Service.successResponse(result, 200));
    } catch (e) {
      reject(Service.rejectResponse(
        e.mensaje || 'Pedido no encontrado',
        404,
      ));
    }
  },
);

module.exports = {
  produccionPedidosCompraPOST,
  produccionPedidosCompraIdGET,
  produccionPedidosCompraPOST,
};
