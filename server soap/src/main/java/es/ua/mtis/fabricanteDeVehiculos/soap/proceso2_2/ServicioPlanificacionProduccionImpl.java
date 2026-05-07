package es.ua.mtis.fabricanteDeVehiculos.soap.proceso2_2;

import es.ua.mtis.fabricanteDeVehiculos.entity.ModeloVehiculo;
import es.ua.mtis.fabricanteDeVehiculos.repository.ModeloVehiculoRepository;
import org.mtis.servicioplanificacionproduccion.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.jws.WebService;
import java.time.LocalDate;
import java.util.Optional;

@Service
@WebService(
    serviceName = "ServicioPlanificacionProduccion",
    portName = "ServicioPlanificacionProduccionPort",
    targetNamespace = "http://mtis.org/ServicioPlanificacionProduccion/",
    endpointInterface = "org.mtis.servicioplanificacionproduccion.ServicioPlanificacionProduccionPortType"
)
public class ServicioPlanificacionProduccionImpl implements ServicioPlanificacionProduccionPortType {

    @Autowired
    private ModeloVehiculoRepository modeloRepo;

    @Override
    public ConsultarCapacidadProduccionResponse consultarCapacidadProduccion(ConsultarCapacidadProduccionRequest parameters) {
        ConsultarCapacidadProduccionResponse res = new ConsultarCapacidadProduccionResponse();
        System.out.println("Consultando capacidad de producción para modelo: " + parameters.getIdModelo()
                + ", configuracion: " + parameters.getConfiguracion());

        // Obtenemos los dias de fabricacion reales desde la tabla modelos
        Optional<ModeloVehiculo> modeloOpt = modeloRepo.findById(parameters.getIdModelo());

        if (modeloOpt.isPresent() && modeloOpt.get().getDiasFabricacion() != null) {
            int dias = modeloOpt.get().getDiasFabricacion();
            LocalDate fechaEntrega = LocalDate.now().plusDays(dias);

            res.setExito(true);
            res.setMensaje("Capacidad de producción consultada correctamente.");
            res.setDiasEstimados(dias);
            res.setFechaEntrega(fechaEntrega.toString()); // Formato ISO: yyyy-MM-dd
        } else if (modeloOpt.isPresent()) {
            res.setExito(false);
            res.setMensaje("El modelo existe pero no tiene días de fabricación configurados.");
        } else {
            res.setExito(false);
            res.setMensaje("Error: El modelo con ID " + parameters.getIdModelo() + " no existe en la base de datos.");
        }

        return res;
    }
}