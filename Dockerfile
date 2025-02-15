## Build stage ##
FROM maven:3.9.9-amazoncorretto-17-alpine AS build

WORKDIR /app

# Sao chép file cấu hình Maven để cache dependency
COPY . .
RUN mvn install -DskipTests=true




## Run stage ##
FROM eclipse-temurin:17-jre-alpine AS runtime

WORKDIR /run

# Sao chép file JAR từ build stage
COPY --from=build /app/target/apiGateway-0.0.1-SNAPSHOT.jar /run/apiGateway-0.0.1-SNAPSHOT.jar

# Mở cổng 8888
EXPOSE 8888

# Khởi chạy ứng dụng
ENTRYPOINT ["java", "-jar", "/run/apiGateway-0.0.1-SNAPSHOT.jar"]
