FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN mvn -N install
RUN mvn clean package -pl serviceRegistry -am -DskipTests

FROM eclipse-temurin:17-jre-alpine AS production
WORKDIR /app
COPY --from=build /app/apiGateway/target/*.jar app.jar
EXPOSE 8088
ENTRYPOINT ["java", "-jar", "app.jar"]