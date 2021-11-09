FROM amazoncorretto:11

RUN --mount=type=secret,id=TWITTER_ACCESS_KEY \
    export TWITTER_ACCESS_KEY=$(cat /run/secrets/TWITTER_ACCESS_KEY)

RUN echo ${TWITTER_ACCESS_KEY}

RUN yum -y update && yum install -y maven

COPY . /saamba
RUN cd /saamba && mvn package

EXPOSE 8080
EXPOSE 80
EXPOSE 5000

ENTRYPOINT ["java","-jar","saamba/target/SaambaAPI-0.0.1-SNAPSHOT.jar","-Dspring.profiles.active=git"]