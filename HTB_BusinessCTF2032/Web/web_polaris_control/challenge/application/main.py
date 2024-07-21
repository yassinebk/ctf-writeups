from flask import Flask
from application.util.general import response
from application.util.jwt import generate_keypair
from application.blueprints.communications import comms
from application.blueprints.token_provider import provider
from application.blueprints.panel import panel

app = Flask(__name__)
app.config.from_object("application.config.Config")

app.register_blueprint(comms, url_prefix="/comms")
app.register_blueprint(panel, url_prefix="/panel")
app.register_blueprint(provider, url_prefix="/provider")

private_key = generate_keypair(app.config["JWKS_PATH"])
app.config["PRIVATE_KEY"] = private_key

@app.route("/", methods=["GET"])
def index():
    return response("200 OK"), 200


@app.errorhandler(404)
def not_found(error):
    return response("404 Not found"), 404


@app.errorhandler(403)
def forbidden(error):
    return response("403 Forbidden"), 403


@app.errorhandler(400)
def bad_request(error):
    return response("400 Bad request"), 400


@app.errorhandler(Exception)
def handle_error(error):
    print(error)
    message = error.description if hasattr(error, "description") else [str(x) for x in error.args]
    response = {
        "error": {
            "type": error.__class__.__name__,
            "message": message
        }
    }

    return response, error.code if hasattr(error, "code") else 500