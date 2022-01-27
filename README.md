## Open Music Scrobbler
[Last.fm](https://www.last.fm) scrobbler by [slindenau](https://github.com/s-lindenau)

### Description
The Open Music Scrobbler can be used to scrobble anything from your [Discogs User Collection](https://www.discogs.com/user/example) to Last.fm. 

This project originated as a personal learning experience in the time my employer has reserved for us to pursue personal growth. In that context i will be working on this project at least one day per month.  

If you just want to easily scrobble something you listened to, I suggest checking out the following projects:
- https://openscrobbler.com
- https://vinylscrobbler.com
- https://codescrobble.com
- https://universalscrobbler.com

### Roadmap
- [x] Initial Prototype (CLI)
- [ ] Desktop GUI
- [ ] Scheduling/offline scrobble with database
- [ ] Shazam-like fingerprinting for automatic scrobble mode
- [ ] Search other music databases (Discogs, MusicBrainz, Last.fm)
- [ ] Web based GUI

### Getting started
- Requirements
  - Java 17
  - Maven 3
  - Discogs collection (public)
  - Last.fm account
  - Last.fm api key
- Dependencies
  - discogs4j: https://github.com/ajdons/discogs4j
    - install as jar in local maven repository
- Building
  - git clone git@github.com:s-lindenau/OpenMusicScrobbler.git
  - mvn clean package
- Running
  - From the packaged build take the following:
    - open-music-scrobbler-_version_.jar 
    - 'dependencies' folder
  - Configure the required properties
  - java.exe -jar open-music-scrobbler-_version_.jar