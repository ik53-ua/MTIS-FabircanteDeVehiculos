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
 * InventarioUpdateRequest
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-05-07T14:57:49.443606900+02:00[Europe/Madrid]", comments = "Generator version: 7.22.0")
public class InventarioUpdateRequest {

  private @Nullable String estadoVehiculo;

  public InventarioUpdateRequest estadoVehiculo(@Nullable String estadoVehiculo) {
    this.estadoVehiculo = estadoVehiculo;
    return this;
  }

  /**
   * Get estadoVehiculo
   * @return estadoVehiculo
   */
  
  @Schema(name = "estadoVehiculo", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("estadoVehiculo")
  public @Nullable String getEstadoVehiculo() {
    return estadoVehiculo;
  }

  @JsonProperty("estadoVehiculo")
  public void setEstadoVehiculo(@Nullable String estadoVehiculo) {
    this.estadoVehiculo = estadoVehiculo;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    InventarioUpdateRequest inventarioUpdateRequest = (InventarioUpdateRequest) o;
    return Objects.equals(this.estadoVehiculo, inventarioUpdateRequest.estadoVehiculo);
  }

  @Override
  public int hashCode() {
    return Objects.hash(estadoVehiculo);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class InventarioUpdateRequest {\n");
    sb.append("    estadoVehiculo: ").append(toIndentedString(estadoVehiculo)).append("\n");
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

