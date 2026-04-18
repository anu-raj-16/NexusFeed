# Stage 1: Build the JAR
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

# 1. Copy the pom.xml first to cache dependencies (Faster builds)
COPY pom.xml .
RUN mvn dependency:go-offline

# 2. Copy the actual source code
COPY src ./src

# 3. Build the project
RUN mvn clean package -DskipTests

# Stage 2: Run
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# 4. Copy the database file into the image so Java can see it
# If the db is in the ROOT of your repo, use this:
COPY ../jobs.db . 

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
