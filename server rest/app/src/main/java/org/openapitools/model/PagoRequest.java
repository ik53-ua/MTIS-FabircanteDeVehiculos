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
 * PagoRequest
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-05-07T14:57:49.443606900+02:00[Europe/Madrid]", comments = "Generator version: 7.22.0")
public class PagoRequest {

  private @Nullable String facturaId;

  private @Nullable BigDecimal cantidad;

  public PagoRequest facturaId(@Nullable String facturaId) {
    this.facturaId = facturaId;
    return this;
  }

  /**
   * Get facturaId
   * @return facturaId
   */
  
  @Schema(name = "facturaId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("facturaId")
  public @Nullable String getFacturaId() {
    return facturaId;
  }

  @JsonProperty("facturaId")
  public void setFacturaId(@Nullable String facturaId) {
    this.facturaId = facturaId;
  }

  public PagoRequest cantidad(@Nullable BigDecimal cantidad) {
    this.cantidad = cantidad;
    return this;
  }

  /**
   * Get cantidad
   * @return cantidad
   */
  @Valid 
  @Schema(name = "cantidad", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("cantidad")
  public @Nullable BigDecimal getCantidad() {
    return cantidad;
  }

  @JsonProperty("cantidad")
  public void setCantidad(@Nullable BigDecimal cantidad) {
    this.cantidad = cantidad;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PagoRequest pagoRequest = (PagoRequest) o;
    return Objects.equals(this.facturaId, pagoRequest.facturaId) &&
        Objects.equals(this.cantidad, pagoRequest.cantidad);
  }

  @Override
  public int hashCode() {
    return Objects.hash(facturaId, cantidad);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PagoRequest {\n");
    sb.append("    facturaId: ").append(toIndentedString(facturaId)).append("\n");
    sb.append("    cantidad: ").append(toIndentedString(cantidad)).append("\n");
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

