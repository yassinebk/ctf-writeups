import random
import os
import time

ORIGINAL_TIMESTAMP="2023-01-14 23:03:32 +0000"
SECRET_OFFSET =  -67198624 # REDACTED
time_struct = time.strptime(ORIGINAL_TIMESTAMP, "%Y-%m-%d %H:%M:%S %z")
timestamp = time.mktime(time_struct)

file=open("file.txt","w")
file.write(str(time.time()))
file.close()
random.seed(round((timestamp + SECRET_OFFSET) * 1000))
os.environ["SECRET_KEY"] = "".join([hex(random.randint(0, 15)) for x in range(32)]).replace("0x", "")


