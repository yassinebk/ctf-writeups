#!/bin/bash



# Start Apache in the background
apachectl -D BACKGROUND &

# Wait for Apache to start
sleep 5

# Execute the PHP script using curl

sed -i '$d' /etc/apache2/envvars


# Remove the access.log and error.log files
rm /var/log/apache2/access.log /var/log/apache2/error.log

# Create symbolic links to /dev/null in their place
ln -s /dev/null /var/log/apache2/access.log
ln -s /dev/null /var/log/apache2/error.log

# Keep the container running
tail -f /dev/null
