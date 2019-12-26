# GPS103 Tracker to Owntracks
Java-based socket server which listens for GPS103 Protocol messages (e.g. INCUTEX TK104). The Location messages are forwarded using MQTT and the Owntracks protocol.   

## Protocol Infos
- see documentation/*.pdf
- https://stackoverflow.com/questions/38991490/gps103-tracker-listening-application-in-c-sharp

## Build
mvn clean install

## Start
- customize `config/app.config` 
- run `java -Dlog4j.configurationFile=/opt/tracker/gps103-to-owntracks/config/log4j2.xml -jar gps103-to-owntracks-app-0.0.1-SNAPSHOT.jar > /dev/null & `

