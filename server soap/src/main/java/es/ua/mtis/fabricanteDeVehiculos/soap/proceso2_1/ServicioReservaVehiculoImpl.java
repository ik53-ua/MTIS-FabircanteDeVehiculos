package es.ua.mtis.fabricanteDeVehiculos.soap.proceso2_1;

import es.ua.mtis.fabricanteDeVehiculos.entity.ReservaVehiculo;
import es.ua.mtis.fabricanteDeVehiculos.repository.ReservaVehiculoRepository;
import org.mtis.servicioreservavehiculo.ServicioReservaVehiculoPortType;
import org.mtis.servicioreservavehiculo.BloquearVehiculoRequest;
import org.mtis.servicioreservavehiculo.BloquearVehiculoResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.jws.WebService;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@WebService(
    serviceName = "ServicioReservaVehiculo",
    portName = "ServicioReservaVehiculoPort",
    targetNamespace = "http://mtis.org/ServicioReservaVehiculo/",
    endpointInterface = "org.mtis.servicioreservavehiculo.ServicioReservaVehiculoPortType"
)
public class ServicioReservaVehiculoImpl implements ServicioReservaVehiculoPortType {

    // ¡Inyectamos la tabla de reservas!
    @Autowired
    private ReservaVehiculoRepository reservaRepo;

    @Override
    public BloquearVehiculoResponse bloquearVehiculo(BloquearVehiculoRequest parameters) {
        BloquearVehiculoResponse response = new BloquearVehiculoResponse();
        System.out.println("Guardando reserva REAL en BD para modelo: " + parameters.getIdModelo());

        if (parameters.getWSKey() != null && !parameters.getWSKey().isEmpty()) {
            
            // Creamos el objeto para la base de datos
            String codigoUnico = "BLK-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
            ReservaVehiculo nuevaReserva = new ReservaVehiculo();
            nuevaReserva.setIdConcesionario(parameters.getIdConcesionario());
            nuevaReserva.setIdModelo(parameters.getIdModelo());
            nuevaReserva.setCodigoBloqueo(codigoUnico);
            nuevaReserva.setEstado("BLOQUEADO");
            nuevaReserva.setFechaReserva(LocalDateTime.now());

            // ¡Lo guardamos físicamente en H2!
            reservaRepo.save(nuevaReserva);

            // Respondemos al servicio SOAP
            response.setExito(true);
            response.setMensaje("Vehículo bloqueado y guardado en base de datos");
            response.setBloqueado(true);
            response.setCodigoBloqueo(codigoUnico);
        } else {
            response.setExito(false);
            response.setMensaje("Error: Validación fallida");
            response.setBloqueado(false);
        }

        return response;
    }
}