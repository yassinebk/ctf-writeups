from flask import Flask, redirect

app = Flask(__name__)

@app.route('/')
def redirect_to_localhost():
    return redirect('http://localhost:80')

if __name__ == '__main__':
    app.run(port=3000,debug=True)

