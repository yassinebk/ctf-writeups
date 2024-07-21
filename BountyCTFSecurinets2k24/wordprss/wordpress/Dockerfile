FROM wordpress:latest

COPY functions.php /var/www/html/wp-content/themes/twentytwentyfour/functions.php
COPY wp-latex /var/www/html/wp-content/plugins/wp-latex/
COPY flag.sh /usr/local/bin/
RUN chmod +x /usr/local/bin/flag.sh

RUN ["flag.sh"]
