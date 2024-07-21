FROM python:3.11-slim
RUN pip install flask gunicorn
COPY ./src /app

WORKDIR /app
CMD bash /app/run.sh
