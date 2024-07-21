import requests 
import hashlib



chars="abcdefghijklmnopqrstuvwxyz0123456789"
password=""

url="http://167.172.50.208:30324/api/login"

# while True:
#     resp=requests.post(url, json={"username": "admin", "password": password})
#     print(resp.text)
    

md5Hash = hashlib.md5(b"meow")

print(md5Hash.hexdigest())

def passwordVerify(hashPassword, password):
    md5Hash = hashlib.md5(password.encode())

    if md5Hash.hexdigest() == hashPassword: return True
    else: return False


print(passwordVerify(md5Hash.hexdigest(), "meow"))