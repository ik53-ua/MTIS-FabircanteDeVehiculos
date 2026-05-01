package es.ua.mtis.fabricanteDeVehiculos.soap.proceso2_1;

import es.ua.mtis.fabricanteDeVehiculos.entity.StockConcesionario;
import es.ua.mtis.fabricanteDeVehiculos.repository.StockConcesionarioRepository;
import org.mtis.serviciostockred.ServicioStockRedPortType;
import org.mtis.serviciostockred.ConsultarStockRedRequest;
import org.mtis.serviciostockred.ConsultarStockRedResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.jws.WebService;
import java.util.List;

@Service
@WebService(
    serviceName = "ServicioStockRed",
    portName = "ServicioStockRedPort",
    targetNamespace = "http://mtis.org/ServicioStockRed/",
    endpointInterface = "org.mtis.serviciostockred.ServicioStockRedPortType"
)
public class ServicioStockRedImpl implements ServicioStockRedPortType {

    @Autowired
    private StockConcesionarioRepository stockRepo;

    @Override
    public ConsultarStockRedResponse consultarStockRed(ConsultarStockRedRequest parameters) {
        ConsultarStockRedResponse response = new ConsultarStockRedResponse();

        System.out.println("Consultando stock en la RED (todos los concesionarios) para modelo: "
                + parameters.getIdModelo() + ", configuracion: " + parameters.getConfiguracion());

        // Suma el stock de todos los concesionarios para ese modelo
        List<StockConcesionario> stocks = stockRepo.findAllByIdModelo(parameters.getIdModelo());
        int totalRed = stocks.stream()
                .mapToInt(StockConcesionario::getCantidadDisponible)
                .sum();

        response.setExito(true);
        if (totalRed > 0) {
            response.setMensaje("Stock de la red consultado correctamente.");
            response.setDisponible(true);
            response.setCantidad(totalRed);
        } else {
            response.setMensaje("Sin stock disponible en la red para este modelo.");
            response.setDisponible(false);
            response.setCantidad(0);
        }

        return response;
    }
}