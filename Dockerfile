FROM eclipse-temurin:21

#WORKDIR /target
# Copy the pre-built JAR file to the container
COPY target/mediLaboSolutionFront-0.0.1-SNAPSHOT.jar app.jar
#EXPOSE 8090


# Define the default command to run the JAR file
CMD ["java", "-jar", "app.jar"]