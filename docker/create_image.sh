mvn clean package
docker build --platform linux/amd64 -t daaaviid03/swappy:1.0.0 -f docker/Dockerfile .