package es.ua.mtis.fabricanteDeVehiculos.soap.proceso2_3;

import es.ua.mtis.fabricanteDeVehiculos.entity.CatalogoPieza;
import es.ua.mtis.fabricanteDeVehiculos.entity.EnvioRepuesto;
import es.ua.mtis.fabricanteDeVehiculos.entity.PedidoRepuesto;
import es.ua.mtis.fabricanteDeVehiculos.repository.CatalogoPiezaRepository;
import es.ua.mtis.fabricanteDeVehiculos.repository.EnvioRepuestoRepository;
import es.ua.mtis.fabricanteDeVehiculos.repository.PedidoRepuestoRepository;
import org.mtis.servicioprocesosolicitudrepuestos.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.jws.WebService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Servicio orquestador del proceso 2.3.
 * Parsea la lista de piezas requeridas, verifica cada una en el catálogo,
 * genera un pedido y programa el envío logístico — todo con datos reales de BD.
 */
@Service
@WebService(
    serviceName = "ServicioProcesoSolicitudRepuestos",
    portName = "ServicioProcesoSolicitudRepuestosPort",
    targetNamespace = "http://mtis.org/ServicioProcesoSolicitudRepuestos/",
    endpointInterface = "org.mtis.servicioprocesosolicitudrepuestos.ServicioProcesoSolicitudRepuestosPortType"
)
public class ServicioProcesoSolicitudRepuestosImpl implements ServicioProcesoSolicitudRepuestosPortType {

    @Autowired
    private CatalogoPiezaRepository catalogoRepo;

    @Autowired
    private PedidoRepuestoRepository pedidoRepo;

    @Autowired
    private EnvioRepuestoRepository envioRepo;

    @Override
    public ProcesarSolicitudRepuestosResponse procesarSolicitudRepuestos(ProcesarSolicitudRepuestosRequest parameters) {
        ProcesarSolicitudRepuestosResponse res = new ProcesarSolicitudRepuestosResponse();
        System.out.println("Procesando solicitud de repuestos para concesionario: "
                + parameters.getIdConcesionario()
                + " | Diagnóstico: " + parameters.getDiagnostico()
                + " | Piezas: " + parameters.getListaPiezasRequeridas());

        if (parameters.getWSKey() == null || parameters.getWSKey().isEmpty()) {
            res.setExito(false);
            res.setMensaje("Error: WSKey inválida o ausente.");
            return res;
        }

        // listaPiezasRequeridas es un String con IDs separados por comas, e.g. "1,3,5"
        String[] idsPiezas = parameters.getListaPiezasRequeridas().split(",");
        int maxSuministro = 0;
        StringBuilder resumenPiezas = new StringBuilder();

        for (String idStr : idsPiezas) {
            try {
                int idPieza = Integer.parseInt(idStr.trim());
                Optional<CatalogoPieza> piezaOpt = catalogoRepo.findById(idPieza);

                if (piezaOpt.isPresent()) {
                    CatalogoPieza pieza = piezaOpt.get();

                    // Generamos un pedido por cada pieza solicitada
                    PedidoRepuesto pedido = new PedidoRepuesto();
                    pedido.setIdConcesionario(parameters.getIdConcesionario());
                    pedido.setIdPieza(idPieza);
                    pedido.setCantidad(1); // Cantidad unitaria por defecto en la solicitud
                    pedido.setEstado("PENDIENTE");
                    pedido.setFechaCreacion(LocalDateTime.now());
                    PedidoRepuesto pedidoGuardado = pedidoRepo.save(pedido);

                    // Programamos el envío para cada pedido
                    int diasEntrega = pieza.getTiempoSuministroDias() != null ? pieza.getTiempoSuministroDias() : 7;
                    if (diasEntrega > maxSuministro) maxSuministro = diasEntrega;

                    EnvioRepuesto envio = new EnvioRepuesto();
                    envio.setIdPedido(pedidoGuardado.getIdPedido());
                    envio.setIdConcesionario(parameters.getIdConcesionario());
                    envio.setFechaEstimada(LocalDate.now().plusDays(diasEntrega).toString());
                    envio.setEstado("PROGRAMADO");
                    envio.setFechaCreacion(LocalDateTime.now());
                    envioRepo.save(envio);

                    resumenPiezas.append(pieza.getNombrePieza()).append("; ");
                } else {
                    resumenPiezas.append("[Pieza ID ").append(idPieza).append(" no encontrada]; ");
                }
            } catch (NumberFormatException e) {
                resumenPiezas.append("[ID inválido: ").append(idStr).append("]; ");
            }
        }

        // La fecha de recepción esperada es la del artículo con mayor tiempo de suministro
        String fechaRecepcion = LocalDate.now().plusDays(maxSuministro > 0 ? maxSuministro : 7).toString();

        res.setExito(true);
        res.setMensaje("Solicitud procesada. Diagnóstico: " + parameters.getDiagnostico()
                + ". Piezas procesadas: " + resumenPiezas.toString().trim());
        res.setFechaRecepcionEsperada(fechaRecepcion);

        return res;
    }
}