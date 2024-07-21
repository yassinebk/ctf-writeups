FROM ubuntu:20.04

ENV DEBIAN_FRONTEND=noninteractive

RUN apt-get update && \
    apt-get install -y --no-install-recommends tzdata curl openssl && \
    echo "Europe/London" > /etc/timezone && \
    dpkg-reconfigure --frontend noninteractive tzdata

RUN apt-get install -y --no-install-recommends apache2 libapache2-mod-php



RUN export RANDOM_ENV=$(openssl rand -base64 12 | tr -dc 'a-zA-Z' | head -c 8) \
    && export RANDOM_VALUE=flag_$(openssl rand -hex 16) \
    && echo "export $RANDOM_ENV=$RANDOM_VALUE" >> /etc/apache2/envvars \
    && echo "flag{test}" > /$RANDOM_VALUE \
    && cat /etc/apache2/envvars 


RUN rm -f /var/www/html/index.html
COPY . /var/www/html
RUN rm /var/www/html/Dockerfile
ADD entrypoint.sh /entrypoint.sh

# Make the entrypoint script executable
RUN chmod +x /entrypoint.sh

# Run the entrypoint script when the container starts
ENTRYPOINT ["/entrypoint.sh"]
