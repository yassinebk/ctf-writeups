```
/oauth/authorize?response_type=code&client_id=dinomarket_app&scope=user_info%3Aread%2Clist_dinos%2Cbuy_dino%2Csell_dino%2Cbuy_dinosaurus&redirect_uri=http%3A%2F%2F172.31.91.29%3A3001%2FreceiveGrant&
```

ON GRANT We don't have a state paramater:

{"loginUser":{"username":"adminos"},"userConfirmCsrfToken":{"token":"csrf-8460543","expiresAt":1699602771451},"_expire":1699688271452,"_maxAge":86400000}


```
/receiveGrant?code=1c0f6dd4ac9ad99fd719e39efb888a16cc54c458&state=os-154 
```