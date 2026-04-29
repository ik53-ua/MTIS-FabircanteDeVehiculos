package es.ua.mtis.fabricanteDeVehiculos.soap.proceso2_2;

import org.mtis.servicioregistropedidos.*;
import org.springframework.stereotype.Service;
import jakarta.jws.WebService;

@Service
@WebService(
    serviceName = "ServicioRegistroPedidos",
    portName = "ServicioRegistroPedidosPort",
    targetNamespace = "http://mtis.org/ServicioRegistroPedidos/",
    endpointInterface = "org.mtis.servicioregistropedidos.ServicioRegistroPedidosPortType"
)
public class ServicioRegistroPedidosImpl implements ServicioRegistroPedidosPortType {
    @Override
    public RegistrarPedidoFabricacionResponse registrarPedidoFabricacion(RegistrarPedidoFabricacionRequest parameters) {
        RegistrarPedidoFabricacionResponse res = new RegistrarPedidoFabricacionResponse();
        int idPedido = (int) (Math.random() * 5000);
        
        res.setExito(true);
        res.setMensaje("Pedido registrado correctamente.");
        res.setIdPedido(idPedido);
        return res;
    }
}