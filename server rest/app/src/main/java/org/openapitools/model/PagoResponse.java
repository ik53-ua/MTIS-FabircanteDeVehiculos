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
 * PagoResponse
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-05-07T14:57:49.443606900+02:00[Europe/Madrid]", comments = "Generator version: 7.22.0")
public class PagoResponse {

  private @Nullable String pagoId;

  private @Nullable String estado;

  public PagoResponse pagoId(@Nullable String pagoId) {
    this.pagoId = pagoId;
    return this;
  }

  /**
   * Get pagoId
   * @return pagoId
   */
  
  @Schema(name = "pagoId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("pagoId")
  public @Nullable String getPagoId() {
    return pagoId;
  }

  @JsonProperty("pagoId")
  public void setPagoId(@Nullable String pagoId) {
    this.pagoId = pagoId;
  }

  public PagoResponse estado(@Nullable String estado) {
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
    PagoResponse pagoResponse = (PagoResponse) o;
    return Objects.equals(this.pagoId, pagoResponse.pagoId) &&
        Objects.equals(this.estado, pagoResponse.estado);
  }

  @Override
  public int hashCode() {
    return Objects.hash(pagoId, estado);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PagoResponse {\n");
    sb.append("    pagoId: ").append(toIndentedString(pagoId)).append("\n");
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

