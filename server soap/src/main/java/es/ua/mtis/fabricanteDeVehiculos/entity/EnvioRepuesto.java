package es.ua.mtis.fabricanteDeVehiculos.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Envío logístico de repuestos hacia un concesionario.
 * Tabla: envios_repuestos
 * Usada por: ServicioLogisticaEnvios (GestionarEnvioRepuestos)
 *            ServicioNotificaciones p23 (EnviarNotificacionEntrega) via idEnvio
 */
@Entity
@Table(name = "envios_repuestos")
public class EnvioRepuesto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idEnvio;

    private Integer idPedido;       // FK lógica a pedidos_repuestos
    private Integer idConcesionario;
    private String fechaEstimada;   // Formato ISO: yyyy-MM-dd
    private String estado;          // "PROGRAMADO", "EN_TRANSITO", "ENTREGADO"
    private LocalDateTime fechaCreacion;

    // Getters y Setters
    public Integer getIdEnvio() { return idEnvio; }
    public void setIdEnvio(Integer idEnvio) { this.idEnvio = idEnvio; }
    public Integer getIdPedido() { return idPedido; }
    public void setIdPedido(Integer idPedido) { this.idPedido = idPedido; }
    public Integer getIdConcesionario() { return idConcesionario; }
    public void setIdConcesionario(Integer idConcesionario) { this.idConcesionario = idConcesionario; }
    public String getFechaEstimada() { return fechaEstimada; }
    public void setFechaEstimada(String fechaEstimada) { this.fechaEstimada = fechaEstimada; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
}
