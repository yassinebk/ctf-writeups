from flask import Blueprint, current_app, send_file

provider = Blueprint("provider", __name__)

@provider.route("/jwks.json", methods=["GET"])
def index():
    return send_file(current_app.config["JWKS_PATH"])