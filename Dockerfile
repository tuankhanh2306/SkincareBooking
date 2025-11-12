# Giai đoạn 1: Build dự án bằng Maven
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Giai đoạn 2: Chạy ứng dụng
FROM openjdk:17-jdk-slim
WORKDIR /app

# Lấy PORT từ biến môi trường của Render
ENV PORT 8080
EXPOSE 8080

# Copy file .jar từ giai đoạn build
COPY --from=build /app/target/*.jar app.jar

# Lệnh khởi động
ENTRYPOINT ["java","-jar","app.jar"]