FROM python:3.7-slim
WORKDIR /usr/src/app/
RUN chmod 777 /usr/src/app/
COPY . .
RUN apt update
RUN apt install python3-pip -y
RUN pip install -r requirement.txt
ENV FLAG="FLAG_Here"
RUN groupadd -r swuser -g 433 && \
    useradd -u 431 -r -g swuser -s /sbin/nologin -c "Docker image user" swuser
USER swuser

CMD [ "python", "main.py" ]

# sudo docker build -t africa .
# sudo docker run -d -p 5000:5000 --name mark4archive africa
# --> http://localhost:5000/
