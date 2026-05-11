package org.openapitools.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * PlanificacionUpdateRequest
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-05-07T14:57:03.923228100+02:00[Europe/Madrid]", comments = "Generator version: 7.22.0")
public class PlanificacionUpdateRequest {

  private @Nullable String estadoProduccion;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  private @Nullable LocalDate nuevaFechaReanudacion;

  public PlanificacionUpdateRequest estadoProduccion(@Nullable String estadoProduccion) {
    this.estadoProduccion = estadoProduccion;
    return this;
  }

  /**
   * Get estadoProduccion
   * @return estadoProduccion
   */
  
  @Schema(name = "estadoProduccion", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("estadoProduccion")
  public @Nullable String getEstadoProduccion() {
    return estadoProduccion;
  }

  @JsonProperty("estadoProduccion")
  public void setEstadoProduccion(@Nullable String estadoProduccion) {
    this.estadoProduccion = estadoProduccion;
  }

  public PlanificacionUpdateRequest nuevaFechaReanudacion(@Nullable LocalDate nuevaFechaReanudacion) {
    this.nuevaFechaReanudacion = nuevaFechaReanudacion;
    return this;
  }

  /**
   * Get nuevaFechaReanudacion
   * @return nuevaFechaReanudacion
   */
  @Valid 
  @Schema(name = "nuevaFechaReanudacion", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("nuevaFechaReanudacion")
  public @Nullable LocalDate getNuevaFechaReanudacion() {
    return nuevaFechaReanudacion;
  }

  @JsonProperty("nuevaFechaReanudacion")
  public void setNuevaFechaReanudacion(@Nullable LocalDate nuevaFechaReanudacion) {
    this.nuevaFechaReanudacion = nuevaFechaReanudacion;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PlanificacionUpdateRequest planificacionUpdateRequest = (PlanificacionUpdateRequest) o;
    return Objects.equals(this.estadoProduccion, planificacionUpdateRequest.estadoProduccion) &&
        Objects.equals(this.nuevaFechaReanudacion, planificacionUpdateRequest.nuevaFechaReanudacion);
  }

  @Override
  public int hashCode() {
    return Objects.hash(estadoProduccion, nuevaFechaReanudacion);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PlanificacionUpdateRequest {\n");
    sb.append("    estadoProduccion: ").append(toIndentedString(estadoProduccion)).append("\n");
    sb.append("    nuevaFechaReanudacion: ").append(toIndentedString(nuevaFechaReanudacion)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(@Nullable Object o) {
    return o == null ? "null" : o.toString().replace("\n", "\n    ");
  }
}

