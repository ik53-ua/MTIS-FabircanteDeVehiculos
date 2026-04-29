package es.ua.mtis.fabricanteDeVehiculos.soap.proceso2_1;

import org.mtis.serviciostockconcesionario.ServicioStockConcesionarioPortType;
import org.mtis.serviciostockconcesionario.ConsultarStockConcesionarioRequest;
import org.mtis.serviciostockconcesionario.ConsultarStockConcesionarioResponse;
import org.springframework.stereotype.Service;
import jakarta.jws.WebService;

@Service
@WebService(
    serviceName = "ServicioStockConcesionario",
    portName = "ServicioStockConcesionarioPort",
    targetNamespace = "http://mtis.org/ServicioStockConcesionario/",
    endpointInterface = "org.mtis.serviciostockconcesionario.ServicioStockConcesionarioPortType"
)
public class ServicioStockConcesionarioImpl implements ServicioStockConcesionarioPortType {

    @Override
    public ConsultarStockConcesionarioResponse consultarStockConcesionario(ConsultarStockConcesionarioRequest parameters) {
        ConsultarStockConcesionarioResponse response = new ConsultarStockConcesionarioResponse();
        
        System.out.println("Consultando stock en concesionario " + parameters.getIdConcesionario() + " para modelo " + parameters.getIdModelo());
        
        // Lógica simulada: Si el ID del modelo es par, hay stock. Si es impar, no.
        if (parameters.getIdModelo() % 2 == 0) { 
            response.setExito(true);
            response.setMensaje("Stock consultado con éxito");
            response.setDisponible(true);
            response.setCantidad(3);
        } else {
            response.setExito(true);
            response.setMensaje("Sin stock en este concesionario");
            response.setDisponible(false);
            response.setCantidad(0);
        }
        
        return response;
    }
}