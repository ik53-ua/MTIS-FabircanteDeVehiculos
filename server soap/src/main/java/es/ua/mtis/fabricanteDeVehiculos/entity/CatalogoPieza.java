package es.ua.mtis.fabricanteDeVehiculos.entity;

import jakarta.persistence.*;

/**
 * Catálogo de piezas/repuestos disponibles.
 * Tabla: catalogo_piezas
 * Usada por: ServicioCatalogoPiezas (ConsultarPieza)
 *            ServicioProcesoSolicitudRepuestos (para buscar piezas de la lista solicitada)
 */
@Entity
@Table(name = "catalogo_piezas")
public class CatalogoPieza {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idPieza;

    private String nombrePieza;
    private Double precio;
    private Integer tiempoSuministroDias;

    // Getters y Setters
    public Integer getIdPieza() { return idPieza; }
    public void setIdPieza(Integer idPieza) { this.idPieza = idPieza; }
    public String getNombrePieza() { return nombrePieza; }
    public void setNombrePieza(String nombrePieza) { this.nombrePieza = nombrePieza; }
    public Double getPrecio() { return precio; }
    public void setPrecio(Double precio) { this.precio = precio; }
    public Integer getTiempoSuministroDias() { return tiempoSuministroDias; }
    public void setTiempoSuministroDias(Integer tiempoSuministroDias) { this.tiempoSuministroDias = tiempoSuministroDias; }
}
