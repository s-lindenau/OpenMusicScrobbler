## Open Music Scrobbler
[Last.fm](https://www.last.fm) scrobbler by [slindenau](https://github.com/s-lindenau)

### Description
The Open Music Scrobbler can be used to manually scrobble anything from your [Discogs User Collection](https://www.discogs.com/user/example) to Last.fm. 

This project originated as a personal learning experience in the time my employer has reserved for us to pursue personal growth. In that context I will be working on this project at least one day per month.  

If you just want to easily scrobble something you listened to, I suggest checking out the following projects:
- https://openscrobbler.com
- https://vinylscrobbler.com
- https://codescrobble.com
- https://universalscrobbler.com

### Roadmap
- [x] Initial Prototype (CLI)
- [ ] Desktop GUI
- [ ] Scheduling/offline scrobble with database
- [ ] Web based GUI
- [ ] Barcode scanner for quick scrobble
- [ ] Last.fm toolbox
- [ ] Collection builder from other sources (mp3, search music databases)
- [ ] Shazam-like fingerprinting for automatic scrobble mode

### Getting started
- Requirements
  - Java 17
  - Maven 3
  - Discogs collection (public)
  - Last.fm account
  - Last.fm api key
- Dependencies
  - discogs4j: (fork of) https://github.com/ajdons/discogs4j
    - `git clone git@github.com:s-lindenau/discogs4j.git`
    - `mvn clean install`
- Building
  - `git clone git@github.com:s-lindenau/OpenMusicScrobbler.git`
  - `mvn clean package`
- Running
  - From the packaged build take the following:
    - ~/target/open-music-scrobbler-_version_.jar 
    - ~/target/dependencies folder
  - Configure the required properties
  - Run with `java -jar` open-music-scrobbler-_version_.jar
- Running different applications
  - The application to run can be optionally specified, by default it will start the main scrobble UI
  - Run with `java -jar` open-music-scrobbler-_version_.jar _application_
  - Possible options are
    - console
    - encrypt