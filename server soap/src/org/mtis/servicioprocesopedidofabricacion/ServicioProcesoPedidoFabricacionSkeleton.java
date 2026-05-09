package org.mtis.servicioprocesopedidofabricacion;

import org.mtis.servicioconfiguracionesvehiculo.ServicioConfiguracionesVehiculoSkeleton;
import org.mtis.servicioplanificacionproduccion.ServicioPlanificacionProduccionSkeleton;
import org.mtis.serviciocalculocostes.ServicioCalculoCostesSkeleton;
import org.mtis.serviciogeneracionordenproduccion.ServicioGeneracionOrdenProduccionSkeleton;
import org.mtis.servicioregistropedidos.ServicioRegistroPedidosSkeleton;
import org.mtis.servicionotificacionespedido.ServicioNotificacionesPedidoSkeleton;

public class ServicioProcesoPedidoFabricacionSkeleton {

    public org.mtis.servicioprocesopedidofabricacion.ProcesarPedidoFabricacionResponse procesarPedidoFabricacion(
        org.mtis.servicioprocesopedidofabricacion.ProcesarPedidoFabricacionRequest req) {

        org.mtis.servicioprocesopedidofabricacion.ProcesarPedidoFabricacionResponse respuestaFinal = 
            new org.mtis.servicioprocesopedidofabricacion.ProcesarPedidoFabricacionResponse();
        
        String wsKey = req.getWSKey();
        int idModelo = req.getIdModelo();
        int idConcesionario = req.getIdConcesionario();
        String configNombre = req.getConfiguracion();

        try {
            // 1. VALIDAR CONFIGURACIÓN 
            ServicioConfiguracionesVehiculoSkeleton srvConfig = new ServicioConfiguracionesVehiculoSkeleton();
            org.mtis.servicioconfiguracionesvehiculo.ValidarConfiguracionRequest reqConfig = 
                new org.mtis.servicioconfiguracionesvehiculo.ValidarConfiguracionRequest();
            
            reqConfig.setWSKey(wsKey);
            reqConfig.setIdModelo(idModelo);
            reqConfig.setConfiguracion(configNombre); 
            
            org.mtis.servicioconfiguracionesvehiculo.ValidarConfiguracionResponse resConfig = 
                srvConfig.validarConfiguracion(reqConfig);
                
            if (!resConfig.getEsViable()) {
                String errorMsg = "Error: La configuración cancelado. No es viable según el fabricante.";
                respuestaFinal.setExito(false);
                respuestaFinal.setMensaje(errorMsg);
                // DELEGAMOS LA NOTIFICACIÓN DE ERROR
                notificarError(wsKey, idConcesionario, 0, errorMsg);
                return respuestaFinal;
            }

            // 2. CONSULTAR PLANIFICACIÓN/TIEMPOS
            ServicioPlanificacionProduccionSkeleton srvPlani = new ServicioPlanificacionProduccionSkeleton();
            org.mtis.servicioplanificacionproduccion.ConsultarCapacidadProduccionRequest reqPlani = 
                new org.mtis.servicioplanificacionproduccion.ConsultarCapacidadProduccionRequest();
                
            reqPlani.setWSKey(wsKey);
            reqPlani.setIdModelo(idModelo);
            
            org.mtis.servicioplanificacionproduccion.ConsultarCapacidadProduccionResponse resPlani = 
                srvPlani.consultarCapacidadProduccion(reqPlani);
            int diasFabricacion = resPlani.getDiasEstimados();

            // 3. CALCULAR COSTE TOTAL 
            ServicioCalculoCostesSkeleton srvCostes = new ServicioCalculoCostesSkeleton();
            org.mtis.serviciocalculocostes.CalcularCosteProduccionRequest reqCostes = 
                new org.mtis.serviciocalculocostes.CalcularCosteProduccionRequest();
                
            reqCostes.setWSKey(wsKey);
            reqCostes.setIdModelo(idModelo);
            reqCostes.setConfiguracion(configNombre); 
            
            org.mtis.serviciocalculocostes.CalcularCosteProduccionResponse resCostes = 
                srvCostes.calcularCosteProduccion(reqCostes);
            double costeTotal = resCostes.getCosteTotal();

            // 4. GENERAR ORDEN DE PRODUCCIÓN 
            ServicioGeneracionOrdenProduccionSkeleton srvOrden = new ServicioGeneracionOrdenProduccionSkeleton();
            org.mtis.serviciogeneracionordenproduccion.GenerarOrdenProduccionRequest reqOrden = 
                new org.mtis.serviciogeneracionordenproduccion.GenerarOrdenProduccionRequest();
                
            reqOrden.setWSKey(wsKey);
            reqOrden.setIdModelo(idModelo);
            reqOrden.setIdConcesionario(idConcesionario);
            reqOrden.setConfiguracion(configNombre); 
            
            org.mtis.serviciogeneracionordenproduccion.GenerarOrdenProduccionResponse resOrden = 
                srvOrden.generarOrdenProduccion(reqOrden);
            int idOrden = resOrden.getIdOrden();

            // 5. REGISTRAR PEDIDO FORMALMENTE
            ServicioRegistroPedidosSkeleton srvRegistro = new ServicioRegistroPedidosSkeleton();
            org.mtis.servicioregistropedidos.RegistrarPedidoFabricacionRequest reqReg = 
                new org.mtis.servicioregistropedidos.RegistrarPedidoFabricacionRequest();
                
            reqReg.setWSKey(wsKey);
            reqReg.setIdOrden(idOrden);
            reqReg.setIdConcesionario(idConcesionario);
            
            srvRegistro.registrarPedidoFabricacion(reqReg);

            // 6. NOTIFICAR AL CONCESIONARIO (ÉXITO)
            ServicioNotificacionesPedidoSkeleton srvNotif = new ServicioNotificacionesPedidoSkeleton();
            org.mtis.servicionotificacionespedido.EnviarNotificacionPedidoRequest reqNotif = 
                new org.mtis.servicionotificacionespedido.EnviarNotificacionPedidoRequest();
                
            reqNotif.setWSKey(wsKey);
            reqNotif.setIdConcesionario(idConcesionario);
            reqNotif.setIdPedido(idOrden); 
            reqNotif.setMensajeAEnviar("Pedido de fabricación #" + idOrden + " aceptado. Entrega en " + diasFabricacion + " días.");
            
            srvNotif.enviarNotificacionPedido(reqNotif);

            respuestaFinal.setExito(true);
            respuestaFinal.setMensaje("Proceso completado con éxito.");
            respuestaFinal.setCoste(costeTotal);
            respuestaFinal.setTiempoFabricacionDias(diasFabricacion);
            respuestaFinal.setFechaEntregaEstimada(java.time.LocalDate.now().plusDays(diasFabricacion).toString());

        } catch (Exception e) {
            String errorMsg = "Error cancelado en orquestación: " + e.getMessage();
            respuestaFinal.setExito(false);
            respuestaFinal.setMensaje(errorMsg);
            // DELEGAMOS LA NOTIFICACIÓN DE EXCEPCIÓN
            notificarError(wsKey, idConcesionario, 0, errorMsg);
        }

        return respuestaFinal;
    }

    // MÉTODO HELPER PARA DELEGAR ERRORES AL SERVICIO CORRESPONDIENTE
    private void notificarError(String wsKey, int idConcesionario, int idPedido, String mensaje) {
        try {
            ServicioNotificacionesPedidoSkeleton srvNotif = new ServicioNotificacionesPedidoSkeleton();
            org.mtis.servicionotificacionespedido.EnviarNotificacionPedidoRequest reqNotif = 
                new org.mtis.servicionotificacionespedido.EnviarNotificacionPedidoRequest();
            reqNotif.setWSKey(wsKey);
            reqNotif.setIdConcesionario(idConcesionario);
            reqNotif.setIdPedido(idPedido);
            reqNotif.setMensajeAEnviar(mensaje);
            srvNotif.enviarNotificacionPedido(reqNotif);
        } catch (Exception ex) {
            System.err.println("No se pudo notificar el error de proceso: " + ex.getMessage());
        }
    }
}