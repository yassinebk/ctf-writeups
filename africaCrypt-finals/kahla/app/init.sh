#!/bin/sh
touch /var/run/mysqld/mysqld.sock
chown -R mysql /var/run/mysqld
/etc/init.d/mysql restart 

USER="kahla"
PASS="REDACTED"

echo "Creating new user ${MYSQL_USER} ..."
mysql -uroot -p$MYSQL_ROOT_PASSWORD -e "CREATE USER '${USER}'@'%' IDENTIFIED BY '${PASS}';"
echo "Granting privileges..."
mysql -uroot -p$MYSQL_ROOT_PASSWORD -e "GRANT ALL PRIVILEGES ON *.* TO '${USER}'@'%';"
mysql -uroot -p$MYSQL_ROOT_PASSWORD -e "FLUSH PRIVILEGES;"
echo "All done."

mysql -u$USER -p$PASS -e "CREATE database kahladb;"
mysql -u$USER -p$PASS kahladb  < /app/init.sql
echo "All done."
sleep 12
#nginx -c /etc/nginx/sites-available/default
service nginx start
python3 app.py 
