const DEFAULT_KEY = "1234";

const restOperations = [
  {
    label: "Produccion - Crear orden de compra",
    method: "POST",
    path: "/produccion/pedidosCompra",
    body: { componenteId: "2", cantidadRequerida: 5 },
  },
  {
    label: "Produccion - Consultar orden de compra",
    method: "GET",
    path: "/produccion/pedidosCompra/{id}",
    params: { id: "1" },
  },
  {
    label: "Produccion - Consultar stock componente",
    method: "GET",
    path: "/produccion/inventario/{componenteId}",
    params: { componenteId: "2" },
  },
  {
    label: "Produccion - Obtener componente",
    method: "GET",
    path: "/produccion/componentes/{id}",
    params: { id: "1" },
  },
  {
    label: "Produccion - Consultar proveedor",
    method: "GET",
    path: "/produccion/proveedores/{id}",
    params: { id: "2" },
  },
  {
    label: "Produccion - Actualizar planificacion",
    method: "PUT",
    path: "/produccion/planificacionProduccion/{id}",
    params: { id: "1" },
    body: { estadoProduccion: "PAUSADA", nuevaFechaReanudacion: "2026-05-20" },
  },
  {
    label: "Ventas - Registrar venta",
    method: "POST",
    path: "/ventas/registrar",
    body: { vehiculoId: "VIN00000000000001", clienteId: 1, precioTotal: 35000 },
  },
  {
    label: "Ventas - Consultar venta",
    method: "GET",
    path: "/ventas/{id}",
    params: { id: "1" },
  },
  {
    label: "Ventas - Generar factura",
    method: "POST",
    path: "/ventas/facturas",
    body: { ventaId: 1, importe: 35000 },
  },
  {
    label: "Ventas - Consultar factura",
    method: "GET",
    path: "/ventas/facturas/{id}",
    params: { id: "1" },
  },
  {
    label: "Ventas - Registrar pago",
    method: "POST",
    path: "/ventas/pagos",
    body: { facturaId: 1, cantidad: 35000 },
  },
  {
    label: "Ventas - Consultar pago",
    method: "GET",
    path: "/ventas/pagos/{id}",
    params: { id: "1" },
  },
  {
    label: "Ventas - Consultar inventario vehiculo",
    method: "GET",
    path: "/ventas/inventario/{vehiculoId}",
    params: { vehiculoId: "VIN00000000000001" },
  },
  {
    label: "Ventas - Actualizar estado vehiculo",
    method: "PUT",
    path: "/ventas/inventario/{vehiculoId}",
    params: { vehiculoId: "VIN00000000000001" },
    body: { estado: "VENDIDO" },
  },
  {
    label: "Ventas - Activar garantia",
    method: "POST",
    path: "/ventas/garantias",
    body: { vehiculoId: "VIN00000000000001", ventaId: 1, duracionMeses: 24 },
  },
  {
    label: "Ventas - Consultar garantia",
    method: "GET",
    path: "/ventas/garantias/{vehiculoId}",
    params: { vehiculoId: "VIN00000000000001" },
  },
];

const muleOperations = [
  {
    label: "Proceso 2.1 - Reserva vehiculo",
    method: "POST",
    path: "/api/soap/reservaVehiculo",
    body: {
      WSKey: DEFAULT_KEY,
      idConcesionario: 1,
      idModelo: 1,
      idCliente: 1,
      configuracion: "Color: Azul; Motor: Hibrido",
    },
  },
  {
    label: "Proceso 2.2 - Pedido fabricacion",
    method: "POST",
    path: "/api/soap/pedidoFabricacion",
    body: {
      WSKey: DEFAULT_KEY,
      idConcesionario: 1,
      idModelo: 1,
      configuracion: "Color: Negro; Motor: Electrico",
    },
  },
  {
    label: "Proceso 2.3 - Solicitud repuestos",
    method: "POST",
    path: "/api/soap/solicitudRepuestos",
    body: {
      WSKey: DEFAULT_KEY,
      idConcesionario: 1,
      diagnostico: "Sustitucion preventiva",
      listaPiezasRequeridas: "Filtro aceite, pastillas freno",
    },
  },
  {
    label: "Produccion - Crear orden de compra",
    method: "POST",
    path: "/api/produccion/v1/pedidosCompra",
    body: { componenteId: "2", cantidadRequerida: 5 },
  },
  {
    label: "Produccion - Consultar stock componente",
    method: "GET",
    path: "/api/produccion/v1/inventario/{componenteId}",
    params: { componenteId: "2" },
  },
  {
    label: "Produccion - Obtener componente",
    method: "GET",
    path: "/api/produccion/v1/componentes/{id}",
    params: { id: "1" },
  },
  {
    label: "Produccion - Consultar proveedor",
    method: "GET",
    path: "/api/produccion/v1/proveedores/{id}",
    params: { id: "2" },
  },
  {
    label: "Produccion - Consultar orden compra",
    method: "GET",
    path: "/api/produccion/v1/pedidosCompra/{id}",
    params: { id: "1" },
  },
  {
    label: "Produccion - Actualizar planificacion",
    method: "PUT",
    path: "/api/produccion/v1/planificacionProduccion/{id}",
    params: { id: "1" },
    body: { estadoProduccion: "PAUSADA", nuevaFechaReanudacion: "2026-05-20" },
  },
  {
    label: "Ventas - Registrar venta",
    method: "POST",
    path: "/api/ventas/v1/ventas",
    body: { vehiculoId: "VIN00000000000001", clienteId: "1", precioTotal: 35000 },
  },
  {
    label: "Ventas - Consultar venta",
    method: "GET",
    path: "/api/ventas/v1/ventas/{id}",
    params: { id: "1" },
  },
  {
    label: "Ventas - Generar factura",
    method: "POST",
    path: "/api/ventas/v1/facturas",
    body: { ventaId: "1", importe: 35000 },
  },
  {
    label: "Ventas - Registrar pago",
    method: "POST",
    path: "/api/ventas/v1/pagos",
    body: { facturaId: "1", cantidad: 35000 },
  },
  {
    label: "Ventas - Actualizar estado vehiculo",
    method: "PUT",
    path: "/api/ventas/v1/inventario/{vehiculoId}",
    params: { vehiculoId: "VIN00000000000001" },
    body: { estadoVehiculo: "VENDIDO" },
  },
];

const soapOperations = [
  {
    label: "Proceso reserva vehiculo",
    method: "POST",
    path: "/ServicioProcesoReservaVehiculo",
    soapAction: "http://mtis.org/ServicioProcesoReservaVehiculo/ReservarVehiculo",
    body: soapEnvelope("ServicioProcesoReservaVehiculo", "ReservarVehiculoRequest", {
      WSKey: DEFAULT_KEY,
      idConcesionario: 1,
      idModelo: 1,
      idCliente: 1,
      configuracion: "Color: Azul; Motor: Hibrido",
    }),
  },
  {
    label: "Pedido fabricacion",
    method: "POST",
    path: "/ServicioProcesoPedidoFabricacion",
    soapAction: "http://mtis.org/ServicioProcesoPedidoFabricacion/ProcesarPedidoFabricacion",
    body: soapEnvelope("ServicioProcesoPedidoFabricacion", "ProcesarPedidoFabricacionRequest", {
      WSKey: DEFAULT_KEY,
      idConcesionario: 1,
      idModelo: 1,
      configuracion: "Color: Negro; Motor: Electrico",
    }),
  },
  {
    label: "Solicitud repuestos",
    method: "POST",
    path: "/ServicioProcesoSolicitudRepuestos",
    soapAction: "http://mtis.org/ServicioProcesoSolicitudRepuestos/ProcesarSolicitudRepuestos",
    body: soapEnvelope("ServicioProcesoSolicitudRepuestos", "ProcesarSolicitudRepuestosRequest", {
      WSKey: DEFAULT_KEY,
      idConcesionario: 1,
      diagnostico: "Sustitucion preventiva",
      listaPiezasRequeridas: "Filtro aceite, pastillas freno",
    }),
  },
];

const tabs = {
  rest: {
    baseUrl: "http://localhost:8000/api",
    hint: "REST usa cabecera x-api-key. El servidor Express tiene CORS activado.",
    operations: restOperations,
  },
  soap: {
    baseUrl: "http://localhost:8080/FabricanteDeVehiculos/services",
    hint: "SOAP envia WSKey dentro del XML y SOAPAction en cabecera.",
    operations: soapOperations,
  },
  mule: {
    baseUrl: "http://localhost:8081",
    hint: "MuleSoft usa x-api-key para las APIs RAML y JSON con WSKey en procesos SOAP orquestados.",
    operations: muleOperations,
  },
};

const state = {
  tab: "rest",
  opIndex: 0,
};

const el = {
  tabs: document.querySelectorAll(".tab"),
  baseUrl: document.querySelector("#base-url"),
  apiKey: document.querySelector("#api-key"),
  operation: document.querySelector("#operation"),
  method: document.querySelector("#method"),
  methodBadge: document.querySelector("#method-badge"),
  finalUrl: document.querySelector("#final-url"),
  params: document.querySelector("#params"),
  body: document.querySelector("#body"),
  send: document.querySelector("#send"),
  copyCurl: document.querySelector("#copy-curl"),
  formatBody: document.querySelector("#format-body"),
  resetOperation: document.querySelector("#reset-operation"),
  hint: document.querySelector("#request-hint"),
  response: document.querySelector("#response"),
  responseMeta: document.querySelector("#response-meta"),
};

function soapEnvelope(service, requestName, values) {
  const fields = Object.entries(values)
    .map(([key, value]) => `        <tns:${key}>${escapeXml(String(value))}</tns:${key}>`)
    .join("\n");

  return `<?xml version="1.0" encoding="UTF-8"?>
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:tns="http://mtis.org/${service}/">
  <soapenv:Header/>
  <soapenv:Body>
    <tns:${requestName}>
${fields}
    </tns:${requestName}>
  </soapenv:Body>
</soapenv:Envelope>`;
}

function escapeXml(value) {
  return value
    .replaceAll("&", "&amp;")
    .replaceAll("<", "&lt;")
    .replaceAll(">", "&gt;")
    .replaceAll('"', "&quot;")
    .replaceAll("'", "&apos;");
}

function activeConfig() {
  return tabs[state.tab];
}

function activeOperation() {
  return activeConfig().operations[state.opIndex];
}

function renderTab() {
  const config = activeConfig();
  el.baseUrl.value = config.baseUrl;
  el.hint.textContent = config.hint;
  el.operation.innerHTML = "";

  config.operations.forEach((operation, index) => {
    const option = document.createElement("option");
    option.value = String(index);
    option.textContent = operation.label;
    el.operation.appendChild(option);
  });

  state.opIndex = 0;
  renderOperation();
}

function renderOperation() {
  const operation = activeOperation();
  el.operation.value = String(state.opIndex);
  el.method.value = operation.method;
  el.methodBadge.textContent = operation.method;
  el.params.innerHTML = "";

  Object.entries(operation.params || {}).forEach(([name, value]) => {
    const label = document.createElement("label");
    label.textContent = name;
    const input = document.createElement("input");
    input.name = name;
    input.value = value;
    input.spellcheck = false;
    input.addEventListener("input", updateFinalUrl);
    label.appendChild(input);
    el.params.appendChild(label);
  });

  if (!operation.params) {
    const empty = document.createElement("p");
    empty.textContent = "Esta operacion no tiene parametros de ruta.";
    empty.className = "muted";
    el.params.appendChild(empty);
  }

  el.body.value = typeof operation.body === "string" ? operation.body : JSON.stringify(operation.body || {}, null, 2);
  updateFinalUrl();
}

function getParamValues() {
  return Array.from(el.params.querySelectorAll("input")).reduce((acc, input) => {
    acc[input.name] = input.value;
    return acc;
  }, {});
}

function buildUrl() {
  const operation = activeOperation();
  const base = el.baseUrl.value.replace(/\/+$/, "");
  const params = getParamValues();
  let path = operation.path;

  Object.entries(params).forEach(([key, value]) => {
    path = path.replace(`{${key}}`, encodeURIComponent(value));
  });

  return `${base}${path}`;
}

function updateFinalUrl() {
  el.finalUrl.textContent = buildUrl();
}

function buildHeaders(operation) {
  const headers = {};

  if (state.tab === "soap") {
    headers["Content-Type"] = "text/xml; charset=utf-8";
    headers.SOAPAction = operation.soapAction;
    return headers;
  }

  headers["Content-Type"] = "application/json";
  headers["x-api-key"] = el.apiKey.value.trim();
  return headers;
}

function buildRequest(operation) {
  const method = operation.method;
  const headers = buildHeaders(operation);
  const init = { method, headers };

  if (method !== "GET") {
    init.body = el.body.value.trim();
  }

  return init;
}

function canUseProxy() {
  return window.location.protocol === "http:" || window.location.protocol === "https:";
}

async function fetchThroughProxy(url, request) {
  const proxyResponse = await fetch("/proxy", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({
      url,
      method: request.method,
      headers: request.headers,
      body: request.body || "",
    }),
  });

  const text = await proxyResponse.text();
  const status = Number(proxyResponse.headers.get("X-Upstream-Status") || proxyResponse.status);
  const statusText = proxyResponse.headers.get("X-Upstream-Status-Text") || proxyResponse.statusText;
  const contentType = proxyResponse.headers.get("content-type") || "";

  return {
    ok: status >= 200 && status < 300,
    status,
    statusText,
    contentType,
    text,
  };
}

async function fetchDirect(url, request) {
  const response = await fetch(url, request);
  return {
    ok: response.ok,
    status: response.status,
    statusText: response.statusText,
    contentType: response.headers.get("content-type") || "",
    text: await response.text(),
  };
}

async function sendRequest() {
  const operation = activeOperation();
  const url = buildUrl();
  const request = buildRequest(operation);
  const startedAt = performance.now();
  setBusy(true);
  el.response.textContent = "Enviando...";
  el.responseMeta.textContent = "";

  try {
    if (operation.method !== "GET" && state.tab !== "soap") {
      JSON.parse(el.body.value || "{}");
    }

    const response = canUseProxy()
      ? await fetchThroughProxy(url, request)
      : await fetchDirect(url, request);
    const elapsed = Math.round(performance.now() - startedAt);

    el.response.textContent = formatResponse(response.text, response.contentType);
    el.responseMeta.innerHTML = `<span class="${response.ok ? "ok" : "error"}">${response.status} ${response.statusText}</span> · ${elapsed} ms`;
  } catch (error) {
    el.response.textContent = `${error.name}: ${error.message}`;
    el.responseMeta.innerHTML = '<span class="error">No se pudo completar la peticion</span>';
  } finally {
    setBusy(false);
  }
}

function formatResponse(text, contentType = "") {
  if (!text) {
    return "(respuesta vacia)";
  }

  if (contentType.includes("json")) {
    try {
      return JSON.stringify(JSON.parse(text), null, 2);
    } catch {
      return text;
    }
  }

  if (text.trim().startsWith("{") || text.trim().startsWith("[")) {
    try {
      return JSON.stringify(JSON.parse(text), null, 2);
    } catch {
      return text;
    }
  }

  return text;
}

function setBusy(isBusy) {
  el.send.disabled = isBusy;
  el.send.textContent = isBusy ? "Enviando..." : "Enviar peticion";
}

function formatBody() {
  if (state.tab === "soap") {
    el.body.value = el.body.value.trim();
    return;
  }

  try {
    el.body.value = JSON.stringify(JSON.parse(el.body.value || "{}"), null, 2);
  } catch (error) {
    el.response.textContent = `JSON invalido: ${error.message}`;
  }
}

async function copyCurl() {
  const operation = activeOperation();
  const headers = buildHeaders(operation);
  const headerArgs = Object.entries(headers)
    .map(([key, value]) => `-H "${key}: ${value}"`)
    .join(" ");
  const bodyArg = operation.method === "GET" ? "" : ` -d '${el.body.value.replaceAll("'", "'\\''")}'`;
  const curl = `curl -X ${operation.method} "${buildUrl()}" ${headerArgs}${bodyArg}`;

  await navigator.clipboard.writeText(curl);
}

el.tabs.forEach((tab) => {
  tab.addEventListener("click", () => {
    state.tab = tab.dataset.tab;
    el.tabs.forEach((item) => item.classList.toggle("active", item === tab));
    renderTab();
  });
});

el.operation.addEventListener("change", () => {
  state.opIndex = Number(el.operation.value);
  renderOperation();
});
el.baseUrl.addEventListener("input", updateFinalUrl);
el.send.addEventListener("click", sendRequest);
el.copyCurl.addEventListener("click", copyCurl);
el.formatBody.addEventListener("click", formatBody);
el.resetOperation.addEventListener("click", renderOperation);

renderTab();
