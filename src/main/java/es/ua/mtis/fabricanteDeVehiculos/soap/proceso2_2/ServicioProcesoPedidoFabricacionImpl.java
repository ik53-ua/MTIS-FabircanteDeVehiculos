package es.ua.mtis.fabricanteDeVehiculos.soap.proceso2_2;

import org.mtis.servicioprocesopedidofabricacion.*;
import org.springframework.stereotype.Service;
import jakarta.jws.WebService;

@Service
@WebService(
    serviceName = "ServicioProcesoPedidoFabricacion",
    portName = "ServicioProcesoPedidoFabricacionPort",
    targetNamespace = "http://mtis.org/ServicioProcesoPedidoFabricacion/",
    endpointInterface = "org.mtis.servicioprocesopedidofabricacion.ServicioProcesoPedidoFabricacionPortType"
)
public class ServicioProcesoPedidoFabricacionImpl implements ServicioProcesoPedidoFabricacionPortType {
    @Override
    public ProcesarPedidoFabricacionResponse procesarPedidoFabricacion(ProcesarPedidoFabricacionRequest parameters) {
        ProcesarPedidoFabricacionResponse res = new ProcesarPedidoFabricacionResponse();
        res.setExito(true);
        res.setMensaje("Pedido de fabricación recibido y en proceso.");
        res.setCoste(18500.00);
        res.setTiempoFabricacionDias(30);
        res.setFechaEntregaEstimada("2026-07-01");
        return res;
    }
}