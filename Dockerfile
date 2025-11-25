# Stage 1: Build the application
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .

# Create toolchains.xml pointing at the JDK in this image
RUN mkdir -p /root/.m2 && \
    JDK_HOME="$(dirname "$(dirname "$(readlink -f "$(which javac)")")")" && \
    printf '%s\n' \
      '<toolchains>' \
      '  <toolchain>' \
      '    <type>jdk</type>' \
      '    <provides>' \
      '      <version>21</version>' \
      '    </provides>' \
      '    <configuration>' \
      "      <jdkHome>${JDK_HOME}</jdkHome>" \
      '    </configuration>' \
      '  </toolchain>' \
      '</toolchains>' \
      > /root/.m2/toolchains.xml

RUN mvn -B clean package -Pproduction -DskipTests


# Stage 2: Run the application
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
ENV PORT 10000
ENV PRODUCTION_MODE true
EXPOSE 10000
ENTRYPOINT ["sh", "-c", "java -jar app.jar --server.port=$PORT"]
