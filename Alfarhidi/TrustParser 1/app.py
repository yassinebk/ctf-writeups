# Template from BasedNote challenge :P

from flask import Flask, redirect, request, session, render_template, flash
import os
import sqlite3
import secrets
import time
import re

import subprocess


app = Flask(__name__)

class TrustyParser:
    def __init__(self):
        self.allowed_tags = ['h1', 'h2', 'h3', 'div', 'p']
        self.result = ""
        self.tag_buffer = ""
        self.is_tag = False
        self.is_comment = False
        self.current_tag = ""

    def parse(self, html):
        for char in html:
            if self.is_comment:
                self.tag_buffer += char
                if self.tag_buffer.endswith("-->"):
                    self.result += self.tag_buffer  
                    self.is_comment = False
                    self.tag_buffer = ""  
                continue

            if char == '<':
                self.is_tag = True
                self.tag_buffer = char
            elif char == '>':
                self.tag_buffer += char
                self.is_tag = False

                if self.tag_buffer.startswith("<!--"):
                    self.is_comment = True
                    self.tag_buffer += char  
                elif self.is_allowed_tag(self.tag_buffer):
                    self.result += self.tag_buffer
                    if self.tag_buffer.startswith("</"):
                        self.current_tag = ""
                    else:
                        self.current_tag = self.tag_buffer.strip("<>/")
                self.tag_buffer = ""
            else:
                if self.is_tag:
                    self.tag_buffer += char
                elif self.current_tag:
                    self.result += char

        return self.result

    def is_allowed_tag(self, tag):
        if tag.startswith("</"):
            tag_name = tag[2:-1]
        else:
            tag_name = tag[1:].split(" ", 1)[0].rstrip(">")

        return tag_name in self.allowed_tags or '/' + tag_name in self.allowed_tags


base_url = os.getenv("BASE_URL", "http://127.0.0.1:5000")

admin_password = "admin"
app.secret_key = secrets.token_bytes(32)

def get_cursor():
    db = sqlite3.connect("/tmp/app.db")
    return db, db.cursor()

def init_db():

    db, cur = get_cursor()

    cur.execute("""
    CREATE TABLE IF NOT EXISTS accounts (
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        username TEXT NOT NULL UNIQUE,
        password TEXT NOT NULL,
        admin INTEGER)
    """)

    cur.execute("INSERT INTO accounts (username, password, admin) VALUES ('admin', ?, 1)", [admin_password])

    cur.execute("""
    CREATE TABLE IF NOT EXISTS notes (
        id TEXT NOT NULL UNIQUE,
        text TEXT NOT NULL,
        creator TEXT,
        expires INTEGER DEFAULT 0)
    """)

    db.commit()
    db.close()

if not os.path.isfile("/tmp/app.db"):
    init_db()

def signup_db(username, password):

    db, cursor = get_cursor()
    try:
        cursor.execute("INSERT INTO accounts (username, password, admin) VALUES (?, ?, 0)", [username, password])
    except sqlite3.IntegrityError:
        return False
    db.commit()
    return True

def login_db(username, password):

    db, cursor = get_cursor()
    cursor.execute("SELECT * FROM accounts WHERE username = ? AND password = ?", [username, password])
    result = cursor.fetchone()
    if not result:
        return None
    return {"id": result[0], "username": result[1], "admin": result[3] == 1}

def get_notes(username):

    db, cursor = get_cursor()
    cursor.execute("SELECT id, text, expires FROM notes WHERE creator = ?", [username])
    rows = cursor.fetchall()

    for i, row in enumerate(rows):
        if row[2] > 0 and row[2] < int(time.time()):
            cursor.execute("DELETE FROM notes WHERE id = ?", [row[0]])
            db.commit()
            rows[i] = None

    return [row for row in rows if row is not None]

def get_note(msg_id):

    db, cursor = get_cursor()
    cursor.execute("SELECT text, creator, expires FROM notes WHERE id = ?", [msg_id])
    row = cursor.fetchone()
    if row is None or (row[2] > 0 and row[2] < int(time.time())):
        cursor.execute("DELETE FROM notes WHERE id = ?", [msg_id])
        db.commit()
        return None
    return {"text": row[0], "creator": row[1]}


def create_note(username, raw_text):

    db, cursor = get_cursor()
    id_val = secrets.token_hex(20)
    expires = int(time.time()) + 60 * 60
    parser = TrustyParser()
    sanitized_text = parser.parse(raw_text)
    cursor.execute("INSERT INTO notes (id, text, creator, expires) VALUES (?, ?, ?, ?)", [id_val, sanitized_text, username, expires])
    db.commit()
    return id_val




@app.route("/")
def mainRoute():
    if not session:
        return redirect("/login")
    notes = get_notes(session["username"])
    return render_template("index.html", notes=notes, logged_out=False)


@app.route("/login", methods = ["GET", "POST"])
def login():
    logged_out = session.get("username", None) is None
    if request.method == "GET":
        return render_template("login.html", logged_out=logged_out)
    elif request.method == "POST":
        password = request.form["password"]
        username = request.form["username"]
        user = login_db(username, password)
        if user:
            session["id"] = user["id"]
            session["username"] = user["username"]
            session["admin"] = user["admin"]
            return redirect("/")
        flash("Invalid username or password")
        return redirect("/login")


@app.route("/signup", methods=["GET", "POST"])
def signup():
    logged_out = session.get("username", None) is None
    if request.method == "GET":
        return render_template("signup.html", logged_out=logged_out)
    if request.method == "POST":
        username = request.form["username"]
        password = request.form["password"]
        if signup_db(username, password):
            return redirect("/login")
        flash("Username already taken")
        return redirect("/signup")


@app.route("/note/<note_id>")
def getnote(note_id):
    logged_out = session.get("username", None) is None
    note = get_note(note_id)
    return render_template("view_note.html", note=note, logged_out=logged_out)


@app.route("/create", methods=["GET", "POST"])
def create():
    if not session:
        flash("Please log in")
        return redirect("/login")
    if request.method == "GET":
        return render_template("create.html", logged_out=False)
    elif request.method == "POST":
        if not request.form["text"]:
            return "Missing text"
        text = request.form["text"]
        note_id = create_note(session["username"], text)
        
        return redirect(f"/note/{note_id}")

@app.route("/report", methods=["GET", "POST"])
def report():
    if not session:
        flash("Please log in")
        return redirect("/login")
    if request.method == "GET":
        return render_template("report.html", logged_out=False)

    value = request.form.get("id")

    if not value or not re.match(r"^[a-f0-9]{40}$", value):
        flash("invalid value!")
        return render_template("report.html", logged_out=False)
    subprocess.Popen(["node", "adminbot.js", base_url, admin_password, value], shell=False)
    flash("Checking...")
    return render_template("report.html", logged_out=False)
