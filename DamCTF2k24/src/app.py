#!/usr/bin/env python3
from flask import send_file, Flask, render_template, request, session, redirect, url_for
import uuid
from werkzeug.middleware.proxy_fix import ProxyFix

from pathlib import Path
import requests
import time
import ipaddress
import os
import urllib
import sys
from dataclasses import dataclass, field
from functools import wraps
import random
import base64
import dns.resolver
from urllib.parse import urlparse

app = Flask(__name__)

app.wsgi_app = ProxyFix(app.wsgi_app, x_for=1, x_proto=1)
# app.config['DEBUG'] = True
# app.config['TEMPLATES_AUTO_RELOAD'] = True


def generate_id():
    return str(uuid.uuid4())


@dataclass
class Flower:
    name: str
    flower_url: str
    description: str
    id: str = "-1"


database: dict[str, Flower] = dict()


def add_flower(flower: Flower):
    flower.id = generate_id()
    database[flower.id] = flower
    return flower


add_flower(
    Flower(
        "Rose",
        "https://i0.wp.com/pikespeakfloral.com/wp-content/uploads/2019/06/roses-1.jpg?resize=800%2C533&ssl=1",
        "A red rose",
    )
)

add_flower(
    Flower(
        "Tulips",
        "https://hips.hearstapps.com/hmg-prod/images/close-up-of-tulips-blooming-in-field-royalty-free-image-1584131603.jpg?crop=0.630xw:1.00xh;0.186xw,0&resize=1200:*",
        "A field of tulips",
    )
)

add_flower(
    Flower(
        "Bleeding-heart",
        "https://www.verdissimo.com/wp-content/uploads/2023/08/Verdissimo_flores_bonitas_CONT_01.jpg",
        "Striking!",
    )
)

add_flower(
    Flower(
        "Daffodil",
        "https://www.southernliving.com/thmb/ebdANhJ7wJ5VfGZkb8ZoueTzUHw=/1500x0/filters:no_upscale():max_bytes(150000):strip_icc()/daffodils_aa51e27f4aab07409494_11303-2000-36defdf7d28848369d40532400a46f5a.jpg",
        "Daffodil!",
    )
)


add_flower(
    Flower(
        "Lantanas",
        "https://www.visaliatimesdelta.com/gcdn/-mm-/a6866234a81c7e41f9230adf9968a10eaba1accf/c=0-202-3888-2389/local/-/media/Visalia/2014/06/04/vtd0605mastergarderner1.jpg",
        "Colorful!",
    )
)


_ = (
    proprietary_secret_ai_algorithm := "Cih2Oj1yYW5kb20uY2hvaWNlcyhbMTQyNSwyMzQsMTIsMHg3QiwtOTEyLDU2LDg3LDI5NCwweDIwLDM5NCwzOCwweDRELDgwMCwxMjMsMzU2LDB4N0QsMTI4NywxNzEyLDMyLDBdLGs9NCksdls6Oi0xXVtpbnQuZnJvbV9ieXRlcyhieXRlcyhmbG93ZXJbMTAwOjEwNF0pKSAlIDRdLy84ICsgdlszXSUyMCA8IDI2MCBhbmQgKHBhdGgubG93ZXIoKS5lbmRzd2l0aCgoJy5wbmcnLCcuanBnJywnLmpwZWcnLCcuYm1wJykpIG9yIGZsb3dlci5zdGFydHN3aXRoKGJ5dGVzKFsweDg5LCAweDUwLCAweDRFLCAweDQ3LCAweDBELCAweDBBLCAweDFBLCAweDBBXSkpKSlbMV0gCg==",
    proprietary_secret_ai_algorithm := base64.b64decode(
        proprietary_secret_ai_algorithm
    ),
)

is_flower = compile(proprietary_secret_ai_algorithm, "<0x1172947815>", "eval")


add_flower(
    Flower(
        "Poppies",
        "https://www.thespruce.com/thmb/4RQA9Uj0jJ3FmBxCnOej27f5NDo=/1500x0/filters:no_upscale():max_bytes(150000):strip_icc()/how-to-grow-and-care-for-california-poppies-4686987-01-fb9c1a1298d54860815c047241788198.jpg",
        "Orange!",
    )
)

add_flower(
    Flower(
        "Common mallow",
        "https://upload.wikimedia.org/wikipedia/commons/9/92/Flower%2C_Common_mallow_-_Flickr_-_nekonomania.jpg",
        "Being common ain't so bad",
    )
)

add_flower(
    Flower(
        "Petunias",
        "/static/flower_0.jpg",
        "I hope",
    )
)

add_flower(
    Flower(
        "Yellow",
        "/static/flower_1.jpg",
        "I didn't take a picture of the tag",
    )
)

add_flower(
    Flower(
        "Pretty",
        "/static/flower_2.jpg",
        "Assorted mix",
    )
)

add_flower(
    Flower(
        "Porch flowers cool",
        "/static/flower_3.jpg",
        "Perennial!",
    )
)

add_flower(
    Flower(
        "Floofy",
        "/static/flower_4.jpg",
        "I meant it!",
    )
)

add_flower(
    Flower(
        "Butter",
        "/static/flower_5.jpg",
        "I can't believe it's butter!!",
    )
)

add_flower(
    Flower(
        "Carnation",
        "/static/flower_6.jpg",
        "W0t in tarnation!!",
    )
)


@app.get("/")
@app.get("/home")
@app.get("/index.html")
def index():
    return render_template("home.html", flowers=database.values())


@app.get("/flower/<flower_id>")
def flower_page(flower_id):
    flower = database.get(flower_id, None)
    if flower:
        return render_template("flower.html", flower=flower)
    else:
        return redirect("/")


@app.get("/special_flower")
def special_flower():
    if not request.remote_addr:
        return "Unauthorized access"

    r_ip = ipaddress.ip_address(request.remote_addr)
    if (
        r_ip in ipaddress.ip_network("192.168.0.0/16")
        or r_ip in ipaddress.ip_network("172.16.0.0/12")
        or r_ip in ipaddress.ip_network("10.0.0.0/8")
        or r_ip in ipaddress.ip_network("127.0.0.0/8")
    ):
        return send_file("flag.png", mimetype="image/png")

    return "Unauthorized access"


@app.get("/filter_flower")
def filter_flower_page():
    return render_template("filter_flower.html")


@app.post("/filter_flower")
def filter_flower():
    flower_url = request.form["flower_url"]

    if not flower_url:
        return "Not a flower :("

    urlp = urlparse(flower_url)
    domain = urlp.hostname
    path = urlp.path

    resolver = dns.resolver.Resolver()
    answer = resolver.resolve(domain, "A")

    ip = None
    print("answer", answer)
    for r in answer:
        ip = str(r)
        break

    if not ip:
        return "Not a flower!"

    r_ip = ipaddress.ip_address(ip)

    print("r_ip", r_ip)

    if (
        r_ip in ipaddress.ip_network("192.168.0.0/16")
        or r_ip in ipaddress.ip_network("172.16.0.0/12")
        or r_ip in ipaddress.ip_network("10.0.0.0/8")
        or r_ip in ipaddress.ip_network("127.0.0.0/8")
    ):
        return "Definitely not a flower!"

    # We need to be very careful in our examination of the flower - this takes time!
    time.sleep(0.919)

    r = requests.get(flower_url, verify=False, timeout=1)

    file_name = Path("temp") / generate_id()

    with open(file_name, "wb") as f:
        f.write(r.content)

    # We leverage our $500 million dollar seed money from highly skilled financial analysts to produce the bleeding-edge AI software technology to detect flowers and only allow valid flowers
    check = eval(is_flower, None, {"flower": r.content, "path": path})

    if check:
        return send_file(file_name, mimetype="image/png")
    else:
        return "Hmmm. Not sure if this is a flower"


@app.errorhandler(Exception)
def handle_error(error):
    return redirect("/")


if __name__ == "__main__":
    app.run(debug=True, host="0.0.0.0", port=80)
