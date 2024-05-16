## Open Music Scrobbler
[Last.fm](https://www.last.fm) scrobbler by [slindenau](https://github.com/s-lindenau)

### Description
The Open Music Scrobbler can be used to manually scrobble anything from your [Discogs User Collection](https://www.discogs.com/user/example) to Last.fm.

Connect with your Discogs account to browse your (public) collection, and select any album to scrobble. Connect with your Last.fm account to scrobble the selected album(s) and track(s) at a given instant in time. If the track duration is present on Discogs, the scrobbles will be created based on the actual playtime of each track. 

This application is currently for desktop or self-hosted only. If you just want to quickly scrobble something you listened to, I suggest also checking out the following projects:
- https://openscrobbler.com
- https://vinylscrobbler.com
- https://codescrobble.com
- https://universalscrobbler.com

### Roadmap
- [x] Initial Prototype (CLI)
- [x] Web GUI
- [ ] Scheduling/offline scrobble with database
- [ ] Barcode/Cover scanner for quick scrobble

### Getting started
- Requirements
  - Java 17
  - Maven 3 (optional; for building the application from sources only)
  - Docker (optional; alternative for building + running the application)
  - Discogs collection (public or authenticate with Personal Access Token)
  - Last.fm account
  - Last.fm api key
- Downloading the application
  - On the Releases tab in GitHub the packaged builds can be downloaded.
  - _Skip the 'Building' section if you're using the released build_
  - _Skip the 'Downloading' and 'Building' sections if you're using Docker_
- Building the JARs from source code with Maven (optional)
  - Dependencies
    - discogs4j: (fork of) https://github.com/ajdons/discogs4j
      - `git clone git@github.com:s-lindenau/discogs4j.git`
      - `mvn clean install`
  - Building
    - `git clone git@github.com:s-lindenau/OpenMusicScrobbler.git`
    - `mvn clean package`
  - Supply api keys
    - If desired the Last.fm api keys can be added to the final build by setting the following options on the `mvn package` command:
      - `-Dlastfm.api.key=mykey`
      - `-Dlastfm.api.secret=mysecret`
  - From the packaged build take the following:
      - ~/target/open-music-scrobbler-_version_.jar
      - ~/target/dependencies folder
      - ~/target/config.properties
  - Release zip
    - All the relevant files listed above are also zipped for convenient distribution
    - See ~/target/release folder
- Running the application
  - Configure the required properties
  - Run with `java -jar` open-music-scrobbler-_version_.jar
- Running different applications
  - The application to run can be optionally specified, by default it will start the main scrobble UI
  - Run with `java -jar` open-music-scrobbler-_version_.jar _application_
  - Possible options for _application_ are
    - `console` the main command line based scrobble client
    - `config` a command line based wizard that helps to set the configuration properties
    - `server` run as a server for the web based client
- Building and Running with Docker (optional)
  - With the `Dockerfile` you can both build the JARs from sources and then run the application, without the need for Java or Maven on your local machine
  - Building the image:
    - Directly from the remote repository: 
      - `docker build https://github.com/s-lindenau/OpenMusicScrobbler.git`
    - Or you can first check out the sources locally:
      - `git clone git@github.com:s-lindenau/OpenMusicScrobbler.git`
      - `docker build .`
    - Note that you may add any tag for this image as desired with `-t tagName`
  - Running a container from the image: 
    - Find the ID of your image, or use the `tagName` to identify the image created with the build command
    - Create and run a new container as the `server` application (default) with: `docker run imageID`
      - The server for the web based client is best started in detached mode. Add `-d` to the `run` command
    - Running different applications can be done by overriding the command: `docker run imageID` _application_
      - Note that for console based applications the container must be created in interactive mode and started with an attached terminal.
        - Add `-i` to the `run` command, and `-a -i` when using `docker start`
      - The console applications are intended to be used 'once', it is recommended to always stop and (re)start these containers.
    - Bind ports to access the web application: add `-p 8080:8080 8081:8081` to the `run` command
    - Optional: recommended to mount an external configuration file to persist settings outside the docker container 
      - add to the `run` command: `-v C:\my\config.properties:/oms/config.properties` 
      - create empty `config.properties` file and replace `C:\my\config.properties` in the `-v` argument with the location of this file
    - Note that you may add any name for this container as desired with `--name containerName`
- Using the application
  - The `console` application is an interactive prompt directly in the console used to run the application. If no console is present, a GUI console alternative is started. Follow the menu and instructions on screen, and input text on various prompts to select and scrobble tracks.
  - The `server` web based application can be accessed with any modern browser. After starting the application, browse to http://localhost:8080 to access the main menu. Follow the links on screen to select and scrobble tracks.