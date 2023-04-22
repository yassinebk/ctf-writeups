import requests 

resp="Can't fing CSRF token in response. Try again!"
while ("Can't fing CSRF token in response. Try again!" in resp):
    resp=requests.get("http://localhost:3001/level19.php").text 
    print(resp)
