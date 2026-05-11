# 🚀 Guía de Pruebas con Postman - API REST Fabricante de Vehículos

## 📋 Requisitos Previos

1. **Servidor ejecutándose**
   ```bash
   cd "server rest/app"
   npm run init-db    # Inicializar BD (una sola vez)
   npm start          # Iniciar servidor en puerto 8000
   ```

2. **Postman instalado**
   - Descargar desde https://www.postman.com/downloads/

## 🔐 Autenticación con x-api-key

### ¿Qué es x-api-key?
`x-api-key` es un header de autenticación obligatorio que valida todas las solicitudes a la API REST.

### Keys válidas (en la BD)
```
1234
admin-rest-key
```

### Cómo agregar x-api-key en Postman

**En TODAS las solicitudes**, agrega este header:

```
Header Name: x-api-key
Header Value: 1234
```

---

## 📦 Importar Colección en Postman

### Pasos
1. Abre Postman
2. Click en **"Import"** (esquina superior izquierda)
3. Selecciona el archivo `Postman_Collection.json`
4. Se cargarán automáticamente todos los endpoints con ejemplos

---

## ⚙️ Cambios en Endpoints PUT - IMPORTANTE

Los endpoints **PUT han sido completamente refactorizados** para funcionar correctamente. Aquí está lo que cambió:

### Problema Original
Los PUT endpoints (`planificacionProduccion/{id}` e `inventario/{vehiculoId}`) no funcionaban porque:
- Esperaban parámetros destructurados con nombres específicos como `wSKey` (que ya no existe)
- Los parámetros reales llegaban dentro de un objeto `body`
- Provocaba errores: **"Cannot read properties of undefined"**

### Solución Implementada
Todos los endpoints PUT ahora:

1. **Reciben un objeto `params` unificado** (en lugar de parámetros destructurados)
2. **Extraen automáticamente el body** desde `params.body` o `params.xxxRequest`
3. **Validan los parámetros requeridos** antes de procesar
4. **Incluyen logging detallado** para debug (con emojis: 🔍, ✓, ❌)

### Endpoints PUT Corregidos

#### ✅ PUT /api/produccion/planificacionProduccion/{id}
- **Cambio:** Ahora acepta parámetros desde `params.body`
- **Extrae:** `estadoProduccion` y `nuevaFechaReanudacion`
- **Valida:** Al menos uno de los dos campos debe estar presente
- **Responde:** Con mensaje de éxito (200) o error (400)

#### ✅ PUT /api/ventas/inventario/{vehiculoId}
- **Cambio:** Ahora acepta parámetros desde `params.body`
- **Extrae:** `estado` (campo obligatorio)
- **Valida:** El estado debe ser uno de los 5 valores válidos
- **Responde:** Con mensaje de éxito (200) o error (400/404)

### Cómo Usar los PUT Ahora

```
PUT http://localhost:8000/api/produccion/planificacionProduccion/1

Headers:
  x-api-key: 1234
  Content-Type: application/json

Body:
{
  "nuevaFechaReanudacion": "2025-01-20"
}
```

✅ **Funciona correctamente** - El servidor extrae `nuevaFechaReanudacion` del body y lo actualiza

```
PUT http://localhost:8000/api/ventas/inventario/VIN00000000000001

Headers:
  x-api-key: 1234
  Content-Type: application/json

Body:
{
  "estado": "STOCK_FABRICANTE"
}
```

✅ **Funciona correctamente** - El servidor extrae `estado` del body y valida contra los 5 estados permitidos

---

## ✅ Flujo de Prueba Completo - Paso a Paso

### 1️⃣ **GET Componente** 
Obtener información de un componente
```
GET http://localhost:8000/api/produccion/componentes/1

Headers:
  x-api-key: 1234
  Content-Type: application/json
```

**Respuesta esperada (200):**
```json
{
  "componenteId": 1,
  "referencia": "COMP-0001",
  "nombre": "Motor Diesel 2.0",
  "descripcion": "Motor base para SUV X-Treme",
  "proveedorId": 2,
  "precioUnitario": "4500.00"
}
```

---

### 2️⃣ **GET Inventario de Componente**
Consultar stock disponible de un componente
```
GET http://localhost:8000/api/produccion/inventario/2

Headers:
  x-api-key: 1234
  Content-Type: application/json
```

**Respuesta esperada (200):**
```json
{
  "componenteId": 2,
  "cantidadActual": 3,
  "umbralMinimo": 5,
  "ultimaRevision": "2026-05-09T11:27:19.000Z",
  "nombreComponente": "Batería Principal 150kW"
}
```

---

### 3️⃣ **GET Proveedor**
Obtener datos de un proveedor
```
GET http://localhost:8000/api/produccion/proveedores/1

Headers:
  x-api-key: 1234
  Content-Type: application/json
```

**Respuesta esperada (200):**
```json
{
  "proveedorId": 1,
  "nombre": "ElectroSupply SL",
  "contacto": "info@electrosupply.es",
  "pais": "España",
  "tiempoEntrega": 5
}
```

---

### 4️⃣ **POST Crear Pedido de Compra**
Crear una nueva orden de compra de componentes
```
POST http://localhost:8000/api/produccion/pedidosCompra

Headers:
  x-api-key: 1234
  Content-Type: application/json

Body (JSON):
{
  "componenteId": 2,
  "cantidadRequerida": 10
}
```

**Parámetros requeridos:**
- `componenteId`: ID del componente a comprar
- `cantidadRequerida`: Cantidad solicitada

**Respuesta esperada (201):**
```json
{
  "id_pedido_compra": 1,
  "id_componente": 2,
  "cantidad_solicitada": 10,
  "coste_total_estimado": 62000.00,
  "estado": "PENDIENTE"
}
```

---

### 5️⃣ **GET Pedido de Compra**
Consultar detalles de un pedido existente
```
GET http://localhost:8000/api/produccion/pedidosCompra/1

Headers:
  x-api-key: 1234
  Content-Type: application/json
```

**Respuesta esperada (200):**
```json
{
  "pedidoId": 1,
  "componenteId": 2,
  "cantidadSolicitada": 10,
  "costoTotalEstimado": "62000.00",
  "estado": "PENDIENTE",
  "fechaPedido": "2026-05-11T11:12:01.000Z"
}
```

---

### 6️⃣ **PUT Actualizar Planificación de Producción**
Actualizar estado o fecha de una planificación existente
```
PUT http://localhost:8000/api/produccion/planificacionProduccion/1

Headers:
  x-api-key: 1234
  Content-Type: application/json

Body (JSON):
{
  "nuevaFechaReanudacion": "2025-01-20"
}
```

**Parámetros opcionales (al menos uno es requerido):**
- `estadoProduccion`: Nuevo estado de producción
- `nuevaFechaReanudacion`: Nueva fecha en formato YYYY-MM-DD

**Ejemplos de body:**

Opción 1 - Solo fecha:
```json
{
  "nuevaFechaReanudacion": "2025-01-20"
}
```

Opción 2 - Solo estado:
```json
{
  "estadoProduccion": "EN_PROCESO"
}
```

Opción 3 - Ambos campos:
```json
{
  "estadoProduccion": "PAUSA",
  "nuevaFechaReanudacion": "2025-01-20"
}
```

**Respuesta esperada (200):**
```json
{
  "mensaje": "Planificación actualizada correctamente"
}
```

---

### 7️⃣ **POST Registrar Venta**
Crear un nuevo registro de venta de vehículo
```
POST http://localhost:8000/api/ventas/registrar

Headers:
  x-api-key: 1234
  Content-Type: application/json

Body (JSON):
{
  "clienteId": 1,
  "vehiculoId": "VIN00000000000001",
  "precioTotal": 35000.00
}
```

**Parámetros requeridos:**
- `clienteId`: ID del cliente que compra
- `vehiculoId`: VIN del vehículo a vender
- `precioTotal`: Precio final de la venta

**Respuesta esperada (201):**
```json
{
  "ventaId": 1,
  "vehiculoId": "VIN00000000000001",
  "clienteId": 1,
  "precioTotal": "35000.00",
  "estado": "COMPLETADA",
  "fechaVenta": "2026-05-11T12:00:00.000Z"
}
```

---

### 8️⃣ **GET Venta**
Obtener detalles de una venta registrada
```
GET http://localhost:8000/api/ventas/1

Headers:
  x-api-key: 1234
  Content-Type: application/json
```

**Respuesta esperada (200):**
```json
{
  "ventaId": 1,
  "vehiculoId": "VIN00000000000001",
  "clienteId": 1,
  "precioTotal": "35000.00",
  "estado": "COMPLETADA",
  "fechaVenta": "2026-05-11T12:00:00.000Z"
}
```

---

### 9️⃣ **POST Generar Factura**
Crear una factura para una venta existente
```
POST http://localhost:8000/api/ventas/facturas

Headers:
  x-api-key: 1234
  Content-Type: application/json

Body (JSON):
{
  "ventaId": 1,
  "importe": 35000.00
}
```

**Parámetros requeridos:**
- `ventaId`: ID de la venta a facturar
- `importe`: Monto a facturar

**Respuesta esperada (201):**
```json
{
  "facturaId": 1,
  "ventaId": 1,
  "importe": "35000.00",
  "estado": "PENDIENTE_PAGO",
  "fechaFactura": "2026-05-11T12:05:00.000Z"
}
```

---

### 🔟 **GET Factura**
Consultar detalles de una factura
```
GET http://localhost:8000/api/ventas/facturas/1

Headers:
  x-api-key: 1234
  Content-Type: application/json
```

**Respuesta esperada (200):**
```json
{
  "facturaId": 1,
  "ventaId": 1,
  "importe": "35000.00",
  "estado": "PENDIENTE_PAGO",
  "fechaFactura": "2026-05-11T12:05:00.000Z"
}
```

---

### 1️⃣1️⃣ **POST Registrar Pago**
Registrar un pago para una factura existente
```
POST http://localhost:8000/api/ventas/pagos

Headers:
  x-api-key: 1234
  Content-Type: application/json

Body (JSON):
{
  "facturaId": 1,
  "cantidad": 35000.00
}
```

**Parámetros requeridos:**
- `facturaId`: ID de la factura a pagar
- `cantidad`: Monto pagado

**Respuesta esperada (201):**
```json
{
  "pagoId": 1,
  "facturaId": 1,
  "cantidad": "35000.00",
  "estado": "COMPLETADO",
  "fechaPago": "2026-05-11T12:10:00.000Z"
}
```

---

### 1️⃣2️⃣ **GET Pago**
Obtener detalles de un pago registrado
```
GET http://localhost:8000/api/ventas/pagos/1

Headers:
  x-api-key: 1234
  Content-Type: application/json
```

**Respuesta esperada (200):**
```json
{
  "pagoId": 1,
  "facturaId": 1,
  "cantidad": "35000.00",
  "estado": "COMPLETADO",
  "fechaPago": "2026-05-11T12:10:00.000Z"
}
```

---

### 1️⃣3️⃣ **POST Activar Garantía**
Crear un registro de garantía para un vehículo
```
POST http://localhost:8000/api/ventas/garantias

Headers:
  x-api-key: 1234
  Content-Type: application/json

Body (JSON):
{
  "vehiculoId": "VIN00000000000001",
  "duracionMeses": 24
}
```

**Parámetros requeridos:**
- `vehiculoId`: VIN del vehículo
- `duracionMeses`: Duración de la garantía en meses

**Respuesta esperada (201):**
```json
{
  "garantiaId": 1,
  "vehiculoId": "VIN00000000000001",
  "fechaInicio": "2026-05-11T12:15:00.000Z",
  "fechaFin": "2028-05-11T12:15:00.000Z",
  "duracionMeses": 24,
  "estado": "ACTIVA"
}
```

---

### 1️⃣4️⃣ **GET Garantía**
Consultar garantía de un vehículo
```
GET http://localhost:8000/api/ventas/garantias/VIN00000000000001

Headers:
  x-api-key: 1234
  Content-Type: application/json
```

**Respuesta esperada (200):**
```json
{
  "garantiaId": 1,
  "vehiculoId": "VIN00000000000001",
  "fechaInicio": "2026-05-11T12:15:00.000Z",
  "fechaFin": "2028-05-11T12:15:00.000Z",
  "duracionMeses": 24,
  "estado": "ACTIVA"
}
```

---

### 1️⃣5️⃣ **GET Inventario de Vehículo**
Consultar estado de inventario de un vehículo
```
GET http://localhost:8000/api/ventas/inventario/VIN00000000000001

Headers:
  x-api-key: 1234
  Content-Type: application/json
```

**Respuesta esperada (200):**
```json
{
  "mensaje": "Estado del vehículo: VENDIDO",
  "datos": {
    "vehiculoId": "VIN00000000000001",
    "modeloId": 1,
    "concesionarioId": 1,
    "estado": "VENDIDO",
    "color": "Gris Metálico",
    "motor": "2.0 Diesel",
    "fechaFabricacion": "2026-03-15T00:00:00.000Z"
  }
}
```

---

### 1️⃣6️⃣ **PUT Actualizar Estado de Vehículo**
Cambiar el estado de inventario de un vehículo
```
PUT http://localhost:8000/api/ventas/inventario/VIN00000000000001

Headers:
  x-api-key: 1234
  Content-Type: application/json

Body (JSON):
{
  "estado": "STOCK_FABRICANTE"
}
```

**Parámetro requerido:**
- `estado`: Nuevo estado del vehículo

**Estados válidos:**
- `EN_FABRICACION`
- `STOCK_FABRICANTE`
- `STOCK_CONCESIONARIO`
- `RESERVADO`
- `VENDIDO`

**Respuesta esperada (200):**
```json
{
  "mensaje": "Vehículo actualizado a estado: STOCK_FABRICANTE"
}
```

---

## 🚫 Pruebas de Error - Validación de Autenticación

### ❌ Sin x-api-key (debe devolver 401)
```
GET http://localhost:8000/api/produccion/componentes/1

(Sin header x-api-key)
```

**Respuesta esperada (401):**
```json
{
  "error": "Unauthorized - x-api-key header requerido"
}
```

---

### ❌ x-api-key inválida (debe devolver 403)
```
GET http://localhost:8000/api/produccion/componentes/1

Headers:
  x-api-key: invalid-key-12345
```

**Respuesta esperada (403):**
```json
{
  "error": "Forbidden - x-api-key inválida o no autorizada"
}
```

---

### ❌ Recurso no encontrado (debe devolver 404)
```
GET http://localhost:8000/api/produccion/componentes/999

Headers:
  x-api-key: 1234
```

**Respuesta esperada (404):**
```json
{
  "error": "Componente 999 no encontrado"
}
```

---

### ❌ Parámetro requerido faltante (debe devolver 400)
```
POST http://localhost:8000/api/produccion/pedidosCompra

Headers:
  x-api-key: 1234
  Content-Type: application/json

Body (incompleto):
{
  "componenteId": 2
}
```

**Respuesta esperada (400):**
```json
{
  "error": "cantidadRequerida es requerido"
}
```

---

## 📊 Datos de Ejemplo Disponibles en BD

### Componentes
| ID | Nombre | Precio | Stock | Umbral |
|----|----|--------|-------|--------|
| 1 | Motor Diesel 2.0 | 4500€ | 15 | 5 |
| 2 | Batería 150kW | 6200€ | 3 | 5 ⚠️ |
| 3 | Motor Gasolina 1.8 | 3800€ | 20 | 5 |
| 4 | Motor Diesel 3.0 | 5500€ | 10 | 5 |
| 5 | Chasis SUV | 3200€ | 12 | 5 |
| 6 | Chasis Urbano | 2100€ | 2 | 5 ⚠️ |

⚠️ = Por debajo del umbral mínimo (requiere reabastecimiento)

### Vehículos (VINs)
| VIN | Modelo | Estado |
|-----|--------|--------|
| VIN00000000000001 | SUV X-Treme | STOCK_FABRICANTE |
| VIN00000000000002 | EcoCity | STOCK_CONCESIONARIO |
| VIN00000000000003 | SUV X-Treme | STOCK_FABRICANTE |
| VIN00000000000004 | EcoCity | STOCK_FABRICANTE |
| VIN00000000000005 | Berlina Ejecutiva | STOCK_CONCESIONARIO |

### Clientes
| ID | DNI | Nombre |
|----|-----|--------|
| 1 | 12345678A | Carlos Martínez López |
| 2 | 87654321B | María García Fernández |
| 3 | 11223344C | Antonio Ruiz Sánchez |

### Concesionarios
| ID | Nombre | Ciudad |
|----|--------|--------|
| 1 | Concesionario Madrid | Madrid |
| 2 | Concesionario Barcelona | Barcelona |
| 3 | Concesionario Valencia | Valencia |

### Proveedores
| ID | Nombre | País | Tiempo Entrega |
|----|--------|------|---|
| 1 | ElectroSupply SL | España | 5 días |
| 2 | ComponentTech | Alemania | 7 días |
| 3 | AutoParts Global | Italia | 10 días |

---

## 🔄 Flujo Completo - Caso de Uso Real

Este es un flujo de negocio completo que demuestra todas las operaciones:

### Paso 1: Verificar stock de componente
```
GET /api/produccion/inventario/2
Header: x-api-key: 1234
```

### Paso 2: Si stock bajo, crear pedido de compra
```
POST /api/produccion/pedidosCompra
Headers: x-api-key: 1234
Body: {"componenteId": 2, "cantidadRequerida": 50}
```

### Paso 3: Registrar venta de vehículo
```
POST /api/ventas/registrar
Headers: x-api-key: 1234
Body: {"clienteId": 1, "vehiculoId": "VIN00000000000001", "precioTotal": 35000}
```

### Paso 4: Generar factura
```
POST /api/ventas/facturas
Headers: x-api-key: 1234
Body: {"ventaId": 1, "importe": 35000}
```

### Paso 5: Registrar pago completo
```
POST /api/ventas/pagos
Headers: x-api-key: 1234
Body: {"facturaId": 1, "cantidad": 35000}
```

### Paso 6: Activar garantía del vehículo
```
POST /api/ventas/garantias
Headers: x-api-key: 1234
Body: {"vehiculoId": "VIN00000000000001", "duracionMeses": 24}
```

### Paso 7: Actualizar estado del vehículo
```
PUT /api/ventas/inventario/VIN00000000000001
Headers: x-api-key: 1234
Body: {"estado": "VENDIDO"}
```

---

## 💡 Tips Útiles

1. **Siempre incluir el header `x-api-key`** en todas las solicitudes
2. **Usar `Content-Type: application/json`** en headers para POST y PUT
3. **Las rutas comienzan con `/api/`** - ej: `http://localhost:8000/api/...`
4. **Copiar IDs de respuestas previas** para usar en solicitudes posteriores
5. **Validar los formatos de fecha** - usar YYYY-MM-DD para fechas
6. **Revisar el puerto 8000** - asegurarse de que el servidor esté activo

---

## 📞 Soporte

Si encuentras errores:
1. Verifica que el servidor está ejecutándose (`npm start`)
2. Verifica que la BD está inicializada (`npm run init-db`)
3. Comprueba que usas x-api-key correctamente en headers
4. Revisa los logs en la consola del servidor
5. Asegúrate de usar URLs correctas con `/api/`
