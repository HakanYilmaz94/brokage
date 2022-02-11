FROM eclipse-temurin:21-jdk

ENV APP_HOME=/usr/app
WORKDIR $APP_HOME

COPY target/*.jar app.jar

EXPOSE 5050

CMD ["java", "-jar", "app.jar"]
