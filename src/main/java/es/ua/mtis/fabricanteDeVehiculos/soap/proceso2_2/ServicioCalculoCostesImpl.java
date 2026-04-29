package es.ua.mtis.fabricanteDeVehiculos.soap.proceso2_2;

import org.mtis.serviciocalculocostes.*;
import org.springframework.stereotype.Service;
import jakarta.jws.WebService;

@Service
@WebService(
    serviceName = "ServicioCalculoCostes",
    portName = "ServicioCalculoCostesPort",
    targetNamespace = "http://mtis.org/ServicioCalculoCostes/",
    endpointInterface = "org.mtis.serviciocalculocostes.ServicioCalculoCostesPortType"
)
public class ServicioCalculoCostesImpl implements ServicioCalculoCostesPortType {
    @Override
    public CalcularCosteProduccionResponse calcularCosteProduccion(CalcularCosteProduccionRequest parameters) {
        CalcularCosteProduccionResponse res = new CalcularCosteProduccionResponse();
        res.setExito(true);
        res.setMensaje("Coste calculado para modelo " + parameters.getIdModelo());
        res.setCosteTotal(15000.50); // Valor simulado
        return res;
    }
}