/**
 * Script para inicializar la base de datos
 * Uso: node init-db.js
 */

const fs = require('fs');
const path = require('path');
const mysql = require('mysql2/promise');

const DB_CONFIG = {
  host: process.env.DB_HOST || 'localhost',
  user: process.env.DB_USER || 'root',
  password: process.env.DB_PASSWORD || '1234',
  port: process.env.DB_PORT || 3307,
  multipleStatements: true, // Permitir múltiples statements
};

async function initDatabase() {
  let connection;
  try {
    console.log('📦 Conectando a MySQL...');
    console.log(`   Host: ${DB_CONFIG.host}`);
    console.log(`   Puerto: ${DB_CONFIG.port}`);
    console.log(`   Usuario: ${DB_CONFIG.user}`);
    
    // Conexión inicial (sin especificar BD)
    connection = await mysql.createConnection(DB_CONFIG);
    console.log('✅ Conectado a MySQL');

    // Leer archivo SQL
    const sqlFilePath = path.join(__dirname, '..', '..', 'fabricanteVehiculosDB.sql');
    console.log(`📄 Leyendo archivo: ${sqlFilePath}`);
    
    const sqlContent = fs.readFileSync(sqlFilePath, 'utf8');
    console.log('✅ Archivo SQL cargado');

    // Ejecutar SQL - primero intentar DROP DATABASE para empezar limpio
    console.log('🚀 Ejecutando scripts SQL...');
    try {
      console.log('   → Limpiando BD anterior...');
      await connection.query('DROP DATABASE IF EXISTS FabricanteVehiculosDB');
      console.log('   ✓ Base de datos previa eliminada');
    } catch (e) {
      console.log('   ⚠️  No había BD previa');
    }

    // Ejecutar el archivo SQL completo con multipleStatements
    try {
      await connection.query(sqlContent);
      console.log('✅ Base de datos inicializada correctamente');
    } catch (error) {
      console.error('❌ Error durante la ejecución del SQL:', error.message);
      // Intentar continuar para diagnosticar
      if (error.code && (error.code === 'ER_TABLE_EXISTS_ERROR' || error.code === 'ER_DB_CREATE_EXISTS')) {
        console.log('   → Puede deberse a tablas existentes, intentando consultar...');
      }
    }

    // Verificar que las tablas se crearon
    const [tables] = await connection.query(
      "SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'FabricanteVehiculosDB'"
    );
    
    console.log(`\n📊 Tablas creadas: ${tables.length}`);
    tables.forEach(table => {
      console.log(`   - ${table.TABLE_NAME}`);
    });
    
    // Verificar datos insertados
    try {
      const [clientesCount] = await connection.query('SELECT COUNT(*) as cnt FROM Cliente');
      const [componentesCount] = await connection.query('SELECT COUNT(*) as cnt FROM ComponenteFabrica');
      const [proveedoresCount] = await connection.query('SELECT COUNT(*) as cnt FROM Proveedor');
      
      console.log(`\n📦 Datos de ejemplo insertados:`);
      console.log(`   - Clientes: ${clientesCount[0].cnt}`);
      console.log(`   - Componentes: ${componentesCount[0].cnt}`);
      console.log(`   - Proveedores: ${proveedoresCount[0].cnt}`);
    } catch (e) {
      console.log(`\n⚠️  No se pudo verificar datos:`, e.message);
    }

  } catch (error) {
    console.error('❌ Error al inicializar la base de datos:');
    console.error(`\nMensaje: ${error.message}`);
    console.error(`Código: ${error.code}`);
    
    // Debugging adicional
    if (error.errno) console.error(`Errno: ${error.errno}`);
    if (error.syscall) console.error(`Syscall: ${error.syscall}`);
    if (error.fatal) console.error(`Fatal: ${error.fatal}`);
    
    if (error.code === 'PROTOCOL_CONNECTION_LOST') {
      console.error('\n💡 Solución: Asegúrate de que MySQL está corriendo');
    } else if (error.code === 'PROTOCOL_ENQUEUE_AFTER_FATAL_ERROR') {
      console.error('\n💡 Solución: MySQL connection perdida');
    } else if (error.code === 'ER_ACCESS_DENIED_FOR_USER') {
      console.error('\n💡 Solución: Usuario/contraseña de MySQL incorrectos');
      console.error('   Ejecuta con: DB_USER=tu_usuario DB_PASSWORD=tu_password node init-db.js');
    } else if (error.code === 'ENOENT') {
      console.error('\n💡 Solución: No se encuentra el archivo SQL');
      console.error(`   Esperado: ${path.join(__dirname, '..', '..', 'fabricanteVehiculosDB.sql')}`);
    } else if (error.code === 'ECONNREFUSED') {
      console.error('\n💡 Solución: No se pudo conectar a MySQL');
      console.error('   ¿MySQL está corriendo? Prueba:');
      console.error('   Windows: net start MySQL80 (o tu versión)');
      console.error('   Linux: sudo service mysql start');
      console.error('   macOS: brew services start mysql');
    }
    
    process.exit(1);
  } finally {
    if (connection) {
      await connection.end();
      console.log('\n🔌 Desconectado de MySQL');
    }
  }
}

// Ejecutar
initDatabase();
