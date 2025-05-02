# Imagen base para el contenedor de compilación
FROM maven:latest AS builder
# Define el directorio de trabajo donde ejecutar comandos
WORKDIR /project
# Copia el archivo pom.xml desde la raíz del proyecto al contenedor
COPY ../pom.xml /project/
# Descarga las dependencias
RUN mvn dependency:go-offline
# Copia los archivos del proyecto desde la carpeta src de la raíz al contenedor
COPY ../src /project/src
# Compila el proyecto y descarga librerías
RUN mvn -B package -DskipTests

# Imagen base para el contenedor de la aplicación
FROM eclipse-temurin:21-jre
# Define el directorio de trabajo donde se encuentra el JAR
WORKDIR /usr/src/app/
# Copia el JAR del contenedor de compilación
COPY --from=builder /project/target/*.jar /usr/src/app/app.jar
# Indica el puerto que expone el contenedor
EXPOSE 8443
# Comando que se ejecuta al hacer docker run
CMD [ "java", "-jar", "app.jar" ]
