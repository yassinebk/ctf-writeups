FROM --platform=amd64 python:3.11-alpine

# Install packages
RUN apk update && apk add --no-cache --update mariadb mariadb-client mariadb-connector-c-dev supervisor neo4j make musl-dev gcc go chromium chromium-chromedriver

# Add chromium to PATH
ENV PATH="/usr/lib/chromium:${PATH}"

# Add go to PATH
RUN export GOPATH=/root/go
RUN export PATH=${GOPATH}/bin:/usr/local/go/bin:$PATH
RUN export GOBIN=$GOROOT/bin
RUN mkdir -p ${GOPATH}/src ${GOPATH}/bin
RUN export GO111MODULE=on

# Upgrade pip
RUN python -m pip install --upgrade pip

# Copy flag
COPY flag.txt /flag.txt

# Setup app
RUN mkdir -p /app

# Switch working environment
WORKDIR /app

# Add application
COPY challenge .

# Install dependencies
RUN pip install -r requirements.txt

# Setup supervisor
COPY config/supervisord.conf /etc/supervisord.conf

# Expose port the server is reachable on
EXPOSE 1337

# Disable pycache
ENV PYTHONDONTWRITEBYTECODE=1

# Create database and start supervisord
COPY --chown=root entrypoint.sh /entrypoint.sh
RUN chmod +x /entrypoint.sh
ENTRYPOINT ["/entrypoint.sh"]