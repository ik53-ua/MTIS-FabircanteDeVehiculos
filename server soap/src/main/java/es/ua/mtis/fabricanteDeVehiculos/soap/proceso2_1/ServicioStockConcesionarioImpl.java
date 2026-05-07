package es.ua.mtis.fabricanteDeVehiculos.soap.proceso2_1;

import es.ua.mtis.fabricanteDeVehiculos.entity.StockConcesionario;
import es.ua.mtis.fabricanteDeVehiculos.repository.StockConcesionarioRepository;
import org.mtis.serviciostockconcesionario.ServicioStockConcesionarioPortType;
import org.mtis.serviciostockconcesionario.ConsultarStockConcesionarioRequest;
import org.mtis.serviciostockconcesionario.ConsultarStockConcesionarioResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.jws.WebService;
import java.util.Optional;

@Service
@WebService(
    serviceName = "ServicioStockConcesionario",
    portName = "ServicioStockConcesionarioPort",
    targetNamespace = "http://mtis.org/ServicioStockConcesionario/",
    endpointInterface = "org.mtis.serviciostockconcesionario.ServicioStockConcesionarioPortType"
)
public class ServicioStockConcesionarioImpl implements ServicioStockConcesionarioPortType {

    // ¡Inyectamos la conexión a la base de datos!
    @Autowired
    private StockConcesionarioRepository stockRepo;

    @Override
    public ConsultarStockConcesionarioResponse consultarStockConcesionario(ConsultarStockConcesionarioRequest parameters) {
        ConsultarStockConcesionarioResponse response = new ConsultarStockConcesionarioResponse();
        
        System.out.println("Consultando stock REAL en BD para concesionario " + parameters.getIdConcesionario() + " y modelo " + parameters.getIdModelo());

        // Buscamos en la tabla stock_concesionarios
        Optional<StockConcesionario> stockOpt = stockRepo.findByIdModeloAndIdConcesionario(parameters.getIdModelo(), parameters.getIdConcesionario());

        // Si lo encuentra y hay más de 0...
        if (stockOpt.isPresent() && stockOpt.get().getCantidadDisponible() > 0) {
            response.setExito(true);
            response.setMensaje("Stock encontrado en base de datos.");
            response.setDisponible(true);
            response.setCantidad(stockOpt.get().getCantidadDisponible());
        } else {
            response.setExito(true);
            response.setMensaje("Sin stock en este concesionario.");
            response.setDisponible(false);
            response.setCantidad(0);
        }

        return response;
    }
}