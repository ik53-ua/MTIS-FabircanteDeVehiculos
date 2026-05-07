package es.ua.mtis.fabricanteDeVehiculos.soap.proceso2_2;

import es.ua.mtis.fabricanteDeVehiculos.entity.PedidoFabricacion;
import es.ua.mtis.fabricanteDeVehiculos.repository.PedidoFabricacionRepository;
import org.mtis.servicionotificacionespedido.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.jws.WebService;
import java.util.Optional;

@Service
@WebService(
    serviceName = "ServicioNotificacionesPedido",
    portName = "ServicioNotificacionesPedidoPort",
    targetNamespace = "http://mtis.org/ServicioNotificacionesPedido/",
    endpointInterface = "org.mtis.servicionotificacionespedido.ServicioNotificacionesPedidoPortType"
)
public class ServicioNotificacionesPedidoImpl implements ServicioNotificacionesPedidoPortType {

    @Autowired
    private PedidoFabricacionRepository pedidoRepo;

    @Override
    public EnviarNotificacionPedidoResponse enviarNotificacionPedido(EnviarNotificacionPedidoRequest parameters) {
        EnviarNotificacionPedidoResponse res = new EnviarNotificacionPedidoResponse();
        System.out.println("Enviando notificación al concesionario: " + parameters.getIdConcesionario()
                + " para pedido: " + parameters.getIdPedido()
                + " — Mensaje: " + parameters.getMensajeAEnviar());

        if (parameters.getWSKey() != null && !parameters.getWSKey().isEmpty()) {
            // idPedido es opcional en el WSDL (minOccurs=0), comprobamos si viene informado
            Integer idPedido = parameters.getIdPedido();

            if (idPedido != null) {
                // Verificamos que el pedido existe en BD antes de notificar
                Optional<PedidoFabricacion> pedidoOpt = pedidoRepo.findById(idPedido);

                if (pedidoOpt.isPresent()) {
                    PedidoFabricacion pedido = pedidoOpt.get();
                    String mensajeCompleto = parameters.getMensajeAEnviar()
                            + " | Estado del pedido: " + pedido.getEstado();
                    res.setExito(true);
                    res.setMensaje(mensajeCompleto);
                } else {
                    res.setExito(false);
                    res.setMensaje("Error: El pedido ID " + idPedido + " no existe en la base de datos.");
                }
            } else {
                // Notificación genérica sin pedido asociado
                res.setExito(true);
                res.setMensaje("Notificación enviada al concesionario " + parameters.getIdConcesionario()
                        + ": " + parameters.getMensajeAEnviar());
            }
        } else {
            res.setExito(false);
            res.setMensaje("Error: WSKey inválida o ausente.");
        }

        return res;
    }
}