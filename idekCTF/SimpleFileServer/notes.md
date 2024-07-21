# notes

Simple File server

- Files will be uploaded in `/tmp/uploadraw/UUID_PATH.zip`
- Files will be unzipped to `/tmp/uploads/uuidpath` directory
  - Upload command is `unzip  src_archive -d dest_directory`

What we need to get actually:
    - `config.py`
    - `/self/proc/environ`

> Trying the thing below
Okay if we can leverage the `symlink` file upload exploit here we can just create a symlink to both these files

> Working ! 
> Finding for config.py
```
import random
import os
import time

SECRET_OFFSET = -67198624
random.seed(round((time.time() + SECRET_OFFSET) * 1000))
os.environ["SECRET_KEY"] = "".join([hex(random.randint(0, 15)) for x in range(32)]).replace("0x", "")
```


SECRET_KEY=e6e23027d0e587c137fb036a5b543ee4