package es.ua.mtis.fabricanteDeVehiculos.soap.proceso2_1;

import org.mtis.servicioprocesoreservavehiculo.ServicioProcesoReservaVehiculoPortType;
import org.mtis.servicioprocesoreservavehiculo.ReservarVehiculoRequest;
import org.mtis.servicioprocesoreservavehiculo.ReservarVehiculoResponse;
import org.springframework.stereotype.Service;
import jakarta.jws.WebService;

@Service
@WebService(
    serviceName = "ServicioProcesoReservaVehiculo",
    portName = "ServicioProcesoReservaVehiculoPort",
    targetNamespace = "http://mtis.org/ServicioProcesoReservaVehiculo/",
    endpointInterface = "org.mtis.servicioprocesoreservavehiculo.ServicioProcesoReservaVehiculoPortType"
)
public class ServicioProcesoReservaVehiculoImpl implements ServicioProcesoReservaVehiculoPortType {

    @Override
    public ReservarVehiculoResponse reservarVehiculo(ReservarVehiculoRequest parameters) {
        ReservarVehiculoResponse response = new ReservarVehiculoResponse();
        
        System.out.println("Procesando reserva para modelo: " + parameters.getIdModelo());
        
        if (parameters.getWSKey() != null) {
            response.setExito(true);
            response.setMensaje("Reserva procesada con éxito");
            response.setIdReserva(999); // ID simulado
        } else {
            response.setExito(false);
            response.setMensaje("Error en la reserva");
        }
        
        return response;
    }
}