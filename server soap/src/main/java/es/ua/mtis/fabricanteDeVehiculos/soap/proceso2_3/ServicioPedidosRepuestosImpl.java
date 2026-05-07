package es.ua.mtis.fabricanteDeVehiculos.soap.proceso2_3;

import es.ua.mtis.fabricanteDeVehiculos.entity.CatalogoPieza;
import es.ua.mtis.fabricanteDeVehiculos.entity.PedidoRepuesto;
import es.ua.mtis.fabricanteDeVehiculos.repository.CatalogoPiezaRepository;
import es.ua.mtis.fabricanteDeVehiculos.repository.PedidoRepuestoRepository;
import org.mtis.serviciopedidosrepuestos.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.jws.WebService;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@WebService(
    serviceName = "ServicioPedidosRepuestos",
    portName = "ServicioPedidosRepuestosPort",
    targetNamespace = "http://mtis.org/ServicioPedidosRepuestos/",
    endpointInterface = "org.mtis.serviciopedidosrepuestos.ServicioPedidosRepuestosPortType"
)
public class ServicioPedidosRepuestosImpl implements ServicioPedidosRepuestosPortType {

    @Autowired
    private CatalogoPiezaRepository catalogoRepo;

    @Autowired
    private PedidoRepuestoRepository pedidoRepo;

    @Override
    public GenerarPedidoRepuestosResponse generarPedidoRepuestos(GenerarPedidoRepuestosRequest parameters) {
        GenerarPedidoRepuestosResponse res = new GenerarPedidoRepuestosResponse();
        System.out.println("Generando pedido de repuestos en BD para concesionario: "
                + parameters.getIdConcesionario() + ", pieza: " + parameters.getIdPieza()
                + ", cantidad: " + parameters.getCantidad());

        if (parameters.getWSKey() == null || parameters.getWSKey().isEmpty()) {
            res.setExito(false);
            res.setMensaje("Error: WSKey inválida o ausente.");
            return res;
        }

        // Verificar que la pieza existe en el catálogo antes de crear el pedido
        Optional<CatalogoPieza> piezaOpt = catalogoRepo.findById(parameters.getIdPieza());
        if (piezaOpt.isEmpty()) {
            res.setExito(false);
            res.setMensaje("Error: La pieza con ID " + parameters.getIdPieza() + " no existe en el catálogo.");
            return res;
        }

        // Crear y persistir el pedido en BD
        PedidoRepuesto pedido = new PedidoRepuesto();
        pedido.setIdConcesionario(parameters.getIdConcesionario());
        pedido.setIdPieza(parameters.getIdPieza());
        pedido.setCantidad(parameters.getCantidad());
        pedido.setEstado("PENDIENTE");
        pedido.setFechaCreacion(LocalDateTime.now());

        PedidoRepuesto guardado = pedidoRepo.save(pedido);

        res.setExito(true);
        res.setMensaje("Pedido de repuestos generado correctamente para la pieza: "
                + piezaOpt.get().getNombrePieza());
        res.setIdPedido(guardado.getIdPedido()); // ID real de BD

        return res;
    }
}