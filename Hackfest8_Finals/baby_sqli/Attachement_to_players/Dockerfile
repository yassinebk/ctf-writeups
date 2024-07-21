FROM php:7.4-apache
ENV DEBIAN_FRONTEND=noninteractive
RUN apt update
RUN apt upgrade -y 
RUN echo "ServerName localhost" >> /etc/apache2/apache2.conf
RUN ln -sf /dev/stdout /var/log/apache2/access.log && \
    ln -sf /dev/stderr /var/log/apache2/error.log
RUN docker-php-ext-install pdo pdo_mysql
RUN docker-php-ext-install mysqli && docker-php-ext-enable mysqli
WORKDIR /var/www/html/
COPY ./src/ .

