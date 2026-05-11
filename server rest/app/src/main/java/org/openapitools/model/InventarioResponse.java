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
 * InventarioResponse
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-05-07T14:57:03.923228100+02:00[Europe/Madrid]", comments = "Generator version: 7.22.0")
public class InventarioResponse {

  private @Nullable String componenteId;

  private @Nullable Integer cantidadActual;

  public InventarioResponse componenteId(@Nullable String componenteId) {
    this.componenteId = componenteId;
    return this;
  }

  /**
   * Get componenteId
   * @return componenteId
   */
  
  @Schema(name = "componenteId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("componenteId")
  public @Nullable String getComponenteId() {
    return componenteId;
  }

  @JsonProperty("componenteId")
  public void setComponenteId(@Nullable String componenteId) {
    this.componenteId = componenteId;
  }

  public InventarioResponse cantidadActual(@Nullable Integer cantidadActual) {
    this.cantidadActual = cantidadActual;
    return this;
  }

  /**
   * Get cantidadActual
   * @return cantidadActual
   */
  
  @Schema(name = "cantidadActual", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("cantidadActual")
  public @Nullable Integer getCantidadActual() {
    return cantidadActual;
  }

  @JsonProperty("cantidadActual")
  public void setCantidadActual(@Nullable Integer cantidadActual) {
    this.cantidadActual = cantidadActual;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    InventarioResponse inventarioResponse = (InventarioResponse) o;
    return Objects.equals(this.componenteId, inventarioResponse.componenteId) &&
        Objects.equals(this.cantidadActual, inventarioResponse.cantidadActual);
  }

  @Override
  public int hashCode() {
    return Objects.hash(componenteId, cantidadActual);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class InventarioResponse {\n");
    sb.append("    componenteId: ").append(toIndentedString(componenteId)).append("\n");
    sb.append("    cantidadActual: ").append(toIndentedString(cantidadActual)).append("\n");
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

