FROM eclipse-temurin:17-jre-alpine

# Application files
COPY target/dependency /oms/dependency
COPY target/open-music-scrobbler-*.jar /oms/open-music-scrobbler.jar

# Configuration file
# NOTE: mount local configuration file if you want to persist settings: -v C:\my\config.properties:/oms/config.properties
COPY target/config.properties /oms/config.properties

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