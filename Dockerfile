# Sử dụng một ảnh Java (image) làm nền tảng (chọn phiên bản 17 hoặc 21)
FROM openjdk:21-slim

# Đặt thư mục làm việc bên trong container
WORKDIR /app

# Sao chép tệp .jar đã được build từ máy của bạn vào container
# Dòng này đã ĐÚNG vì bạn đã build tệp .jar ở bước trước
COPY target/skincare-booking-system-0.0.1-SNAPSHOT.jar app.jar

# Cho Docker biết rằng ứng dụng của bạn sẽ chạy trên cổng 8080
EXPOSE 8080

# LỖI CŨ: ENTRYPOINT ["java", "-jar", "/app.jar"] (sai đường dẫn)
# Vì WORKDIR là /app, tệp app.jar sẽ nằm ở /app/app.jar
# SỬA LẠI: Dùng đường dẫn tương đối "app.jar" là đủ
ENTRYPOINT ["java", "-jar", "app.jar"]