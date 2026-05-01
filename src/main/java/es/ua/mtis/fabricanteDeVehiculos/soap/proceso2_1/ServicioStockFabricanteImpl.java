package es.ua.mtis.fabricanteDeVehiculos.soap.proceso2_1;

import es.ua.mtis.fabricanteDeVehiculos.entity.StockFabricante;
import es.ua.mtis.fabricanteDeVehiculos.repository.StockFabricanteRepository;
import org.mtis.serviciostockfabricante.ServicioStockFabricantePortType;
import org.mtis.serviciostockfabricante.ConsultarStockFabricanteRequest;
import org.mtis.serviciostockfabricante.ConsultarStockFabricanteResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.jws.WebService;
import java.util.Optional;

@Service
@WebService(
    serviceName = "ServicioStockFabricante",
    portName = "ServicioStockFabricantePort",
    targetNamespace = "http://mtis.org/ServicioStockFabricante/",
    endpointInterface = "org.mtis.serviciostockfabricante.ServicioStockFabricantePortType"
)
public class ServicioStockFabricanteImpl implements ServicioStockFabricantePortType {

    @Autowired
    private StockFabricanteRepository stockFabricanteRepo;

    @Override
    public ConsultarStockFabricanteResponse consultarStockFabricante(ConsultarStockFabricanteRequest parameters) {
        ConsultarStockFabricanteResponse response = new ConsultarStockFabricanteResponse();

        System.out.println("Consultando stock en la FÁBRICA (BD) para modelo: "
                + parameters.getIdModelo() + ", configuracion: " + parameters.getConfiguracion());

        // Consultamos la tabla stock_fabricante por idModelo
        Optional<StockFabricante> stockOpt = stockFabricanteRepo.findByIdModelo(parameters.getIdModelo());

        response.setExito(true);
        if (stockOpt.isPresent() && stockOpt.get().getCantidadDisponible() > 0) {
            response.setMensaje("Stock de fábrica consultado correctamente.");
            response.setDisponible(true);
            response.setCantidad(stockOpt.get().getCantidadDisponible());
        } else {
            response.setMensaje("Sin stock disponible en fábrica para este modelo.");
            response.setDisponible(false);
            response.setCantidad(0);
        }

        return response;
    }
}