from flask import Flask,request,jsonify
import os,pymysql,jwt,json
from urllib3 import PoolManager
from urllib.parse import urlsplit

app= Flask(__name__)
USERNAME=os.getenv("USERNAME")
PASSWORD=os.getenv("PASSWORD")
app.config["SECRET_KEY"]=os.urandom(15).hex()

SECRET_KEY=os.getenv("Secret_key")


def get_db():
	mydb = pymysql.connect(
	host="authdb",
	user="kahla",
	password=os.getenv("mysql_pass"),
	database="kahladb"
	)
	return mydb.cursor(),mydb

def create_token(username):

	print("[-] Creating token from private key")
	private_key=open("keypair.pem").read() # We redacted the private key used in production to not ruin the task :-) This pair is just for local test not the same as in production!
	return jwt.encode({"username":username},private_key,algorithm="RS256",headers={"kid":"001122334455","jku":"http://localhost:5000/.well-known/jwks.json"})
	
# TO REVIEW, it might be vulnerable to JWT injection
def decode_token(token):
	print("[-] Decoding token")
	headers=jwt.get_unverified_header(token)
	url=headers["jku"]
	parsed=urlsplit(url)
	print(parsed.netloc,url)
	if parsed.netloc.lower() and parsed.netloc.lower()!="localhost:5000":
		raise Exception("Hacker Detected")
	else:
		http=PoolManager()
		jwks=json.loads(http.request("GET",url=url).data.decode('utf-8'))["keys"][0]
		signing_key = jwt.algorithms.RSAAlgorithm.from_jwk(jwks)
		data=jwt.decode(token,signing_key,algorithms=["RS256"])
		return data

@app.route("/register",methods=["POST"])
def register():

    print("[-] Registering in the identity server")
    if request.method == "POST":

        if request.get_json()["username"] and len(request.get_json()["username"])<50 and request.get_json()["password"]:
            cursor,mydb = get_db()
            query = "SELECT * FROM users WHERE username=%s"
            cursor.execute(query, request.get_json()["username"])
            result = cursor.fetchone()
            if result is not None:
                return jsonify({"error":"Username already exists"})
            try:
                mydb.commit()
                cursor.close()
                cursor,mydb=get_db()
                query="INSERT INTO users(id,username,password) VALUES(null,%s,%s)"
                values=(request.get_json()["username"],request.get_json()["password"])
                cursor.execute(query,values)
                mydb.commit()
                token=create_token(request.get_json()["username"])
                cursor.close()
                return jsonify({"token":token})
            except IndexError as e:
                return jsonify({"error":"Error happened while registering"})
        else:
            return jsonify({"Error":"Your username is really too much"})

@app.route("/.well-known/jwks.json",methods=["GET"])
def jwks():
	return jsonify({"keys":[{"alg":"RS256","kty":"RSA","use":"sig","n":"wpkuuqRWQzVpHbo1W-B43F8TTnCBJGhkbRDwS42duJ_f4GT81IqxqnvTLaA3VPP10z3NLkG5vDkjdawbFmxK19OGypKssM7Pe-L4Z6-NqkMgW47sKASmnRXOF7KoC3XiSmc5G9kbx81q5YAyVnj0UfaC_dtRhzFYRqiHbYyNWhzekewqlLbL28ljLqHseleH6Hn6STCzKPJDUCq2pwXIxiCnZET5yxpH_IDUZLMcgI0T5uXL9zaHv9oxtVNZVkXn30loOdbvhHyeBn_rtlSQS79oAAiO_lbV64SCpI4H-b6bbgNQyPQm1KeiIMjpBt5TJ-MhlgcJjyTZIfT3Vn2tXw","e":"AQAB","kid":"001122334455"}]})

@app.route("/get_secret_key",methods=["GET"])
def get_secret_key():

	if request.headers.get("Auth"):
		try:
			data=decode_token(request.headers.get("Auth"))
		except Exception as e:
			return jsonify({"Error":"This is bad "+str(e)})
		if data["username"]=="admin":
			return jsonify({"Secret_key":SECRET_KEY})
		else:
			return jsonify({"Error":"You are not authorized"})
	else:
 		return jsonify({"Error":"You are not authenticated"})

@app.route("/get_creds",methods=["GET"])
def get_creds():
	if request.headers.get("Auth") :
		try:
			data=decode_token(request.headers.get("Auth"))
		except Exception:
			return jsonify({"Error":"This is bad"})
		if data["username"]=="admin":
			return jsonify({"username":USERNAME,"password":PASSWORD})
		else:
			return jsonify({"Error":"You are not authorized"})
	else:
 		return jsonify({"Error":"You are not authenticated"})


@app.route("/get_token",methods=["POST"])
def get_token():
	username=request.get_json()["username"]
	password=request.get_json()["password"]
	if username is None or password is None:
		return jsonify({"Error":"No username or password are specified"})
	else:
		cursor,mydb = get_db()
		query = "SELECT * FROM users WHERE username=%s AND password=%s"
		cursor.execute(query, (username, password))
		result = cursor.fetchone()
		if result is not None:
			token=create_token(request.get_json()["username"])
			mydb.commit()
			cursor.close()
			return jsonify({"token":token})
		else:
			return jsonify({"Error":"Username or password is incorrect"})

if __name__=="__main__":
	app.run(host="0.0.0.0",debug=True)
