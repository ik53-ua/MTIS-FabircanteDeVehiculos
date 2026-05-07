package es.ua.mtis.fabricanteDeVehiculos.soap.proceso2_2;

import es.ua.mtis.fabricanteDeVehiculos.entity.OrdenProduccion;
import es.ua.mtis.fabricanteDeVehiculos.repository.OrdenProduccionRepository;
import org.mtis.serviciogeneracionordenproduccion.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.jws.WebService;
import java.time.LocalDateTime;

@Service
@WebService(
    serviceName = "ServicioGeneracionOrdenProduccion",
    portName = "ServicioGeneracionOrdenProduccionPort",
    targetNamespace = "http://mtis.org/ServicioGeneracionOrdenProduccion/",
    endpointInterface = "org.mtis.serviciogeneracionordenproduccion.ServicioGeneracionOrdenProduccionPortType"
)
public class ServicioGeneracionOrdenProduccionImpl implements ServicioGeneracionOrdenProduccionPortType {

    @Autowired
    private OrdenProduccionRepository ordenRepo;

    @Override
    public GenerarOrdenProduccionResponse generarOrdenProduccion(GenerarOrdenProduccionRequest parameters) {
        GenerarOrdenProduccionResponse res = new GenerarOrdenProduccionResponse();
        System.out.println("Generando orden de producción para concesionario: " + parameters.getIdConcesionario()
                + ", modelo: " + parameters.getIdModelo()
                + ", configuracion: " + parameters.getConfiguracion());

        if (parameters.getWSKey() != null && !parameters.getWSKey().isEmpty()) {
            // Creamos y persistimos la orden en la BD
            OrdenProduccion orden = new OrdenProduccion();
            orden.setIdConcesionario(parameters.getIdConcesionario());
            orden.setIdModelo(parameters.getIdModelo());
            orden.setConfiguracion(parameters.getConfiguracion());
            orden.setEstado("PENDIENTE");
            orden.setFechaCreacion(LocalDateTime.now());

            OrdenProduccion guardada = ordenRepo.save(orden);

            res.setExito(true);
            res.setMensaje("Orden de producción generada y registrada en BD.");
            res.setIdOrden(guardada.getIdOrden()); // ID real generado por H2
        } else {
            res.setExito(false);
            res.setMensaje("Error: WSKey inválida o ausente.");
        }

        return res;
    }
}