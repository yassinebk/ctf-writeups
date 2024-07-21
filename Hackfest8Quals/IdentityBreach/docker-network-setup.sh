docker network create --driver bridge sql-identity-breach --subnet 172.16.0.0/24
docker-compose -f setup/docker-compose.yaml -p "identity-breach" up --build -d
