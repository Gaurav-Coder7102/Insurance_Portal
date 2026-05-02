# ============================================================
# Stage 1: Build the JAR using Maven
# ============================================================
FROM maven:3.9.6-eclipse-temurin-17 AS builder

# Set working directory inside container
WORKDIR /app

# Copy pom.xml first (for dependency caching - faster rebuilds)
COPY pom.xml .

# Download all dependencies (this layer is cached if pom.xml doesn't change)
RUN mvn dependency:go-offline -B

# Copy the rest of the source code
COPY src ./src

# Build the JAR, skip tests to speed up the build
RUN mvn clean package -DskipTests -B

# ============================================================
# Stage 2: Run the JAR in a lightweight JRE image
# ============================================================
FROM eclipse-temurin:17-jre-jammy

# Set working directory
WORKDIR /app

# Copy only the built JAR from Stage 1 (keeps final image small)
COPY --from=builder /app/target/insurance-portal-backend-1.0.0-SNAPSHOT.jar app.jar

# Expose the port your Spring Boot app runs on
EXPOSE 8080

# Start the application
ENTRYPOINT ["java", "-jar", "app.jar"]
