package es.ua.mtis.fabricanteDeVehiculos.soap.proceso2_3;

import es.ua.mtis.fabricanteDeVehiculos.entity.EnvioRepuesto;
import es.ua.mtis.fabricanteDeVehiculos.repository.EnvioRepuestoRepository;
import org.mtis.servicionotificaciones.p23.ServicioNotificacionesPortType;
import org.mtis.servicionotificaciones.p23.EnviarNotificacionEntregaRequest;
import org.mtis.servicionotificaciones.p23.EnviarNotificacionEntregaResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.jws.WebService;
import java.util.Optional;

@Service("servicioNotificacionesEntrega")
@WebService(
    serviceName = "ServicioNotificacionesEntrega",
    portName = "ServicioNotificacionesEntregaPort",
    targetNamespace = "http://mtis.org/ServicioNotificaciones/",
    endpointInterface = "org.mtis.servicionotificaciones.p23.ServicioNotificacionesPortType"
)
public class ServicioNotificacionesEntregaImpl implements ServicioNotificacionesPortType {

    @Autowired
    private EnvioRepuestoRepository envioRepo;

    @Override
    public EnviarNotificacionEntregaResponse enviarNotificacionEntrega(EnviarNotificacionEntregaRequest parameters) {
        EnviarNotificacionEntregaResponse res = new EnviarNotificacionEntregaResponse();
        System.out.println("Enviando notificación de entrega al concesionario: "
                + parameters.getIdConcesionario()
                + ", envío: " + parameters.getIdEnvio()
                + ", fecha recepción: " + parameters.getFechaRecepcionEstimada());

        if (parameters.getWSKey() == null || parameters.getWSKey().isEmpty()) {
            res.setExito(false);
            res.setMensaje("Error: WSKey inválida o ausente.");
            return res;
        }

        // idEnvio es opcional en el WSDL (minOccurs=0)
        Integer idEnvio = parameters.getIdEnvio();

        if (idEnvio != null) {
            // Verificamos que el envío existe en BD antes de notificar
            Optional<EnvioRepuesto> envioOpt = envioRepo.findById(idEnvio);

            if (envioOpt.isPresent()) {
                EnvioRepuesto envio = envioOpt.get();
                res.setExito(true);
                res.setMensaje("Notificación enviada al concesionario " + parameters.getIdConcesionario()
                        + ". Fecha de recepción estimada: " + parameters.getFechaRecepcionEstimada()
                        + ". Estado del envío: " + envio.getEstado());
            } else {
                res.setExito(false);
                res.setMensaje("Error: El envío ID " + idEnvio + " no existe en la base de datos.");
            }
        } else {
            // Notificación genérica sin envío específico asociado
            res.setExito(true);
            res.setMensaje("Notificación genérica enviada al concesionario " + parameters.getIdConcesionario()
                    + ". Fecha estimada: " + parameters.getFechaRecepcionEstimada());
        }

        return res;
    }
}