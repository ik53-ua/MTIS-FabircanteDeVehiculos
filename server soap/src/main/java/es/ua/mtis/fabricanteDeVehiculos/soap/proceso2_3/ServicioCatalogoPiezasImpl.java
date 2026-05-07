package es.ua.mtis.fabricanteDeVehiculos.soap.proceso2_3;

import es.ua.mtis.fabricanteDeVehiculos.entity.CatalogoPieza;
import es.ua.mtis.fabricanteDeVehiculos.repository.CatalogoPiezaRepository;
import org.mtis.serviciocatalogopiezas.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.jws.WebService;
import java.util.Optional;

@Service
@WebService(
    serviceName = "ServicioCatalogoPiezas",
    portName = "ServicioCatalogoPiezasPort",
    targetNamespace = "http://mtis.org/ServicioCatalogoPiezas/",
    endpointInterface = "org.mtis.serviciocatalogopiezas.ServicioCatalogoPiezasPortType"
)
public class ServicioCatalogoPiezasImpl implements ServicioCatalogoPiezasPortType {

    @Autowired
    private CatalogoPiezaRepository catalogoRepo;

    @Override
    public ConsultarPiezaResponse consultarPieza(ConsultarPiezaRequest parameters) {
        ConsultarPiezaResponse res = new ConsultarPiezaResponse();
        System.out.println("Consultando catálogo en BD para la pieza: " + parameters.getIdPieza());

        Optional<CatalogoPieza> piezaOpt = catalogoRepo.findById(parameters.getIdPieza());

        if (piezaOpt.isPresent()) {
            CatalogoPieza pieza = piezaOpt.get();
            res.setExito(true);
            res.setMensaje("Pieza encontrada en el catálogo.");
            res.setNombrePieza(pieza.getNombrePieza());
            res.setPrecio(pieza.getPrecio());
            res.setTiempoSuministroDias(pieza.getTiempoSuministroDias());
        } else {
            res.setExito(false);
            res.setMensaje("Pieza con ID " + parameters.getIdPieza() + " no encontrada en el catálogo.");
        }

        return res;
    }
}