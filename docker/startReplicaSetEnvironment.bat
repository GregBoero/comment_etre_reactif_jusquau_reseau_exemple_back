
docker-compose --file docker-compose.yml up -d

echo "****** Waiting for ${DELAY} seconds for containers to go up ******"
timeout 10

docker exec mongo1 /scripts/rs-init.sh