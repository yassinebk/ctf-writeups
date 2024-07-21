import hashlib
def getIpBack():
    color1 = "32cae2"
    color2 = "92e1e8"
    tries = 0
    potentialIps = []
    for a in range(10, 224): # skip 0.0.0.0/8 reserved,
        # 224.0.0.0/4 multicast, 240.0.0.0/4 reserved
        for b in range(256):
            for c in range(256):
                for d in range(1, 255): # omit network x.x.x.0 and broadcast x.x.x.255
                    ipString = f"{a}.{b}.{c}.{d}"
                    sha256 = hashlib.new('sha256')
                    sha256.update(str.encode(ipString + str(1)))
                    colorSlice = slice(0, 6)
                    if(sha256.hexdigest()[colorSlice] == color1):
                        potentialIps.append(ipString)
                        hash2 = hashlib.new('sha256')
                        hash2.update(str.encode(ipString + str(2)))
                        if (hash2.hexdigest()[colorSlice] == color2):
                            return ipString
                    tries += 1
                    if (tries % 1000000 == 0):
                        print(tries)
                        print(ipString)
                        print(len(potentialIps))
                        print(potentialIps)
    print(potentialIps)
    return "Done"
print(getIpBack())
