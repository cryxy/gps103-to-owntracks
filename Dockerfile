FROM adoptopenjdk:11-jre-hotspot
ADD /target/gps103-to-owntracks-app.jar /start/gps103-to-owntracks-app.jar
ADD /target/libs /start/libs
EXPOSE 22022
WORKDIR "/start"
CMD ["java","-Dlog4j.configurationFile=/start/config/log4j2.xml","-jar","gps103-to-owntracks-app.jar"]