from flask import Flask, send_file

app = Flask(__name__)


@app.post("/download")
def download_file():
    return send_file("test.json", as_attachment=True, download_name="test.json")


if __name__ == "__main__":
    app.run(debug=True)
