# ✅ Step 1: Use Java 21 for build
FROM maven:3.9.6-eclipse-temurin-21 AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn -B -DskipTests clean package

# ✅ Step 2: Use Java 21 JRE to run the app
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar

ENV PORT=8080
ENTRYPOINT ["sh", "-c", "java -jar app.jar --server.port=$PORT"]
