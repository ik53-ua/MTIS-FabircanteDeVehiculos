package es.ua.mtis.fabricanteDeVehiculos.soap.proceso2_1;

import org.mtis.serviciostockred.ServicioStockRedPortType;
import org.mtis.serviciostockred.ConsultarStockRedRequest;
import org.mtis.serviciostockred.ConsultarStockRedResponse;
import org.springframework.stereotype.Service;
import jakarta.jws.WebService;

@Service
@WebService(
    serviceName = "ServicioStockRed",
    portName = "ServicioStockRedPort",
    targetNamespace = "http://mtis.org/ServicioStockRed/",
    endpointInterface = "org.mtis.serviciostockred.ServicioStockRedPortType"
)
public class ServicioStockRedImpl implements ServicioStockRedPortType {

    @Override
    public ConsultarStockRedResponse consultarStockRed(ConsultarStockRedRequest parameters) {
        ConsultarStockRedResponse response = new ConsultarStockRedResponse();
        
        System.out.println("Consultando stock en toda la red para el modelo: " + parameters.getIdModelo());
        
        response.setExito(true);
        response.setMensaje("Stock de la red consultado correctamente");
        response.setDisponible(true);
        response.setCantidad(12); 
        
        return response;
    }
}