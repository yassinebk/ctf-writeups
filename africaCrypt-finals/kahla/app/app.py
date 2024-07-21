from flask import Flask,request,render_template,redirect,session
import os,pymysql,jwt,requests
from flaskSession import KVSessionExtension
from flaskSession.KVstore import FilesystemStore
from flask_wtf.csrf import CSRFProtect

app= Flask(__name__)
CSRFProtect(app)
store=FilesystemStore("/tmp/sessions")

KVSessionExtension(store,app)

PASSWORD=os.getenv("PASSWORD")

def get_db():
	mydb = pymysql.connect(
	host="localhost",
	user="kahla",
	password=os.getenv("mysql_pass"),
	database="kahladb"
	)
	return mydb.cursor(),mydb

def get_secret():
	print("[+] Getting secret key")
	# Get token then secret_key
	a=requests.post("http://auth:5000/get_token",json={"username":"admin","password":PASSWORD})
	token=a.json()["token"]
	# Get secret key
	r=requests.get("http://auth:5000/get_secret_key",headers={"Auth":token})
	return r.json()["Secret_key"]

# Get token then creds
def get_creds():
	print("[+] Getting creds")
	a=requests.post("http://auth:5000/get_token",json={"username":"admin","password":PASSWORD})
	token=a.json()["token"]
	r=requests.get("http://auth:5000/get_creds",headers={"Auth":token})
	return r.json()

app.secret_key=get_secret()

@app.route("/",methods=["GET"])
def index():
	return render_template("index.html")

@app.route("/register",methods=["GET","POST"])
def register():
	if request.method == "POST":
		if session.get("username") is not None:
			return redirect("/home")
		if request.form.get("username") and len(request.form.get("username"))<50 and request.form.get("password"):
			cursor,mydb = get_db()
			query = "SELECT * FROM users WHERE username=%s"
			cursor.execute(query, request.form.get("username"))
			result = cursor.fetchone()
			if result is not None:
				return render_template("register.html",error="Username already exists")
			try:
				mydb.commit()
				cursor.close()
				cursor,mydb=get_db()
				query="INSERT INTO users(id,username,password) VALUES(null,%s,%s)"
				values=(request.form.get("username"),request.form.get("password"))
				cursor.execute(query,values)
				mydb.commit()
				session["username"]=request.form.get("username")

				print("[+] Modified the session")
				cursor.close()
				return redirect("/home")
			except IndexError as e:
				return render_template("register.html",error="Error happened while registering")
		else:
			return redirect("/login",302)
	else:
		if session.get("username") is not None:
			return redirect("/home")
		else:
			return render_template("register.html",error="")

@app.route("/home",methods=["GET"])
def home():
	if session.get("username") is not None :
		return render_template("home.html",username=session["username"])
	else:
 		return redirect("/login")

# USELESS ROUTE
@app.route("/ninja_scrolls")
def ninja_scrolls():
	creds=get_creds()
	if session.get("username") is not None:
		if session["username"]==creds["username"]:
				cursor,mydb = get_db()
				query = "SELECT * FROM ninja_scrolls WHERE title LIKE '%{search}%'"
				cursor.execute(query.format(search=request.args.get("search")))
				result = cursor.fetchall()
				if result is not None:
					cursor.close()
					return render_template("scrolls.html",scrolls=result)
				else:
					cursor,mydb = get_db()
					query = "SELECT * FROM ninja_scrolls"
					cursor.execute(query)
					result = cursor.fetchall()
					cursor.close()
					return render_template("scrolls.html",scrolls=result)
		else:
			return redirect("/home")
	else:
		return redirect("/home")


@app.route("/login",methods=["GET","POST"])
def login():
	if request.method=="GET":
		if session.get("username") is not None:
			print("[+] Retrieving value from the session in login")
			return redirect("/home")
		else:
			return render_template("login.html",error="")
	else:
		username=request.values.get("username")
		password=request.values.get("password")
		if username is None or password is None:
			return render_template("login.html",error="")
		else:
			cursor,mydb = get_db()
			query = "SELECT * FROM users WHERE username=%s AND password=%s"
			cursor.execute(query, (username, password))
			result = cursor.fetchone()
			if result is not None:
				session["username"]=request.values.get("username")
				mydb.commit()
				cursor.close()
				return redirect("/home")
			else:
				return render_template("login.html",error="Username or password is incorrect")

@app.route("/logout",methods=["GET"])
def logout():
	print("[+] Logging out")
	session["username"]=None	
	return redirect("/login")


if __name__=="__main__":
	app.run(host="0.0.0.0",debug=True)
