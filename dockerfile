FROM openjdk:19-jdk

WORKDIR /usr/src/app
COPY . .

RUN ./mvnw -Dmaven.test.skip=true package

EXPOSE 8080
CMD ["java","-jar","/usr/src/app/target/powerlifting-0.0.1-SNAPSHOT.jar"]