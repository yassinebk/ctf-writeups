FROM ubuntu:18.04
ARG DEBIAN_FRONTEND=noninteractive
RUN apt-get update && apt-get upgrade -y 
RUN apt-get install -y python3 python3-pip mariadb-server nginx
COPY ./REDACTED.txt /
COPY ./app /app
COPY ./app/default /etc/nginx/sites-available/
WORKDIR /app/
RUN pip3 install -r requirements.txt
RUN mkdir /run/mysqld/
RUN chmod +x /app/init.sh
CMD /app/init.sh
