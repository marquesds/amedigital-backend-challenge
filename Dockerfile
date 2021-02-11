FROM maven:3.6.3-jdk-11-slim

WORKDIR /code

ADD pom.xml /code/pom.xml
RUN ["mvn", "dependency:resolve"]
RUN ["mvn", "verify"]

ADD src /code/src
RUN ["mvn", "package", "-Dmaven.test.skip=true"]

EXPOSE 8080
CMD ["mvn", "exec:java"]
