import requests
import hashlib
import itertools


data="fe5aef87196862910264695b0a"
difficulty=6
target="399d85306e8f6deae6c952bc8c6ac886"

def brute_force(charset, length):
    return (''.join(candidate)
        for candidate in itertools.chain.from_iterable(itertools.product(charset, repeat=i)
        for i in range(length,length+1)))


attempts=brute_force("abcdefghijklmnopqrstuvwxyz0123456789",difficulty)
for nonce in attempts:
    my_data= f"{data}{nonce}".encode('utf-8')
    print(my_data)
    digest= hashlib.md5(my_data).hexdigest()


    print("[+] Result for try :", nonce,len(nonce))
    if str(digest)==target:
        print("[-] Solution: ", digest)






