FROM sapmachine:lts
MAINTAINER Ruslan Vinogradov <get.hard.sp1@gmail.com>
WORKDIR /app
COPY /target/*.jar /app
EXPOSE 9094
CMD ["java", "-jar", "org-service.jar"]