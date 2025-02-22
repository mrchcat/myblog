FROM tomcat:latest

ARG WAR_FILE=target/*.war
COPY ${WAR_FILE} /usr/local/tomcat/webapps/

CMD ["catalina.sh", "run"]

