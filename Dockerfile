# ----- GIAI ĐOẠN 1: BUILD -----
# Sử dụng image Maven & Java 17 để build dự án
FROM maven:3.8.5-openjdk-17 AS build

# Đặt thư mục làm việc
WORKDIR /app

# Copy toàn bộ code dự án vào container
COPY . .

# Chạy lệnh Maven để build ra file .jar, bỏ qua test
RUN mvn clean package -DskipTests

# ----- GIAI ĐOẠN 2: RUN -----
# Sử dụng một image Java 17 nhỏ (slim) để chạy
FROM openjdk:17-jdk-slim

# Đặt thư mục làm việc
WORKDIR /app

# Lấy PORT từ biến môi trường của Render (Render.yaml đã đặt là 8080)
ENV PORT 8080
EXPOSE 8080

# Copy file .jar đã được build ở Giai đoạn 1 vào
# (Tìm bất kỳ file .jar nào trong target và đổi tên thành app.jar)
COPY --from=build /app/target/*.jar app.jar

# Lệnh khởi động để chạy ứng dụng
ENTRYPOINT ["java","-jar","app.jar"]