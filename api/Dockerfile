# Stage 1: Build the application
FROM maven:3 AS build
WORKDIR /home/app
COPY pom.xml .
# Use a volume for the Maven repository
VOLUME ["/root/.m2"]
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn package -DskipTests

# Stage 2: Run the application
FROM eclipse-temurin:17
COPY --from=build /home/app/target/*.jar app.jar
# Download wait-for-it.sh and make it executable
ADD https://raw.githubusercontent.com/vishnubob/wait-for-it/master/wait-for-it.sh wait-for-it.sh
RUN chmod +x wait-for-it.sh
EXPOSE 8080
# Use wait-for-it.sh before starting the application
ENTRYPOINT ["./wait-for-it.sh", "db:6379", "--", "java", "-jar", "/app.jar"]
