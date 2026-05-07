CREATE DATABASE IF NOT EXISTS FabricanteVehiculosDB;
USE FabricanteVehiculosDB;

CREATE TABLE Concesionario (
    id_concesionario INT AUTO_INCREMENT PRIMARY KEY,
    nombre           VARCHAR(100) NOT NULL,
    ciudad           VARCHAR(100),
    direccion        VARCHAR(150),
    telefono         VARCHAR(20)
);

CREATE TABLE Cliente (
    id_cliente INT AUTO_INCREMENT PRIMARY KEY,
    dni        VARCHAR(20)  UNIQUE NOT NULL,
    nombre     VARCHAR(100) NOT NULL,
    apellidos  VARCHAR(100) NOT NULL,
    email      VARCHAR(100),
    telefono   VARCHAR(20)
);

CREATE TABLE ModeloVehiculo (
    id_modelo   INT AUTO_INCREMENT PRIMARY KEY,
    nombre      VARCHAR(100)   NOT NULL,
    descripcion TEXT,
    precio_base DECIMAL(10,2)  NOT NULL
);

CREATE TABLE InventarioVehiculo (
    vin               VARCHAR(50) PRIMARY KEY,   
    id_modelo         INT  NOT NULL,
    id_concesionario  INT  NOT NULL DEFAULT 0,   
    estado            ENUM('EN_FABRICACION','STOCK_FABRICANTE','STOCK_CONCESIONARIO',
                           'RESERVADO','VENDIDO') DEFAULT 'STOCK_FABRICANTE',
    color             VARCHAR(50),
    motor             VARCHAR(50),
    fecha_fabricacion DATE,
    FOREIGN KEY (id_modelo)        REFERENCES ModeloVehiculo(id_modelo),
    FOREIGN KEY (id_concesionario) REFERENCES Concesionario(id_concesionario)
);

CREATE TABLE Reserva (
    id_reserva       INT AUTO_INCREMENT PRIMARY KEY,
    id_cliente       INT          NOT NULL,
    id_concesionario INT          NOT NULL,
    vin              VARCHAR(50)  NOT NULL,
    fecha_reserva    DATETIME     DEFAULT CURRENT_TIMESTAMP,
    estado_reserva   ENUM('ACTIVA','COMPLETADA','CANCELADA') DEFAULT 'ACTIVA',
    senal_entregada  DECIMAL(10,2) DEFAULT 0.00,
    FOREIGN KEY (id_cliente)       REFERENCES Cliente(id_cliente),
    FOREIGN KEY (id_concesionario) REFERENCES Concesionario(id_concesionario),
    FOREIGN KEY (vin)              REFERENCES InventarioVehiculo(vin)
);

CREATE TABLE PedidoFabricacion (
    id_pedido             INT AUTO_INCREMENT PRIMARY KEY,
    id_concesionario      INT          NOT NULL,
    id_modelo             INT          NOT NULL,
    configuracion_detalles TEXT,              
    fecha_pedido          DATETIME     DEFAULT CURRENT_TIMESTAMP,
    estado_pedido         ENUM('PENDIENTE','EN_PRODUCCION','FINALIZADO','ENVIADO')
                          DEFAULT 'PENDIENTE',
    coste_produccion      DECIMAL(10,2),         
    FOREIGN KEY (id_concesionario) REFERENCES Concesionario(id_concesionario),
    FOREIGN KEY (id_modelo)        REFERENCES ModeloVehiculo(id_modelo)
);

CREATE TABLE Proveedor (
    id_proveedor INT AUTO_INCREMENT PRIMARY KEY,
    nombre       VARCHAR(100) NOT NULL,
    contacto     VARCHAR(100),
    telefono     VARCHAR(20),
    email        VARCHAR(100)
);

CREATE TABLE Pieza (
    id_pieza     INT AUTO_INCREMENT PRIMARY KEY,
    referencia   VARCHAR(50)  UNIQUE NOT NULL,
    nombre       VARCHAR(100) NOT NULL,
    descripcion  TEXT,
    precio       DECIMAL(10,2) NOT NULL,
    id_proveedor INT,
    FOREIGN KEY (id_proveedor) REFERENCES Proveedor(id_proveedor)
);

CREATE TABLE StockRepuesto (
    id_concesionario INT,   
    id_pieza         INT,
    cantidad         INT DEFAULT 0,
    PRIMARY KEY (id_concesionario, id_pieza),
    FOREIGN KEY (id_concesionario) REFERENCES Concesionario(id_concesionario),
    FOREIGN KEY (id_pieza)         REFERENCES Pieza(id_pieza)
);

CREATE TABLE PedidoRepuesto (
    id_pedido_repuesto INT AUTO_INCREMENT PRIMARY KEY,
    id_concesionario   INT      NOT NULL,
    fecha_pedido       DATETIME DEFAULT CURRENT_TIMESTAMP,
    estado             ENUM('PENDIENTE','PROCESANDO','ENVIADO','ENTREGADO','CANCELADO')
                       DEFAULT 'PENDIENTE',
    FOREIGN KEY (id_concesionario) REFERENCES Concesionario(id_concesionario)
);

CREATE TABLE DetallePedidoRepuesto (
    id_pedido_repuesto INT,
    id_pieza           INT,
    cantidad           INT NOT NULL,
    PRIMARY KEY (id_pedido_repuesto, id_pieza),
    FOREIGN KEY (id_pedido_repuesto) REFERENCES PedidoRepuesto(id_pedido_repuesto),
    FOREIGN KEY (id_pieza)           REFERENCES Pieza(id_pieza)
);

CREATE TABLE EnvioLogistica (
    id_envio                INT AUTO_INCREMENT PRIMARY KEY,
    id_pedido_repuesto      INT          NOT NULL,
    empresa_transporte      VARCHAR(100),
    codigo_seguimiento      VARCHAR(100),
    estado_envio            ENUM('PREPARACION','EN_TRANSITO','REPARTO','ENTREGADO')
                            DEFAULT 'PREPARACION',
    fecha_salida            DATETIME,
    fecha_entrega_estimada  DATETIME,
    FOREIGN KEY (id_pedido_repuesto) REFERENCES PedidoRepuesto(id_pedido_repuesto)
);

CREATE TABLE Venta (
    id_venta         INT AUTO_INCREMENT PRIMARY KEY,
    id_cliente       INT          NOT NULL,
    id_concesionario INT          NOT NULL,
    vin              VARCHAR(50)  NOT NULL,
    id_reserva       INT          NULL,           
    fecha_venta      DATETIME     DEFAULT CURRENT_TIMESTAMP,
    precio_final     DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (id_cliente)       REFERENCES Cliente(id_cliente),
    FOREIGN KEY (id_concesionario) REFERENCES Concesionario(id_concesionario),
    FOREIGN KEY (vin)              REFERENCES InventarioVehiculo(vin),
    FOREIGN KEY (id_reserva)       REFERENCES Reserva(id_reserva)
);

CREATE TABLE Factura (
    id_factura    INT AUTO_INCREMENT PRIMARY KEY,
    id_venta      INT           NOT NULL,
    fecha_emision DATETIME      DEFAULT CURRENT_TIMESTAMP,
    total         DECIMAL(10,2) NOT NULL,
    estado        ENUM('PENDIENTE','PAGADA') DEFAULT 'PENDIENTE',
    FOREIGN KEY (id_venta) REFERENCES Venta(id_venta)
);

CREATE TABLE Pago (
    id_pago      INT AUTO_INCREMENT PRIMARY KEY,
    id_factura   INT           NOT NULL,
    monto        DECIMAL(10,2) NOT NULL,
    metodo_pago  VARCHAR(50),
    fecha_pago   DATETIME      DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_factura) REFERENCES Factura(id_factura)
);

CREATE TABLE Garantia (
    id_garantia    INT AUTO_INCREMENT PRIMARY KEY,
    vin            VARCHAR(50)  NOT NULL,
    fecha_inicio   DATE         NOT NULL,
    fecha_fin      DATE         NOT NULL,
    tipo_cobertura VARCHAR(100),
    FOREIGN KEY (vin) REFERENCES InventarioVehiculo(vin)
);

CREATE TABLE Mantenimiento (
    id_mantenimiento   INT AUTO_INCREMENT PRIMARY KEY,
    vin                VARCHAR(50)   NOT NULL,
    id_concesionario   INT           NOT NULL,
    fecha_entrada      DATETIME      DEFAULT CURRENT_TIMESTAMP,
    descripcion_problema TEXT,
    estado             ENUM('RECEPCIONADO','DIAGNOSTICO','EN_REPARACION',
                            'FINALIZADO','ENTREGADO') DEFAULT 'RECEPCIONADO',
    coste_mano_obra    DECIMAL(10,2) DEFAULT 0.00,
    FOREIGN KEY (vin)              REFERENCES InventarioVehiculo(vin),
    FOREIGN KEY (id_concesionario) REFERENCES Concesionario(id_concesionario)
);

CREATE TABLE RepuestosUtilizados (
    id_mantenimiento INT,
    id_pieza         INT,
    cantidad         INT           NOT NULL,
    precio_aplicado  DECIMAL(10,2),
    PRIMARY KEY (id_mantenimiento, id_pieza),
    FOREIGN KEY (id_mantenimiento) REFERENCES Mantenimiento(id_mantenimiento),
    FOREIGN KEY (id_pieza)         REFERENCES Pieza(id_pieza)
);

CREATE TABLE ComponenteFabrica (
    id_componente INT AUTO_INCREMENT PRIMARY KEY,
    referencia    VARCHAR(50)  UNIQUE NOT NULL,
    nombre        VARCHAR(100) NOT NULL,
    descripcion   TEXT,
    id_proveedor  INT,
    FOREIGN KEY (id_proveedor) REFERENCES Proveedor(id_proveedor)
);

CREATE TABLE InventarioComponente (
    id_componente    INT PRIMARY KEY,
    cantidad_actual  INT           NOT NULL DEFAULT 0,
    umbral_minimo    INT           NOT NULL DEFAULT 0, 
    ultima_revision  DATETIME      DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_componente) REFERENCES ComponenteFabrica(id_componente)
);

CREATE TABLE PedidoCompra (
    id_pedido_compra   INT AUTO_INCREMENT PRIMARY KEY,
    id_componente      INT           NOT NULL,
    id_proveedor       INT           NOT NULL,
    cantidad_solicitada INT          NOT NULL,
    fecha_pedido       DATETIME      DEFAULT CURRENT_TIMESTAMP,
    estado             ENUM('PENDIENTE','CONFIRMADO','EN_TRANSITO','RECIBIDO','CANCELADO')
                       DEFAULT 'PENDIENTE',
    fecha_entrega_estimada DATETIME,
    FOREIGN KEY (id_componente) REFERENCES ComponenteFabrica(id_componente),
    FOREIGN KEY (id_proveedor)  REFERENCES Proveedor(id_proveedor)
);

CREATE TABLE PlanificacionProduccion (
    id_planificacion  INT AUTO_INCREMENT PRIMARY KEY,
    id_modelo         INT           NOT NULL,
    cantidad_prevista INT           NOT NULL,
    fecha_inicio      DATE          NOT NULL,
    fecha_fin_prevista DATE         NOT NULL,
    estado            ENUM('PLANIFICADO','EN_PRODUCCION','COMPLETADO','PAUSADO')
                      DEFAULT 'PLANIFICADO',
    observaciones     TEXT,
    FOREIGN KEY (id_modelo) REFERENCES ModeloVehiculo(id_modelo)
);

CREATE TABLE SoapKey (
    id       INT AUTO_INCREMENT PRIMARY KEY,
    soap_key VARCHAR(100) UNIQUE NOT NULL
);

CREATE TABLE RestKey (
    id       INT AUTO_INCREMENT PRIMARY KEY,
    rest_key VARCHAR(100) UNIQUE NOT NULL
);

INSERT INTO Concesionario (id_concesionario, nombre, ciudad, direccion, telefono) VALUES
    (0, 'Sede Central Fabricante', 'Valencia', 'Polígono Industrial 1', '900111222');

INSERT INTO Concesionario (nombre, ciudad, direccion, telefono) VALUES
    ('Motor Levante', 'Alicante', 'Av. Denia 123',  '965111222'),
    ('Auto Sur',      'Murcia',   'Ronda Sur 45',    '968333444');

INSERT INTO ModeloVehiculo (nombre, descripcion, precio_base) VALUES
    ('SUV X-Treme', 'Todoterreno 4x4',         35000.00),
    ('EcoCity',     'Coche urbano eléctrico',   22000.00);

INSERT INTO InventarioVehiculo (vin, id_modelo, id_concesionario, estado, color, motor, fecha_fabricacion) VALUES
    ('VIN00000000000001', 1, 0,    'STOCK_FABRICANTE',    'Rojo',   'Diesel 2.0',      '2023-10-01'),
    ('VIN00000000000002', 2, 1,    'STOCK_CONCESIONARIO', 'Blanco', 'Eléctrico 150kW', '2023-11-15');

INSERT INTO Proveedor (nombre, contacto, telefono, email) VALUES
    ('Recambios García', 'Luis García', '961000111', 'luis@recambiosgarcia.com'),
    ('Componentes Tech', 'Ana Ruiz',    '962000222', 'ana@componentestech.com');

INSERT INTO Pieza (referencia, nombre, descripcion, precio, id_proveedor) VALUES
    ('PIE-0001', 'Filtro de aceite',  'Filtro para motor Diesel 2.0',      12.50, 1),
    ('PIE-0002', 'Pastillas de freno','Kit delantero para SUV X-Treme',    45.00, 1),
    ('PIE-0003', 'Batería 12V',       'Batería auxiliar para EcoCity',     110.00, 2);

INSERT INTO ComponenteFabrica (referencia, nombre, descripcion, id_proveedor) VALUES
    ('COMP-0001', 'Motor Diesel 2.0',      'Motor base para SUV X-Treme', 2),
    ('COMP-0002', 'Batería Principal 150kW','Batería de tracción EcoCity', 2);

INSERT INTO InventarioComponente (id_componente, cantidad_actual, umbral_minimo) VALUES
    (1, 15, 5),
    (2,  3, 5);  

INSERT INTO SoapKey (soap_key) VALUES
    ('1234');

INSERT INTO RestKey (rest_key) VALUES
    ('1234');