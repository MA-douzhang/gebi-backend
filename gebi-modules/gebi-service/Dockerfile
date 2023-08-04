FROM openjdk:8
VOLUME /tmp
COPY target/*.jar gebi-service.jar
ENV PORT=9094
ENTRYPOINT ["java","-jar","-Xms64m","-Xmx128m","gebi-service.jar","--spring.profiles.active=prod"]
EXPOSE $PORT
