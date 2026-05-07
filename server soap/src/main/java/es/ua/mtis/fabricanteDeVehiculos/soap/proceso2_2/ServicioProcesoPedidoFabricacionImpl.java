package es.ua.mtis.fabricanteDeVehiculos.soap.proceso2_2;

import es.ua.mtis.fabricanteDeVehiculos.entity.ModeloVehiculo;
import es.ua.mtis.fabricanteDeVehiculos.entity.OrdenProduccion;
import es.ua.mtis.fabricanteDeVehiculos.entity.PedidoFabricacion;
import es.ua.mtis.fabricanteDeVehiculos.repository.ConfiguracionModeloRepository;
import es.ua.mtis.fabricanteDeVehiculos.repository.ModeloVehiculoRepository;
import es.ua.mtis.fabricanteDeVehiculos.repository.OrdenProduccionRepository;
import es.ua.mtis.fabricanteDeVehiculos.repository.PedidoFabricacionRepository;
import org.mtis.servicioprocesopedidofabricacion.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.jws.WebService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Servicio orquestador del proceso 2.2.
 * Valida configuración, calcula costes, consulta capacidad, genera orden y registra pedido —
 * todo internamente usando los datos reales de la BD.
 */
@Service
@WebService(
    serviceName = "ServicioProcesoPedidoFabricacion",
    portName = "ServicioProcesoPedidoFabricacionPort",
    targetNamespace = "http://mtis.org/ServicioProcesoPedidoFabricacion/",
    endpointInterface = "org.mtis.servicioprocesopedidofabricacion.ServicioProcesoPedidoFabricacionPortType"
)
public class ServicioProcesoPedidoFabricacionImpl implements ServicioProcesoPedidoFabricacionPortType {

    @Autowired
    private ModeloVehiculoRepository modeloRepo;

    @Autowired
    private ConfiguracionModeloRepository configuracionRepo;

    @Autowired
    private OrdenProduccionRepository ordenRepo;

    @Autowired
    private PedidoFabricacionRepository pedidoRepo;

    @Override
    public ProcesarPedidoFabricacionResponse procesarPedidoFabricacion(ProcesarPedidoFabricacionRequest parameters) {
        ProcesarPedidoFabricacionResponse res = new ProcesarPedidoFabricacionResponse();
        System.out.println("Procesando pedido de fabricación para concesionario: " + parameters.getIdConcesionario()
                + ", modelo: " + parameters.getIdModelo()
                + ", configuracion: " + parameters.getConfiguracion());

        if (parameters.getWSKey() == null || parameters.getWSKey().isEmpty()) {
            res.setExito(false);
            res.setMensaje("Error: WSKey inválida o ausente.");
            return res;
        }

        // 1. Verificar que el modelo existe y obtener sus datos
        Optional<ModeloVehiculo> modeloOpt = modeloRepo.findById(parameters.getIdModelo());
        if (modeloOpt.isEmpty()) {
            res.setExito(false);
            res.setMensaje("Error: El modelo con ID " + parameters.getIdModelo() + " no existe.");
            return res;
        }
        ModeloVehiculo modelo = modeloOpt.get();

        // 2. Verificar que la configuración es viable (equivale a llamar a ServicioConfiguracionesVehiculo)
        boolean configViable = configuracionRepo
                .findByIdModeloAndConfiguracion(parameters.getIdModelo(), parameters.getConfiguracion())
                .map(c -> Boolean.TRUE.equals(c.getEsViable()))
                .orElse(false);

        if (!configViable) {
            res.setExito(false);
            res.setMensaje("Error: La configuración '" + parameters.getConfiguracion()
                    + "' no es viable para este modelo.");
            return res;
        }

        // 3. Obtener coste y días de fabricación del modelo (equivale a ServicioCalculoCostes + ServicioPlanificacion)
        Double coste = modelo.getCosteProduccion() != null ? modelo.getCosteProduccion() : 0.0;
        Integer dias = modelo.getDiasFabricacion() != null ? modelo.getDiasFabricacion() : 30;
        LocalDate fechaEntrega = LocalDate.now().plusDays(dias);

        // 4. Generar orden de producción en BD (equivale a ServicioGeneracionOrdenProduccion)
        OrdenProduccion orden = new OrdenProduccion();
        orden.setIdConcesionario(parameters.getIdConcesionario());
        orden.setIdModelo(parameters.getIdModelo());
        orden.setConfiguracion(parameters.getConfiguracion());
        orden.setEstado("PENDIENTE");
        orden.setFechaCreacion(LocalDateTime.now());
        OrdenProduccion ordenGuardada = ordenRepo.save(orden);

        // 5. Registrar pedido de fabricación en BD (equivale a ServicioRegistroPedidos)
        PedidoFabricacion pedido = new PedidoFabricacion();
        pedido.setIdConcesionario(parameters.getIdConcesionario());
        pedido.setIdOrden(ordenGuardada.getIdOrden());
        pedido.setEstado("REGISTRADO");
        pedido.setFechaRegistro(LocalDateTime.now());
        pedidoRepo.save(pedido);

        // Actualizar estado de la orden
        ordenGuardada.setEstado("REGISTRADA");
        ordenRepo.save(ordenGuardada);

        // Respuesta con datos reales de BD
        res.setExito(true);
        res.setMensaje("Pedido de fabricación procesado correctamente. Orden ID: " + ordenGuardada.getIdOrden());
        res.setCoste(coste);
        res.setTiempoFabricacionDias(dias);
        res.setFechaEntregaEstimada(fechaEntrega.toString());

        return res;
    }
}