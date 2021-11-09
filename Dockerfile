FROM amazoncorretto:11

RUN yum -y update && yum install -y maven

COPY . /saamba
RUN cd /saamba && mvn package

EXPOSE 8080
EXPOSE 80
EXPOSE 5000

ENTRYPOINT ["java","-jar","saamba/target/SaambaAPI-0.0.1-SNAPSHOT.jar","-Dspring.profiles.active=git",
            "-Dspring-boot.run.arguements=--client.spotify.accesskey=${SPOTIFY_ACCESS_KEY}, --client.spotify.secretkey=${SPOTIFY_SECRET_KEY}, --client.ibm.discovery.key=${IBM_DISCOVERY_KEY}, --client.ibm.discovery.url=${IBM_DISCOVERY_URL}, --client.ibm.discovery.envid=${IBM_DISCOVERY_ENVID}, --client.ibm.discovery.colid=${IBM_DISCOVERY_COLID}, --client.ibm.tone.key=${IBM_TONE_KEY}, --client.ibm.tone.url=${IBM_TONE_URL}, --client.aws.accesskey=${AWS_ACCESS_KEY}, --client.aws.secretkey=${AWS_SECRET_KEY}, --client.genius.token=${GENIUS_TOKEN}, --client.twitter.accesskey=${TWITTER_ACCESS_KEY}, --client.twitter.secretkey=${TWITTER_SECRET_KEY}, --client.twitter.bearer.token=${TWITTER_BEARER_TOKEN}, --client.twitter.accesstoken=${TWITTER_ACCESS_TOKEN}, --client.twitter.accesssecret=${TWITTER_ACCESS_SECRET}"]