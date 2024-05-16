FROM eclipse-temurin:21-jdk-alpine
COPY sa-server.jar app.jar
COPY application.yaml application.yaml 
ENTRYPOINT ["java","-jar","app.jar"]
