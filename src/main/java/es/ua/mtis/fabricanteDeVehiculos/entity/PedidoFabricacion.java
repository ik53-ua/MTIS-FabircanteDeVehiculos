package es.ua.mtis.fabricanteDeVehiculos.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Representa un pedido de fabricación registrado en el sistema central.
 * Tabla: pedidos_fabricacion
 * Usada por: ServicioRegistroPedidos (RegistrarPedidoFabricacion)
 *            ServicioNotificacionesPedido (EnviarNotificacionPedido) via idPedido
 */
@Entity
@Table(name = "pedidos_fabricacion")
public class PedidoFabricacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idPedido;

    private Integer idConcesionario;
    private Integer idOrden;   // FK lógica a ordenes_produccion
    private String estado;     // "REGISTRADO", "EN_PRODUCCION", "ENTREGADO"
    private LocalDateTime fechaRegistro;

    // Getters y Setters
    public Integer getIdPedido() { return idPedido; }
    public void setIdPedido(Integer idPedido) { this.idPedido = idPedido; }
    public Integer getIdConcesionario() { return idConcesionario; }
    public void setIdConcesionario(Integer idConcesionario) { this.idConcesionario = idConcesionario; }
    public Integer getIdOrden() { return idOrden; }
    public void setIdOrden(Integer idOrden) { this.idOrden = idOrden; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(LocalDateTime fechaRegistro) { this.fechaRegistro = fechaRegistro; }
}
