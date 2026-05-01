package es.ua.mtis.fabricanteDeVehiculos.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Representa una orden de producción generada para un concesionario.
 * Tabla: ordenes_produccion
 * Usada por: ServicioGeneracionOrdenProduccion (GenerarOrdenProduccion)
 *            ServicioRegistroPedidos (RegistrarPedidoFabricacion) via idOrden
 */
@Entity
@Table(name = "ordenes_produccion")
public class OrdenProduccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idOrden;

    private Integer idConcesionario;
    private Integer idModelo;
    private String configuracion;
    private String estado; // "PENDIENTE", "REGISTRADA", "CANCELADA"
    private LocalDateTime fechaCreacion;

    // Getters y Setters
    public Integer getIdOrden() { return idOrden; }
    public void setIdOrden(Integer idOrden) { this.idOrden = idOrden; }
    public Integer getIdConcesionario() { return idConcesionario; }
    public void setIdConcesionario(Integer idConcesionario) { this.idConcesionario = idConcesionario; }
    public Integer getIdModelo() { return idModelo; }
    public void setIdModelo(Integer idModelo) { this.idModelo = idModelo; }
    public String getConfiguracion() { return configuracion; }
    public void setConfiguracion(String configuracion) { this.configuracion = configuracion; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
}
