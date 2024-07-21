FROM php:7.4-apache

WORKDIR /var/www/html

COPY . /var/www/html

RUN apt-get update && apt-get install -y \
    libzip-dev \
    zip \
    && docker-php-ext-install zip

RUN docker-php-ext-install mysqli pdo pdo_mysql

RUN a2enmod rewrite

RUN chown -R www-data:www-data /var/www/html

RUN chmod -R 777 /var/www/html

RUN echo "AlFarahidi{this_is_the_flag}" > /flag03f21b939e022176e87feb7704477bbc

EXPOSE 80

CMD ["apache2-foreground"]