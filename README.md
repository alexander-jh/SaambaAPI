## SaambaAPI Build Instructions

#### Dependencies
- Docker

####C ommands 
From project root run:
- Build: `docker build -t saamba .`
- Run: `docker run -t saamba`
- Endpoint: `localhost:8080`

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