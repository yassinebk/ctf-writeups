#!/bin/sh
while ! nc authdb 3306; do sleep 2; done
python app.py