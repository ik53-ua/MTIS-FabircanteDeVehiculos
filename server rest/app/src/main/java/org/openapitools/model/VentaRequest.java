package org.openapitools.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.math.BigDecimal;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * VentaRequest
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-05-07T14:57:49.443606900+02:00[Europe/Madrid]", comments = "Generator version: 7.22.0")
public class VentaRequest {

  private @Nullable String vehiculoId;

  private @Nullable String clienteId;

  private @Nullable BigDecimal precioTotal;

  public VentaRequest vehiculoId(@Nullable String vehiculoId) {
    this.vehiculoId = vehiculoId;
    return this;
  }

  /**
   * Get vehiculoId
   * @return vehiculoId
   */
  
  @Schema(name = "vehiculoId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("vehiculoId")
  public @Nullable String getVehiculoId() {
    return vehiculoId;
  }

  @JsonProperty("vehiculoId")
  public void setVehiculoId(@Nullable String vehiculoId) {
    this.vehiculoId = vehiculoId;
  }

  public VentaRequest clienteId(@Nullable String clienteId) {
    this.clienteId = clienteId;
    return this;
  }

  /**
   * Get clienteId
   * @return clienteId
   */
  
  @Schema(name = "clienteId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("clienteId")
  public @Nullable String getClienteId() {
    return clienteId;
  }

  @JsonProperty("clienteId")
  public void setClienteId(@Nullable String clienteId) {
    this.clienteId = clienteId;
  }

  public VentaRequest precioTotal(@Nullable BigDecimal precioTotal) {
    this.precioTotal = precioTotal;
    return this;
  }

  /**
   * Get precioTotal
   * @return precioTotal
   */
  @Valid 
  @Schema(name = "precioTotal", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("precioTotal")
  public @Nullable BigDecimal getPrecioTotal() {
    return precioTotal;
  }

  @JsonProperty("precioTotal")
  public void setPrecioTotal(@Nullable BigDecimal precioTotal) {
    this.precioTotal = precioTotal;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    VentaRequest ventaRequest = (VentaRequest) o;
    return Objects.equals(this.vehiculoId, ventaRequest.vehiculoId) &&
        Objects.equals(this.clienteId, ventaRequest.clienteId) &&
        Objects.equals(this.precioTotal, ventaRequest.precioTotal);
  }

  @Override
  public int hashCode() {
    return Objects.hash(vehiculoId, clienteId, precioTotal);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class VentaRequest {\n");
    sb.append("    vehiculoId: ").append(toIndentedString(vehiculoId)).append("\n");
    sb.append("    clienteId: ").append(toIndentedString(clienteId)).append("\n");
    sb.append("    precioTotal: ").append(toIndentedString(precioTotal)).append("\n");
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

