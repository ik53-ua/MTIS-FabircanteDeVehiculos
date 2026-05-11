/**
 * Pool de conexiones a la base de datos MySQL
 */

const mysql = require('mysql2/promise');
const dbConfig = require('./db-config');
const logger = require('./logger');

let pool;

/**
 * Inicializa el pool de conexiones
 */
async function initializePool() {
  try {
    pool = mysql.createPool(dbConfig);
    logger.info('Pool de conexiones a base de datos inicializado correctamente');
  } catch (error) {
    logger.error('Error al inicializar pool de conexiones:', error);
    process.exit(1);
  }
}

/**
 * Obtiene una conexión del pool
 */
async function getConnection() {
  if (!pool) {
    await initializePool();
  }
  return pool.getConnection();
}

/**
 * Ejecuta una query sin parámetros
 */
async function query(sql) {
  const conn = await getConnection();
  try {
    const [result] = await conn.query(sql);
    return result;
  } finally {
    conn.release();
  }
}

/**
 * Ejecuta una query con parámetros (prepared statement)
 */
async function queryWithParams(sql, params) {
  const conn = await getConnection();
  try {
    const [result] = await conn.execute(sql, params);
    return result;
  } finally {
    conn.release();
  }
}

module.exports = {
  initializePool,
  getConnection,
  query,
  queryWithParams,
};
