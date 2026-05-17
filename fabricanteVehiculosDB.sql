CREATE DATABASE IF NOT EXISTS FabricanteVehiculosDB;
USE FabricanteVehiculosDB;

-- ============================================================
-- TABLAS BASE
-- ============================================================

CREATE TABLE IF NOT EXISTS Concesionario (
    id_concesionario INT AUTO_INCREMENT PRIMARY KEY,
    nombre           VARCHAR(100) NOT NULL,
    ciudad           VARCHAR(100),
    direccion        VARCHAR(150),
    telefono         VARCHAR(20)
);

CREATE TABLE IF NOT EXISTS Cliente (
    id_cliente INT AUTO_INCREMENT PRIMARY KEY,
    dni        VARCHAR(20)  UNIQUE NOT NULL,
    nombre     VARCHAR(100) NOT NULL,
    apellidos  VARCHAR(100) NOT NULL,
    email      VARCHAR(100),
    telefono   VARCHAR(20)
);

CREATE TABLE IF NOT EXISTS ModeloVehiculo (
    id_modelo        INT AUTO_INCREMENT PRIMARY KEY,
    nombre           VARCHAR(100)   NOT NULL,
    descripcion      TEXT,
    precio_base      DECIMAL(10,2)  NOT NULL,
    coste_produccion DECIMAL(10,2)  NULL       COMMENT 'Coste estimado de fabricación del modelo',
    dias_fabricacion  INT            NULL       COMMENT 'Días estimados de producción (Proceso 2.2)'
);

CREATE TABLE IF NOT EXISTS InventarioVehiculo (
    vin               VARCHAR(50) PRIMARY KEY,
    id_modelo         INT  NOT NULL,
    id_concesionario  INT  NOT NULL DEFAULT 1,
    estado            ENUM('EN_FABRICACION','STOCK_FABRICANTE','STOCK_CONCESIONARIO',
                           'RESERVADO','VENDIDO') DEFAULT 'STOCK_FABRICANTE',
    color             VARCHAR(50),
    motor             VARCHAR(50),
    fecha_fabricacion  DATE,
    FOREIGN KEY (id_modelo)        REFERENCES ModeloVehiculo(id_modelo),
    FOREIGN KEY (id_concesionario) REFERENCES Concesionario(id_concesionario)
);

-- ============================================================
-- PROCESO 2.1 - Consulta y Reserva de Vehículos en Stock (SOAP)
-- ============================================================

CREATE TABLE IF NOT EXISTS Reserva (
    id_reserva       INT AUTO_INCREMENT PRIMARY KEY,
    id_cliente       INT          NOT NULL,
    id_concesionario INT          NOT NULL,
    vin              VARCHAR(50)  NOT NULL,
    fecha_reserva    DATETIME     DEFAULT CURRENT_TIMESTAMP,
    estado_reserva   ENUM('ACTIVA','COMPLETADA','CANCELADA') DEFAULT 'ACTIVA',
    senal_entregada  DECIMAL(10,2) DEFAULT 0.00,
    codigo_bloqueo   VARCHAR(50)  NULL  COMMENT 'Código de bloqueo temporal para evitar ventas duplicadas (Proceso 2.1)',
    FOREIGN KEY (id_cliente)       REFERENCES Cliente(id_cliente),
    FOREIGN KEY (id_concesionario) REFERENCES Concesionario(id_concesionario),
    FOREIGN KEY (vin)              REFERENCES InventarioVehiculo(vin)
);

-- ============================================================
-- PROCESO 2.2 - Pedido de Fabricación a Medida (SOAP)
-- ============================================================

CREATE TABLE IF NOT EXISTS ConfiguracionModelo (
    id_configuracion INT AUTO_INCREMENT PRIMARY KEY,
    id_modelo        INT          NOT NULL,
    nombre_config    VARCHAR(100) NOT NULL  COMMENT 'Ej: DIESEL_MANUAL_ROJO, ELECTRICO_AUTO_BLANCO',
    motor            VARCHAR(50)  NOT NULL,
    color            VARCHAR(50)  NOT NULL,
    extras           TEXT         NULL      COMMENT 'Descripción de extras opcionales',
    es_viable        BOOLEAN      NOT NULL DEFAULT TRUE COMMENT 'Si la configuración es producible',
    sobrecoste       DECIMAL(10,2) DEFAULT 0.00 COMMENT 'Coste adicional sobre el precio base del modelo',
    FOREIGN KEY (id_modelo) REFERENCES ModeloVehiculo(id_modelo)
);

CREATE TABLE IF NOT EXISTS PedidoFabricacion (
    id_pedido              INT AUTO_INCREMENT PRIMARY KEY,
    id_concesionario       INT          NOT NULL,
    id_modelo              INT          NOT NULL,
    id_configuracion       INT          NULL      COMMENT 'FK a ConfiguracionModelo elegida',
    configuracion_detalles TEXT                    COMMENT 'Texto libre con detalles de personalización',
    fecha_pedido           DATETIME     DEFAULT CURRENT_TIMESTAMP,
    estado_pedido          ENUM('PENDIENTE','ACEPTADO','CANCELADO','EN_PRODUCCION','FINALIZADO','ENVIADO')
                           DEFAULT 'PENDIENTE',
    coste_produccion       DECIMAL(10,2)          COMMENT 'Coste calculado por ServicioCalculoCostes',
    dias_estimados         INT          NULL       COMMENT 'Días estimados calculados por ServicioPlanificacionProduccion',
    fecha_entrega_estimada DATE         NULL       COMMENT 'Fecha estimada de entrega al concesionario (Proceso 2.2)',
    FOREIGN KEY (id_concesionario) REFERENCES Concesionario(id_concesionario),
    FOREIGN KEY (id_modelo)        REFERENCES ModeloVehiculo(id_modelo),
    FOREIGN KEY (id_configuracion) REFERENCES ConfiguracionModelo(id_configuracion)
);

-- ============================================================
-- PROCESO 2.3 - Solicitud de Piezas para Mantenimiento (SOAP)
-- ============================================================

CREATE TABLE IF NOT EXISTS Proveedor (
    id_proveedor INT AUTO_INCREMENT PRIMARY KEY,
    nombre       VARCHAR(100) NOT NULL,
    contacto     VARCHAR(100),
    telefono     VARCHAR(20),
    email        VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS Pieza (
    id_pieza              INT AUTO_INCREMENT PRIMARY KEY,
    referencia            VARCHAR(50)  UNIQUE NOT NULL,
    nombre                VARCHAR(100) NOT NULL,
    descripcion           TEXT,
    precio                DECIMAL(10,2) NOT NULL,
    id_proveedor          INT,
    tiempo_suministro_dias INT          NULL  COMMENT 'Días estimados de suministro desde almacén central (Proceso 2.3)',
    FOREIGN KEY (id_proveedor) REFERENCES Proveedor(id_proveedor)
);

CREATE TABLE IF NOT EXISTS StockRepuesto (
    id_concesionario INT,
    id_pieza         INT,
    cantidad         INT DEFAULT 0,
    PRIMARY KEY (id_concesionario, id_pieza),
    FOREIGN KEY (id_concesionario) REFERENCES Concesionario(id_concesionario),
    FOREIGN KEY (id_pieza)         REFERENCES Pieza(id_pieza)
);

CREATE TABLE IF NOT EXISTS PedidoRepuesto (
    id_pedido_repuesto INT AUTO_INCREMENT PRIMARY KEY,
    id_concesionario   INT      NOT NULL,
    id_mantenimiento   INT      NULL      COMMENT 'FK al mantenimiento que originó el pedido (puede ser NULL si es reposición general)',
    fecha_pedido       DATETIME DEFAULT CURRENT_TIMESTAMP,
    estado             ENUM('PENDIENTE','PROCESANDO','ENVIADO','ENTREGADO','CANCELADO')
                       DEFAULT 'PENDIENTE',
    FOREIGN KEY (id_concesionario) REFERENCES Concesionario(id_concesionario)
    -- FK a Mantenimiento se añade tras crear esa tabla (ALTER TABLE abajo)
);

CREATE TABLE IF NOT EXISTS DetallePedidoRepuesto (
    id_pedido_repuesto INT,
    id_pieza           INT,
    cantidad           INT NOT NULL,
    PRIMARY KEY (id_pedido_repuesto, id_pieza),
    FOREIGN KEY (id_pedido_repuesto) REFERENCES PedidoRepuesto(id_pedido_repuesto),
    FOREIGN KEY (id_pieza)           REFERENCES Pieza(id_pieza)
);

CREATE TABLE IF NOT EXISTS EnvioLogistica (
    id_envio                INT AUTO_INCREMENT PRIMARY KEY,
    id_pedido_repuesto      INT          NOT NULL,
    id_concesionario_destino INT         NULL      COMMENT 'Concesionario destino del envío',
    empresa_transporte      VARCHAR(100),
    codigo_seguimiento      VARCHAR(100),
    estado_envio            ENUM('PREPARACION','EN_TRANSITO','REPARTO','ENTREGADO')
                            DEFAULT 'PREPARACION',
    fecha_salida            DATETIME,
    fecha_entrega_estimada  DATETIME,
    FOREIGN KEY (id_pedido_repuesto)      REFERENCES PedidoRepuesto(id_pedido_repuesto),
    FOREIGN KEY (id_concesionario_destino) REFERENCES Concesionario(id_concesionario)
);

-- ============================================================
-- PROCESO 2.5 - Registro y Cierre de Venta Final (REST)
-- ============================================================

CREATE TABLE IF NOT EXISTS Venta (
    id_venta         INT AUTO_INCREMENT PRIMARY KEY,
    id_cliente       INT          NOT NULL,
    id_concesionario INT          NOT NULL,
    vin              VARCHAR(50)  NOT NULL,
    id_reserva       INT          NULL        COMMENT 'NULL si la venta no viene de una reserva previa',
    fecha_venta      DATETIME     DEFAULT CURRENT_TIMESTAMP,
    precio_final     DECIMAL(10,2) NOT NULL,
    estado_venta     ENUM('PENDIENTE_PAGO','COMPLETADA','CANCELADA') DEFAULT 'PENDIENTE_PAGO'
                     COMMENT 'Estado del cierre de venta (Proceso 2.5)',
    FOREIGN KEY (id_cliente)       REFERENCES Cliente(id_cliente),
    FOREIGN KEY (id_concesionario) REFERENCES Concesionario(id_concesionario),
    FOREIGN KEY (vin)              REFERENCES InventarioVehiculo(vin),
    FOREIGN KEY (id_reserva)       REFERENCES Reserva(id_reserva)
);

CREATE TABLE IF NOT EXISTS Factura (
    id_factura    INT AUTO_INCREMENT PRIMARY KEY,
    id_venta      INT           NOT NULL,
    fecha_emision DATETIME      DEFAULT CURRENT_TIMESTAMP,
    total         DECIMAL(10,2) NOT NULL,
    estado        ENUM('PENDIENTE','PAGADA') DEFAULT 'PENDIENTE',
    FOREIGN KEY (id_venta) REFERENCES Venta(id_venta)
);

CREATE TABLE IF NOT EXISTS Pago (
    id_pago      INT AUTO_INCREMENT PRIMARY KEY,
    id_factura   INT           NOT NULL,
    monto        DECIMAL(10,2) NOT NULL,
    metodo_pago  VARCHAR(50),
    fecha_pago   DATETIME      DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_factura) REFERENCES Factura(id_factura)
);

CREATE TABLE IF NOT EXISTS Garantia (
    id_garantia      INT AUTO_INCREMENT PRIMARY KEY,
    vin              VARCHAR(50)  NOT NULL,
    id_venta         INT          NULL      COMMENT 'FK a la venta que activó esta garantía (Proceso 2.5)',
    fecha_inicio     DATE         NOT NULL,
    fecha_fin        DATE         NOT NULL,
    duracion_meses   INT          NULL      COMMENT 'Duración en meses de la garantía',
    tipo_cobertura   VARCHAR(100),
    estado           ENUM('ACTIVA','EXPIRADA','ANULADA') DEFAULT 'ACTIVA'
                     COMMENT 'Estado de la garantía (Proceso 2.5)',
    FOREIGN KEY (vin)      REFERENCES InventarioVehiculo(vin),
    FOREIGN KEY (id_venta) REFERENCES Venta(id_venta)
);

-- ============================================================
-- TABLAS DE MANTENIMIENTO (usadas por Proceso 2.3)
-- ============================================================

CREATE TABLE IF NOT EXISTS Mantenimiento (
    id_mantenimiento   INT AUTO_INCREMENT PRIMARY KEY,
    vin                VARCHAR(50)   NOT NULL,
    id_concesionario   INT           NOT NULL,
    fecha_entrada      DATETIME      DEFAULT CURRENT_TIMESTAMP,
    descripcion_problema TEXT,
    estado             ENUM('RECEPCIONADO','DIAGNOSTICO','EN_REPARACION',
                            'FINALIZADO','ENTREGADO') DEFAULT 'RECEPCIONADO',
    coste_mano_obra    DECIMAL(10,2) DEFAULT 0.00,
    coste_total        DECIMAL(10,2) NULL COMMENT 'Coste total = mano de obra + repuestos utilizados',
    FOREIGN KEY (vin)              REFERENCES InventarioVehiculo(vin),
    FOREIGN KEY (id_concesionario) REFERENCES Concesionario(id_concesionario)
);

CREATE TABLE IF NOT EXISTS RepuestosUtilizados (
    id_mantenimiento INT,
    id_pieza         INT,
    cantidad         INT           NOT NULL,
    precio_aplicado  DECIMAL(10,2),
    PRIMARY KEY (id_mantenimiento, id_pieza),
    FOREIGN KEY (id_mantenimiento) REFERENCES Mantenimiento(id_mantenimiento),
    FOREIGN KEY (id_pieza)         REFERENCES Pieza(id_pieza)
);

-- FK diferida: PedidoRepuesto.id_mantenimiento -> Mantenimiento
ALTER TABLE PedidoRepuesto
    ADD FOREIGN KEY (id_mantenimiento) REFERENCES Mantenimiento(id_mantenimiento);

-- ============================================================
-- PROCESO 2.4 - Reabastecimiento Automático de Componentes (REST)
-- ============================================================

CREATE TABLE IF NOT EXISTS ComponenteFabrica (
    id_componente  INT AUTO_INCREMENT PRIMARY KEY,
    referencia     VARCHAR(50)  UNIQUE NOT NULL,
    nombre         VARCHAR(100) NOT NULL,
    descripcion    TEXT,
    id_proveedor   INT,
    precio_unitario DECIMAL(10,2) NULL COMMENT 'Precio unitario del componente (Proceso 2.4)',
    FOREIGN KEY (id_proveedor) REFERENCES Proveedor(id_proveedor)
);

CREATE TABLE IF NOT EXISTS InventarioComponente (
    id_componente    INT PRIMARY KEY,
    cantidad_actual  INT           NOT NULL DEFAULT 0,
    umbral_minimo    INT           NOT NULL DEFAULT 0  COMMENT 'Nivel mínimo de seguridad que dispara reabastecimiento (Proceso 2.4)',
    ultima_revision  DATETIME      DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_componente) REFERENCES ComponenteFabrica(id_componente)
);

CREATE TABLE IF NOT EXISTS PedidoCompra (
    id_pedido_compra     INT AUTO_INCREMENT PRIMARY KEY,
    id_componente        INT           NOT NULL,
    id_proveedor         INT           NOT NULL,
    cantidad_solicitada  INT           NOT NULL,
    coste_total_estimado DECIMAL(10,2) NULL COMMENT 'cantidad_solicitada * precio_unitario del componente',
    fecha_pedido         DATETIME      DEFAULT CURRENT_TIMESTAMP,
    estado               ENUM('PENDIENTE','CONFIRMADO','EN_TRANSITO','RECIBIDO','CANCELADO')
                         DEFAULT 'PENDIENTE',
    fecha_entrega_estimada DATETIME,
    FOREIGN KEY (id_componente) REFERENCES ComponenteFabrica(id_componente),
    FOREIGN KEY (id_proveedor)  REFERENCES Proveedor(id_proveedor)
);

CREATE TABLE IF NOT EXISTS PlanificacionProduccion (
    id_planificacion   INT AUTO_INCREMENT PRIMARY KEY,
    id_modelo          INT           NOT NULL,
    cantidad_prevista  INT           NOT NULL,
    fecha_inicio       DATE          NOT NULL,
    fecha_fin_prevista DATE          NOT NULL,
    estado             ENUM('PLANIFICADO','EN_PRODUCCION','COMPLETADO','PAUSADO')
                       DEFAULT 'PLANIFICADO',
    observaciones      TEXT,
    FOREIGN KEY (id_modelo) REFERENCES ModeloVehiculo(id_modelo)
);

CREATE TABLE IF NOT EXISTS ComponenteModelo (
    id_componente INT NOT NULL,
    id_modelo     INT NOT NULL,
    cantidad_necesaria INT NOT NULL DEFAULT 1 COMMENT 'Unidades del componente necesarias por vehículo',
    PRIMARY KEY (id_componente, id_modelo),
    FOREIGN KEY (id_componente) REFERENCES ComponenteFabrica(id_componente),
    FOREIGN KEY (id_modelo)     REFERENCES ModeloVehiculo(id_modelo)
);

-- ============================================================
-- TABLAS DE AUTENTICACIÓN (WSKey para SOAP y REST)
-- ============================================================

CREATE TABLE IF NOT EXISTS SoapKey (
    id       INT AUTO_INCREMENT PRIMARY KEY,
    soap_key VARCHAR(100) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS RestKey (
    id       INT AUTO_INCREMENT PRIMARY KEY,
    rest_key VARCHAR(100) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS Notificacion (
    id_notificacion    INT AUTO_INCREMENT PRIMARY KEY,
    id_concesionario   INT          NULL,
    id_cliente         INT          NULL,
    tipo               ENUM('RESERVA_CONFIRMADA','RESERVA_SIN_STOCK',
                            'PEDIDO_FABRICACION','PEDIDO_CANCELADO',
                            'REPUESTOS_ENVIADOS','REPUESTOS_ENTREGADOS',
                            'REABASTECIMIENTO','VENTA_COMPLETADA',
                            'GARANTIA_ACTIVADA','OTRO') NOT NULL,
    mensaje            TEXT         NOT NULL,
    fecha_envio        DATETIME     DEFAULT CURRENT_TIMESTAMP,
    leida              BOOLEAN      DEFAULT FALSE,
    FOREIGN KEY (id_concesionario) REFERENCES Concesionario(id_concesionario),
    FOREIGN KEY (id_cliente)       REFERENCES Cliente(id_cliente)
);

-- ============================================================
-- DATOS DE EJEMPLO
-- ============================================================

-- Sede central (id=1) y concesionarios de la red
INSERT INTO Concesionario (nombre, ciudad, direccion, telefono) VALUES
    ('Sede Central Fabricante', 'Valencia', 'Polígono Industrial 1', '900111222');

INSERT INTO Concesionario (nombre, ciudad, direccion, telefono) VALUES
    ('Motor Levante',  'Alicante',  'Av. Denia 123',       '965111222'),
    ('Auto Sur',       'Murcia',    'Ronda Sur 45',         '968333444'),
    ('Coches del Este', 'Castellón', 'Calle Mayor 88',      '964555666');

-- Clientes
INSERT INTO Cliente (dni, nombre, apellidos, email, telefono) VALUES
    ('12345678A', 'Carlos',  'Martínez López',   'carlos@email.com',  '600111222'),
    ('87654321B', 'María',   'García Fernández',  'maria@email.com',   '600333444'),
    ('11223344C', 'Antonio', 'Ruiz Sánchez',      'antonio@email.com', '600555666');

-- Modelos de vehículo (con campos nuevos)
INSERT INTO ModeloVehiculo (nombre, descripcion, precio_base, coste_produccion, dias_fabricacion) VALUES
    ('SUV X-Treme',       'Todoterreno 4x4',                35000.00, 25000.00, 45),
    ('EcoCity',           'Coche urbano eléctrico',          22000.00, 14000.00, 30),
    ('Berlina Ejecutiva', 'Berlina de gama alta',            48000.00, 35000.00, 60);

-- Inventario de vehículos (stock en fábrica y concesionarios)
INSERT INTO InventarioVehiculo (vin, id_modelo, id_concesionario, estado, color, motor, fecha_fabricacion) VALUES
    ('VIN00000000000001', 1, 1, 'STOCK_FABRICANTE',     'Rojo',    'Diesel 2.0',       '2023-10-01'),
    ('VIN00000000000002', 2, 2, 'STOCK_CONCESIONARIO',  'Blanco',  'Eléctrico 150kW',  '2023-11-15'),
    ('VIN00000000000003', 1, 1, 'STOCK_FABRICANTE',     'Negro',   'Gasolina 1.8',     '2024-01-10'),
    ('VIN00000000000004', 2, 1, 'STOCK_FABRICANTE',     'Azul',    'Eléctrico 150kW',  '2024-02-20'),
    ('VIN00000000000005', 3, 3, 'STOCK_CONCESIONARIO',  'Gris',    'Diesel 3.0',       '2024-03-05'),
    ('VIN00000000000006', 1, 2, 'STOCK_CONCESIONARIO',  'Blanco',  'Diesel 2.0',       '2024-03-15'),
    ('VIN00000000000007', 2, 3, 'STOCK_CONCESIONARIO',  'Verde',   'Eléctrico 150kW',  '2024-04-01'),
    ('VIN00000000000008', 3, 1, 'STOCK_FABRICANTE',     'Negro',   'Gasolina 3.0',     '2024-04-10');

-- Configuraciones válidas por modelo (Proceso 2.2)
INSERT INTO ConfiguracionModelo (id_modelo, nombre_config, motor, color, extras, es_viable, sobrecoste) VALUES
    (1, 'DIESEL_MANUAL_ROJO',      'Diesel 2.0',       'Rojo',    'Pack Off-Road',         TRUE,  1500.00),
    (1, 'GASOLINA_AUTO_NEGRO',     'Gasolina 1.8',     'Negro',   'Pack Premium',          TRUE,  2000.00),
    (1, 'ELECTRICO_AUTO_BLANCO',   'Eléctrico 200kW',  'Blanco',  'Pack Eco',              FALSE, 0.00),
    (2, 'ELECTRICO_AUTO_BLANCO',   'Eléctrico 150kW',  'Blanco',  NULL,                    TRUE,  0.00),
    (2, 'ELECTRICO_AUTO_AZUL',     'Eléctrico 150kW',  'Azul',    'Techo solar',           TRUE,  800.00),
    (2, 'DIESEL_MANUAL_ROJO',      'Diesel 1.5',       'Rojo',    NULL,                    FALSE, 0.00),
    (3, 'DIESEL_AUTO_NEGRO',       'Diesel 3.0',       'Negro',   'Pack Ejecutivo',        TRUE,  3500.00),
    (3, 'GASOLINA_AUTO_GRIS',      'Gasolina 3.0',     'Gris',    'Pack Sport',            TRUE,  4000.00),
    (3, 'ELECTRICO_AUTO_BLANCO',   'Eléctrico 300kW',  'Blanco',  'Pack Full Electric',    TRUE,  5000.00);

-- Proveedores
INSERT INTO Proveedor (nombre, contacto, telefono, email) VALUES
    ('Recambios García',  'Luis García', '961000111', 'luis@recambiosgarcia.com'),
    ('Componentes Tech',  'Ana Ruiz',    '962000222', 'ana@componentestech.com'),
    ('AutoParts Global',  'Pedro Sanz',  '963000333', 'pedro@autopartsglobal.com');

-- Piezas de repuesto (con tiempo_suministro_dias)
INSERT INTO Pieza (referencia, nombre, descripcion, precio, id_proveedor, tiempo_suministro_dias) VALUES
    ('PIE-0001', 'Filtro de aceite',    'Filtro para motor Diesel 2.0',      12.50,  1, 2),
    ('PIE-0002', 'Pastillas de freno',  'Kit delantero para SUV X-Treme',    45.00,  1, 3),
    ('PIE-0003', 'Batería 12V',         'Batería auxiliar para EcoCity',      110.00, 2, 4),
    ('PIE-0004', 'Amortiguador trasero','Amortiguador para Berlina',          155.00, 1, 5),
    ('PIE-0005', 'Correa distribución', 'Correa para motor Diesel 3.0',      210.00, 3, 7),
    ('PIE-0006', 'Kit embrague',        'Kit completo para caja manual',      320.00, 3, 6);

-- Stock de repuestos en almacén central del fabricante (Proceso 2.3:
-- si el concesionario no tiene stock local, se consulta aquí antes de pedir al proveedor)
INSERT INTO StockRepuesto (id_concesionario, id_pieza, cantidad) VALUES
    (1, 1, 200),  -- Sede Central: 200 filtros de aceite
    (1, 2, 150),  -- Sede Central: 150 kits pastillas
    (1, 3, 80),   -- Sede Central: 80 baterías 12V
    (1, 4, 60),   -- Sede Central: 60 amortiguadores
    (1, 5, 40),   -- Sede Central: 40 correas distribución
    (1, 6, 30);   -- Sede Central: 30 kits embrague

-- Stock de repuestos en concesionarios
INSERT INTO StockRepuesto (id_concesionario, id_pieza, cantidad) VALUES
    (2, 1, 20),  -- Motor Levante: 20 filtros de aceite
    (2, 2, 8),   -- Motor Levante: 8 kits pastillas
    (2, 3, 0),   -- Motor Levante: sin stock de baterías
    (3, 1, 15),  -- Auto Sur: 15 filtros de aceite
    (3, 4, 6),   -- Auto Sur: 6 amortiguadores
    (3, 5, 0),   -- Auto Sur: sin stock de correas
    (4, 1, 10),  -- Coches del Este: 10 filtros
    (4, 2, 5);   -- Coches del Este: 5 kits pastillas

-- Componentes de fábrica (con precio_unitario)
INSERT INTO ComponenteFabrica (referencia, nombre, descripcion, id_proveedor, precio_unitario) VALUES
    ('COMP-0001', 'Motor Diesel 2.0',       'Motor base para SUV X-Treme',      2, 4500.00),
    ('COMP-0002', 'Batería Principal 150kW', 'Batería de tracción EcoCity',      2, 6200.00),
    ('COMP-0003', 'Motor Gasolina 1.8',     'Motor gasolina para SUV',          3, 3800.00),
    ('COMP-0004', 'Motor Diesel 3.0',       'Motor para Berlina Ejecutiva',     3, 5500.00),
    ('COMP-0005', 'Chasis SUV',             'Chasis base para SUV X-Treme',     2, 3200.00),
    ('COMP-0006', 'Chasis Urbano',          'Chasis base para EcoCity',         2, 2100.00);

-- Inventario de componentes en fábrica
INSERT INTO InventarioComponente (id_componente, cantidad_actual, umbral_minimo) VALUES
    (1, 15, 5),
    (2,  3, 5),   -- ¡Por debajo del umbral! Debería disparar reabastecimiento
    (3, 20, 8),
    (4, 10, 5),
    (5, 12, 4),
    (6,  2, 5);   -- ¡Por debajo del umbral!

-- Relación componentes-modelos
INSERT INTO ComponenteModelo (id_componente, id_modelo, cantidad_necesaria) VALUES
    (1, 1, 1),  -- SUV X-Treme necesita 1 Motor Diesel 2.0
    (3, 1, 1),  -- SUV X-Treme necesita 1 Motor Gasolina 1.8 (variante)
    (5, 1, 1),  -- SUV X-Treme necesita 1 Chasis SUV
    (2, 2, 1),  -- EcoCity necesita 1 Batería Principal 150kW
    (6, 2, 1),  -- EcoCity necesita 1 Chasis Urbano
    (4, 3, 1);  -- Berlina Ejecutiva necesita 1 Motor Diesel 3.0

-- Planificación de producción
INSERT INTO PlanificacionProduccion (id_modelo, cantidad_prevista, fecha_inicio, fecha_fin_prevista, estado, observaciones) VALUES
    (1, 50, '2024-06-01', '2024-08-15', 'PLANIFICADO', 'Lote verano SUV X-Treme'),
    (2, 80, '2024-06-01', '2024-07-31', 'PLANIFICADO', 'Lote verano EcoCity'),
    (3, 20, '2024-07-01', '2024-09-30', 'PLANIFICADO', 'Lote Berlina Ejecutiva Q3');

-- Claves de autenticación para servicios SOAP y REST
INSERT INTO SoapKey (soap_key) VALUES
    ('1234'),
    ('admin-soap-key');

INSERT INTO RestKey (rest_key) VALUES
    ('1234'),
    ('admin-rest-key');
