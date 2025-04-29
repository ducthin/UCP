# Hướng Dẫn Cấu Hình Cơ Sở Dữ Liệu Railway

Hướng dẫn này giải thích cách thiết lập ứng dụng Spring Boot của bạn để kết nối với cơ sở dữ liệu MySQL trên Railway.

## Điều Kiện Tiên Quyết

1. Bạn cần có tài khoản Railway (https://railway.app/).
2. Kiến thức cơ bản về Spring Boot và MySQL.

## Các Bước Thiết Lập Cơ Sở Dữ Liệu Railway

### 1. Tạo Dự Án Railway

1. Đăng nhập vào Railway (https://railway.app/).
2. Nhấp vào "New Project" và chọn "Provision MySQL" từ các tùy chọn.
3. Đợi Railway cung cấp cơ sở dữ liệu MySQL của bạn.

### 2. Lấy Thông Tin Đăng Nhập Cơ Sở Dữ Liệu

Sau khi cơ sở dữ liệu MySQL của bạn được cung cấp, bạn sẽ cần các thông tin đăng nhập sau:

- URL cơ sở dữ liệu
- Tên người dùng
- Mật khẩu
- Tên cơ sở dữ liệu

Những thông tin này có thể được tìm thấy trong tab "Connect" của dịch vụ MySQL trên bảng điều khiển Railway.

### 3. Cấu Hình Biến Môi Trường

Railway cho phép bạn thiết lập các biến môi trường. Thiết lập các biến sau:

- `RAILWAY_DATABASE_URL`: URL JDBC cho cơ sở dữ liệu MySQL của bạn
  Định dạng: `jdbc:mysql://<HOST>:<PORT>/<DATABASE_NAME>`
- `RAILWAY_DATABASE_USERNAME`: Tên người dùng cơ sở dữ liệu của bạn
- `RAILWAY_DATABASE_PASSWORD`: Mật khẩu cơ sở dữ liệu của bạn

### 4. Triển Khai Ứng Dụng Của Bạn

Bạn có hai lựa chọn:

#### Lựa chọn 1: Triển khai sử dụng Railway CLI

1. Cài đặt Railway CLI: `npm i -g @railway/cli`
2. Đăng nhập: `railway login`
3. Liên kết với dự án của bạn: `railway link`
4. Triển khai: `railway up`

#### Lựa chọn 2: Triển khai sử dụng Tích hợp GitHub

1. Kết nối repository GitHub của bạn với Railway
2. Railway sẽ tự động triển khai khi bạn đẩy code lên repository

### 5. Xác Minh Kết Nối

Sau khi triển khai, kiểm tra nhật ký ứng dụng của bạn để xác minh rằng ứng dụng đã kết nối thành công với cơ sở dữ liệu.

## Phát Triển Cục Bộ

Tệp application.properties được cấu hình để sử dụng MySQL cục bộ nếu các biến môi trường Railway không được thiết lập.

```
spring.datasource.url=${RAILWAY_DATABASE_URL:jdbc:mysql://localhost:3306/ucp_db?createDatabaseIfNotExist=true}
spring.datasource.username=${RAILWAY_DATABASE_USERNAME:root}
spring.datasource.password=${RAILWAY_DATABASE_PASSWORD:ducthinh123}
```

Điều này có nghĩa là ứng dụng của bạn sẽ hoạt động tốt cả trên Railway và trong môi trường phát triển cục bộ của bạn.

## Lưu Ý Quan Trọng

- Đảm bảo rằng ứng dụng của bạn có phiên bản tương thích với cơ sở hạ tầng của Railway.
- Tệp `railway.json` trong thư mục gốc dự án của bạn giúp Railway hiểu cách xây dựng và triển khai ứng dụng của bạn.
- Luôn sử dụng biến môi trường cho thông tin nhạy cảm như thông tin đăng nhập cơ sở dữ liệu.
