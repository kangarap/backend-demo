FROM openjdk:8-jdk-alpine
VOLUME /tmp
ADD backend-0.0.1-SNAPSHOT.jar app.jar

RUN sed -i 's/dl-cdn.alpinelinux.org/mirrors.aliyun.com/g' /etc/apk/repositories

RUN set -xe \
&& apk --no-cache add ttf-dejavu fontconfig

EXPOSE 8088
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar", "--spring.profiles.active=dev"]