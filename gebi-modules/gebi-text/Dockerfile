FROM openjdk:8
VOLUME /tmp
COPY target/*.jar gebi-text.jar
ENV PORT=9096
ENTRYPOINT ["java","-jar","-Xms32m","-Xmx64m","gebi-text.jar","--spring.profiles.active=prod"]
EXPOSE $PORT
