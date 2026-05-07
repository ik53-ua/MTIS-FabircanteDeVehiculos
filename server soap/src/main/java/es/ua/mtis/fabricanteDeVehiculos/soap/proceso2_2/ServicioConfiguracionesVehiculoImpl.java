package es.ua.mtis.fabricanteDeVehiculos.soap.proceso2_2;

import es.ua.mtis.fabricanteDeVehiculos.entity.ConfiguracionModelo;
import es.ua.mtis.fabricanteDeVehiculos.repository.ConfiguracionModeloRepository;
import org.mtis.servicioconfiguracionesvehiculo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.jws.WebService;
import java.util.Optional;

@Service
@WebService(
    serviceName = "ServicioConfiguracionesVehiculo",
    portName = "ServicioConfiguracionesVehiculoPort",
    targetNamespace = "http://mtis.org/ServicioConfiguracionesVehiculo/",
    endpointInterface = "org.mtis.servicioconfiguracionesvehiculo.ServicioConfiguracionesVehiculoPortType"
)
public class ServicioConfiguracionesVehiculoImpl implements ServicioConfiguracionesVehiculoPortType {

    @Autowired
    private ConfiguracionModeloRepository configuracionRepo;

    @Override
    public ValidarConfiguracionResponse validarConfiguracion(ValidarConfiguracionRequest parameters) {
        ValidarConfiguracionResponse res = new ValidarConfiguracionResponse();
        System.out.println("Validando configuración '" + parameters.getConfiguracion()
                + "' para modelo: " + parameters.getIdModelo());

        // Buscamos en la tabla configuraciones_modelo si existe y es viable
        Optional<ConfiguracionModelo> configOpt = configuracionRepo
                .findByIdModeloAndConfiguracion(parameters.getIdModelo(), parameters.getConfiguracion());

        res.setExito(true);
        if (configOpt.isPresent()) {
            boolean viable = Boolean.TRUE.equals(configOpt.get().getEsViable());
            res.setEsViable(viable);
            res.setMensaje(viable
                    ? "Configuración válida y viable para el modelo."
                    : "Configuración encontrada pero marcada como no viable.");
        } else {
            res.setEsViable(false);
            res.setMensaje("Configuración no registrada para este modelo.");
        }

        return res;
    }
}