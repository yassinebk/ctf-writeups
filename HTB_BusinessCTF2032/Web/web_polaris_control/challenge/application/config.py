import os
from dotenv import load_dotenv
from application.util.general import generate

load_dotenv()

class Config(object):
    VERSION = "4.5.0"
    IMPLANT_AGENT_NAME = "Polaris Control"
    IMPLANT_SRC_PATH = "/app/implant"
    IMPLANT_SRC_FILE = "polaris-agent.go"
    BOT_AGENT_NAME = "Polaris Browser"
    SECRET_KEY = generate(50)
    JWKS_PATH = "/tmp/jwks.json"
    UPLOAD_FOLDER = "/app/application/static/uploads"
    ALLOWED_EXTENSIONS = {"png"}
    MYSQL_HOST = os.getenv("MYSQL_HOST")
    MYSQL_DATABASE = os.getenv("MYSQL_DATABASE")
    MYSQL_USER = os.getenv("MYSQL_USER")
    MYSQL_PASSWORD = os.getenv("MYSQL_PASSWORD")
    NEO4J_HOST = os.getenv("NEO4J_HOST")
    NEO4J_DATABASE = os.getenv("NEO4J_DATABASE")
    NEO4J_USER = os.getenv("NEO4J_USER")
    NEO4J_PASSWORD = os.getenv("NEO4J_PASSWORD")
    MODERATOR_USER = os.getenv("MODERATOR_USER")
    MODERATOR_PASSWORD = os.getenv("MODERATOR_PASSWORD")


class ProductionConfig(Config):
    pass


class DevelopmentConfig(Config):
    DEBUG = True


class TestingConfig(Config):
    TESTING = True