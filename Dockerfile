FROM amazoncorretto:11

RUN yum -y update && yum install -y maven

RUN echo ${SPOTIFY_ACCESS_KEY}

COPY . /saamba
RUN cd /saamba && mvn package

EXPOSE 8080
EXPOSE 80
EXPOSE 5000

ENTRYPOINT ["java","-jar","saamba/target/SaambaAPI-0.0.1-SNAPSHOT.jar","-Dspring.profiles.active=git"]