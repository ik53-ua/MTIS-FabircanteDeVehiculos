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
 * PedidoCompraRequest
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-05-07T14:57:03.923228100+02:00[Europe/Madrid]", comments = "Generator version: 7.22.0")
public class PedidoCompraRequest {

  private @Nullable String componenteId;

  private @Nullable Integer cantidadRequerida;

  public PedidoCompraRequest componenteId(@Nullable String componenteId) {
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

  public PedidoCompraRequest cantidadRequerida(@Nullable Integer cantidadRequerida) {
    this.cantidadRequerida = cantidadRequerida;
    return this;
  }

  /**
   * Get cantidadRequerida
   * @return cantidadRequerida
   */
  
  @Schema(name = "cantidadRequerida", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("cantidadRequerida")
  public @Nullable Integer getCantidadRequerida() {
    return cantidadRequerida;
  }

  @JsonProperty("cantidadRequerida")
  public void setCantidadRequerida(@Nullable Integer cantidadRequerida) {
    this.cantidadRequerida = cantidadRequerida;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PedidoCompraRequest pedidoCompraRequest = (PedidoCompraRequest) o;
    return Objects.equals(this.componenteId, pedidoCompraRequest.componenteId) &&
        Objects.equals(this.cantidadRequerida, pedidoCompraRequest.cantidadRequerida);
  }

  @Override
  public int hashCode() {
    return Objects.hash(componenteId, cantidadRequerida);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PedidoCompraRequest {\n");
    sb.append("    componenteId: ").append(toIndentedString(componenteId)).append("\n");
    sb.append("    cantidadRequerida: ").append(toIndentedString(cantidadRequerida)).append("\n");
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

