FROM openjdk:8-jdk-alpine
MAINTAINER xuxueli
ADD target/xxl-job-admin-*.jar /app.jar

ENV TZ=PRC
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
ENV PARAMS=""
ENV JAVA_OPTS=""
ENTRYPOINT ["sh","-c","java $JAVA_OPTS $PARAMS -jar /app.jar","-ea","&"]
EXPOSE 8080
