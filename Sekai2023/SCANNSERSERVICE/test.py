from flask import Flask, send_from_directory

app = Flask(__name__)


## Routing – so when a browser asks for a resource, it gets something from the app
@app.route("/")  # empty route should serve ./cov_html/index.html
def index():
    return send_from_directory("./", "index.html")


@app.route("/<path:path>")  # Everything else just goes by filename
def sendstuff(path):
    print(path)
    return send_from_directory("./", path)


if __name__ == "__main__":
    app.run(debug=True)
