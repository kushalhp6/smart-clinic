# =====================================================
# Stage 1: Build the Spring Boot application
# Uses Maven with JDK 17 to compile the source code
# and package the application into an executable JAR.
# =====================================================
FROM maven:3.9.6-eclipse-temurin-17 AS builder

# Set the working directory inside the container
WORKDIR /app

# Copy the Maven project configuration
COPY pom.xml .

# Copy the application source code
COPY src ./src

# Build the application and skip tests to speed up
# the Docker image creation process
RUN mvn clean package -DskipTests

# =====================================================
# Stage 2: Create a lightweight runtime image
# Uses only the JRE, reducing the final image size.
# =====================================================
FROM eclipse-temurin:17-jre

# Set the working directory for the runtime container
WORKDIR /app

# Copy the executable JAR generated in the build stage
COPY --from=builder /app/target/*.jar app.jar

# Document the port used by the Spring Boot application
EXPOSE 8080

# Start the Spring Boot application when the container launches
ENTRYPOINT ["java", "-jar", "app.jar"]
