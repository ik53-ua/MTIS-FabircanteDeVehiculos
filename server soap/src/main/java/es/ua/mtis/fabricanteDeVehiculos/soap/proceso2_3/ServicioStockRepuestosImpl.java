package es.ua.mtis.fabricanteDeVehiculos.soap.proceso2_3;

import es.ua.mtis.fabricanteDeVehiculos.entity.StockRepuesto;
import es.ua.mtis.fabricanteDeVehiculos.repository.StockRepuestoRepository;
import org.mtis.serviciostockrepuestos.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.jws.WebService;
import java.util.Optional;

@Service
@WebService(
    serviceName = "ServicioStockRepuestos",
    portName = "ServicioStockRepuestosPort",
    targetNamespace = "http://mtis.org/ServicioStockRepuestos/",
    endpointInterface = "org.mtis.serviciostockrepuestos.ServicioStockRepuestosPortType"
)
public class ServicioStockRepuestosImpl implements ServicioStockRepuestosPortType {

    @Autowired
    private StockRepuestoRepository stockRepuestoRepo;

    @Override
    public ConsultarStockRepuestosResponse consultarStockRepuestos(ConsultarStockRepuestosRequest parameters) {
        ConsultarStockRepuestosResponse res = new ConsultarStockRepuestosResponse();
        System.out.println("Consultando stock de repuestos en BD para concesionario: "
                + parameters.getIdConcesionario() + ", pieza: " + parameters.getIdPieza());

        Optional<StockRepuesto> stockOpt = stockRepuestoRepo
                .findByIdConcesionarioAndIdPieza(parameters.getIdConcesionario(), parameters.getIdPieza());

        res.setExito(true);
        if (stockOpt.isPresent()) {
            res.setMensaje("Stock consultado correctamente.");
            res.setCantidadDisponible(stockOpt.get().getCantidadDisponible());
        } else {
            res.setMensaje("Sin stock registrado para esta pieza en el concesionario indicado.");
            res.setCantidadDisponible(0);
        }

        return res;
    }
}