package es.ua.mtis.fabricanteDeVehiculos.soap.proceso2_2;

import org.mtis.serviciogeneracionordenproduccion.*;
import org.springframework.stereotype.Service;
import jakarta.jws.WebService;

@Service
@WebService(
    serviceName = "ServicioGeneracionOrdenProduccion",
    portName = "ServicioGeneracionOrdenProduccionPort",
    targetNamespace = "http://mtis.org/ServicioGeneracionOrdenProduccion/",
    endpointInterface = "org.mtis.serviciogeneracionordenproduccion.ServicioGeneracionOrdenProduccionPortType"
)
public class ServicioGeneracionOrdenProduccionImpl implements ServicioGeneracionOrdenProduccionPortType {
    @Override
    public GenerarOrdenProduccionResponse generarOrdenProduccion(GenerarOrdenProduccionRequest parameters) {
        GenerarOrdenProduccionResponse res = new GenerarOrdenProduccionResponse();
        int idOrden = (int) (Math.random() * 10000);
        
        res.setExito(true);
        res.setMensaje("Orden de producción generada.");
        res.setIdOrden(idOrden);
        return res;
    }
}