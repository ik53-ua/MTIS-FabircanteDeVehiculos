package es.ua.mtis.fabricanteDeVehiculos.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reservas")
public class ReservaVehiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idReserva;
    
    private Integer idConcesionario;
    private Integer idModelo;
    private String codigoBloqueo;
    private String estado; // "BLOQUEADO", "CONFIRMADO", "CANCELADO"
    private LocalDateTime fechaReserva;

    // Getters y Setters
    public Integer getIdReserva() { return idReserva; }
    public void setIdReserva(Integer idReserva) { this.idReserva = idReserva; }
    public Integer getIdConcesionario() { return idConcesionario; }
    public void setIdConcesionario(Integer idConcesionario) { this.idConcesionario = idConcesionario; }
    public Integer getIdModelo() { return idModelo; }
    public void setIdModelo(Integer idModelo) { this.idModelo = idModelo; }
    public String getCodigoBloqueo() { return codigoBloqueo; }
    public void setCodigoBloqueo(String codigoBloqueo) { this.codigoBloqueo = codigoBloqueo; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public LocalDateTime getFechaReserva() { return fechaReserva; }
    public void setFechaReserva(LocalDateTime fechaReserva) { this.fechaReserva = fechaReserva; }
}