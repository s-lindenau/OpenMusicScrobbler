FROM eclipse-temurin:17-jre

# Application files
COPY target/dependency /oms/dependency
COPY target/open-music-scrobbler-*.jar /oms/open-music-scrobbeler.jar

# Configuration file
# NOTE: mount local configuration file if you want to persist settings: -v C:\my\config.properties:/oms/config.properties
COPY target/config.properties /oms/config.properties

# Dropwizard application
EXPOSE 8080
# Dropwizard admin
EXPOSE 8081
# NOTE: bind ports on host when running this container: -p 8080:8080 8081:8081

# Run application in `server` mode with configuration file /oms/config.properties
WORKDIR /oms
ENTRYPOINT ["java", "-jar", "-Doms.config=/oms/", "/oms/open-music-scrobbeler.jar", "server"]