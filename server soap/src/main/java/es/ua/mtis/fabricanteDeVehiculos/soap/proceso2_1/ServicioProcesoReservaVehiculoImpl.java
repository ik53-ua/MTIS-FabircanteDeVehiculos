package es.ua.mtis.fabricanteDeVehiculos.soap.proceso2_1;

import es.ua.mtis.fabricanteDeVehiculos.entity.ReservaVehiculo;
import es.ua.mtis.fabricanteDeVehiculos.repository.ReservaVehiculoRepository;
import org.mtis.servicioprocesoreservavehiculo.ServicioProcesoReservaVehiculoPortType;
import org.mtis.servicioprocesoreservavehiculo.ReservarVehiculoRequest;
import org.mtis.servicioprocesoreservavehiculo.ReservarVehiculoResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.jws.WebService;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@WebService(
    serviceName = "ServicioProcesoReservaVehiculo",
    portName = "ServicioProcesoReservaVehiculoPort",
    targetNamespace = "http://mtis.org/ServicioProcesoReservaVehiculo/",
    endpointInterface = "org.mtis.servicioprocesoreservavehiculo.ServicioProcesoReservaVehiculoPortType"
)
public class ServicioProcesoReservaVehiculoImpl implements ServicioProcesoReservaVehiculoPortType {

    @Autowired
    private ReservaVehiculoRepository reservaRepo;

    @Override
    public ReservarVehiculoResponse reservarVehiculo(ReservarVehiculoRequest parameters) {
        ReservarVehiculoResponse response = new ReservarVehiculoResponse();

        System.out.println("Procesando reserva completa en BD para modelo: " + parameters.getIdModelo()
                + ", concesionario: " + parameters.getIdConcesionario());

        if (parameters.getWSKey() != null && !parameters.getWSKey().isEmpty()) {

            // Generamos un código de bloqueo único para esta reserva
            String codigoUnico = "BLK-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

            // Creamos la reserva final en la base de datos con todos los campos obligatorios
            ReservaVehiculo reservaConfirmada = new ReservaVehiculo();
            reservaConfirmada.setIdConcesionario(parameters.getIdConcesionario());
            reservaConfirmada.setIdModelo(parameters.getIdModelo());
            reservaConfirmada.setCodigoBloqueo(codigoUnico);   // ← campo obligatorio, antes faltaba
            reservaConfirmada.setEstado("CONFIRMADO");
            reservaConfirmada.setFechaReserva(LocalDateTime.now());

            // Guardamos y obtenemos el ID real generado por H2
            ReservaVehiculo guardada = reservaRepo.save(reservaConfirmada);

            response.setExito(true);
            response.setMensaje("Reserva procesada y guardada con éxito. Código: " + codigoUnico);
            response.setIdReserva(guardada.getIdReserva());
        } else {
            response.setExito(false);
            response.setMensaje("Error de seguridad: WSKey inválida o ausente.");
        }

        return response;
    }
}