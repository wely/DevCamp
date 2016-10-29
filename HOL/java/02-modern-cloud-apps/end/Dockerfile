FROM anapsix/alpine-java

ENV AZURE_STORAGE_ACCESS_KEY ""

VOLUME /tmp

ADD build/libs/*.jar app.jar

RUN sh -c 'touch /app.jar'

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]