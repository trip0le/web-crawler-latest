FROM openjdk:17-jdk-slim
EXPOSE 8080
COPY build/libs/spring-summarizer-app.jar spring-summarizer-app.jar
COPY libs/ libs/
ENTRYPOINT ["java","-jar","/spring-summarizer-app.jar"]
