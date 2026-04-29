package es.ua.mtis.fabricanteDeVehiculos.soap.proceso2_1;

import org.mtis.servicioreservavehiculo.ServicioReservaVehiculoPortType;
import org.mtis.servicioreservavehiculo.BloquearVehiculoRequest;
import org.mtis.servicioreservavehiculo.BloquearVehiculoResponse;
import org.springframework.stereotype.Service;
import jakarta.jws.WebService;
import java.util.UUID;

@Service
@WebService(
    serviceName = "ServicioReservaVehiculo",
    portName = "ServicioReservaVehiculoPort",
    targetNamespace = "http://mtis.org/ServicioReservaVehiculo/",
    endpointInterface = "org.mtis.servicioreservavehiculo.ServicioReservaVehiculoPortType"
)
public class ServicioReservaVehiculoImpl implements ServicioReservaVehiculoPortType {

    @Override
    public BloquearVehiculoResponse bloquearVehiculo(BloquearVehiculoRequest parameters) {
        BloquearVehiculoResponse response = new BloquearVehiculoResponse();
        
        System.out.println("Intentando bloquear vehículo modelo: " + parameters.getIdModelo() + " para el concesionario: " + parameters.getIdConcesionario());
        
        if (parameters.getWSKey() != null && !parameters.getWSKey().isEmpty()) {
            response.setExito(true);
            response.setMensaje("Vehículo bloqueado con éxito");
            response.setBloqueado(true);
            // Generamos un código de bloqueo ficticio para simular el comportamiento real
            response.setCodigoBloqueo("BLK-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        } else {
            response.setExito(false);
            response.setMensaje("Error: Validación de seguridad fallida");
            response.setBloqueado(false);
        }
        
        return response;
    }
}