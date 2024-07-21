from flask import Flask, render_template
from analyze import analyze_bp

app = Flask(__name__)
app.register_blueprint(analyze_bp)


@app.route("/", methods=["GET"])
def home():
    return render_template("index.html")


@app.route("/instructions", methods=["GET"])
def instruction():
    return render_template("instruction.html")


if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000)
