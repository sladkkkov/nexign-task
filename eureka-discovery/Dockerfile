FROM eclipse-temurin:11-alpine as jre-build

RUN $JAVA_HOME/bin/jlink \
         --add-modules java.base,java.desktop,java.logging,java.management,java.naming,java.security.jgss,java.instrument,java.sql,jdk.crypto.cryptoki,jdk.unsupported \
         --output /java

FROM alpine:3.17.3

ARG PRODUCT_NAME=eureka-discovery
ARG USER_UID=1001
ARG USER_GID=1001

ARG USER_NAME=$PRODUCT_NAME
ARG USER_GROUP=$PRODUCT_NAME
ARG USER_HOME=/home/$USER_NAME

RUN addgroup -g $USER_GID $USER_GROUP && \
    adduser -u $USER_UID -G $USER_GROUP -h $USER_HOME -s /bin/sh -D $USER_NAME && \
    chown -R $USER_NAME:$USER_GROUP $USER_HOME

USER $USER_NAME
WORKDIR $USER_HOME

ENV JAVA_HOME=${USER_HOME}/opt/java/openjdk
ENV PATH "${JAVA_HOME}/bin:${PATH}"
COPY --from=jre-build /java $JAVA_HOME

ARG JAR_FILE=target/*.jar
ENV JAR_FILE ${JAR_FILE}

COPY --chown=$USER_NAME:$USER_GROUP ${JAR_FILE} eureka-discovery-1.0-SNAPSHOT.jar

ENTRYPOINT ["java","-jar","eureka-discovery-1.0-SNAPSHOT.jar"]










