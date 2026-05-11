/* eslint-disable no-unused-vars */
const { queryWithParams } = require('../db');
const logger = require('../logger');
const Service = require('./Service');

/**
 * Obtener información del componente
 */
const obtenerComponente = ({ id }) => new Promise(
  async (resolve, reject) => {
    try {
      const sql = `
        SELECT 
          id_componente as componenteId,
          referencia,
          nombre,
          descripcion,
          id_proveedor as proveedorId,
          precio_unitario as precioUnitario
        FROM ComponenteFabrica
        WHERE id_componente = ?
      `;

      const result = await queryWithParams(sql, [id]);
      
      if (result.length === 0) {
        return reject({
          mensaje: `Componente ${id} no encontrado`,
        });
      }

      resolve(result[0]);
    } catch (e) {
      logger.error('Error al obtener componente:', e);
      reject({
        mensaje: e.message || 'Error al obtener componente',
      });
    }
  },
);

/**
 * Obtener información del componente (endpoint)
 */
const produccionComponentesIdGET = ({ id }) => new Promise(
  async (resolve, reject) => {
    try {
      const result = await obtenerComponente({ id });
      resolve(Service.successResponse(result, 200));
    } catch (e) {
      reject(Service.rejectResponse(
        e.mensaje || 'Componente no encontrado',
        404,
      ));
    }
  },
);

module.exports = {
  produccionComponentesIdGET,
};
