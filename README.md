./mvnw clean package
docker build -t greattusk/integracion:latest .
docker push greattusk/integracion:latest