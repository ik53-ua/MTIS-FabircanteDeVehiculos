package es.ua.mtis.fabricanteDeVehiculos.entity;

import jakarta.persistence.*;

/**
 * Representa una configuración válida para un modelo de vehículo.
 * Tabla: configuraciones_modelo
 * Usada por: ServicioConfiguracionesVehiculo (ValidarConfiguracion)
 */
@Entity
@Table(name = "configuraciones_modelo")
public class ConfiguracionModelo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer idModelo;
    private String configuracion; // e.g. "GASOLINA_MANUAL_ROJO"
    private Boolean esViable;

    // Getters y Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Integer getIdModelo() { return idModelo; }
    public void setIdModelo(Integer idModelo) { this.idModelo = idModelo; }
    public String getConfiguracion() { return configuracion; }
    public void setConfiguracion(String configuracion) { this.configuracion = configuracion; }
    public Boolean getEsViable() { return esViable; }
    public void setEsViable(Boolean esViable) { this.esViable = esViable; }
}
