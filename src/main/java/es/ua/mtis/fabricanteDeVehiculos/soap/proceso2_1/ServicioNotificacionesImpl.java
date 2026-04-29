package es.ua.mtis.fabricanteDeVehiculos.soap.proceso2_1;

import org.mtis.servicionotificaciones.ServicioNotificacionesPortType;
import org.mtis.servicionotificaciones.EnviarConfirmacionRequest;
import org.mtis.servicionotificaciones.EnviarConfirmacionResponse;
import org.springframework.stereotype.Service;
import jakarta.jws.WebService;

@Service
@WebService(
    serviceName = "ServicioNotificaciones",
    portName = "ServicioNotificacionesPort",
    targetNamespace = "http://mtis.org/ServicioNotificaciones/",
    endpointInterface = "org.mtis.servicionotificaciones.ServicioNotificacionesPortType"
)
public class ServicioNotificacionesImpl implements ServicioNotificacionesPortType {

    @Override
    public EnviarConfirmacionResponse enviarConfirmacion(EnviarConfirmacionRequest parameters) {
        EnviarConfirmacionResponse response = new EnviarConfirmacionResponse();
        
        System.out.println("Enviando confirmación al concesionario: " + parameters.getIdConcesionario() + " para la reserva: " + parameters.getIdReserva());
        
        if (parameters.getWSKey() != null && !parameters.getWSKey().isEmpty()) {
            response.setExito(true);
            response.setMensaje("Notificación enviada correctamente: " + parameters.getMensajeAEnviar());
        } else {
            response.setExito(false);
            response.setMensaje("Error: WSKey inválida");
        }
        
        return response;
    }
}