FROM python:alpine3.7
RUN pip install --upgrade pip
RUN apk update && apk add python3-dev \
                        gcc \
                        libc-dev libffi-dev
COPY ./identity/app /app
WORKDIR /app
RUN pip install -r requirements.txt && chmod +x init.sh
EXPOSE 5000
CMD /app/init.sh
