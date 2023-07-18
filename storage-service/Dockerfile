FROM sapmachine:lts
MAINTAINER Ruslan Vinogradov <get.hard.sp1@gmail.com>
WORKDIR /app
COPY /target/*.jar /app
EXPOSE 9097
CMD ["java", "-jar", "storage-service.jar"]