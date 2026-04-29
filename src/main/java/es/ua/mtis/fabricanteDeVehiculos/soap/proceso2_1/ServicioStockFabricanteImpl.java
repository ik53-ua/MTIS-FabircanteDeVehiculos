package es.ua.mtis.fabricanteDeVehiculos.soap.proceso2_1;

import org.mtis.serviciostockfabricante.ServicioStockFabricantePortType;
import org.mtis.serviciostockfabricante.ConsultarStockFabricanteRequest;
import org.mtis.serviciostockfabricante.ConsultarStockFabricanteResponse;
import org.springframework.stereotype.Service;
import jakarta.jws.WebService;

@Service
@WebService(
    serviceName = "ServicioStockFabricante",
    portName = "ServicioStockFabricantePort",
    targetNamespace = "http://mtis.org/ServicioStockFabricante/",
    endpointInterface = "org.mtis.serviciostockfabricante.ServicioStockFabricantePortType"
)
public class ServicioStockFabricanteImpl implements ServicioStockFabricantePortType {

    @Override
    public ConsultarStockFabricanteResponse consultarStockFabricante(ConsultarStockFabricanteRequest parameters) {
        ConsultarStockFabricanteResponse response = new ConsultarStockFabricanteResponse();
        
        System.out.println("Consultando stock en la fábrica para el modelo: " + parameters.getIdModelo());
        
        response.setExito(true);
        response.setMensaje("Stock de fábrica consultado correctamente");
        response.setDisponible(true);
        response.setCantidad(50); // En la fábrica suele haber más stock
        
        return response;
    }
}