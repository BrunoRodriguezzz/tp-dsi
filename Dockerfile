# --- ETAPA 1: BUILD ---
FROM maven:3.9-eclipse-temurin-17 AS build

# IMPORTANTE: Recibimos el argumento aquí
ARG serviceName

WORKDIR /app
COPY . .

RUN mvn -N install

# Esta es tu línea de compilación
RUN mvn clean package -pl ${serviceName} -am -DskipTests -Dproject.build.sourceEncoding=UTF-8 -Dfile.encoding=UTF-8

# --- ETAPA 2: RUNTIME ---
FROM eclipse-temurin:17-jre-alpine AS production

ARG serviceName
ARG servicePort

WORKDIR /app

# Copiamos TODOS los jars generados en target
COPY --from=build /app/${serviceName}/target/*.jar ./

# Tu script de selección inteligente
RUN if ls *exec.jar 1> /dev/null 2>&1; then \
      mv *exec.jar app.jar; \
    else \
      mv *.jar app.jar; \
    fi

# Limpieza opcional
RUN rm -f *.original 2>/dev/null || true

EXPOSE ${servicePort}
ENTRYPOINT ["java", "-jar", "app.jar"]