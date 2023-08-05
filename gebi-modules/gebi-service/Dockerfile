FROM openjdk:8
VOLUME /tmp
COPY target/*.jar gebi-service.jar
ENV PORT=9094
ENTRYPOINT ["java","-jar","-Xms128m","-Xmx256m","gebi-service.jar","--spring.profiles.active=prod"]
EXPOSE $PORT
