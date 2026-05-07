package es.ua.mtis.fabricanteDeVehiculos.soap.proceso2_2;

import es.ua.mtis.fabricanteDeVehiculos.entity.OrdenProduccion;
import es.ua.mtis.fabricanteDeVehiculos.entity.PedidoFabricacion;
import es.ua.mtis.fabricanteDeVehiculos.repository.OrdenProduccionRepository;
import es.ua.mtis.fabricanteDeVehiculos.repository.PedidoFabricacionRepository;
import org.mtis.servicioregistropedidos.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.jws.WebService;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@WebService(
    serviceName = "ServicioRegistroPedidos",
    portName = "ServicioRegistroPedidosPort",
    targetNamespace = "http://mtis.org/ServicioRegistroPedidos/",
    endpointInterface = "org.mtis.servicioregistropedidos.ServicioRegistroPedidosPortType"
)
public class ServicioRegistroPedidosImpl implements ServicioRegistroPedidosPortType {

    @Autowired
    private OrdenProduccionRepository ordenRepo;

    @Autowired
    private PedidoFabricacionRepository pedidoRepo;

    @Override
    public RegistrarPedidoFabricacionResponse registrarPedidoFabricacion(RegistrarPedidoFabricacionRequest parameters) {
        RegistrarPedidoFabricacionResponse res = new RegistrarPedidoFabricacionResponse();
        System.out.println("Registrando pedido para concesionario: " + parameters.getIdConcesionario()
                + " con orden: " + parameters.getIdOrden());

        // Verificamos que la orden existe en BD (fue generada previamente por ServicioGeneracionOrdenProduccion)
        Optional<OrdenProduccion> ordenOpt = ordenRepo.findById(parameters.getIdOrden());

        if (ordenOpt.isPresent()) {
            // Actualizamos el estado de la orden
            OrdenProduccion orden = ordenOpt.get();
            orden.setEstado("REGISTRADA");
            ordenRepo.save(orden);

            // Creamos el pedido de fabricación en BD
            PedidoFabricacion pedido = new PedidoFabricacion();
            pedido.setIdConcesionario(parameters.getIdConcesionario());
            pedido.setIdOrden(parameters.getIdOrden());
            pedido.setEstado("REGISTRADO");
            pedido.setFechaRegistro(LocalDateTime.now());

            PedidoFabricacion guardado = pedidoRepo.save(pedido);

            res.setExito(true);
            res.setMensaje("Pedido de fabricación registrado correctamente.");
            res.setIdPedido(guardado.getIdPedido()); // ID real de BD
        } else {
            res.setExito(false);
            res.setMensaje("Error: No existe una orden de producción con ID " + parameters.getIdOrden() + ".");
        }

        return res;
    }
}