#!/bin/ash

# Change flag name
mv /flag.txt /flag$(cat /dev/urandom | tr -cd 'a-f0-9' | head -c 10).txt

# Secure entrypoint
chmod 600 /entrypoint.sh

# Random password
function genPass() {
    echo -n $RANDOM | md5sum | head -c 32
}

# Set environment variables
export MYSQL_HOST="localhost"
export MYSQL_DATABASE="polaris_control"
export MYSQL_USER="polaris"
export MYSQL_PASSWORD=$(genPass)
export NEO4J_HOST="127.0.0.1"
export NEO4J_DATABASE="polaris_control"
export NEO4J_USER="neo4j"
export NEO4J_PASSWORD=$(genPass)
export MODERATOR_USER="lean"
export MODERATOR_PASSWORD=$(genPass)

# Initialize and start mysql
mkdir -p /run/mysqld
chown -R mysql:mysql /run/mysqld
mysql_install_db --user=mysql --ldata=/var/lib/mysql
mysqld --user=mysql --console --skip-networking=0 &

# Wait for mysql to start
while ! mysqladmin ping -h'localhost' --silent; do echo 'not up' && sleep .2; done

# Start neo4j
neo4j-admin set-initial-password $NEO4J_PASSWORD
neo4j start
sleep 30

# Create mysql user
mysql -u root -h $MYSQL_HOST << EOF
CREATE USER '${MYSQL_USER}'@'${MYSQL_HOST}' IDENTIFIED BY '${MYSQL_PASSWORD}';
CREATE DATABASE IF NOT EXISTS ${MYSQL_DATABASE};
GRANT ALL PRIVILEGES ON ${MYSQL_DATABASE}.* TO '${MYSQL_USER}'@'${MYSQL_HOST}';
FLUSH PRIVILEGES;
EOF

# Populate databases
python /app/populate.py

# Revoke unused permissions
mysql -u root -h $MYSQL_HOST << EOF
REVOKE ALL PRIVILEGES ON ${MYSQL_DATABASE}.* FROM '${MYSQL_USER}'@'${MYSQL_HOST}';
GRANT SELECT, INSERT, UPDATE ON ${MYSQL_DATABASE}.implants TO '${MYSQL_USER}'@'${MYSQL_HOST}';
GRANT SELECT ON ${MYSQL_DATABASE}.users TO '${MYSQL_USER}'@'${MYSQL_HOST}';
GRANT SELECT, INSERT, UPDATE ON ${MYSQL_DATABASE}.trusted_external_token_providers TO '${MYSQL_USER}'@'${MYSQL_HOST}';
FLUSH PRIVILEGES;
EOF

# Start application
/usr/bin/supervisord -c /etc/supervisord.conf