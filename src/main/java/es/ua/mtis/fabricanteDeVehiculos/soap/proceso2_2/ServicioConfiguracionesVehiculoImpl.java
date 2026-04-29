package es.ua.mtis.fabricanteDeVehiculos.soap.proceso2_2;

import org.mtis.servicioconfiguracionesvehiculo.*;
import org.springframework.stereotype.Service;
import jakarta.jws.WebService;

@Service
@WebService(
    serviceName = "ServicioConfiguracionesVehiculo",
    portName = "ServicioConfiguracionesVehiculoPort",
    targetNamespace = "http://mtis.org/ServicioConfiguracionesVehiculo/",
    endpointInterface = "org.mtis.servicioconfiguracionesvehiculo.ServicioConfiguracionesVehiculoPortType"
)
public class ServicioConfiguracionesVehiculoImpl implements ServicioConfiguracionesVehiculoPortType {
    @Override
    public ValidarConfiguracionResponse validarConfiguracion(ValidarConfiguracionRequest parameters) {
        ValidarConfiguracionResponse res = new ValidarConfiguracionResponse();
        System.out.println("Validando configuración para modelo: " + parameters.getIdModelo());
        
        res.setExito(true);
        res.setMensaje("Configuración validada.");
        res.setEsViable(true);
        return res;
    }
}