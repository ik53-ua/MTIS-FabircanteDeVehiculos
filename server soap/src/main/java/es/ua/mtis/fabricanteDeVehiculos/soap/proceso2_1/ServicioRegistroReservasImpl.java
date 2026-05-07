package es.ua.mtis.fabricanteDeVehiculos.soap.proceso2_1;

import es.ua.mtis.fabricanteDeVehiculos.entity.ReservaVehiculo;
import es.ua.mtis.fabricanteDeVehiculos.repository.ReservaVehiculoRepository;
import org.mtis.servicioregistroreservas.ServicioRegistroReservasPortType;
import org.mtis.servicioregistroreservas.RegistrarReservaRequest;
import org.mtis.servicioregistroreservas.RegistrarReservaResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.jws.WebService;
import java.util.Optional;

@Service
@WebService(
    serviceName = "ServicioRegistroReservas",
    portName = "ServicioRegistroReservasPort",
    targetNamespace = "http://mtis.org/ServicioRegistroReservas/",
    endpointInterface = "org.mtis.servicioregistroreservas.ServicioRegistroReservasPortType"
)
public class ServicioRegistroReservasImpl implements ServicioRegistroReservasPortType {

    @Autowired
    private ReservaVehiculoRepository reservaRepo;

    @Override
    public RegistrarReservaResponse registrarReserva(RegistrarReservaRequest parameters) {
        RegistrarReservaResponse response = new RegistrarReservaResponse();
        System.out.println("Registrando reserva en BD con código de bloqueo: " + parameters.getCodigoBloqueo());

        // Buscamos la reserva por su código de bloqueo en H2
        Optional<ReservaVehiculo> reservaOpt = reservaRepo.findByCodigoBloqueo(parameters.getCodigoBloqueo());

        if (reservaOpt.isPresent()) {
            ReservaVehiculo reserva = reservaOpt.get();
            reserva.setEstado("CONFIRMADO"); // Actualizamos el estado al valor canónico
            reservaRepo.save(reserva); // Guardamos el cambio en la BD

            response.setExito(true);
            response.setMensaje("Reserva confirmada y registrada en el sistema central.");
            response.setIdReserva(reserva.getIdReserva());
        } else {
            response.setExito(false);
            response.setMensaje("Error: No se ha encontrado ninguna reserva con ese código de bloqueo.");
            response.setIdReserva(0);
        }

        return response;
    }
}