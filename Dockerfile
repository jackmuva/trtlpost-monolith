FROM eclipse-temurin:17

LABEL maintainer="jackmu@umich.edu"

WORKDIR /app

COPY target/trtlpost-monolith.jar /app/trtlpost-monolith.jar

ENTRYPOINT ["java", "-jar", "trtlmail-rest.jar"]