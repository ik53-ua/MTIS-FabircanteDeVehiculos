package org.openapitools.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * VentaResponse
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-05-07T14:57:49.443606900+02:00[Europe/Madrid]", comments = "Generator version: 7.22.0")
public class VentaResponse {

  private @Nullable String ventaId;

  private @Nullable String estado;

  public VentaResponse ventaId(@Nullable String ventaId) {
    this.ventaId = ventaId;
    return this;
  }

  /**
   * Get ventaId
   * @return ventaId
   */
  
  @Schema(name = "ventaId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("ventaId")
  public @Nullable String getVentaId() {
    return ventaId;
  }

  @JsonProperty("ventaId")
  public void setVentaId(@Nullable String ventaId) {
    this.ventaId = ventaId;
  }

  public VentaResponse estado(@Nullable String estado) {
    this.estado = estado;
    return this;
  }

  /**
   * Get estado
   * @return estado
   */
  
  @Schema(name = "estado", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("estado")
  public @Nullable String getEstado() {
    return estado;
  }

  @JsonProperty("estado")
  public void setEstado(@Nullable String estado) {
    this.estado = estado;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    VentaResponse ventaResponse = (VentaResponse) o;
    return Objects.equals(this.ventaId, ventaResponse.ventaId) &&
        Objects.equals(this.estado, ventaResponse.estado);
  }

  @Override
  public int hashCode() {
    return Objects.hash(ventaId, estado);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class VentaResponse {\n");
    sb.append("    ventaId: ").append(toIndentedString(ventaId)).append("\n");
    sb.append("    estado: ").append(toIndentedString(estado)).append("\n");
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

