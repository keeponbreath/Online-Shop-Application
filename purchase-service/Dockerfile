FROM sapmachine:lts
MAINTAINER Ruslan Vinogradov <get.hard.sp1@gmail.com>
WORKDIR /app
COPY /target/*.jar /app
EXPOSE 9096
CMD ["java", "-jar", "purchase-service.jar"]