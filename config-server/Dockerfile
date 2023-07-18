FROM sapmachine:lts
MAINTAINER Ruslan Vinogradov <get.hard.sp1@gmail.com>
WORKDIR /app
COPY /target/*.jar /app
EXPOSE 8081
CMD ["java", "-jar", "config-server.jar"]