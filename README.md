## SaambaAPI Build Instructions

#### Dependencies
- Docker

#### Commands 
From project root run:
- Build: `docker build -t saamba .`
- Run: `docker run -p 8080:8080 -t saamba`
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