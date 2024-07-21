#!/bin/bash
FLAG="flag{lqosdq}"
filename=$(mktemp -u)
echo $FLAG > $filename
chown www-data:www-data $filename

