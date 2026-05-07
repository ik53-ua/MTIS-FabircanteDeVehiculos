package es.ua.mtis.fabricanteDeVehiculos.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Pedido de repuestos generado por un concesionario.
 * Tabla: pedidos_repuestos
 * Usada por: ServicioPedidosRepuestos (GenerarPedidoRepuestos)
 *            ServicioLogisticaEnvios (GestionarEnvioRepuestos) via idPedido
 */
@Entity
@Table(name = "pedidos_repuestos")
public class PedidoRepuesto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idPedido;

    private Integer idConcesionario;
    private Integer idPieza;
    private Integer cantidad;
    private String estado;  // "PENDIENTE", "EN_ENVIO", "ENTREGADO"
    private LocalDateTime fechaCreacion;

    // Getters y Setters
    public Integer getIdPedido() { return idPedido; }
    public void setIdPedido(Integer idPedido) { this.idPedido = idPedido; }
    public Integer getIdConcesionario() { return idConcesionario; }
    public void setIdConcesionario(Integer idConcesionario) { this.idConcesionario = idConcesionario; }
    public Integer getIdPieza() { return idPieza; }
    public void setIdPieza(Integer idPieza) { this.idPieza = idPieza; }
    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
}
