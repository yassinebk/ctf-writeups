from pwn import *

p = process("./license")
context.log_level = 'debug'

p.recvuntil(b'challenge?! (y/n)')
p.send(b'y')
p.recvuntil(' ')
p.send('PasswordNumeroUno')
p.recvuntil(' ')
p.send(b'P4ssw0rdTw0')
p.recvuntil(' ')
p.send('ThirdAndFinal!!!')
p.interactive()
