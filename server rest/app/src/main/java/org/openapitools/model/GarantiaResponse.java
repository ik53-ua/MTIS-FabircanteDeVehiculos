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
 * GarantiaResponse
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-05-07T14:57:49.443606900+02:00[Europe/Madrid]", comments = "Generator version: 7.22.0")
public class GarantiaResponse {

  private @Nullable String garantiaId;

  private @Nullable String estado;

  public GarantiaResponse garantiaId(@Nullable String garantiaId) {
    this.garantiaId = garantiaId;
    return this;
  }

  /**
   * Get garantiaId
   * @return garantiaId
   */
  
  @Schema(name = "garantiaId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("garantiaId")
  public @Nullable String getGarantiaId() {
    return garantiaId;
  }

  @JsonProperty("garantiaId")
  public void setGarantiaId(@Nullable String garantiaId) {
    this.garantiaId = garantiaId;
  }

  public GarantiaResponse estado(@Nullable String estado) {
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
    GarantiaResponse garantiaResponse = (GarantiaResponse) o;
    return Objects.equals(this.garantiaId, garantiaResponse.garantiaId) &&
        Objects.equals(this.estado, garantiaResponse.estado);
  }

  @Override
  public int hashCode() {
    return Objects.hash(garantiaId, estado);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GarantiaResponse {\n");
    sb.append("    garantiaId: ").append(toIndentedString(garantiaId)).append("\n");
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

