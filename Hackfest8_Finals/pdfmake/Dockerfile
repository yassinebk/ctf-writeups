FROM node:16

WORKDIR /opt

RUN apt-get update && apt-get install -y \
    curl \
    xz-utils \
    unzip

RUN wget https://github.com/bpampuch/pdfmake/archive/refs/tags/0.3.0-beta.8.zip -O pdfmake.zip \
    && unzip pdfmake.zip \
    && mv pdfmake-0.3.0-beta.8 pdfmake \
    && rm pdfmake.zip

WORKDIR /opt/pdfmake
RUN npm install
RUN npm run build

WORKDIR /opt/app
RUN mkdir public

COPY server.js .
COPY index.html public/index.html

RUN npm install body-parser express
RUN npm install -g nodemon
RUN echo 'HACKFEST{fake}' > /flag.txt

EXPOSE 3000

CMD ["nodemon", "server.js"]
