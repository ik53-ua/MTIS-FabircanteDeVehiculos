package es.ua.mtis.fabricanteDeVehiculos.soap.proceso2_1;

import org.mtis.servicioregistroreservas.ServicioRegistroReservasPortType;
import org.mtis.servicioregistroreservas.RegistrarReservaRequest;
import org.mtis.servicioregistroreservas.RegistrarReservaResponse;
import org.springframework.stereotype.Service;
import jakarta.jws.WebService;

@Service
@WebService(
    serviceName = "ServicioRegistroReservas",
    portName = "ServicioRegistroReservasPort",
    targetNamespace = "http://mtis.org/ServicioRegistroReservas/",
    endpointInterface = "org.mtis.servicioregistroreservas.ServicioRegistroReservasPortType"
)
public class ServicioRegistroReservasImpl implements ServicioRegistroReservasPortType {

    @Override
    public RegistrarReservaResponse registrarReserva(RegistrarReservaRequest parameters) {
        RegistrarReservaResponse response = new RegistrarReservaResponse();
        
        System.out.println("Registrando reserva con código de bloqueo: " + parameters.getCodigoBloqueo());
        
        response.setExito(true);
        response.setMensaje("Reserva registrada en el sistema central.");
        response.setIdReserva(1024);
        
        return response;
    }
}