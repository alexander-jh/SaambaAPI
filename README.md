## SaambaAPI Build Instructions

#### Dependencies
- Docker

#### Commands 
From cloned project root run:
- Build: `docker build -t saamba .`
- Run: `docker run -p 8080:8080 -t saamba`
- Endpoint: `localhost:8080`

Pull regularly from repo before trying to build image. If Docker commands fails try running with root privledges (e.g. `sudo` or equivalent call)

#### API Endpoints
To retrieve playlist GET request it is `localhost:8080/getPlaylist/{accountName}`. Returns JSON of the form:
````
{ 
    "date": <date playlist created>,
    "songs": [
        "<Spotify URI to each song>"
    ]
}
````