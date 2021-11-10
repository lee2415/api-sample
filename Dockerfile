FROM gradle:6.8.0-jdk11 AS cache
RUN mkdir -p /home/gradle/cache_home
ENV GRADLE_USER_HOME /home/gradle/cache_home
COPY build.gradle /home/gradle/java-code/
WORKDIR /home/gradle/java-code
RUN gradle clean build -x test -i --stacktrace

FROM gradle:6.8.0-jdk11 AS builder
COPY --from=cache /home/gradle/cache_home /home/gradle/.gradle
COPY . /usr/src/java-code/
WORKDIR /usr/src/java-code
RUN gradle bootJar -i --stacktrace

FROM openjdk:11-jre-slim
USER root
RUN mkdir /usr/src/java-app && chown -R nobody /usr/src/java-app
WORKDIR /usr/src/java-app
COPY --from=builder /usr/src/java-code/build/libs/*.jar ./spring-boot-application.jar
RUN touch /tmp/spring.log && ln -sf /dev/stdout /tmp/spring.log
ENTRYPOINT ["java", "-server", "-XX:+UnlockExperimentalVMOptions", "-XX:+UseContainerSupport", "-Djava.security.egd=file:/dev/./urandom", "-jar","spring-boot-application.jar"]
