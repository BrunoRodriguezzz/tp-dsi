# --- DEFINIMOS LOS ARGUMENTOS GLOBALES ---
# (Docker requiere re-declararlos dentro de cada etapa)

# --- ETAPA 1: BUILD ---
FROM maven:3.9-eclipse-temurin-17 AS build

# Recibimos el nombre del servicio como argumento de construcción
ARG serviceName

WORKDIR /app
COPY . .

# Instalamos el proyecto padre (N) para que los módulos se reconozcan entre sí
RUN mvn -N install

# Empaquetamos el servicio específico y sus dependencias (-am)
RUN mvn clean package -pl ${serviceName} -am -DskipTests -Dproject.build.sourceEncoding=UTF-8 -Dfile.encoding=UTF-8

# --- ETAPA 2: RUNTIME ---
FROM eclipse-temurin:17-jre-alpine AS production

# Necesitamos re-declarar los ARG aquí para usarlos en esta etapa
ARG serviceName
ARG servicePort

WORKDIR /app

# --- CAMBIO CRITICO AQUI ---
# Copiamos el JAR.
# Si usaste <classifier>exec</classifier>, el archivo se llamará "...-exec.jar".
# Si NO lo usaste (como en client), se llamará "...-SNAPSHOT.jar".
# Este comando busca:
# 1. Archivos que terminen en .jar
# 2. EXCLUYE los que terminan en -sources.jar o -javadoc.jar (si existieran)
# 3. Al haber dos (original y exec), necesitamos asegurar copiar el ejecutable.
#
# La forma más robusta genérica: Copiamos todo lo que parezca el JAR principal y renombramos.
# Si existe el -exec.jar, Docker lo copiará. Si solo existe el normal, copiará ese.
# TRUCO: Copiamos específicamente el que tenga Spring Boot.
# PERO para simplificar tu vida ahora mismo, usa este patrón que prefiere el exec si existe:

COPY --from=build /app/${serviceName}/target/*.jar ./

# Script pequeño para detectar cuál es el JAR correcto y renombrarlo a app.jar
# Esto soluciona el problema de tener "servicio-0.0.1.jar" y "servicio-0.0.1-exec.jar" juntos.
RUN if [ -f *exec.jar ]; then mv *exec.jar app.jar; else mv *.jar app.jar; fi

# Borramos restos para limpiar (opcional, pero limpio)
RUN rm -f *.original 2>/dev/null || true

# ---------------------------

# NOTA: EXPOSE es solo documentación en Docker, no abre el puerto mágicamente.
EXPOSE ${servicePort}

ENTRYPOINT ["java", "-jar", "app.jar"]