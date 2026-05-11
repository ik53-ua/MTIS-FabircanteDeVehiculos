/* eslint-disable no-unused-vars */
const { queryWithParams } = require('../db');
const logger = require('../logger');
const Service = require('./Service');

/**
 * Consultar datos del proveedor
 */
const obtenerProveedor = ({ id }) => new Promise(
  async (resolve, reject) => {
    try {
      const sql = `
        SELECT 
          id_proveedor as proveedorId,
          nombre as nombreEmpresa,
          contacto,
          telefono,
          email
        FROM Proveedor
        WHERE id_proveedor = ?
      `;

      const result = await queryWithParams(sql, [id]);
      
      if (result.length === 0) {
        return reject({
          mensaje: `Proveedor ${id} no encontrado`,
        });
      }

      resolve(result[0]);
    } catch (e) {
      logger.error('Error al obtener proveedor:', e);
      reject({
        mensaje: e.message || 'Error al obtener proveedor',
      });
    }
  },
);

/**
 * Consultar datos del proveedor (endpoint)
 */
const produccionProveedoresIdGET = ({ id }) => new Promise(
  async (resolve, reject) => {
    try {
      const result = await obtenerProveedor({ id });
      resolve(Service.successResponse(result, 200));
    } catch (e) {
      reject(Service.rejectResponse(
        e.mensaje || 'Proveedor no encontrado',
        404,
      ));
    }
  },
);

module.exports = {
  produccionProveedoresIdGET,
};
