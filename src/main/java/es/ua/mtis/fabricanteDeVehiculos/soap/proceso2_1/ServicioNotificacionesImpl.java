package es.ua.mtis.fabricanteDeVehiculos.soap.proceso2_1;

import es.ua.mtis.fabricanteDeVehiculos.entity.ReservaVehiculo;
import es.ua.mtis.fabricanteDeVehiculos.repository.ReservaVehiculoRepository;
import org.mtis.servicionotificaciones.p21.ServicioNotificacionesPortType;
import org.mtis.servicionotificaciones.p21.EnviarConfirmacionRequest;
import org.mtis.servicionotificaciones.p21.EnviarConfirmacionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.jws.WebService;
import java.util.Optional;

@Service
@WebService(
    serviceName = "ServicioNotificaciones",
    portName = "ServicioNotificacionesPort",
    targetNamespace = "http://mtis.org/ServicioNotificaciones/",
    endpointInterface = "org.mtis.servicionotificaciones.p21.ServicioNotificacionesPortType"
)
public class ServicioNotificacionesImpl implements ServicioNotificacionesPortType {

    @Autowired
    private ReservaVehiculoRepository reservaRepo;

    @Override
    public EnviarConfirmacionResponse enviarConfirmacion(EnviarConfirmacionRequest parameters) {
        EnviarConfirmacionResponse response = new EnviarConfirmacionResponse();
        System.out.println("Enviando notificación al concesionario: " + parameters.getIdConcesionario()
                + " para la reserva: " + parameters.getIdReserva()
                + " — Mensaje: " + parameters.getMensajeAEnviar());

        if (parameters.getWSKey() != null && !parameters.getWSKey().isEmpty()) {
            // Verificamos que la reserva existe en nuestra BD H2
            Optional<ReservaVehiculo> reservaOpt = reservaRepo.findById(parameters.getIdReserva());

            if (reservaOpt.isPresent()) {
                ReservaVehiculo reserva = reservaOpt.get();
                // Usamos el mensajeAEnviar definido en el WSDL para construir la respuesta
                String mensajeCompleto = parameters.getMensajeAEnviar()
                        + " | Estado actual de la reserva: " + reserva.getEstado();
                response.setExito(true);
                response.setMensaje(mensajeCompleto);
            } else {
                response.setExito(false);
                response.setMensaje("Error: La reserva ID " + parameters.getIdReserva()
                        + " no existe en la base de datos.");
            }
        } else {
            response.setExito(false);
            response.setMensaje("Error: WSKey inválida o ausente.");
        }
        return response;
    }
}