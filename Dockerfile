FROM adoptopenjdk:11-jre-hotspot
ADD /target/gps103-to-owntracks-app.jar /start/gps103-to-owntracks-app.jar
ADD /target/libs /start/libs
EXPOSE 22022
CMD ["java","-jar","/start/gps103-to-owntracks-app.jar"]