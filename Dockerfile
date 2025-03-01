# Stage 1 - Build Angular
#=========================
FROM node:23 AS ng-build

WORKDIR /src

RUN npm i -g @angular/cli

COPY client/public public
COPY client/src src
COPY client/*.json .

RUN npm ci && ng build


# Stage 2 - Build SpringBoot
#============================
FROM openjdk:23-jdk AS j-build

WORKDIR /src

COPY server/.mvn .mvn
COPY server/src src
COPY server/mvnw .
COPY server/pom.xml .

# Copy all angular app files over 
COPY --from=ng-build /src/dist/day35-open-weather/browser/* src/main/resources/static

RUN chmod a+x mvnw && ./mvnw package -Dmaven.test.skip=true


# Stage 3 - Build Full App
#==========================
FROM openjdk:23-jdk

WORKDIR /app

COPY --from=j-build /src/target/day35_owm-0.0.1-SNAPSHOT.jar app.jar

ENV PORT=8080
ENV OPENWEATHERMAP_API_KEY=
ENV SPRING_DATA_REDIS_HOST=localhost
ENV SPRING_DATA_REDIS_PORT=6379
ENV SPRING_DATA_REDIS_DATABASE=0
ENV SPRING_DATA_REDIS_USERNAME=
ENV SPRING_DATA_REDIS_PASSWORD=

EXPOSE ${PORT}

SHELL ["/bin/sh", "-c"]
ENTRYPOINT SERVER_PORT=${PORT} java -jar app.jar