package es.ua.mtis.fabricanteDeVehiculos.soap.proceso2_2;

import es.ua.mtis.fabricanteDeVehiculos.entity.ModeloVehiculo;
import es.ua.mtis.fabricanteDeVehiculos.repository.ModeloVehiculoRepository;
import org.mtis.serviciocalculocostes.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.jws.WebService;
import java.util.Optional;

@Service
@WebService(
    serviceName = "ServicioCalculoCostes",
    portName = "ServicioCalculoCostesPort",
    targetNamespace = "http://mtis.org/ServicioCalculoCostes/",
    endpointInterface = "org.mtis.serviciocalculocostes.ServicioCalculoCostesPortType"
)
public class ServicioCalculoCostesImpl implements ServicioCalculoCostesPortType {

    @Autowired
    private ModeloVehiculoRepository modeloRepo;

    @Override
    public CalcularCosteProduccionResponse calcularCosteProduccion(CalcularCosteProduccionRequest parameters) {
        CalcularCosteProduccionResponse res = new CalcularCosteProduccionResponse();
        System.out.println("Calculando coste de producción para modelo: " + parameters.getIdModelo()
                + ", configuracion: " + parameters.getConfiguracion());

        // Obtenemos el coste real desde la tabla modelos
        Optional<ModeloVehiculo> modeloOpt = modeloRepo.findById(parameters.getIdModelo());

        if (modeloOpt.isPresent()) {
            res.setExito(true);
            res.setMensaje("Coste de producción calculado correctamente.");
            res.setCosteTotal(modeloOpt.get().getCosteProduccion());
        } else {
            res.setExito(false);
            res.setMensaje("Error: El modelo con ID " + parameters.getIdModelo() + " no existe en la base de datos.");
        }

        return res;
    }
}