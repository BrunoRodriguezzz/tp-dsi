# --- Build Stage ---
FROM maven:3.9.6-eclipse-temurin-17 AS builder

# Copiamos TODO el proyecto (se necesita el pom padre)
WORKDIR /app
COPY ../.. /app

# Instalamos el POM padre sin construir módulos
RUN mvn -B -DskipTests -N install

# Construimos SOLO el módulo servicioAutenticacion
WORKDIR /app/servicioAutenticacion
RUN mvn -B -DskipTests clean package

# --- Runtime Stage ---
FROM eclipse-temurin:17-jdk

WORKDIR /app

# Copiamos el jar del módulo de build
COPY --from=builder /app/servicioAutenticacion/target/*.jar app.jar

EXPOSE 8087

# Ejecutar la app
ENTRYPOINT ["java", "-jar", "app.jar"]
