# --- DEFINIMOS LOS ARGUMENTOS GLOBALES ---
# (Docker requiere re-declararlos dentro de cada etapa)

# --- ETAPA 1: BUILD ---
FROM maven:3.9-eclipse-temurin-17 AS build

# Recibimos el nombre del servicio como argumento de construcción
ARG serviceName

WORKDIR /app
COPY . .

RUN mvn -N install

# Usamos la variable. Si serviceName está vacío, esto fallará, lo cual es bueno.
RUN mvn clean package -pl ${serviceName} -am -DskipTests -Dproject.build.sourceEncoding=UTF-8 -Dfile.encoding=UTF-8

# --- ETAPA 2: RUNTIME ---
FROM eclipse-temurin:17-jre-alpine AS production

# Necesitamos re-declarar los ARG aquí para usarlos en esta etapa
ARG serviceName
ARG servicePort

WORKDIR /app

# Copiamos usando la variable
COPY --from=build /app/${serviceName}/target/*.jar app.jar

# NOTA: EXPOSE es solo documentación en Docker, no abre el puerto mágicamente.
# La apertura real la hace Dokploy en la configuración de red.
EXPOSE ${servicePort}

ENTRYPOINT ["java", "-jar", "app.jar"]