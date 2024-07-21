#!/bin/bash
docker rm -f web_polaris_control
docker build -t web_polaris_control .
docker run --name=web_polaris_control --rm -p1337:1337 -it web_polaris_control