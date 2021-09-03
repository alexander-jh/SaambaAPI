#### Workflow
- Pulls all genres from Spotify
- By genre pulls top 100 artists 
- For each artist pulls top 100 songs 
- Scrapes HTML for lyrics

#### Build Instructions

`docker build -t saambaapi .`

`docker run -it saambaapi`