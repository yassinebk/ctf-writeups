import re
from flask import Blueprint, redirect, render_template, request, current_app, make_response, send_file
from application.util.general import response, machine_info, build_implant
from application.util.jwt import create_jwt, verify_jwt
from application.util.mysql import MysqlInterface
from application.util.neo4j import Neo4jInterface

panel = Blueprint("panel", __name__)

def moderator_middleware(func):
    def check_moderator(*args, **kwargs):
        jwt_cookie = request.cookies.get("jwt")
        if not jwt_cookie:
            return response("Unauthorized"), 401

        mysql_interface = MysqlInterface(current_app.config)
        allowed_hosts = mysql_interface.fetch_token_providers()
        token = verify_jwt(allowed_hosts, jwt_cookie)
        
        if not token:
            return response("Unauthorized"), 401
        
        try:
            account_type = token["account_type"]

            if not account_type or account_type != "moderator" and account_type != "administrator":
                return response("Forbidden"), 403
            
            request.user_data = token

        except:
            return response("Invalid token"), 401

        return func(*args, **kwargs)

    check_moderator.__name__ = func.__name__
    return check_moderator


def administrator_middleware(func):
    def check_administrator(*args, **kwargs):
        jwt_cookie = request.cookies.get("jwt") or request.args.get("token")
        if not jwt_cookie:
            return response("Unauthorized"), 401

        mysql_interface = MysqlInterface(current_app.config)
        allowed_hosts = mysql_interface.fetch_token_providers()
        token = verify_jwt(allowed_hosts, jwt_cookie)
        
        if not token:
            return response("Unauthorized"), 401
        
        try:
            account_type = token["account_type"]

            if not account_type or account_type != "administrator":
                return response("Forbidden"), 403
            
            request.user_data = token

        except:
            return response("Invalid token"), 401

        return func(*args, **kwargs)

    check_administrator.__name__ = func.__name__
    return check_administrator


def localhost_middleware(func):
    def check_localhost(*args, **kwargs):
        remote_addr = request.remote_addr
        if remote_addr != "127.0.0.1" and remote_addr != "::1":
            return response("Unauthorized"), 401

        user_agent = request.headers.get("User-Agent")
        if current_app.config["BOT_AGENT_NAME"] in user_agent:
            return response("Unauthorized"), 401

        return func(*args, **kwargs)

    check_localhost.__name__ = func.__name__
    return check_localhost


def csp_middleware(func):
    def set_csp(*args, **kwargs):
        pattern = r'/panel/implant/(\w+)'
        match = re.search(pattern, request.url)
        image_url = None

        if match:
            mysql_interface = MysqlInterface(current_app.config)
            image_url = mysql_interface.fetch_implant_by_identifier(match.group(1))["image_url"]

        img_policy = f"'self' {image_url[1:]}" if image_url else "'self'"

        response = make_response(func(*args, **kwargs))
        response.headers["Content-Security-Policy"] = f"default-src 'self'; frame-ancestors 'none'; object-src 'none'; base-uri 'none'; img-src {img_policy};"
        return response

    set_csp.__name__ = func.__name__
    return set_csp


@panel.route("/login", methods=["GET", "POST"])
def login():
    if request.method == "GET":
        return render_template("login.html", title="Log-in")

    if request.method == "POST":
        username = request.form.get("username")
        password = request.form.get("password")
        
        if not username or not password:
            return response("Missing parameters"), 400
        
        mysql_interface = MysqlInterface(current_app.config)
        user_valid = mysql_interface.check_user(username, password)

        if not user_valid:
            return response("Invalid user or password"), 401
        
        allowed_hosts = mysql_interface.fetch_token_providers()
        user_data = mysql_interface.fetch_user_by_username(username)
        
        jwt = create_jwt(allowed_hosts, current_app.config["PRIVATE_KEY"], user_data["account_type"], username)
        
        if not jwt:
            return response("No allowed hosts found"), 403
        
        resp = make_response(redirect("/panel/home"))
        resp.set_cookie("jwt", jwt, httponly=True)
        return resp, 302
        

@panel.route("/logout", methods=["GET"])
@moderator_middleware
def logout():
    resp = make_response(redirect("/panel/login"))
    resp.set_cookie("jwt", "", expires=0)
    return resp, 302


@panel.route("/home", methods=["GET", "POST"])
@moderator_middleware
def home():
    server_info = machine_info()
    
    mysql_interface = MysqlInterface(current_app.config)
    statistics = mysql_interface.fetch_implant_statistics()
    
    implants = None
    
    if request.method == "GET":
        implants = mysql_interface.fetch_implant_data()
        
    if request.method == "POST":    
        field = request.form.get("field")
        query_eq = request.form.get("query_eq")
        query_like = request.form.get("query_like")
    
        if not field or not query_eq:
            return response("Missing parameters"), 400
        
        if not query_like:
            query_like = query_eq

        implants = mysql_interface.search_implants(field, query_eq, query_like)
        
    return render_template("home.html", 
        title="Home", 
        nav_enabled=True, 
        user_data=request.user_data, 
        statistics=statistics, 
        implant_data=implants, 
        server_info=server_info
    )


@panel.route("/implant/<identifier>", methods=["GET"])
@csp_middleware
@moderator_middleware
def implant(identifier):
    if not identifier:
        return response("Missing parameters"), 400
        
    mysql_interface = MysqlInterface(current_app.config)
    implant = mysql_interface.fetch_implant_by_identifier(identifier)

    if not implant:
        return response("Implant not found"), 404
    
    return render_template("implant.html",
        title="Implant information", 
        nav_enabled=True, 
        user_data=request.user_data,
        implant_data=implant,
    )


@panel.route("/network", methods=["GET", "POST"])
@administrator_middleware
def network():
    mysql_interface = MysqlInterface(current_app.config)
    neo4j_interface = Neo4jInterface(current_app.config)

    implant_connections = []
    implants = mysql_interface.fetch_implant_data()

    if request.method == "GET":
        implant_connections = neo4j_interface.fetch_implant_connections()

    if request.method == "POST":    
        query = request.form.get("query")
    
        if not query:
            return response("Missing parameters"), 400
        
        implant_connections = neo4j_interface.search_implant_connections(query)

    return render_template("network.html",
        title="Nodes", 
        nav_enabled=True, 
        user_data=request.user_data,
        implant_data=implants,
        connections=implant_connections
    )


@panel.route("/server", methods=["GET"])
@localhost_middleware
@administrator_middleware
def server():
    server_url = request.args.get("server_url")
    
    if not server_url:
        return render_template("server.html",
            title="Builder", 
            nav_enabled=True, 
            user_data=request.user_data,
        )

    return send_file(build_implant(
        current_app.config["IMPLANT_SRC_PATH"], 
        current_app.config["IMPLANT_SRC_FILE"], 
        server_url
    ))