package es.ua.mtis.fabricanteDeVehiculos.config;

import es.ua.mtis.fabricanteDeVehiculos.entity.*;
import es.ua.mtis.fabricanteDeVehiculos.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    // ---- Proceso 2.1 ----
    @Autowired private ModeloVehiculoRepository modeloRepo;
    @Autowired private StockConcesionarioRepository stockConcesionarioRepo;
    @Autowired private StockFabricanteRepository stockFabricanteRepo;

    // ---- Proceso 2.2 ----
    @Autowired private ConfiguracionModeloRepository configuracionModeloRepo;

    // ---- Proceso 2.3 ----
    @Autowired private CatalogoPiezaRepository catalogoPiezaRepo;
    @Autowired private StockRepuestoRepository stockRepuestoRepo;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("========================================");
        System.out.println(" Cargando datos iniciales en H2...");
        System.out.println("========================================");

        // =================================================================
        // MODELOS DE VEHÍCULO (usados por los 3 procesos)
        // =================================================================
        ModeloVehiculo m1 = new ModeloVehiculo();
        m1.setNombre("SUV Premium");
        m1.setCosteProduccion(25000.0);
        m1.setDiasFabricacion(45);
        modeloRepo.save(m1); // ID = 1

        ModeloVehiculo m2 = new ModeloVehiculo();
        m2.setNombre("Compacto Urbano");
        m2.setCosteProduccion(12000.0);
        m2.setDiasFabricacion(30);
        modeloRepo.save(m2); // ID = 2

        ModeloVehiculo m3 = new ModeloVehiculo();
        m3.setNombre("Berlina Ejecutiva");
        m3.setCosteProduccion(38000.0);
        m3.setDiasFabricacion(60);
        modeloRepo.save(m3); // ID = 3

        // =================================================================
        // PROCESO 2.1 — Stock concesionario y stock fabricante
        // =================================================================

        // Stock en el concesionario 55
        StockConcesionario sc1 = new StockConcesionario();
        sc1.setIdConcesionario(55);
        sc1.setIdModelo(m1.getId());
        sc1.setCantidadDisponible(5);
        stockConcesionarioRepo.save(sc1);

        StockConcesionario sc2 = new StockConcesionario();
        sc2.setIdConcesionario(55);
        sc2.setIdModelo(m2.getId());
        sc2.setCantidadDisponible(0); // Sin stock local
        stockConcesionarioRepo.save(sc2);

        StockConcesionario sc3 = new StockConcesionario();
        sc3.setIdConcesionario(77);
        sc3.setIdModelo(m1.getId());
        sc3.setCantidadDisponible(3);
        stockConcesionarioRepo.save(sc3);

        StockConcesionario sc4 = new StockConcesionario();
        sc4.setIdConcesionario(77);
        sc4.setIdModelo(m2.getId());
        sc4.setCantidadDisponible(8);
        stockConcesionarioRepo.save(sc4);

        // Stock en fábrica (para ServicioStockFabricante)
        StockFabricante sf1 = new StockFabricante();
        sf1.setIdModelo(m1.getId());
        sf1.setCantidadDisponible(150);
        stockFabricanteRepo.save(sf1);

        StockFabricante sf2 = new StockFabricante();
        sf2.setIdModelo(m2.getId());
        sf2.setCantidadDisponible(300);
        stockFabricanteRepo.save(sf2);

        StockFabricante sf3 = new StockFabricante();
        sf3.setIdModelo(m3.getId());
        sf3.setCantidadDisponible(80);
        stockFabricanteRepo.save(sf3);

        // =================================================================
        // PROCESO 2.2 — Configuraciones válidas por modelo
        // =================================================================
        String[] configs = {"GASOLINA_MANUAL_ROJO", "DIESEL_AUTO_NEGRO", "ELECTRICO_AUTO_BLANCO"};
        boolean[] viables = {true, true, false}; // La tercera no es viable

        for (ModeloVehiculo modelo : new ModeloVehiculo[]{m1, m2, m3}) {
            for (int i = 0; i < configs.length; i++) {
                ConfiguracionModelo cm = new ConfiguracionModelo();
                cm.setIdModelo(modelo.getId());
                cm.setConfiguracion(configs[i]);
                cm.setEsViable(viables[i]);
                configuracionModeloRepo.save(cm);
            }
        }

        // =================================================================
        // PROCESO 2.3 — Catálogo de piezas y stock de repuestos
        // =================================================================
        CatalogoPieza p1 = new CatalogoPieza();
        p1.setNombrePieza("Filtro de Aceite");
        p1.setPrecio(45.50);
        p1.setTiempoSuministroDias(3);
        catalogoPiezaRepo.save(p1); // ID = 1

        CatalogoPieza p2 = new CatalogoPieza();
        p2.setNombrePieza("Pastillas de Freno Delanteras");
        p2.setPrecio(89.90);
        p2.setTiempoSuministroDias(5);
        catalogoPiezaRepo.save(p2); // ID = 2

        CatalogoPieza p3 = new CatalogoPieza();
        p3.setNombrePieza("Correa de Distribución");
        p3.setPrecio(210.00);
        p3.setTiempoSuministroDias(7);
        catalogoPiezaRepo.save(p3); // ID = 3

        CatalogoPieza p4 = new CatalogoPieza();
        p4.setNombrePieza("Amortiguador Trasero");
        p4.setPrecio(155.00);
        p4.setTiempoSuministroDias(4);
        catalogoPiezaRepo.save(p4); // ID = 4

        // Stock de repuestos en concesionario 55
        StockRepuesto sr1 = new StockRepuesto();
        sr1.setIdConcesionario(55);
        sr1.setIdPieza(p1.getIdPieza());
        sr1.setCantidadDisponible(20);
        stockRepuestoRepo.save(sr1);

        StockRepuesto sr2 = new StockRepuesto();
        sr2.setIdConcesionario(55);
        sr2.setIdPieza(p2.getIdPieza());
        sr2.setCantidadDisponible(8);
        stockRepuestoRepo.save(sr2);

        StockRepuesto sr3 = new StockRepuesto();
        sr3.setIdConcesionario(55);
        sr3.setIdPieza(p3.getIdPieza());
        sr3.setCantidadDisponible(0); // Sin stock local
        stockRepuestoRepo.save(sr3);

        // Stock de repuestos en concesionario 77
        StockRepuesto sr4 = new StockRepuesto();
        sr4.setIdConcesionario(77);
        sr4.setIdPieza(p1.getIdPieza());
        sr4.setCantidadDisponible(15);
        stockRepuestoRepo.save(sr4);

        StockRepuesto sr5 = new StockRepuesto();
        sr5.setIdConcesionario(77);
        sr5.setIdPieza(p4.getIdPieza());
        sr5.setCantidadDisponible(6);
        stockRepuestoRepo.save(sr5);

        System.out.println("========================================");
        System.out.println(" Datos iniciales cargados correctamente");
        System.out.println(" Modelos: " + modeloRepo.count());
        System.out.println(" Stock concesionarios: " + stockConcesionarioRepo.count());
        System.out.println(" Stock fábrica: " + stockFabricanteRepo.count());
        System.out.println(" Configuraciones modelo: " + configuracionModeloRepo.count());
        System.out.println(" Catálogo piezas: " + catalogoPiezaRepo.count());
        System.out.println(" Stock repuestos: " + stockRepuestoRepo.count());
        System.out.println("========================================");
    }
}