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
    body: { estadoProduccion: "PAUSADO", nuevaFechaReanudacion: "2026-05-20" },
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
      configuracion: "DIESEL_MANUAL_ROJO",
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
      listaPiezasRequeridas: "1",
    }),
  },
];

const flowDefinitions = [
  {
    title: "Consulta y Reserva de Vehículos en Stock",
    image: "assets/flows/Consulta y reserva.png",
    imageAlt: "Diagrama del flujo Consulta y Reserva de Vehículos en Stock",
    endpoints: [
      {
        type: "MULE",
        method: "POST",
        url: "http://localhost:8081/api/soap/reservaVehiculo",
        role: "Principal",
      },
      {
        type: "SOAP",
        method: "POST",
        url: "http://localhost:8080/FabricanteDeVehiculos/services/ServicioStockConcesionario",
        role: "Interno",
      },
      {
        type: "SOAP",
        method: "POST",
        url: "http://localhost:8080/FabricanteDeVehiculos/services/ServicioStockFabricante",
        role: "Interno",
      },
      {
        type: "SOAP",
        method: "POST",
        url: "http://localhost:8080/FabricanteDeVehiculos/services/ServicioStockRed",
        role: "Interno",
      },
      {
        type: "SOAP",
        method: "POST",
        url: "http://localhost:8080/FabricanteDeVehiculos/services/ServicioReservaVehiculo",
        role: "Interno",
      },
      {
        type: "SOAP",
        method: "POST",
        url: "http://localhost:8080/FabricanteDeVehiculos/services/ServicioRegistroReservas",
        role: "Interno",
      },
      {
        type: "SOAP",
        method: "POST",
        url: "http://localhost:8080/FabricanteDeVehiculos/services/ServicioNotificaciones",
        role: "Interno",
      },
    ],
    fields: [
      { name: "WSKey", label: "WSKey", value: DEFAULT_KEY },
      { name: "idConcesionario", label: "ID concesionario", value: "2", valueType: "number" },
      { name: "idModelo", label: "ID modelo", value: "2", valueType: "number" },
      { name: "idCliente", label: "ID cliente", value: "1", valueType: "number" },
      { name: "configuracion", label: "Configuracion", value: "ELECTRICO_AUTO_BLANCO", multiline: true },
    ],
  },
  {
    title: "Pedido de Fabricación a Medida",
    image: "assets/flows/Fabricacion a medida.png",
    imageAlt: "Diagrama del flujo Fabricación a Medida",
    endpoints: [
      {
        type: "MULE",
        method: "POST",
        url: "http://localhost:8081/api/soap/pedidoFabricacion",
        role: "Principal",
      },
      {
        type: "SOAP",
        method: "POST",
        url: "http://localhost:8080/FabricanteDeVehiculos/services/ServicioConfiguracionesVehiculo",
        role: "Interno",
      },
      {
        type: "SOAP",
        method: "POST",
        url: "http://localhost:8080/FabricanteDeVehiculos/services/ServicioPlanificacionProduccion",
        role: "Interno",
      },
      {
        type: "SOAP",
        method: "POST",
        url: "http://localhost:8080/FabricanteDeVehiculos/services/ServicioCalculoCostes",
        role: "Interno",
      },
      {
        type: "SOAP",
        method: "POST",
        url: "http://localhost:8080/FabricanteDeVehiculos/services/ServicioGeneracionOrdenProduccion",
        role: "Interno",
      },
      {
        type: "SOAP",
        method: "POST",
        url: "http://localhost:8080/FabricanteDeVehiculos/services/ServicioRegistroPedidos",
        role: "Interno",
      },
      {
        type: "SOAP",
        method: "POST",
        url: "http://localhost:8080/FabricanteDeVehiculos/services/ServicioNotificacionesPedido",
        role: "Interno",
      },
    ],
    fields: [
      { name: "WSKey", label: "WSKey", value: DEFAULT_KEY },
      { name: "idConcesionario", label: "ID concesionario", value: "1", valueType: "number" },
      { name: "idModelo", label: "ID modelo", value: "1", valueType: "number" },
      { name: "configuracion", label: "Configuracion del vehiculo personalizado", value: "DIESEL_MANUAL_ROJO", multiline: true },
    ],
  },
  {
    title: "Solicitud de Piezas para Mantenimiento",
    image: "assets/flows/Piezas mantenimiento.png",
    imageAlt: "Diagrama del flujo Solicitud de Piezas para Mantenimiento",
    endpoints: [
      {
        type: "MULE",
        method: "POST",
        url: "http://localhost:8081/api/soap/solicitudRepuestos",
        role: "Principal",
      },
      {
        type: "SOAP",
        method: "POST",
        url: "http://localhost:8080/FabricanteDeVehiculos/services/ServicioCatalogoPiezas",
        role: "Interno",
      },
      {
        type: "SOAP",
        method: "POST",
        url: "http://localhost:8080/FabricanteDeVehiculos/services/ServicioStockRepuestos",
        role: "Interno",
      },
      {
        type: "SOAP",
        method: "POST",
        url: "http://localhost:8080/FabricanteDeVehiculos/services/ServicioPedidosRepuestos",
        role: "Interno",
      },
      {
        type: "SOAP",
        method: "POST",
        url: "http://localhost:8080/FabricanteDeVehiculos/services/ServicioLogisticaEnvios",
        role: "Interno",
      },
      {
        type: "SOAP",
        method: "POST",
        url: "http://localhost:8080/FabricanteDeVehiculos/services/ServicioNotificacionesEntrega",
        role: "Interno",
      },
    ],
    fields: [
      { name: "WSKey", label: "WSKey", value: DEFAULT_KEY },
      { name: "idConcesionario", label: "ID concesionario", value: "1", valueType: "number" },
      { name: "idPieza", label: "ID pieza", value: "1", valueType: "number" },
      { name: "cantidad", label: "Cantidad requerida", value: "2", valueType: "number" },
    ],
  },
  {
    title: "Reabastecimiento Automático de Componentes",
    image: "assets/flows/Reabastecimiento automático.png",
    imageAlt: "Diagrama del flujo Reabastecimiento Automático de Componentes",
    endpoints: [
      {
        type: "MULE",
        method: "POST",
        url: "http://localhost:8081/api/produccion/v1/pedidosCompra",
        role: "Principal",
      },
      {
        type: "REST",
        method: "GET",
        url: "http://localhost:8081/api/produccion/v1/inventario/{componenteId}",
        role: "Interno",
      },
      {
        type: "REST",
        method: "GET",
        url: "http://localhost:8081/api/produccion/v1/componentes/{componenteId}",
        role: "Interno",
      },
      {
        type: "REST",
        method: "GET",
        url: "http://localhost:8081/api/produccion/v1/proveedores/{proveedorId}",
        role: "Interno",
      },
      {
        type: "REST",
        method: "PUT",
        url: "http://localhost:8081/api/produccion/v1/planificacionProduccion/{planificacionId}",
        role: "Interno",
      },
    ],
    fields: [
      { name: "x-api-key", label: "x-api-key", value: DEFAULT_KEY, headerName: "x-api-key" },
      { name: "componenteId", label: "ID componente", value: "2" },
      { name: "cantidadRequerida", label: "Cantidad requerida", value: "5", valueType: "number" },
    ],
  },
  {
    title: "Registro y Cierre de Venta Final",
    image: "assets/flows/Registro de venta final.png",
    imageAlt: "Diagrama del flujo Registro y Cierre de Venta Final",
    endpoints: [
      {
        type: "MULE",
        method: "POST",
        url: "http://localhost:8081/api/ventas/v1/ventas",
        role: "Principal",
      },
      {
        type: "REST",
        method: "POST",
        url: "http://localhost:8081/api/ventas/v1/facturas",
        role: "Interno",
      },
      {
        type: "REST",
        method: "POST",
        url: "http://localhost:8081/api/ventas/v1/pagos",
        role: "Interno",
      },
      {
        type: "REST",
        method: "PUT",
        url: "http://localhost:8081/api/ventas/v1/inventario/{vehiculoId}",
        role: "Interno",
      },
      {
        type: "REST",
        method: "POST",
        url: "http://localhost:8081/api/ventas/v1/garantias",
        role: "Interno",
      },
    ],
    fields: [
      { name: "vehiculoId", label: "VIN vehiculo", value: "VIN00000000000001" },
      { name: "x-api-key", label: "x-api-key", value: DEFAULT_KEY, headerName: "x-api-key" },
      { name: "clienteId", label: "ID cliente", value: "1" },
      { name: "precioTotal", label: "Precio total", value: "35000", valueType: "number" },
    ],
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
};

const state = {
  tab: "flows",
  opIndex: 0,
};

const el = {
  tabs: document.querySelectorAll(".tab"),
  flowsPanel: document.querySelector("#flows-panel"),
  endpointPanels: document.querySelectorAll(".endpoint-panel"),
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
  renderActiveView();

  if (state.tab === "flows") {
    renderFlows();
    return;
  }

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

function renderActiveView() {
  const isFlows = state.tab === "flows";
  el.flowsPanel.hidden = !isFlows;
  el.endpointPanels.forEach((panel) => {
    panel.hidden = isFlows;
  });
}

function renderFlows() {
  el.flowsPanel.innerHTML = "";

  flowDefinitions.forEach((flow) => {
    const article = document.createElement("article");
    article.className = "panel flow-card";

    const header = document.createElement("div");
    header.className = "flow-header";

    const title = document.createElement("h2");
    title.textContent = flow.title;
    header.appendChild(title);
    article.appendChild(header);

    const imageFrame = document.createElement("figure");
    imageFrame.className = "flow-image-frame";

    const image = document.createElement("img");
    image.className = "flow-image";
    image.src = flow.image;
    image.alt = flow.imageAlt;
    image.loading = "lazy";

    const imageNote = document.createElement("figcaption");
    imageNote.className = "flow-image-note";
    imageNote.textContent = `Imagen pendiente: guarda el archivo en ${flow.image}`;

    image.addEventListener("load", () => {
      imageNote.hidden = true;
    });
    image.addEventListener("error", () => {
      image.hidden = true;
      imageNote.hidden = false;
    });

    imageFrame.appendChild(image);
    imageFrame.appendChild(imageNote);
    article.appendChild(imageFrame);

    const content = document.createElement("div");
    content.className = "flow-content";

    const endpointsSection = document.createElement("section");
    const endpointsTitle = document.createElement("h3");
    endpointsTitle.textContent = "Endpoints llamados en orden";
    endpointsSection.appendChild(endpointsTitle);

    const endpoints = document.createElement("ol");
    endpoints.className = "endpoint-list";

    flow.endpoints.forEach((endpoint) => {
      const item = document.createElement("li");

      const meta = document.createElement("div");
      meta.className = "endpoint-meta";

      const type = document.createElement("span");
      type.className = `endpoint-type endpoint-type-${endpoint.type.toLowerCase()}`;
      type.textContent = endpoint.type;
      meta.appendChild(type);

      const role = document.createElement("span");
      role.className = `endpoint-role endpoint-role-${endpoint.role === "Principal" ? "primary" : "internal"}`;
      role.textContent = endpoint.role || "Interno";
      meta.appendChild(role);

      const method = document.createElement("span");
      method.className = "endpoint-method";
      method.textContent = endpoint.method;
      meta.appendChild(method);

      const url = document.createElement("code");
      url.textContent = endpoint.url;

      item.appendChild(meta);
      item.appendChild(url);
      endpoints.appendChild(item);
    });

    endpointsSection.appendChild(endpoints);
    content.appendChild(endpointsSection);

    const dataSection = document.createElement("section");
    const dataTitle = document.createElement("h3");
    dataTitle.textContent = "Datos del flujo";
    dataSection.appendChild(dataTitle);

    const fields = document.createElement("div");
    fields.className = "flow-fields";

    flow.fields.forEach((field) => {
      const label = document.createElement("label");
      label.textContent = field.label;

      const input = field.multiline ? document.createElement("textarea") : document.createElement("input");
      input.name = field.name;
      input.value = field.value;
      input.spellcheck = false;

      if (field.multiline) {
        input.rows = 3;
      }

      label.appendChild(input);
      fields.appendChild(label);
    });

    dataSection.appendChild(fields);

    const actions = document.createElement("div");
    actions.className = "flow-actions";

    const sendButton = document.createElement("button");
    sendButton.className = "primary";
    sendButton.type = "button";
    sendButton.textContent = "Enviar peticion";
    actions.appendChild(sendButton);

    const requestUrl = document.createElement("code");
    requestUrl.textContent = flow.endpoints[0].url;
    actions.appendChild(requestUrl);

    const responseMeta = document.createElement("div");
    responseMeta.className = "flow-response-meta";

    const response = document.createElement("pre");
    response.className = "flow-response";
    response.textContent = "Rellena los datos y pulsa \"Enviar peticion\".";

    sendButton.addEventListener("click", () => {
      sendFlowRequest(flow, fields, sendButton, response, responseMeta);
    });

    dataSection.appendChild(actions);
    dataSection.appendChild(responseMeta);
    dataSection.appendChild(response);
    content.appendChild(dataSection);
    article.appendChild(content);
    el.flowsPanel.appendChild(article);
  });
}

function coerceFlowValue(value, field) {
  if (field.valueType === "number") {
    return Number(value);
  }

  return value;
}

function buildFlowRequest(flow, fieldsContainer) {
  const headers = { "Content-Type": "application/json" };
  const body = {};

  flow.fields.forEach((field) => {
    const input = fieldsContainer.querySelector(`[name="${field.name}"]`);
    const value = coerceFlowValue(input.value, field);

    if (field.headerName) {
      const headerValue = String(value).trim();
      headers[field.headerName] = headerValue;

      if (field.headerName === "x-api-key") {
        headers.WSKey = headerValue;
      }

      return;
    }

    body[field.name] = value;
  });

  return {
    method: flow.endpoints[0].method,
    headers,
    body: JSON.stringify(body),
  };
}

async function sendFlowRequest(flow, fieldsContainer, button, responseEl, metaEl) {
  const url = flow.endpoints[0].url;
  const request = buildFlowRequest(flow, fieldsContainer);
  const startedAt = performance.now();

  button.disabled = true;
  button.textContent = "Enviando...";
  responseEl.textContent = "Enviando...";
  metaEl.textContent = "";

  try {
    const response = canUseProxy()
      ? await fetchThroughProxy(url, request)
      : await fetchDirect(url, request);
    const elapsed = Math.round(performance.now() - startedAt);

    responseEl.textContent = formatResponse(response.text, response.contentType);
    metaEl.innerHTML = `<span class="${response.ok ? "ok" : "error"}">${response.status} ${response.statusText}</span> - ${elapsed} ms`;
  } catch (error) {
    responseEl.textContent = `${error.name}: ${error.message}`;
    metaEl.innerHTML = '<span class="error">No se pudo completar la peticion</span>';
  } finally {
    button.disabled = false;
    button.textContent = "Enviar peticion";
  }
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

  if (contentType.includes("xml") || text.trim().startsWith("<?xml") || text.trim().startsWith("<soap")) {
    return formatXml(text);
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

function formatXml(text) {
  try {
    const parser = new DOMParser();
    const xml = parser.parseFromString(text, "application/xml");

    if (xml.querySelector("parsererror")) {
      return text;
    }

    const serialized = new XMLSerializer().serializeToString(xml);
    return serialized
      .replace(/>\s*</g, ">\n<")
      .split("\n")
      .reduce((lines, line) => {
        const trimmed = line.trim();
        const decreasesIndent = /^<\//.test(trimmed);
        const currentIndent = decreasesIndent ? Math.max(lines.indent - 1, 0) : lines.indent;

        lines.output.push(`${"  ".repeat(currentIndent)}${trimmed}`);

        if (/^<[^!?/][^>]*[^/]>$/.test(trimmed) && !trimmed.includes("</")) {
          lines.indent = currentIndent + 1;
        } else {
          lines.indent = currentIndent;
        }

        return lines;
      }, { indent: 0, output: [] })
      .output
      .join("\n");
  } catch {
    return text;
  }
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
