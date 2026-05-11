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
 * PedidoCompraResponse
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-05-07T14:57:03.923228100+02:00[Europe/Madrid]", comments = "Generator version: 7.22.0")
public class PedidoCompraResponse {

  private @Nullable String pedidoId;

  private @Nullable String estado;

  public PedidoCompraResponse pedidoId(@Nullable String pedidoId) {
    this.pedidoId = pedidoId;
    return this;
  }

  /**
   * Get pedidoId
   * @return pedidoId
   */
  
  @Schema(name = "pedidoId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("pedidoId")
  public @Nullable String getPedidoId() {
    return pedidoId;
  }

  @JsonProperty("pedidoId")
  public void setPedidoId(@Nullable String pedidoId) {
    this.pedidoId = pedidoId;
  }

  public PedidoCompraResponse estado(@Nullable String estado) {
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
    PedidoCompraResponse pedidoCompraResponse = (PedidoCompraResponse) o;
    return Objects.equals(this.pedidoId, pedidoCompraResponse.pedidoId) &&
        Objects.equals(this.estado, pedidoCompraResponse.estado);
  }

  @Override
  public int hashCode() {
    return Objects.hash(pedidoId, estado);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PedidoCompraResponse {\n");
    sb.append("    pedidoId: ").append(toIndentedString(pedidoId)).append("\n");
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

