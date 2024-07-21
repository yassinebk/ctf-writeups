import time
from neo4j import GraphDatabase

class Neo4jInterface:
    def __init__(self, config):
        self.driver = None
        while self.driver is None:
            try:
                self.driver = GraphDatabase.driver(f"bolt://{config['NEO4J_HOST']}:7687", auth=(config["NEO4J_USER"], config["NEO4J_PASSWORD"]))
            except Exception as err:
                print("Failed to connect to neo4j")
                print("Retrying in 5 seconds")
                time.sleep(5)
        
        
    def __del__(self):
        self.close()

        
    def close(self):
        if self.driver is not None:
            self.driver.close()
        
        
    def query(self, query, parameters=None, db=None):
        assert self.driver is not None, "Driver not initialized"
        session = None
        response = None
        try: 
            session = self.driver.session(database=db) if db is not None else self.driver.session() 
            response = list(session.run(query, parameters))
        except Exception as e:
            print("Query failed:", e)
        finally: 
            if session is not None:
                session.close()
        return response


    def create_implant_connection(self, bot_id_1, bot_id_2):
        self.query("""
        MERGE (i1:Implant {identifier: $identifier1})
        MERGE (i2:Implant {identifier: $identifier2})
        MERGE (i1)-[:CONNECTED_TO]->(i2)
        /*Creates a connection between two implants*/
        """, {"identifier1": bot_id_1, "identifier2": bot_id_2})


    def fetch_implant_connections(self):
        implants = self.query("""
        MATCH (i1:Implant)-[:CONNECTED_TO]->(i2:Implant)
        RETURN i1.identifier AS identifier1, i2.identifier AS identifier2
        /*Fetches all implant connections*/
        """)

        connections = []
        for record in implants:
            connection = {
                "identifier1": record["identifier1"],
                "identifier2": record["identifier2"]
            }
            connections.append(connection)

        return connections


    def search_implant_connections(self, search_query=None):
        implants = self.query(f"""
        MATCH (i1:Implant)-[:CONNECTED_TO]->(i2:Implant)
        WHERE i1.identifier = '{search_query}' OR i2.identifier = '{search_query}'
        RETURN i1.identifier AS identifier1, i2.identifier AS identifier2
        /*Fetches all connections for a select implant*/
        """)

        connections = []
        for record in implants:
            connection = {
                "identifier1": record["identifier1"],
                "identifier2": record["identifier2"]
            }
            connections.append(connection)

        return connections