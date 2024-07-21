import random

import json
import base64
import pickle
from requests  import *
from http.server import BaseHTTPRequestHandler, HTTPServer
from urllib.parse import urlparse, unquote_plus
from jinja2 import Environment


updates = {}

env = Environment()


class UpdatesHandler(BaseHTTPRequestHandler):
    def do_GET(self):
        parsed = urlparse(self.path)
        if parsed.path == "/":
            self.send_response(200)
            self.send_header("Content-type", "text/html")
            self.end_headers()
            with open("templates/index.html", "r") as f:
                self.wfile.write(f.read().encode())
            return
        elif parsed.path == "/view-updates":
            params = parsed.query.split("&")
            params = [p.split("=") for p in params]   #  [ [Tactical_Update_id,5], .. ] 
            tuid = None
            filler = "##"
            space = "__"
            escape="  "
            for p in params:
                if p[0] == "tuid":
                    tuid = p[1]  # value 
                elif p[0] == "filler":
                    filler = p[1]
                elif p[0] == "space":
                    space = p[1]
            if tuid == None:
                self.send_response(400)
                self.send_header("Content-type", "text/html")
                self.end_headers()
                self.wfile.write("No tuid specified".encode())
                return
            if tuid not in updates:
                self.send_response(404)
                self.send_header("Content-type", "text/html")
                self.end_headers()
                self.wfile.write(
                    "No Tactical updated found in the database with tuid {}".format(tuid).encode())
                return
            large_template = """
    <!DOCTYPE html>
    <html>
        <head>
            <title> Tactical Updates  </title>
            <style>
                html * {
                    font-size: 12px;
                    line-height: 1.625;
                    font-family: Consolas; }
            </style>
        </head>
        <body>
            <code> """ + str(pickle.loads(updates[tuid])) + """ </code>
            <h2> UPDATES :  </h2>
            {% if True %}
            {% endif %}
            {{space*100}}
            {% if True %}
            {% endif %}
            {{space*100}}
            {% if True %}
            {% endif %}
            {{space*15+filler*8+space*10+filler*10+space*1+filler*10+space*10+filler*8}}
            {% if True %}
            {% endif %}
            {{space*15+filler*8+space*10+filler*10+space*1+filler*10+space*10+filler*8}}
            {% if True %}
            {% endif %}
            {{space*15+filler*8+space*10+filler*10+space*1+filler*10+space*10+filler*8}}
            {% if True %}
            {% endif %}
            {{space*28+filler*12+space*1+filler*20+space*18}}
            {% if True %}
            {% endif %}
            {{space*28+filler*12+space*1+filler*20+space*18}}
            {% if True %}
            {% endif %}
            {{space*28+filler*12+space*1+filler*20+space*18}}
            {% if True %}
            {% endif %}
            {{space*28+filler*6+space*11+filler*5+space*11+filler*8+'#'+space*18}}
            {% if True %}
            {% endif %}
            {{space*28+filler*6+space*11+filler*5+space*11+filler*8+'#'+space*18}}
            {% if True %}
            {% endif %}
            {{space*28+filler*6+space*11+filler*5+space*11+filler*8+'#'+space*18}}
            {% if True %}
            {% endif %}
            {{space*28+filler*6+space*11+filler*5+space*11+filler*8+'#'+space*18}}
            {% if True %}
            {% endif %}
            {{space*28+filler*6+space*11+filler*5+space*11+filler*8+'#'+space*18}}
            {% if True %}
            {% endif %}
             {{space*15+filler*8+space*10+filler*11+filler*10+space*10+filler*8}}
            {% if True %}
            {% endif %}
            {{space*15+filler*8+space*10+filler*11+filler*10+space*10+filler*8}}
            {% if True %}
            {% endif %}
            {{space*15+filler*8+space*10+filler*11+filler*10+space*10+filler*8}}
            {% if True %}
            {% endif %}
            {{space*100}}
            {% if True %}
            {% endif %}
            {{space*37+filler*6+space+filler*6+space+filler*6+space*24}}
            {% if True %}
            {% endif %}
            {{space*37+filler*6+space+filler*6+space+filler*6+space*24}}
            {% if True %}
            {% endif %}
            {{space*37+filler*6+space+filler*6+space+filler*6+space*24}}
            {% if True %}
            {% endif %}
            {{space*100}}
            {% if True %}
            {% endif %}
            {{space*100}}
            {% if True %}
            {% endif %}
        </body>
    </html>
"""
            try:
                res = env.from_string(large_template).render(
                    filler=filler, space=space, escape=escape)
                self.send_response(200)
                self.send_header("Content-type", "text/html")
                self.end_headers()
                self.wfile.write(res.encode())
            except Exception as e:
                print(e)
                self.send_response(500)
                self.send_header("Content-type", "text/html")
                self.end_headers()
                self.wfile.write(str(e).encode())
                #self.wfile.write("Error rendering template".encode())
            return
        else:
            self.send_response(404)
            self.send_header("Content-type", "text/html")
            self.end_headers()
            self.wfile.write("Not found".encode())
            return

    def do_POST(self):
        parsed = urlparse(self.path)
        if parsed.path == "/create-update":
            length = int(self.headers.get("content-length"))
            body = self.rfile.read(length).decode()
            try:
                data = unquote_plus(body.split("=")[1]).strip()
                print(f"data: {data}")
                data = json.loads(data)

                print(f"json loaded data: {data}")
                pp = pickle.dumps(data)

                print(f"pp dumped data: {pp}")
                #s = base64.b64encode(pp).decode('ascii')
                tuid = generate_random_hexstring(32)
                updates[tuid] = pp
                print(f"updates: {updates}")
                self.send_response(200)
                self.send_header("Content-type", "text/html")
                self.end_headers()
                self.wfile.write(tuid.encode())
                return
            except Exception as e:
                print(e)
                self.send_response(400)
                self.send_header("Content-type", "text/html")
                self.end_headers()
                self.wfile.write("Invalid JSON".encode())
                return
        else:
            self.send_response(404)
            self.send_header("Content-type", "text/html")
            self.end_headers()
            self.wfile.write("Not found".encode())
            return


def render_template_string_sanitized(env, template, **args):
    # it works!
    global_vars = ['self', 'request', 'session', 'g', 'app']
    for var in global_vars:
        template = "{% set " + var + " = None %}\n" + template
    return env.from_string(template).render(**args)


def generate_random_hexstring(length):
    return "".join(random.choice("0123456789abcdef") for _ in range(length))


if __name__ == "__main__":
    PORT = 9229
    with HTTPServer(("", PORT), UpdatesHandler) as httpd:
        print(f"Listening on 0.0.0.0:{PORT}")
        httpd.serve_forever()
