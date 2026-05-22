# Spring Boot + Thymeleaf + JPA + MySQL Project Documentation

## 1. Kiến trúc hệ thống

Dự án được xây dựng theo mô hình nhiều layer để tách biệt trách nhiệm giữa xử lý giao diện, business logic và truy cập dữ liệu.

---

# Tổng quan kiến trúc

```text
Client Browser
    ↓
Controller Layer
    ↓
Service Layer
    ↓
Repository / JPA Layer
    ↓
MySQL Database
```

---

# Các package chính

## `configuration`

Chứa các class cấu hình của ứng dụng.

Ví dụ:

* Spring Security configuration
* DataLoader để seed dữ liệu mặc định
* Bean configuration

Vai trò:

* Khởi tạo ứng dụng
* Cấu hình bảo mật
* Cấu hình authentication/authorization
* Khởi tạo role mặc định

---

## `domain`

Chứa các Entity ánh xạ với database.

Ví dụ:

* `User`
* `Role`

Vai trò:

* Mapping bảng database bằng JPA/Hibernate
* Khai báo relationship (`@ManyToMany`)
* Đại diện cho dữ liệu persistence

Ví dụ:

```java
@ManyToMany(cascade = CascadeType.MERGE)
@JoinTable(name = "users_roles")
private List<Role> roles;
```

---

## `service`

Chứa business logic.

Ví dụ:

* `UserService`
* `RoleService`

Vai trò:

* Validate dữ liệu
* Xử lý nghiệp vụ
* Gọi repository thao tác DB
* Tách business logic khỏi controller

Ví dụ:

```text
Controller → gọi UserService
UserService → xử lý logic → save User
```

---

## `service.userDetails`

Tích hợp Spring Security.

Vai trò:

* Load thông tin user khi login
* Mapping role thành authority
* Authentication

Class thường gặp:

```text
CustomUserDetailsService
```

---

## `web.controllers`

Xử lý HTTP request.

Được chia thành:

### `viewControllers`

Trả về Thymeleaf view.

Ví dụ:

* Login page
* User page
* Role page

### `restApiControllers`

Trả JSON API.

Ví dụ:

* `/adminPage/json-users`

Vai trò:

* Nhận request
* Validate dữ liệu đầu vào
* Gọi service
* Trả về view hoặc JSON

---

## `web.dto`

Chứa DTO object.

Vai trò:

* Tách dữ liệu request/response khỏi entity
* Tránh expose entity trực tiếp
* Validate form

Ví dụ:

```text
RegisterDto
UserDto
```

---

## `web.paging`

Xử lý pagination và searching.

Vai trò:

* Phân trang dữ liệu
* Sort
* Search/filter

---

# 2. Danh sách Endpoint

## Public Endpoints

| Method | Endpoint               | Chức năng      |
| ------ | ---------------------- | -------------- |
| GET    | `/`                    | Trang chủ      |
| GET    | `/index`               | Trang chủ      |
| GET    | `/login`               | Trang login    |
| GET    | `/login-error`         | Login fail     |
| GET    | `/register`            | Trang đăng ký  |
| POST   | `/submit-registration` | Submit đăng ký |

---

## User Management

Base URL:

```text
/adminPage
```

| Method | Endpoint                   | Chức năng      |
| ------ | -------------------------- | -------------- |
| GET    | `/adminPage/users`         | Danh sách user |
| GET    | `/adminPage/users/{id}`    | Chi tiết user  |
| POST   | `/adminPage/users/{id}`    | Update user    |
| GET    | `/adminPage/users/newUser` | Form tạo user  |
| POST   | `/adminPage/users/newUser` | Tạo user       |

---

## Role Management

| Method | Endpoint                   | Chức năng      |
| ------ | -------------------------- | -------------- |
| GET    | `/adminPage/roles`         | Danh sách role |
| GET    | `/adminPage/roles/{id}`    | Chi tiết role  |
| POST   | `/adminPage/roles/{id}`    | Update role    |
| GET    | `/adminPage/roles/newRole` | Form tạo role  |
| POST   | `/adminPage/roles/newRole` | Tạo role       |

---

## REST API

| Method | Endpoint                | Chức năng                    |
| ------ | ----------------------- | ---------------------------- |
| GET    | `/adminPage/json-users` | Trả danh sách user dạng JSON |

---

# 3. Data Flow của chức năng đăng ký User

Ví dụ flow:

```text
User mở trang /register
        ↓
RegisterController trả về register.html
        ↓
User nhập form đăng ký
        ↓
POST /submit-registration
        ↓
RegisterController nhận request
        ↓
Validate DTO
        ↓
Gọi UserService.save()
        ↓
UserService encode password
        ↓
UserService gọi UserRepository.save()
        ↓
Hibernate/JPA persist xuống database
        ↓
Trả kết quả về Controller
        ↓
Redirect sang login page
```

---

# Data Flow chi tiết

## Bước 1 — Request

Browser gửi:

```http
POST /submit-registration
```

Body:

```text
username
email
password
```

---

## Bước 2 — Controller

`RegisterController`

Nhiệm vụ:

* Nhận form data
* Validate dữ liệu
* Gọi service

---

## Bước 3 — Service Layer

`UserService`

Nhiệm vụ:

* Check username tồn tại
* Encode password bằng BCrypt
* Gán role mặc định
* Save user

---

## Bước 4 — Persistence Layer

JPA/Hibernate:

```text
User entity
↓
INSERT INTO users
↓
INSERT INTO users_roles
```

---

## Bước 5 — Response

Controller redirect:

```text
/login
```

Người dùng đăng nhập hệ thống.

---

# 4. Đề xuất cải tiến

## A. Cải tiến kỹ thuật

### 1. Chuyển sang Spring Data Repository chuẩn

Đề xuất:

* Tạo interface:

```java
public interface UserRepository extends JpaRepository<User, Long>
```

Lợi ích:

* Giảm code CRUD
* Pagination built-in
* Sorting built-in
* Query method mạnh hơn
* Maintain dễ hơn

---

### 2. Tách DTO và Entity rõ ràng hơn

Hiện một số flow có thể đang thao tác trực tiếp với entity.

Đề xuất:

```text
Request DTO
↓
Mapper
↓
Entity
```

Lợi ích:

* Bảo mật tốt hơn
* Không expose entity
* Dễ validate
* API clean hơn

Có thể dùng:

```text
MapStruct
ModelMapper
```

---

## B. Cải tiến UX/UI

### 1. Thêm notification/toast message

Hiện tại thao tác create/update chưa có feedback UX tốt.

Đề xuất:

```text
User created successfully
Role updated successfully
Invalid password
```

Dùng:

* Bootstrap Toast
* SweetAlert2

Lợi ích:

* UX trực quan hơn
* Người dùng biết trạng thái thao tác

---

### 2. Thêm search + pagination realtime

Danh sách user/role hiện tại có thể khó sử dụng khi dữ liệu lớn.

Đề xuất:

* Search theo username/email
* Pagination
* Sort theo cột
* AJAX loading

Ví dụ:

```text
Search user by email
Sort by role
Page size selector
```

Lợi ích:

* Tăng usability
* Quản lý dữ liệu lớn tốt hơn
* Giảm thời gian tải trang



# 5. Kết luận

Dự án đã áp dụng tương đối tốt mô hình layered architecture của Spring Boot:

* Controller xử lý request
* Service xử lý business logic
* Entity mapping database
* Spring Security xử lý authentication
* Thymeleaf render UI

Các điểm mạnh:

* Cấu trúc package rõ ràng
* Có phân quyền role
* Có authentication
* Có REST API và View Controller tách biệt

Các hướng nâng cấp tiếp theo:

* DTO + Mapper chuẩn hóa
* API validation tốt hơn
* AJAX/UI hiện đại hơn
* Pagination/Search tối ưu
* Logging và exception handling tập trung
