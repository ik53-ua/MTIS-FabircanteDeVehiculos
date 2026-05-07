package es.ua.mtis.fabricanteDeVehiculos.soap.proceso2_3;

import es.ua.mtis.fabricanteDeVehiculos.entity.EnvioRepuesto;
import es.ua.mtis.fabricanteDeVehiculos.entity.PedidoRepuesto;
import es.ua.mtis.fabricanteDeVehiculos.repository.EnvioRepuestoRepository;
import es.ua.mtis.fabricanteDeVehiculos.repository.PedidoRepuestoRepository;
import org.mtis.serviciologisticaenvios.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.jws.WebService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@WebService(
    serviceName = "ServicioLogisticaEnvios",
    portName = "ServicioLogisticaEnviosPort",
    targetNamespace = "http://mtis.org/ServicioLogisticaEnvios/",
    endpointInterface = "org.mtis.serviciologisticaenvios.ServicioLogisticaEnviosPortType"
)
public class ServicioLogisticaEnviosImpl implements ServicioLogisticaEnviosPortType {

    @Autowired
    private PedidoRepuestoRepository pedidoRepo;

    @Autowired
    private EnvioRepuestoRepository envioRepo;

    @Override
    public GestionarEnvioRepuestosResponse gestionarEnvioRepuestos(GestionarEnvioRepuestosRequest parameters) {
        GestionarEnvioRepuestosResponse res = new GestionarEnvioRepuestosResponse();
        System.out.println("Gestionando envío logístico en BD para pedido: " + parameters.getIdPedido()
                + ", concesionario: " + parameters.getIdConcesionario());

        if (parameters.getWSKey() == null || parameters.getWSKey().isEmpty()) {
            res.setExito(false);
            res.setMensaje("Error: WSKey inválida o ausente.");
            return res;
        }

        // Verificar que el pedido existe en BD
        Optional<PedidoRepuesto> pedidoOpt = pedidoRepo.findById(parameters.getIdPedido());
        if (pedidoOpt.isEmpty()) {
            res.setExito(false);
            res.setMensaje("Error: No existe un pedido de repuestos con ID " + parameters.getIdPedido() + ".");
            return res;
        }

        // Actualizar el estado del pedido
        PedidoRepuesto pedido = pedidoOpt.get();
        pedido.setEstado("EN_ENVIO");
        pedidoRepo.save(pedido);

        // Calcular fecha estimada de entrega según el tiempo de suministro de la pieza (7 días por defecto)
        String fechaEstimada = LocalDate.now().plusDays(7).toString();

        // Crear el registro de envío en BD
        EnvioRepuesto envio = new EnvioRepuesto();
        envio.setIdPedido(parameters.getIdPedido());
        envio.setIdConcesionario(parameters.getIdConcesionario());
        envio.setFechaEstimada(fechaEstimada);
        envio.setEstado("PROGRAMADO");
        envio.setFechaCreacion(LocalDateTime.now());

        EnvioRepuesto guardado = envioRepo.save(envio);

        res.setExito(true);
        res.setMensaje("Envío logístico programado y registrado en BD.");
        res.setIdEnvio(guardado.getIdEnvio()); // ID real de BD
        res.setFechaEstimada(fechaEstimada);

        return res;
    }
}