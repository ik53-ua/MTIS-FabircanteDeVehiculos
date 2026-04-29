package es.ua.mtis.fabricanteDeVehiculos.config;

import es.ua.mtis.fabricanteDeVehiculos.soap.proceso2_1.*;
import es.ua.mtis.fabricanteDeVehiculos.soap.proceso2_2.ServicioCalculoCostesImpl;
import es.ua.mtis.fabricanteDeVehiculos.soap.proceso2_2.ServicioConfiguracionesVehiculoImpl;
import es.ua.mtis.fabricanteDeVehiculos.soap.proceso2_2.ServicioGeneracionOrdenProduccionImpl;
import es.ua.mtis.fabricanteDeVehiculos.soap.proceso2_2.ServicioNotificacionesPedidoImpl;
import es.ua.mtis.fabricanteDeVehiculos.soap.proceso2_2.ServicioPlanificacionProduccionImpl;
import es.ua.mtis.fabricanteDeVehiculos.soap.proceso2_2.ServicioProcesoPedidoFabricacionImpl;
import es.ua.mtis.fabricanteDeVehiculos.soap.proceso2_2.ServicioRegistroPedidosImpl;

import org.apache.cxf.Bus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import jakarta.xml.ws.Endpoint;

@Configuration
public class CxfConfig {

    @Autowired
    private Bus bus;
    /*----------------------------------------- Proceso 2.1 ------------------------------------------ */
    @Autowired
    private ServicioNotificacionesImpl notificacionesImpl;
    
    @Autowired
    private ServicioProcesoReservaVehiculoImpl procesoReservaImpl;
    
    @Autowired
    private ServicioRegistroReservasImpl registroReservasImpl;

    @Autowired
    private ServicioReservaVehiculoImpl reservaVehiculoImpl;
    
    @Autowired
    private ServicioStockConcesionarioImpl stockConcesionarioImpl;
    
    @Autowired
    private ServicioStockFabricanteImpl stockFabricanteImpl;
    
    @Autowired
    private ServicioStockRedImpl stockRedImpl;

    /*----------------------------------------- Proceso 2.2 ------------------------------------------ */

    @Autowired
    private ServicioCalculoCostesImpl calculoCostesImpl;

    @Autowired
    private ServicioPlanificacionProduccionImpl planificacionImpl;

    @Autowired
    private ServicioProcesoPedidoFabricacionImpl procesoPedidoImpl;

    @Autowired
    private ServicioConfiguracionesVehiculoImpl configVehiculoImpl;

    @Autowired
    private ServicioGeneracionOrdenProduccionImpl ordenProduccionImpl;

    @Autowired
    private ServicioNotificacionesPedidoImpl notificacionesPedidoImpl;

    @Autowired
    private ServicioRegistroPedidosImpl registroPedidosImpl;


    /*----------------------------------------- Proceso 2.1 ------------------------------------------ */
    @Bean
    public Endpoint endpointNotificaciones() {
        EndpointImpl endpoint = new EndpointImpl(bus, notificacionesImpl);
        endpoint.publish("/ServicioNotificaciones"); 
        return endpoint;
    }

    @Bean
    public Endpoint endpointProcesoReserva() {
        EndpointImpl endpoint = new EndpointImpl(bus, procesoReservaImpl);
        endpoint.publish("/ServicioProcesoReservaVehiculo"); 
        return endpoint;
    }
    
    @Bean
    public Endpoint endpointRegistroReservas() {
        EndpointImpl endpoint = new EndpointImpl(bus, registroReservasImpl);
        endpoint.publish("/ServicioRegistroReservas"); 
        return endpoint;
    }

    @Bean
    public Endpoint endpointReservaVehiculo() {
        EndpointImpl endpoint = new EndpointImpl(bus, reservaVehiculoImpl);
        endpoint.publish("/ServicioReservaVehiculo"); 
        return endpoint;
    }
    
    @Bean
    public Endpoint endpointStockConcesionario() {
        EndpointImpl endpoint = new EndpointImpl(bus, stockConcesionarioImpl);
        endpoint.publish("/ServicioStockConcesionario"); 
        return endpoint;
    }
    
    @Bean
    public Endpoint endpointStockFabricante() {
        EndpointImpl endpoint = new EndpointImpl(bus, stockFabricanteImpl);
        endpoint.publish("/ServicioStockFabricante"); 
        return endpoint;
    }
    
    @Bean
    public Endpoint endpointStockRed() {
        EndpointImpl endpoint = new EndpointImpl(bus, stockRedImpl);
        endpoint.publish("/ServicioStockRed"); 
        return endpoint;
    }

    /*----------------------------------------- Proceso 2.2 ------------------------------------------ */

    @Bean
    public Endpoint endpointCalculoCostes() {
        EndpointImpl endpoint = new EndpointImpl(bus, calculoCostesImpl);
        endpoint.publish("/ServicioCalculoCostes");
        return endpoint;
    }

    @Bean
    public Endpoint endpointPlanificacion() {
        EndpointImpl endpoint = new EndpointImpl(bus, planificacionImpl);
        endpoint.publish("/ServicioPlanificacionProduccion");
        return endpoint;
    }

    @Bean
    public Endpoint endpointProcesoPedido() {
        EndpointImpl endpoint = new EndpointImpl(bus, procesoPedidoImpl);
        endpoint.publish("/ServicioProcesoPedidoFabricacion");
        return endpoint;
    }

    @Bean
    public Endpoint endpointConfigVehiculo() {
        EndpointImpl endpoint = new EndpointImpl(bus, configVehiculoImpl);
        endpoint.publish("/ServicioConfiguracionesVehiculo");
        return endpoint;
    }

    @Bean
    public Endpoint endpointOrdenProduccion() {
        EndpointImpl endpoint = new EndpointImpl(bus, ordenProduccionImpl);
        endpoint.publish("/ServicioGeneracionOrdenProduccion");
        return endpoint;
    }

    @Bean
    public Endpoint endpointNotificacionesPedido() {
        EndpointImpl endpoint = new EndpointImpl(bus, notificacionesPedidoImpl);
        endpoint.publish("/ServicioNotificacionesPedido");
        return endpoint;
    }

    @Bean
    public Endpoint endpointRegistroPedidos() {
        EndpointImpl endpoint = new EndpointImpl(bus, registroPedidosImpl);
        endpoint.publish("/ServicioRegistroPedidos");
        return endpoint;
    }
}