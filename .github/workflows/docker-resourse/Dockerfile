#FROM amazoncorretto:17-alpine-jdk
#VOLUME /tmp
#ARG JAR_FILE
#COPY /target/*.jar app.jar
#EXPOSE 2222
#ENTRYPOINT exec java $JAVA_OPTS -jar /app.jar $ARGS

FROM eclipse-temurin:17-jdk-alpine as build
WORKDIR /workspace/app

COPY ./mvnw .
COPY ./.mvn .mvn
COPY pom.xml .
COPY ../src src

RUN ./mvnw install -DskipTests
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
ARG DEPENDENCY=/workspace/app/target/dependency
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app
ENTRYPOINT ["java","-cp","app:app/lib/*","com.runapp.shoesservice.ShoesServiceApplication"]
