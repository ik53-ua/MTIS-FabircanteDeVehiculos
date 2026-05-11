/* eslint-disable no-unused-vars */
const { queryWithParams } = require('../db');
const logger = require('../logger');
const Service = require('./Service');

/**
 * Consultar stock de un componente
 */
const obtenerInventarioComponente = ({ componenteId }) => new Promise(
  async (resolve, reject) => {
    try {
      const sql = `
        SELECT 
          ic.id_componente as componenteId,
          ic.cantidad_actual as cantidadActual,
          ic.umbral_minimo as umbralMinimo,
          ic.ultima_revision as ultimaRevision,
          cf.nombre as nombreComponente
        FROM InventarioComponente ic
        JOIN ComponenteFabrica cf ON ic.id_componente = cf.id_componente
        WHERE ic.id_componente = ?
      `;

      const result = await queryWithParams(sql, [componenteId]);
      
      if (result.length === 0) {
        return reject({
          mensaje: `Componente ${componenteId} no encontrado en inventario`,
        });
      }

      resolve(result[0]);
    } catch (e) {
      logger.error('Error al consultar inventario:', e);
      reject({
        mensaje: e.message || 'Error al consultar inventario',
      });
    }
  },
);

/**
 * Consultar stock de un componente (endpoint)
 */
const produccionInventarioComponenteIdGET = ({ componenteId }) => new Promise(
  async (resolve, reject) => {
    try {
      const result = await obtenerInventarioComponente({ componenteId });
      resolve(Service.successResponse(result, 200));
    } catch (e) {
      reject(Service.rejectResponse(
        e.mensaje || 'Inventario no encontrado',
        404,
      ));
    }
  },
);

module.exports = {
  produccionInventarioComponenteIdGET,
};
