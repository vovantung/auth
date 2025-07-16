FROM eclipse-temurin:17-jdk-alpine
COPY main-app/target/auth.jar /auth.jar
ENTRYPOINT ["java","-jar","/auth.jar"]
EXPOSE 8080