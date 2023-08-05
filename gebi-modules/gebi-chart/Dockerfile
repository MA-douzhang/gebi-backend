FROM openjdk:8
VOLUME /tmp
COPY target/*.jar gebi-chart.jar
ENV PORT=9095
ENTRYPOINT ["java","-jar","-Xms128m","-Xmx256m","gebi-chart.jar","--spring.profiles.active=prod"]
EXPOSE $PORT
