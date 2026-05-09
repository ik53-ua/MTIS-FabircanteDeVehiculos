package org.mtis.servicioprocesoreservavehiculo;

import org.mtis.servicioreservavehiculo.ServicioReservaVehiculoSkeleton;
import org.mtis.servicioregistroreservas.ServicioRegistroReservasSkeleton;
import org.mtis.servicionotificaciones.ServicioNotificacionesSkeleton;

public class ServicioProcesoReservaVehiculoSkeleton {
    public org.mtis.servicioprocesoreservavehiculo.ReservarVehiculoResponse reservarVehiculo(
            org.mtis.servicioprocesoreservavehiculo.ReservarVehiculoRequest req) {
        
        org.mtis.servicioprocesoreservavehiculo.ReservarVehiculoResponse resFinal = 
            new org.mtis.servicioprocesoreservavehiculo.ReservarVehiculoResponse();
        String key = req.getWSKey();
        int mod = req.getIdModelo();
        int conc = req.getIdConcesionario();

        try {
            // 1. BLOQUEAR VEHÍCULO 
            ServicioReservaVehiculoSkeleton sRes = new ServicioReservaVehiculoSkeleton();
            org.mtis.servicioreservavehiculo.BloquearVehiculoRequest reqB = 
                new org.mtis.servicioreservavehiculo.BloquearVehiculoRequest();
            reqB.setWSKey(key); 
            reqB.setIdModelo(mod); 
            reqB.setIdConcesionario(conc);
            
            org.mtis.servicioreservavehiculo.BloquearVehiculoResponse resB = sRes.bloquearVehiculo(reqB);

            if (resB.getExito() && resB.getBloqueado()) {
                ServicioRegistroReservasSkeleton sReg = new ServicioRegistroReservasSkeleton();
                org.mtis.servicioregistroreservas.RegistrarReservaRequest reqR = new org.mtis.servicioregistroreservas.RegistrarReservaRequest();
                reqR.setWSKey(key); reqR.setIdModelo(mod); reqR.setIdConcesionario(conc); reqR.setCodigoBloqueo(resB.getCodigoBloqueo());
                org.mtis.servicioregistroreservas.RegistrarReservaResponse resR = sReg.registrarReserva(reqR);
                
                String mensajeExito = "Reserva completada con éxito. Código: " + resB.getCodigoBloqueo();
                
                // DELEGAMOS NOTIFICACIÓN DE ÉXITO
                notificarEvento(key, conc, resR.getIdReserva(), mensajeExito);

                resFinal.setExito(true);
                resFinal.setMensaje(mensajeExito);

            } else {
                String mensajeFallo = "Error: No se encontró stock o no se pudo realizar la reserva: " + resB.getMensaje();
                resFinal.setExito(false);
                resFinal.setMensaje(mensajeFallo);
                
                // DELEGAMOS NOTIFICACIÓN DE FALLO
                notificarEvento(key, conc, 0, mensajeFallo);
            }
        } catch (Exception e) {
            String mensajeError = "Error en la orquestación: " + e.getMessage();
            resFinal.setExito(false);
            resFinal.setMensaje(mensajeError);
            
            // DELEGAMOS NOTIFICACIÓN DE EXCEPCIÓN
            notificarEvento(key, conc, 0, mensajeError);
        }
        return resFinal;
    }

    // MÉTODO HELPER PARA LLAMAR AL SERVICIO DE NOTIFICACIONES DE RESERVA
    private void notificarEvento(String wsKey, int idConcesionario, int idReserva, String mensaje) {
        try {
            ServicioNotificacionesSkeleton sNot = new ServicioNotificacionesSkeleton();
            org.mtis.servicionotificaciones.EnviarConfirmacionRequest reqN = new org.mtis.servicionotificaciones.EnviarConfirmacionRequest();
            reqN.setWSKey(wsKey); 
            reqN.setIdConcesionario(idConcesionario); 
            reqN.setIdReserva(idReserva);
            reqN.setMensajeAEnviar(mensaje); // El servicio notificaciones decidirá por el texto si es éxito o error
            sNot.enviarConfirmacion(reqN);
        } catch (Exception ex) {
            System.err.println("Fallo al delegar notificación de reserva: " + ex.getMessage());
        }
    }
}