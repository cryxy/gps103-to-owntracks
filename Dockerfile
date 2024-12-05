FROM adoptopenjdk:11-jre-hotspot
# copy the app
ADD /target/gps103-to-owntracks-app.jar /start/gps103-to-owntracks-app.jar
ADD /target/libs /start/libs
# create a user
RUN groupadd -r gps103 -g 970
RUN useradd -u 970 -r -g gps103 -m -d /start gps103
# Chown all the files to the app user.
RUN chown -R gps103:gps103 /start

EXPOSE 22022
WORKDIR "/start"
USER gps103
CMD ["java","-Dlog4j.configurationFile=/start/config/log4j2.xml","-jar","gps103-to-owntracks-app.jar"]
