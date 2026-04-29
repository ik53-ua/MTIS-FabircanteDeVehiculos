package es.ua.mtis.fabricanteDeVehiculos.soap.proceso2_2;

import org.mtis.servicionotificacionespedido.*;
import org.springframework.stereotype.Service;
import jakarta.jws.WebService;

@Service
@WebService(
    serviceName = "ServicioNotificacionesPedido",
    portName = "ServicioNotificacionesPedidoPort",
    targetNamespace = "http://mtis.org/ServicioNotificacionesPedido/",
    endpointInterface = "org.mtis.servicionotificacionespedido.ServicioNotificacionesPedidoPortType"
)
public class ServicioNotificacionesPedidoImpl implements ServicioNotificacionesPedidoPortType {
    @Override
    public EnviarNotificacionPedidoResponse enviarNotificacionPedido(EnviarNotificacionPedidoRequest parameters) {
        EnviarNotificacionPedidoResponse res = new EnviarNotificacionPedidoResponse();
        System.out.println("Notificando pedido " + parameters.getIdPedido() + " al concesionario " + parameters.getIdConcesionario());
        
        res.setExito(true);
        res.setMensaje("Notificación de pedido enviada.");
        return res;
    }
}