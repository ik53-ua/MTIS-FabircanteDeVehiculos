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
 * RespuestaBase
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-05-07T14:57:49.443606900+02:00[Europe/Madrid]", comments = "Generator version: 7.22.0")
public class RespuestaBase {

  private @Nullable String mensaje;

  public RespuestaBase mensaje(@Nullable String mensaje) {
    this.mensaje = mensaje;
    return this;
  }

  /**
   * Resultado de la operación o detalle del error.
   * @return mensaje
   */
  
  @Schema(name = "mensaje", description = "Resultado de la operación o detalle del error.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("mensaje")
  public @Nullable String getMensaje() {
    return mensaje;
  }

  @JsonProperty("mensaje")
  public void setMensaje(@Nullable String mensaje) {
    this.mensaje = mensaje;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RespuestaBase respuestaBase = (RespuestaBase) o;
    return Objects.equals(this.mensaje, respuestaBase.mensaje);
  }

  @Override
  public int hashCode() {
    return Objects.hash(mensaje);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RespuestaBase {\n");
    sb.append("    mensaje: ").append(toIndentedString(mensaje)).append("\n");
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

