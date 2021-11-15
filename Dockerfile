FROM amazoncorretto:11

ARG SPOTIFY_ACCESS_KEY
ARG SPOTIFY_SECRET_KEY
ARG IBM_DISCOVERY_KEY
ARG IBM_DISCOVERY_URL
ARG IBM_DISCOVERY_ENVID
ARG IBM_DISCOVERY_COLID
ARG IBM_TONE_KEY
ARG IBM_TONE_URL
ARG AWS_ACCESS_KEY
ARG AWS_SECRET_KEY
ARG GENIUS_TOKEN
ARG TWITTER_ACCESS_KEY
ARG TWITTER_SECRET_KEY
ARG TWITTER_BEARER_TOKEN
ARG TWITTER_ACCESS_TOKEN
ARG TWITTER_ACCESS_SECRET

RUN echo $TWITTER_ACCESS_KEY

RUN yum -y update && yum install -y maven

COPY . /saamba
RUN cd /saamba && mvn package

EXPOSE 8080
EXPOSE 80
EXPOSE 5000

ENTRYPOINT ["java","-jar","saamba/target/SaambaAPI-0.0.1-SNAPSHOT.jar","-Dspring.profiles.active=git"]