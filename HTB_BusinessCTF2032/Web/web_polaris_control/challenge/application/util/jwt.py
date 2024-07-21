import jwt, json, requests, base64, random
from cryptography.hazmat.primitives.asymmetric import rsa
from cryptography.hazmat.primitives import serialization
from cryptography.hazmat.backends import default_backend

def generate_keypair(file_path):
    private_key = rsa.generate_private_key(
        public_exponent=65537,
        key_size=2048
    )

    pem_private_key = private_key.private_bytes(
        encoding=serialization.Encoding.PEM,
        format=serialization.PrivateFormat.PKCS8,
        encryption_algorithm=serialization.NoEncryption(),
    ).decode()
    
    public_numbers = private_key.public_key().public_numbers()
    n_base64 = base64.urlsafe_b64encode(public_numbers.n.to_bytes((public_numbers.n.bit_length() + 7) // 8, "big")).rstrip(b"=").decode()
    e_base64 = base64.urlsafe_b64encode(public_numbers.e.to_bytes((public_numbers.e.bit_length() + 7) // 8, "big")).rstrip(b"=").decode()

    jwks = {
        "keys": [
            {
                "kty": "RSA",
                "alg": "RS256",
                "use": "sig",
                "kid": "1",
                "n": n_base64,
                "e": e_base64
            }
        ]
    }

    with open(file_path, "w") as file:
        json.dump(jwks, file)

    return pem_private_key


def create_jwt(whitelist, private_key, account_type, username):
    header = {
        "alg": "RS256",
        "jku": None
    }

    payload = {
        "username": username,
        "account_type": account_type
    }

    for host in whitelist:
        if host["host_url"].startswith("https://") or host["host_url"].startswith("http://"):
            header["jku"] = host["host_url"]
            break

    if not header["jku"]:
        return False

    token = jwt.encode(payload, private_key, algorithm="RS256", headers=header)

    return token


def verify_jwt(whitelist, token):
    decoded_headers = jwt.get_unverified_header(token)

    if "jku" not in decoded_headers:
        return False

    jku_url = decoded_headers["jku"]

    if jku_url not in [host["host_url"] for host in whitelist]:
        return False

    jwk_set = requests.get(jku_url).json()
    
    public_keys = {}
    for jwk in jwk_set["keys"]:
        kid = jwk["kid"]
        public_keys[kid] = jwt.algorithms.RSAAlgorithm.from_jwk(json.dumps(jwk))

    try:
        decoded_token = jwt.decode(token, key=random.choice(list(public_keys.values())), algorithms=["RS256"])
        return decoded_token
    except jwt.InvalidTokenError:
        return False