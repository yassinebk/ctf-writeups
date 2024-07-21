import hashlib



def bruteforce():

    for i in range(40,255):
        for i1 in range(1,255):
            for i2 in range(1,255):
                for i3 in range(1,255):
                    x=f"{i}.{i1}.{i2}.{i3}"
                    m = hashlib.sha256()
                    trying=x.encode("utf-8")+b"1"
                    m.update(trying)
                    y=m.hexdigest()
                    if y[0:7]=="32cae2":
                        print(x)
                        return 

bruteforce()






