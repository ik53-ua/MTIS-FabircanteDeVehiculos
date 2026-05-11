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
 * ProveedorResponse
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-05-07T14:57:03.923228100+02:00[Europe/Madrid]", comments = "Generator version: 7.22.0")
public class ProveedorResponse {

  private @Nullable String proveedorId;

  private @Nullable String nombreEmpresa;

  public ProveedorResponse proveedorId(@Nullable String proveedorId) {
    this.proveedorId = proveedorId;
    return this;
  }

  /**
   * Get proveedorId
   * @return proveedorId
   */
  
  @Schema(name = "proveedorId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("proveedorId")
  public @Nullable String getProveedorId() {
    return proveedorId;
  }

  @JsonProperty("proveedorId")
  public void setProveedorId(@Nullable String proveedorId) {
    this.proveedorId = proveedorId;
  }

  public ProveedorResponse nombreEmpresa(@Nullable String nombreEmpresa) {
    this.nombreEmpresa = nombreEmpresa;
    return this;
  }

  /**
   * Get nombreEmpresa
   * @return nombreEmpresa
   */
  
  @Schema(name = "nombreEmpresa", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("nombreEmpresa")
  public @Nullable String getNombreEmpresa() {
    return nombreEmpresa;
  }

  @JsonProperty("nombreEmpresa")
  public void setNombreEmpresa(@Nullable String nombreEmpresa) {
    this.nombreEmpresa = nombreEmpresa;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ProveedorResponse proveedorResponse = (ProveedorResponse) o;
    return Objects.equals(this.proveedorId, proveedorResponse.proveedorId) &&
        Objects.equals(this.nombreEmpresa, proveedorResponse.nombreEmpresa);
  }

  @Override
  public int hashCode() {
    return Objects.hash(proveedorId, nombreEmpresa);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ProveedorResponse {\n");
    sb.append("    proveedorId: ").append(toIndentedString(proveedorId)).append("\n");
    sb.append("    nombreEmpresa: ").append(toIndentedString(nombreEmpresa)).append("\n");
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

