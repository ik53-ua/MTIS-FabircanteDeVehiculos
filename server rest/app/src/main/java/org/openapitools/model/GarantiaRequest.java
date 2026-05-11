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
 * GarantiaRequest
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-05-07T14:57:49.443606900+02:00[Europe/Madrid]", comments = "Generator version: 7.22.0")
public class GarantiaRequest {

  private @Nullable String vehiculoId;

  private @Nullable Integer duracionMeses;

  public GarantiaRequest vehiculoId(@Nullable String vehiculoId) {
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

  public GarantiaRequest duracionMeses(@Nullable Integer duracionMeses) {
    this.duracionMeses = duracionMeses;
    return this;
  }

  /**
   * Get duracionMeses
   * @return duracionMeses
   */
  
  @Schema(name = "duracionMeses", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("duracionMeses")
  public @Nullable Integer getDuracionMeses() {
    return duracionMeses;
  }

  @JsonProperty("duracionMeses")
  public void setDuracionMeses(@Nullable Integer duracionMeses) {
    this.duracionMeses = duracionMeses;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GarantiaRequest garantiaRequest = (GarantiaRequest) o;
    return Objects.equals(this.vehiculoId, garantiaRequest.vehiculoId) &&
        Objects.equals(this.duracionMeses, garantiaRequest.duracionMeses);
  }

  @Override
  public int hashCode() {
    return Objects.hash(vehiculoId, duracionMeses);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GarantiaRequest {\n");
    sb.append("    vehiculoId: ").append(toIndentedString(vehiculoId)).append("\n");
    sb.append("    duracionMeses: ").append(toIndentedString(duracionMeses)).append("\n");
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

