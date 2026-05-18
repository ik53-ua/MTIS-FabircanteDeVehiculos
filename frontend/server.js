const http = require("http");
const https = require("https");
const fs = require("fs");
const path = require("path");

const PORT = Number(process.env.PORT || 5173);
const ROOT = __dirname;
const MIME_TYPES = {
  ".html": "text/html; charset=utf-8",
  ".css": "text/css; charset=utf-8",
  ".js": "application/javascript; charset=utf-8",
  ".json": "application/json; charset=utf-8",
  ".png": "image/png",
  ".jpg": "image/jpeg",
  ".jpeg": "image/jpeg",
  ".webp": "image/webp",
  ".svg": "image/svg+xml; charset=utf-8",
};

function send(res, status, body, headers = {}) {
  res.writeHead(status, headers);
  res.end(body);
}

function serveStatic(req, res) {
  const requestPath = req.url === "/" ? "/index.html" : decodeURIComponent(req.url.split("?")[0]);
  const filePath = path.resolve(ROOT, `.${requestPath}`);

  if (!filePath.startsWith(ROOT)) {
    send(res, 403, "Forbidden", { "Content-Type": "text/plain; charset=utf-8" });
    return;
  }

  fs.readFile(filePath, (error, content) => {
    if (error) {
      send(res, 404, "Not found", { "Content-Type": "text/plain; charset=utf-8" });
      return;
    }

    const contentType = MIME_TYPES[path.extname(filePath)] || "application/octet-stream";
    send(res, 200, content, { "Content-Type": contentType });
  });
}

function readJson(req) {
  return new Promise((resolve, reject) => {
    let body = "";
    req.on("data", (chunk) => {
      body += chunk;
      if (body.length > 2_000_000) {
        req.destroy();
        reject(new Error("Request too large"));
      }
    });
    req.on("end", () => {
      try {
        resolve(JSON.parse(body || "{}"));
      } catch (error) {
        reject(error);
      }
    });
    req.on("error", reject);
  });
}

function proxyRequest(payload, res) {
  const target = new URL(payload.url);
  const client = target.protocol === "https:" ? https : http;
  const body = payload.body || "";
  const headers = { ...(payload.headers || {}) };

  if (body && !headers["Content-Length"]) {
    headers["Content-Length"] = Buffer.byteLength(body);
  }

  const request = client.request(
    {
      method: payload.method || "GET",
      hostname: target.hostname,
      port: target.port,
      path: `${target.pathname}${target.search}`,
      headers,
    },
    (upstream) => {
      const chunks = [];
      upstream.on("data", (chunk) => chunks.push(chunk));
      upstream.on("end", () => {
        const responseBody = Buffer.concat(chunks);
        send(res, 200, responseBody, {
          "Content-Type": upstream.headers["content-type"] || "text/plain; charset=utf-8",
          "X-Upstream-Status": String(upstream.statusCode || 0),
          "X-Upstream-Status-Text": upstream.statusMessage || "",
        });
      });
    },
  );

  request.on("error", (error) => {
    send(
      res,
      502,
      JSON.stringify({ error: error.message }, null, 2),
      { "Content-Type": "application/json; charset=utf-8" },
    );
  });

  if (body) {
    request.write(body);
  }
  request.end();
}

const server = http.createServer(async (req, res) => {
  if (req.method === "POST" && req.url === "/proxy") {
    try {
      const payload = await readJson(req);
      proxyRequest(payload, res);
    } catch (error) {
      send(
        res,
        400,
        JSON.stringify({ error: error.message }, null, 2),
        { "Content-Type": "application/json; charset=utf-8" },
      );
    }
    return;
  }

  serveStatic(req, res);
});

server.listen(PORT, () => {
  console.log(`Cliente disponible en http://localhost:${PORT}`);
});
