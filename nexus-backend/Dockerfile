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
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copy the built Java app
COPY --from=build /app/target/*.jar app.jar

# COPY THE DATABASE (Crucial!)
# If your DB is in the root of your repo, use this:
COPY jobs.db . 

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]

