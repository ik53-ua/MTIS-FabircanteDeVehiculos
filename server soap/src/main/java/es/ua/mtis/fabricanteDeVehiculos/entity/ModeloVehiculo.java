package es.ua.mtis.fabricanteDeVehiculos.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "modelos")
public class ModeloVehiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nombre;
    private Double costeProduccion;
    private Integer diasFabricacion; // Días estimados de producción, usado por ServicioPlanificacionProduccion

    // Getters y Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public Double getCosteProduccion() { return costeProduccion; }
    public void setCosteProduccion(Double costeProduccion) { this.costeProduccion = costeProduccion; }
    public Integer getDiasFabricacion() { return diasFabricacion; }
    public void setDiasFabricacion(Integer diasFabricacion) { this.diasFabricacion = diasFabricacion; }
}