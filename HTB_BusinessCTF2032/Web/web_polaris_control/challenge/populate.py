import random
from application.util.mysql import MysqlInterface
from application.util.neo4j import Neo4jInterface
from application.main import app
from application.util.general import macos_hostnames, linux_hostnames, windows_hostnames, platforms, screenshots, regions, avs, arches, permission_types, generate

class Populator:
	def __init__(self, mysql_interface, neo4j_interface):
		self.mysql_interface = mysql_interface
		self.neo4j_interface = neo4j_interface
		
		self.moderator_user = app.config["MODERATOR_USER"]
		self.moderator_password = app.config["MODERATOR_PASSWORD"]

		self.bot_count = 200
		self.max_connections = 3

		self.platform_to_hostname = {
			platforms[0]: macos_hostnames,
			platforms[1]: linux_hostnames,
			platforms[2]: windows_hostnames
		}

		self.platform_to_screenshot = {
			platforms[0]: screenshots[0],
			platforms[1]: screenshots[1],
			platforms[2]: screenshots[2]
		}
	

	def generate_bot(self):
		platform = random.choice(platforms)
		
		return {
			"ip_address": f"{random.randint(0, 255)}:{random.randint(0, 255)}:{random.randint(0, 255)}:{random.randint(0, 255)}",
			"region": random.choice(regions),
			"version": f"{random.randint(1, 3)}.{random.randint(1, 12)}.{random.randint(1, 5)}",
			"antivirus": random.choice(avs),
			"arch": random.choice(arches),
			"platform": platform,
			"hostname": random.choice(self.platform_to_hostname[platform]),
			"rights": random.choice(permission_types),
			"image_url": self.platform_to_screenshot[platform]
		}
				
			
	def populate_init(self):
		self.mysql_interface.query("""
		CREATE TABLE IF NOT EXISTS implants (
			identifier varchar(255) NOT NULL,
			token varchar(255) NOT NULL,
			ip_address varchar(255) NOT NULL,
			region varchar(255),
			version varchar(255),
			antivirus varchar(255),
			arch varchar(255),
			platform varchar(255),
			hostname varchar(2555),
			rights varchar(255),
			image_url varchar(255),
			last_login DATETIME NOT NULL,
			installation_date DATETIME NOT NULL
		);
		""")
		
		self.mysql_interface.query("""
		CREATE TABLE IF NOT EXISTS users (
			identifier int NOT NULL AUTO_INCREMENT,
			username varchar(255) NOT NULL,
			password varchar(255) NOT NULL,
			account_type varchar(255) NOT NULL,
			primary key (identifier)
		);
		""")
		
		self.mysql_interface.query("""
		CREATE TABLE IF NOT EXISTS trusted_external_token_providers (
			identifier int NOT NULL AUTO_INCREMENT,
			host_url varchar(255) NOT NULL,
			primary key (identifier)
		);
		""")
		
		self.mysql_interface.register_user(self.moderator_user, self.moderator_password, "moderator")

		for i in range(self.bot_count):
			new_bot = self.generate_bot()
			self.mysql_interface.register_implant(
				new_bot["ip_address"], 
				new_bot["region"],
				new_bot["version"],
				new_bot["antivirus"],
				new_bot["arch"],
				new_bot["platform"],
				new_bot["hostname"],
				new_bot["rights"],
				new_bot["image_url"],
				True
			)
			
		self.mysql_interface.register_token_provider("http://127.0.0.1:1337/provider/jwks.json")

		new_bots = self.mysql_interface.fetch_implant_data()
		random.shuffle(new_bots)

		for i in range(len(new_bots)):			
			for j in range(self.max_connections):
				implant1 = random.choice(new_bots)
				implant2 = random.choice(new_bots)
				self.neo4j_interface.create_implant_connection(implant1["identifier"], implant2["identifier"])


if __name__ == "__main__":
	mysql_interface = MysqlInterface(app.config)
	neo4j_interface = Neo4jInterface(app.config)

	populator = Populator(mysql_interface, neo4j_interface)
	populator.populate_init()
