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
 * FacturaRequest
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-05-07T14:57:49.443606900+02:00[Europe/Madrid]", comments = "Generator version: 7.22.0")
public class FacturaRequest {

  private @Nullable String ventaId;

  private @Nullable BigDecimal importe;

  public FacturaRequest ventaId(@Nullable String ventaId) {
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

  public FacturaRequest importe(@Nullable BigDecimal importe) {
    this.importe = importe;
    return this;
  }

  /**
   * Get importe
   * @return importe
   */
  @Valid 
  @Schema(name = "importe", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("importe")
  public @Nullable BigDecimal getImporte() {
    return importe;
  }

  @JsonProperty("importe")
  public void setImporte(@Nullable BigDecimal importe) {
    this.importe = importe;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FacturaRequest facturaRequest = (FacturaRequest) o;
    return Objects.equals(this.ventaId, facturaRequest.ventaId) &&
        Objects.equals(this.importe, facturaRequest.importe);
  }

  @Override
  public int hashCode() {
    return Objects.hash(ventaId, importe);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FacturaRequest {\n");
    sb.append("    ventaId: ").append(toIndentedString(ventaId)).append("\n");
    sb.append("    importe: ").append(toIndentedString(importe)).append("\n");
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

