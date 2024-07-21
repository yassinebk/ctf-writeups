from pwn import *

p = process(b"./token_validation")

context.log_level = "debug"

p.send(b"111111\n")
p.send(b"111111\n")
x = p.recvline()

print("Heeere")
print(x)
p.send("12\n")

p.recvuntil(b"[6 digits]: ")

p.sendline(b"32")

result = p.recvline().decode().strip()

print("Token generation result:", result)

p.close()
