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
 * ComponenteResponse
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-05-07T14:57:03.923228100+02:00[Europe/Madrid]", comments = "Generator version: 7.22.0")
public class ComponenteResponse {

  private @Nullable String componenteId;

  private @Nullable String nombre;

  private @Nullable String proveedorId;

  public ComponenteResponse componenteId(@Nullable String componenteId) {
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

  public ComponenteResponse nombre(@Nullable String nombre) {
    this.nombre = nombre;
    return this;
  }

  /**
   * Get nombre
   * @return nombre
   */
  
  @Schema(name = "nombre", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("nombre")
  public @Nullable String getNombre() {
    return nombre;
  }

  @JsonProperty("nombre")
  public void setNombre(@Nullable String nombre) {
    this.nombre = nombre;
  }

  public ComponenteResponse proveedorId(@Nullable String proveedorId) {
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ComponenteResponse componenteResponse = (ComponenteResponse) o;
    return Objects.equals(this.componenteId, componenteResponse.componenteId) &&
        Objects.equals(this.nombre, componenteResponse.nombre) &&
        Objects.equals(this.proveedorId, componenteResponse.proveedorId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(componenteId, nombre, proveedorId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ComponenteResponse {\n");
    sb.append("    componenteId: ").append(toIndentedString(componenteId)).append("\n");
    sb.append("    nombre: ").append(toIndentedString(nombre)).append("\n");
    sb.append("    proveedorId: ").append(toIndentedString(proveedorId)).append("\n");
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

