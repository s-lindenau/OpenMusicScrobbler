# Build stage (1): maven package the project and its dependencies
# Image is based on Alpine Linux, Package manager: apk
# On updating the Java version, also update the pom.xml file
FROM maven:3-eclipse-temurin-17-alpine AS maven-build

# Install GIT
RUN apk add --no-cache git

# Build Discogs4j (fork)
WORKDIR /discogs4j
# On updating the version number of Discogs4j (fork), also update the pom.xml file
RUN git clone --depth 1 --branch 1.3 https://github.com/s-lindenau/discogs4j.git .
RUN mvn clean install

# Build Open Music Scrobbler
WORKDIR /oms-build
COPY pom.xml .
COPY src src
RUN mvn clean package

ENTRYPOINT []
CMD ["/bin/bash"]

# Build stage (2): create the oms-application
# On updating the Java version, also update the pom.xml file
FROM eclipse-temurin:17-jre-alpine AS oms-application

# Application files
COPY --from=maven-build oms-build/target/dependency /oms/dependency
COPY --from=maven-build oms-build/target/open-music-scrobbler-*.jar /oms/open-music-scrobbler.jar

# Configuration file
# NOTE: mount local configuration file if you want to persist settings: -v C:\my\config.properties:/oms/config.properties
COPY --from=maven-build oms-build/target/config.properties /oms/config.properties

# Dropwizard application
EXPOSE 8080
# Dropwizard admin
EXPOSE 8081
# NOTE: bind ports on host when running this container: -p 8080:8080 8081:8081

# Run application with configuration file /oms/config.properties
WORKDIR /oms
ENTRYPOINT ["java", "-jar", "-Doms.config=/oms/", "/oms/open-music-scrobbler.jar"]

# Run by default in `server` mode, override command to run different applications.
# Use -i for interactive mode if the specific application requires console interaction.
CMD ["server"]