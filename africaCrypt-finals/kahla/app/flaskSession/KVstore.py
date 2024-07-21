import os
class FilesystemStore():
    def __init__(self,root):
        # /tmp/sessions
        self.root=root
    
    def delete(self,sid):
        path=os.path.abspath(os.path.join(self.root,sid))
        os.unlink(path)

    def get(self,sid):
        path=os.path.abspath(os.path.join(self.root,sid))
        local_signed_cookie=Signer('REDACTED').sign(path).decode('utf-8')
        print(path)
        f=open(path,"rb").read()
        return f

    def keys(self):
        root = os.path.abspath(self.root)
        result = []
        for dp, dn, fn in os.walk(root):
            for f in fn:
                key = os.path.join(dp, f)[len(root) + 1:]
                result.append(key)
        return result

    def put(self,sid,cnt):
        if not os.path.isdir(self.root):
            try:
                os.makedirs(self.root)
            except OSError as e:
                if not os.path.isdir(self.root):
                    raise e
        target=os.path.join(self.root,sid)
        open(target, 'wb').write(cnt)