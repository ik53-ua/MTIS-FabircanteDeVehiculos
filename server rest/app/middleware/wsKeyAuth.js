const db = require('../db');
const logger = require('../logger');

/**
 * Middleware para validar WSKey en headers
 * El WSKey debe enviarse en el header 'x-api-key'
 */
const wsKeyAuth = async (request, response, next) => {
  try {
    // Obtener WSKey del header
    const wsKey = request.headers['x-api-key'];

    if (!wsKey) {
      logger.warn('Solicitud sin WSKey detectada en', request.method, request.path);
      return response.status(401).json({ 
        error: 'Unauthorized - WSKey requerido en header x-api-key' 
      });
    }

    // Validar WSKey en base de datos
    const sql = `SELECT id FROM RestKey WHERE rest_key = ?`;
    const result = await db.queryWithParams(sql, [wsKey]);

    if (!result || result.length === 0) {
      logger.warn('WSKey inválido intentado:', wsKey);
      return response.status(403).json({ 
        error: 'Forbidden - WSKey inválido' 
      });
    }

    // WSKey válido - continuar
    logger.info('Autenticación exitosa con WSKey:', wsKey.substring(0, 4) + '***');
    next();
  } catch (error) {
    logger.error('Error en validación de WSKey:', error);
    return response.status(500).json({ 
      error: 'Error en validación de autenticación' 
    });
  }
};

module.exports = wsKeyAuth;
