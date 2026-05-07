package es.ua.mtis.fabricanteDeVehiculos.entity;

import jakarta.persistence.*;

/**
 * Stock de repuestos de cada concesionario.
 * Tabla: stock_repuestos
 * Usada por: ServicioStockRepuestos (ConsultarStockRepuestos)
 */
@Entity
@Table(name = "stock_repuestos")
public class StockRepuesto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer idConcesionario;
    private Integer idPieza;
    private Integer cantidadDisponible;

    // Getters y Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Integer getIdConcesionario() { return idConcesionario; }
    public void setIdConcesionario(Integer idConcesionario) { this.idConcesionario = idConcesionario; }
    public Integer getIdPieza() { return idPieza; }
    public void setIdPieza(Integer idPieza) { this.idPieza = idPieza; }
    public Integer getCantidadDisponible() { return cantidadDisponible; }
    public void setCantidadDisponible(Integer cantidadDisponible) { this.cantidadDisponible = cantidadDisponible; }
}
