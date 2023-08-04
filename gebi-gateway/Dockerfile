FROM openjdk:8
VOLUME /tmp
COPY target/*.jar gebi-gateway.jar
ENV PORT=8099
ENTRYPOINT ["java","-jar","-Xms32m","-Xmx64m","gebi-gateway.jar","--spring.profiles.active=prod"]
EXPOSE $PORT
