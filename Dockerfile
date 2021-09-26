FROM amazoncorretto:11

RUN yum -y update && yum install -y maven

COPY . /saamba
RUN cd /saamba && mvn package

RUN ls
RUN ls saamba/target/
ENTRYPOINT ["java","-jar","saamba/target/SaambaAPI-0.0.1-SNAPSHOT.jar"]