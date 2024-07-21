FROM ubuntu:latest

RUN apt-get update

#server
RUN apt-get install -y nodejs npm redis-server 

#nginx 
RUN apt-get install -y nginx
COPY nginx.conf /etc/nginx/nginx.conf


COPY redis.conf /etc/redis/redis.conf
RUN mkdir /run/redis/

EXPOSE 80

RUN echo "" > /flag
RUN mkdir /app
COPY src/server /app/server
WORKDIR /app/server

RUN npm install


WORKDIR /app
COPY src/build.sh /app
RUN chmod 777 build.sh

ENTRYPOINT ["bash","build.sh"]

