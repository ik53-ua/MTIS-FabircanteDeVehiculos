package es.ua.mtis.fabricanteDeVehiculos.soap.proceso2_2;

import org.mtis.servicioplanificacionproduccion.*;
import org.springframework.stereotype.Service;
import jakarta.jws.WebService;

@Service
@WebService(
    serviceName = "ServicioPlanificacionProduccion",
    portName = "ServicioPlanificacionProduccionPort",
    targetNamespace = "http://mtis.org/ServicioPlanificacionProduccion/",
    endpointInterface = "org.mtis.servicioplanificacionproduccion.ServicioPlanificacionProduccionPortType"
)
public class ServicioPlanificacionProduccionImpl implements ServicioPlanificacionProduccionPortType {
    @Override
    public ConsultarCapacidadProduccionResponse consultarCapacidadProduccion(ConsultarCapacidadProduccionRequest parameters) {
        ConsultarCapacidadProduccionResponse res = new ConsultarCapacidadProduccionResponse();
        res.setExito(true);
        res.setMensaje("Capacidad disponible");
        res.setDiasEstimados(45);
        res.setFechaEntrega("2026-06-15");
        return res;
    }
}