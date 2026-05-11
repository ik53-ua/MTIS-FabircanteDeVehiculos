/**
 * Script para probar los endpoints de la API
 */

const http = require('http');

function makeRequest(method, path, data = null) {
  return new Promise((resolve, reject) => {
    const options = {
      hostname: 'localhost',
      port: 8000,
      path: path,
      method: method,
      headers: {
        'Content-Type': 'application/json',
      }
    };

    const req = http.request(options, (res) => {
      let body = '';
      res.on('data', (chunk) => { body += chunk; });
      res.on('end', () => {
        try {
          const json = JSON.parse(body);
          resolve({ status: res.statusCode, body: json });
        } catch (e) {
          resolve({ status: res.statusCode, body: body });
        }
      });
    });

    req.on('error', reject);
    
    if (data) {
      req.write(JSON.stringify(data));
    }
    req.end();
  });
}

async function test() {
  console.log('🧪 Probando API REST...\n');

  // Test 1: GET componente
  console.log('1️⃣  GET /api/produccion/componentes/1');
  const comp = await makeRequest('GET', '/api/produccion/componentes/1?WSKey=1234');
  console.log(`   Status: ${comp.status}`);
  console.log(`   Response:`, JSON.stringify(comp.body, null, 2));
  console.log();

  // Test 2: POST pedido de compra
  console.log('2️⃣  POST /api/produccion/pedidosCompra');
  const data = {
    componenteId: '2',
    cantidadRequerida: 5
  };
  console.log(`   Body:`, JSON.stringify(data));
  const pedido = await makeRequest('POST', '/api/produccion/pedidosCompra?WSKey=1234', data);
  console.log(`   Status: ${pedido.status}`);
  console.log(`   Response:`, JSON.stringify(pedido.body, null, 2));
  console.log();

  // Test 3: GET inventario
  console.log('3️⃣  GET /api/produccion/inventario/2');
  const inv = await makeRequest('GET', '/api/produccion/inventario/2?WSKey=1234');
  console.log(`   Status: ${inv.status}`);
  console.log(`   Response:`, JSON.stringify(inv.body, null, 2));
  console.log();

  // Test 4: GET proveedor
  console.log('4️⃣  GET /api/produccion/proveedores/2');
  const prov = await makeRequest('GET', '/api/produccion/proveedores/2?WSKey=1234');
  console.log(`   Status: ${prov.status}`);
  console.log(`   Response:`, JSON.stringify(prov.body, null, 2));
  console.log();
}

test().catch(console.error);
