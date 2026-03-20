FROM eclipse-temurin:21

LABEL developer="greyson renatus shawa"

WORKDIR /app

COPY target/library-0.0.1-SNAPSHOT.jar /app/library.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "library.jar"]