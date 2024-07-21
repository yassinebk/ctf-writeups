FROM python:3.12-slim as base

WORKDIR /app
COPY requirements.txt /app/
RUN pip install -r requirements.txt
COPY app.py flag.png /app/
COPY static/ /app/static
COPY templates/ /app/templates

RUN mkdir temp

CMD ["python3", "-m", "flask", "run", "--host=0.0.0.0", "--port=80"]
