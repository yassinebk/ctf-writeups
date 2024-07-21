from flask import Flask, request, jsonify, make_response
from flask_sqlalchemy import SQLAlchemy
from werkzeug.security import generate_password_hash, check_password_hash
import uuid
import jwt
import datetime
from functools import wraps
import os
import time
import re
import argparse
import tempfile
import PyPDF2
import string
import random
import datetime
from subprocess import PIPE, Popen
from reportlab.pdfgen import canvas
from itsdangerous import Signer, base64_encode, base64_decode
from flask import Flask, request, render_template, make_response, g, Response
from flask.views import MethodView
from urllib.parse import urlparse
from urllib.parse import urljoin	
import shutil
import utilss
import os
import mimetypes

app = Flask(__name__.split('.')[0])
app.config.from_object(__name__)

BUFFER_SIZE = 128000

URI_BEGINNING_PATH = {
    'authorization': '/loginss/',
    'weeb': '/kkk/wtf/',
}

def generate_key():
    app.secret_key = os.urandom(24)


def generate_cookie_info(origin=None):
    if not origin:
        origin = request.headers.get('Origin')
    useragent = request.headers.get('User-Agent')
    return '%s %s' % (str(origin), str(useragent))

def verify_cookie(cookey):

    is_correct = False

    cookie_value = request.cookies.get(cookey)
    if cookie_value:
        s = Signer(app.secret_key)
        expected_cookie_content = \
            generate_cookie_info(base64_decode(cookey))
        expected_cookie_content = s.get_signature(expected_cookie_content)

        if expected_cookie_content == cookie_value:
            is_correct = True

    return is_correct

def is_authorized():

    origin = request.headers.get('Origin')
    if origin is None: 
        return True
    return verify_cookie(base64_encode(origin))


@app.before_request
def before_request():

        headers = {}
        headers['Access-Control-Max-Age'] = '3600'
        headers['Access-Control-Allow-Credentials'] = 'true'
        headers['Access-Control-Allow-Headers'] = \
            'Origin, Accept, Accept-Encoding, Content-Length, ' + \
            'Content-Type, Authorization, Depth, If-Modified-Since, '+ \
            'If-None-Match'
        headers['Access-Control-Expose-Headers'] = \
            'Content-Type, Last-Modified, WWW-Authenticate'
        origin = request.headers.get('Origin')
        headers['Access-Control-Allow-Origin'] = origin

        specific_header = request.headers.get('Access-Control-Request-Headers')

        if is_authorized():
            status_code = 200

        elif request.method == 'OPTIONS' and specific_header:
            headers['Access-Control-Request-Headers'] = specific_header
            headers['Access-Control-Allow-Methods'] = ', '.join(['GET', 'PUT', 'DELETE','COPY', 'PROPFIND','MOVE', 'OPTIONS'])
            response = make_response('', 200, headers)
            return response

        g.status = 200
        g.headers = headers
        #response = make_response('', 200, headers)
        #return response

class weeb(MethodView):
    methods = ['GET', 'PUT', 'PROPFIND', 'DELETE','COPY', 'MOVE', 'OPTIONS']

    def __init__(self):
        self.baseuri = URI_BEGINNING_PATH['weeb']

    def get_body(self):

        request_data = request.data

        try:
            length = int(request.headers.get('Content-length'))
        except ValueError:
            length = 0

        if not request_data and length:
            try:
                request_data = request.form.items()[0][0]
            except IndexError:
                request_data = None
        return request_data

    def get(self, pathname):

        status = g.status
        headers = g.headers
        status = 501

        return make_response('', status, headers)

    def put(self, pathname):
        status = g.status
        headers = g.headers
        status = 501

        return make_response('', status, headers)

    def propfind(self, pathname):
        status = g.status
        headers = g.headers

        pf = utilss.PropfindProcessor(
            URI_BEGINNING_PATH['weeb'] + pathname,
            app.fs_handler,
            request.headers.get('Depth', 'infinity'),
            self.get_body())
        try:
        	return pf.create_response().decode()
        except:
        	response = 'not found'

        #return response


    def delete(self, pathname):
        status = g.status
        headers = g.headers
        status = 501

        return make_response('', status, headers)

    def copy(self, pathname):
        status = g.status
        headers = g.headers
        status = 501

        return make_response('', status, headers)

    def move(self, pathname):
        status = g.status
        headers = g.headers
        status = 501

        return make_response('', status, headers)

    def options(self, pathname):

        return make_response('', g.status, g.headers)

weeb_view = weeb.as_view('dav')
app.add_url_rule(
    '/kkk/wtf/',
    defaults={'pathname': ''},
    view_func=weeb_view
)

app.add_url_rule(
    URI_BEGINNING_PATH['weeb'] + '<path:pathname>',
    view_func=weeb_view
)
ALLOWED_EXTENSIONS = {'pdf', 'png', 'jpg', 'jpeg'}
UPLOAD_FOLDER = './static/'
app.config['UPLOAD_FOLDER'] = UPLOAD_FOLDER

app.config['SECRET_KEY']='censored'
app.config['SQLALCHEMY_DATABASE_URI']='sqlite://///app/library.db'
app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = True

reg=re.compile('[0-9a_f]{32}\Z', re.I)

db = SQLAlchemy(app)
class Users(db.Model):
     id = db.Column(db.Integer, primary_key=True)
     public_id = db.Column(db.Integer)
     name = db.Column(db.String(50))
     password = db.Column(db.String(50))
     admin = db.Column(db.Boolean)

class Tokens(db.Model):
     id = db.Column(db.Integer, primary_key=True)
     token = db.Column(db.String(32))

db.create_all()

def check_valid_token(intoken):
  if bool(reg.match(intoken)):
    rez=Tokens.query.filter(Tokens.token.ilike(intoken)).first()
    print(rez)
    if rez:
      return True
    else:
      return False
  else:
    return False

def allowed_file(filename):
    return '.' in filename and \
           filename.rsplit('.', 1)[1].lower() in ALLOWED_EXTENSIONS

def get_random_string(length):
    letters = string.ascii_lowercase
    result_str = ''.join(random.choice(letters) for i in range(length))
    return result_str

def _get_tmp_filename(suffix=".pdf"):
    with tempfile.NamedTemporaryFile(suffix=".pdf") as fh:
        return fh.name

coords='1x100x100x150x40'
path='./static/'
date=False
output=None
pdf='pf.pdf'
signature='signe.jpg'

def sign_pdf(inputfile):
    infile=path+inputfile
    pdf=path+get_random_string(8)+'.pdf'
    cmd='unoconv -v -o '+pdf+' -f pdf '+infile
    p = Popen(cmd, shell=True, stdout=PIPE, stderr=PIPE)
    stdout, stderr = p.communicate()
    page_num, x1, y1, width, height = [int(a) for a in coords.split("x")]
    page_num -= 1

    output_filename = output or "{}_signed{}".format(
        *os.path.splitext(pdf)
    )

    pdf_fh = open(pdf, 'rb')
    sig_tmp_fh = None

    pdf = PyPDF2.PdfFileReader(pdf_fh)
    writer = PyPDF2.PdfFileWriter()
    sig_tmp_filename = None

    for i in range(0, pdf.getNumPages()):
        page = pdf.getPage(i)

        if i == page_num:
            sig_tmp_filename = _get_tmp_filename()
            c = canvas.Canvas(sig_tmp_filename, pagesize=page.cropBox)
            c.drawImage(signature, x1, y1, width, height, mask='auto')
            if date:
                c.drawString(x1 + width, y1, datetime.datetime.now().strftime("%Y-%m-%d"))
            c.showPage()
            c.save()

            sig_tmp_fh = open(sig_tmp_filename, 'rb')
            sig_tmp_pdf = PyPDF2.PdfFileReader(sig_tmp_fh)
            sig_page = sig_tmp_pdf.getPage(0)
            sig_page.mediaBox = page.mediaBox
            page.mergePage(sig_page)

        writer.addPage(page)

    with open(output_filename, 'wb') as fh:
        writer.write(fh)

    for handle in [pdf_fh, sig_tmp_fh]:
        if handle:
            handle.close()
    if sig_tmp_filename:
        os.remove(sig_tmp_filename)
    return output_filename

def token_required(f):
   @wraps(f)
   def decorator(*args, **kwargs):
      token = None
      if 'x-access-tokens' in request.headers:
         token = request.headers['x-access-tokens']

      if not token:
         return jsonify({'message': 'a valid token is missing'})

      try:
         data = jwt.decode(token, app.config['SECRET_KEY'])
         current_user = Users.query.filter_by(public_id=data['public_id']).first()
      except:
         return jsonify({'message': 'token is invalid'})

      return f(current_user, *args, **kwargs)
   return decorator

@app.route(URI_BEGINNING_PATH['authorization'], methods=['GET', 'POST'])
def authorize():
    origin = request.args.get('origin')

    if request.method == 'POST':
        response = make_response()
        if request.form.get('continue') != 'true':
            generate_key()
        s = Signer(app.secret_key)
        if s.get_signature(origin) == request.args.get('sig'):
            key = base64_encode(str(origin))
            back = request.args.get('back_url')

            info = generate_cookie_info(origin=origin)
            response.set_cookie(key, value=s.get_signature(info), max_age=None,
                expires=None, path='/', domain=None, secure=True, httponly=True)
        else:
            return 'Something went wrong...'

        response.status = '301' # 
        response.headers['Location'] = '/' if not back else back

    else:
        response = make_response(render_template('authorization_page.html',
                                 cookie_list=[ base64_decode(cookey)
                                               for cookey in
                                               request.cookies.keys()
                                               if verify_cookie(cookey) ],
                                 origin=request.args.get('origin'),
                                 back_url=request.args.get('back_url')))
    return response

@app.route('/', methods=['GET', 'POST'])
def home():
  return jsonify({'message': 'Welcome \O// '})

@app.route('/register', methods=['GET', 'POST'])
def signup_user():  
 data = request.get_json()  

 hashed_password = generate_password_hash(data['password'], method='sha256')
 
 new_user = Users(public_id=str(uuid.uuid4()), name=data['name'], password=hashed_password, admin=False) 
 db.session.add(new_user)  
 db.session.commit()    

 return jsonify({'message': 'registered successfully'})
@app.route('/login', methods=['GET', 'POST'])  
def login_user(): 
  auth = request.authorization   

  if not auth or not auth.username or not auth.password:  
     return make_response('could not verify', 401, {'WWW.Authentication': 'Basic realm: "login required"'})    

  user = Users.query.filter_by(name=auth.username).first()   
     
  if check_password_hash(user.password, auth.password):  
     token = jwt.encode({'public_id': user.public_id, 'exp' : datetime.datetime.utcnow() + datetime.timedelta(minutes=30)}, app.config['SECRET_KEY'])  
     return jsonify({'token' : token.decode('UTF-8')}) 
  return make_response('could not verify',  401, {'WWW.Authentication': 'Basic realm: "login required"'})

@app.route('/users', methods=['GET'])
@token_required
def get_all_users(current_user):
   if current_user.admin:
       users = Users.query.all() 
       result = []   

       for user in users:   
           user_data = {}   
           user_data['public_id'] = user.public_id
           user_data['name'] = user.name 
           user_data['admin'] = user.admin 
           
           result.append(user_data)   
   else:
      return jsonify({'error': "not admin"})

   return jsonify({'users': result})
   
@app.route('/role_user', methods=['GET'])
@token_required
def role(current_user):
    if not(current_user.admin):
        return jsonify({'role': "ROLE_USER"})
    else:
       return jsonify({'role': "ROLE_ADMIN"})

@app.route('/share_file', methods=['GET', 'POST'])
@token_required
def upload_file(current_user):
    if request.method == 'POST':
        if 'file' not in request.files:
            return jsonify({'error': "upload file..."})
        file = request.files['file']
        if file.filename == '':
            return jsonify({'error': "no filename !!"})
        if file and allowed_file(file.filename):
            ext=file.filename.rsplit('.', 1)[1]
            relfile=get_random_string(8)+'.'+ext
            file.save(os.path.join(app.config['UPLOAD_FOLDER'], relfile))
            if not(request.args.get('sign_token')) or (not(check_valid_token(request.args.get('sign_token')))):
              return jsonify({'file': "/static/censored"})
            else:
              signed_file=sign_pdf(relfile)
              return jsonify({'signed_file':"/static/censored"})
        else:
          return jsonify({'error': "go away!!"})


@app.route('/sign_tokens', methods=['GET'])
@token_required
def sign_tokens(current_user):
  if current_user.admin:
   
       tokens = Tokens.query.all() 

       result = []   

       for tok in tokens:   
           token_data = {}   
           token_data['id'] = tok.id
           token_data['token'] = tok.token 
           
           result.append(token_data)
  else:
      return jsonify({'error': "not admin"})

  return jsonify({'users': result})

@app.route('/add_tokens', methods=['PUT'])
@token_required
def add_tok(current_user):
   if current_user.admin:
    try:
      data = request.get_json() 
      new_token = Tokens(token=data['token']) 
      db.session.add(new_token)  
      db.session.commit()
    except:
      return jsonify({'error': "//"})
    return jsonify({'message': "Added successfully"})
   else:
    return jsonify({'error': "not admin"})

if __name__ == '__main__':

    app.fs_path = '/app/'
    app.fs_handler = utilss.FilesystemHandler(app.fs_path,
                                             URI_BEGINNING_PATH['weeb'])

    generate_key()
    app.run(host="0.0.0.0",debug=True)
