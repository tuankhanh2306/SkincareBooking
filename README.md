# Skincare Booking System 🧴💻

> **Hệ thống đặt lịch dịch vụ chăm sóc da** – Một ứng dụng web giúp khách hàng đặt lịch hẹn, quản lý dịch vụ và thanh toán.

## 🚀 Giới thiệu
Ứng dụng này hỗ trợ trung tâm chăm sóc da trong việc quản lý khách hàng, dịch vụ và lịch hẹn.

## 📌 Tính năng chính
- 📅 Đặt lịch hẹn với chuyên gia chăm sóc da.
- 📝 Gợi ý dịch vụ dựa trên bảng câu hỏi.
- 💰 Thanh toán và áp dụng chính sách khuyến mãi.
- 📊 Quản lý phản hồi của khách hàng.
- 👥 Quản lý hồ sơ khách hàng và báo cáo thống kê.

## 🏗️ Công nghệ sử dụng
- **Backend**: Java Spring Boot, Spring Data JPA, Hibernate
- **Frontend**: HTML, CSS, JavaScript (có thể dùng Thymeleaf hoặc React/Vue)
- **Database**: MySQL
- **Dev Tools**: IntelliJ IDEA, Git, Maven

## ⚙️ Cài đặt và chạy dự án

### 1. Clone repository
```sh
git clone https://gitlab.com/tuankhanh2306/skin-care-management-software.git
cd skin-care-management-software
```

### 2. Cấu hình cơ sở dữ liệu
- Sử dụng MySQL, tạo database:
  ```sql
  CREATE DATABASE dermatology_center;
  ```
- Cập nhật thông tin trong `application.properties`:
  ```properties
  spring.datasource.url=jdbc:mysql://localhost:3306/dermatology_center
  spring.datasource.username=root
  spring.datasource.password=1234
  ```

### 3. Chạy dự án
```sh
mvn spring-boot:run
```
Mở trình duyệt và truy cập: [http://localhost:8080](http://localhost:8080)

## 🛠 Cấu trúc thư mục
```
📦 dermatology-center
 ┣ 📂 src
 ┃ ┣ 📂 main
 ┃ ┃ ┣ 📂 java
 ┃ ┃ ┃ ┗ 📂 com.example.dermatologycenter     # Tên package chính của bạn
 ┃ ┃ ┃ ┃ ┣ 📂 config
 ┃ ┃ ┃ ┃ ┃ ┗ 🔹 SecurityConfig.java           # Cấu hình bảo mật, Spring Security
 ┃ ┃ ┃ ┃ ┣ 📂 controllers
 ┃ ┃ ┃ ┃ ┃ ┣ 🔹 AuthController.java            # API xử lý đăng ký/đăng nhập
 ┃ ┃ ┃ ┃ ┃ ┣ 🔹 UserController.java            # API cho người dùng (khách hàng, admin)
 ┃ ┃ ┃ ┃ ┃ ┣ 🔹 ServiceController.java         # API dịch vụ chăm sóc da
 ┃ ┃ ┃ ┃ ┃ ┣ 🔹 SpecialistController.java      # API chuyên viên trị liệu da
 ┃ ┃ ┃ ┃ ┃ ┣ 🔹 AppointmentController.java     # API lịch hẹn
 ┃ ┃ ┃ ┃ ┃ ┣ 🔹 PaymentController.java         # API thanh toán
 ┃ ┃ ┃ ┃ ┃ ┗ 🔹 FeedbackController.java        # API đánh giá
 ┃ ┃ ┃ ┃ ┣ 📂 models
 ┃ ┃ ┃ ┃ ┃ ┣ 🔹 User.java                      # Entity bảng users
 ┃ ┃ ┃ ┃ ┃ ┣ 🔹 Service.java                   # Entity bảng services
 ┃ ┃ ┃ ┃ ┃ ┣ 🔹 Specialist.java                # Entity bảng specialists
 ┃ ┃ ┃ ┃ ┃ ┣ 🔹 Appointment.java               # Entity bảng appointments
 ┃ ┃ ┃ ┃ ┃ ┣ 🔹 Payment.java                   # Entity bảng payments
 ┃ ┃ ┃ ┃ ┃ ┗ 🔹 Feedback.java                  # Entity bảng feedbacks
 ┃ ┃ ┃ ┃ ┣ 📂 repositories
 ┃ ┃ ┃ ┃ ┃ ┣ 🔹 UserRepository.java            # Giao diện thao tác với bảng users
 ┃ ┃ ┃ ┃ ┃ ┣ 🔹 ServiceRepository.java         # Giao diện thao tác với bảng services
 ┃ ┃ ┃ ┃ ┃ ┣ 🔹 SpecialistRepository.java      # Giao diện thao tác với bảng specialists
 ┃ ┃ ┃ ┃ ┃ ┣ 🔹 AppointmentRepository.java     # Giao diện thao tác với bảng appointments
 ┃ ┃ ┃ ┃ ┃ ┣ 🔹 PaymentRepository.java         # Giao diện thao tác với bảng payments
 ┃ ┃ ┃ ┃ ┃ ┗ 🔹 FeedbackRepository.java        # Giao diện thao tác với bảng feedbacks
 ┃ ┃ ┃ ┃ ┣ 📂 services
 ┃ ┃ ┃ ┃ ┃ ┣ 🔹 AuthService.java               # Xử lý logic đăng nhập, mã hóa mật khẩu
 ┃ ┃ ┃ ┃ ┃ ┣ 🔹 UserService.java               # Xử lý logic người dùng
 ┃ ┃ ┃ ┃ ┃ ┣ 🔹 ServiceService.java            # Xử lý logic dịch vụ chăm sóc da
 ┃ ┃ ┃ ┃ ┃ ┣ 🔹 SpecialistService.java         # Xử lý logic chuyên viên trị liệu
 ┃ ┃ ┃ ┃ ┃ ┣ 🔹 AppointmentService.java        # Xử lý logic đặt lịch và quy trình
 ┃ ┃ ┃ ┃ ┃ ┣ 🔹 PaymentService.java            # Xử lý logic thanh toán và chính sách hủy
 ┃ ┃ ┃ ┃ ┃ ┗ 🔹 FeedbackService.java           # Xử lý logic feedback & rating
 ┃ ┃ ┃ ┃ ┗ 🔹 DermatologyCenterApplication.java # File main để chạy ứng dụng
 ┃ ┃ ┗ 📂 resources
 ┃ ┃ ┃ ┣ 📂 static                             # CSS, JS, hình ảnh frontend
 ┃ ┃ ┃ ┣ 📂 templates                          # Giao diện Thymeleaf (nếu dùng)
 ┃ ┃ ┃ ┣ 🔹 application.properties             # Thông tin cấu hình hệ thống
 ┃ ┃ ┃ ┣ 🔹 schema.sql                         # Tạo bảng DB khi khởi động
 ┃ ┃ ┃ ┗ 🔹 data.sql                           # Dữ liệu mẫu: tài khoản, dịch vụ, chuyên viên
 ┃ ┗ 📂 test
 ┃ ┃ ┗ 📂 java/com/example/dermatologycenter  # Viết unit test, integration test
 ┃ ┃ ┃ ┗ 🔹 AppointmentServiceTest.java        # Ví dụ unit test
 ┣ 📂 target                                   # Thư mục build bởi Maven
 ┣ 📄 pom.xml                                  # Cấu hình Maven, dependencies (Spring Boot, JPA, MySQL, Security, v.v.)
 ┗ 📄 README.md                                # Giới thiệu, hướng dẫn cài đặt và sử dụng

```

## 👨‍💻 Thành viên nhóm
- 🏆 **Nhóm 5 người - Đồ án môn học**


