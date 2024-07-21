from flask import Flask, render_template, request, redirect, url_for, flash,session
import configparser ,os ,secrets, string

app = Flask(__name__)

app.secret_key = os.urandom(16) 

def read(file_path):
    config = {}
    current_section = None
    with open(file_path, 'r') as f:
        for line in f:
            line = line.strip()
            if line.startswith('[') and line.endswith(']'):
                current_section = line[1:-1]
                config[current_section] = {}
            elif '=' in line and current_section is not None:
                key, value = line.split('=', 1)
                key = key.strip()
                value = value.strip().strip('"')
                config[current_section][key] = value

    return config


def register(username, password,role='guest',access='guest', file_path='users.ini'):
    config = configparser.ConfigParser()
    config.read(file_path)
    
    if username in config.sections():
        return False
    
    config[username] = {
        'username':username,
        'password': password,
        'role': role,
        'access': access
    }
    
    with open(file_path, 'w') as configfile:
        config.write(configfile)
    
    return True  


@app.route('/login', methods=['GET', 'POST'])
def login():
    if request.method == 'POST':
        username = request.form['username']
        password = request.form['password']
        user_data = read('users.ini')
        if username in user_data and user_data[username]['password'] == password:
            session['username'] = username
            session['role'] = user_data[username]['role']
            session['access'] = user_data[username]['access']
       
            return redirect(url_for('dashboard'))
        else:
            return 'Invalid credentials, please try again.'
    return render_template('login.html')

@app.route('/register', methods=['GET', 'POST'])
def register_route():
    if request.method == 'POST':
        username = request.form['username']
        password = request.form['password']
        
        registration_success = register(username,password,'guest','guest','users.ini')
        
        if registration_success:
            flash('Registration successful!')
            return redirect(url_for('login'))
        else:
            return 'Registration failed. User exists.'
    
    return render_template('register.html')

@app.route('/config')
def configx():
    config = configparser.ConfigParser()
    config.read("users.ini")
    return str(config)

@app.route('/dashboard')
def dashboard():
    if 'username' not in session:
        flash('Please log in to access the dashboard.')
        return redirect(url_for('login'))

    if session['username'] == 'admin' and session['role'] == 'admin' and session['access'] == 'flag':
        flag = 'HACKFEST{fake}'
    else:
        flag = None
    
    return render_template('dashboard.html', flag=flag)


@app.route('/')
def index():
    return redirect(url_for('login'))

register('admin',''.join(secrets.choice(string.ascii_letters + string.digits) for _ in range(25)),'admin','all','users.ini')
