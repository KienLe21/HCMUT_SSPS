# A SMART PRINTING SERVICE FOR STUDENTS AT HCMUT
![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white) ![Spring](https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white) ![MySQL](https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white)

- [BACKEND SERVER]()
- [API docs]()

## Thông tin dự án
Ứng dụng này được hiện thực để phục vụ cho môn học Công nghệ phần mềm, thuộc về Trường Đại học Bách khoa, ĐHQG TP.HCM. Dự án hướng đến mục tiêu xây dựng nên một trang web cung cấp dịch vụ in ấn tiện lợi cho sinh viên của trường.

### Tính năng:
#### 1. Sinh Viên:
- **Đăng nhập:** Sinh viên cần đăng nhập qua HCMUT_SSO để xác thực trước khi sử dụng hệ thống.
- **Tải tệp tài liệu:** Chỉ được tải lên các tệp phù hợp, đảm bảo kích thước file nằm trong giới hạn cho phép.
- **Cấu hình in ấn:** Sinh viên chọn máy in, định dạng bản in (khổ giấy, in 1 hay 2 mặt, số lượng bản sao, số trang).
- **Kiểm soát số trang in:** Hệ thống chỉ cho phép in số trang nằm trong giới hạn còn lại của tài khoản. Nếu không đủ, sẽ yêu cầu nạp thêm tiền.
- **Xem trạng thái tài khoản:** Hiển thị số lượng trang A4 còn lại trong tài khoản.
- **Thanh toán:** Cung cấp tính năng mua thêm số trang in qua các hệ thống thanh toán trực tuyến như BKPay.
- **Lịch sử in:** Sinh viên có thể xem lịch sử in của mình trong một khoảng thời gian, kèm theo bản tóm tắt số trang đã in theo từng khổ giấy.
- **Đánh giá dịch vụ:** Cho phép sinh viên đánh giá chất lượng dịch vụ in.
- **Thông báo:** Gửi thông báo khi hoàn thành in tài liệu hoặc khi hệ thống ngừng hoạt động.

---

#### 2. SPSO (Quản Lý Dịch Vụ In):
- **Đăng nhập:** Sử dụng HCMUT_SSO để xác thực trước khi sử dụng hệ thống.
- **Quản lý tệp tài liệu:** Quy định loại và kích thước tệp được phép tải lên để in (thêm, xóa, chỉnh sửa).
- **Lịch sử in:** Xem lịch sử in của tất cả sinh viên trong một khoảng thời gian, bao gồm các thông tin:
  - Mã số sinh viên.
  - ID máy in.
  - Tên tệp tin.
  - Thời gian bắt đầu và kết thúc in.
  - Số trang cho từng khổ giấy.
  - Ca trực của nhân viên.
- **Báo cáo sử dụng:** Tạo và xem báo cáo sử dụng hệ thống in theo tháng, năm, với các tùy chọn:
  - Theo từng sinh viên, nhóm sinh viên, hoặc toàn bộ sinh viên.
  - Theo một hoặc nhiều máy in.
- **Điều chỉnh số trang mặc định:** Thay đổi số lượng trang in mặc định mỗi học kỳ, đặc biệt vào đầu học kỳ.
- **Quản lý máy in:** Theo dõi tình trạng máy in để thêm mới, bật hoặc tắt khi cần.

---

#### 3. Nhân Viên Phòng In:
- **Đăng nhập:** Xác thực qua HCMUT_SSO trước khi sử dụng hệ thống.
- **Xử lý yêu cầu in:** Nhận thông tin và thực hiện in tài liệu theo yêu cầu của sinh viên.
- **Báo cáo tình trạng máy in:** Cập nhật tình trạng hoạt động của máy in, đề xuất thêm mới, bật hoặc tắt máy.
- **Xác nhận tình trạng in:** Ghi nhận trạng thái hoàn thành của tài liệu (đã in xong hoặc gặp lỗi). Đối với lỗi, cung cấp thời gian dự kiến để in lại.
- **Thông báo hết hạn:** Thông báo cho sinh viên nếu sắp hết hạn lấy tài liệu. Sau thời gian quy định, tài liệu chưa lấy sẽ bị hủy.
- **Lịch làm việc:** Đăng ký ca làm việc, xem lịch trực và xin nghỉ khi có việc đột xuất.

### Ngôn ngữ và công nghệ
- ![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white) **Ngôn ngữ:** Java
- ![MySQL](https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white) **Cơ sở dữ liệu:** MySQL
- ![Spring](https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white) **Công nghệ:** Spring Boot
### Hướng dẫn cài đặt
Các úng dụng cần cài đặt trước khi chạy ứng dụng:
- ![Java](https://www.oracle.com/java/technologies/downloads/)
- ![Apache maven](https://maven.apache.org/)
- ![MySQL](https://www.mysql.com/)

## Lệnh cần chạy
```bash
git clone https://github.com/KienLe21/HCMUT_SSPS.git
mvn spring-boot:run
```

Tại vị trí thư mục gốc của ứng dụng (hcmut-ssps), mở file cấu hình ứng dụng tại đường dẫn `server/src/main/resources/application.properties`, thay đổi các dòng sau với username và password của tài khoản MySQL mà bạn muốn sử dụng:
```properties
spring.datasource.username=...
spring.datasource.password=...
```
## Thành viên nhóm
| STT | MSSV    | Tên Thành Viên            | Role |
|-----|---------|----------------------------|------|
| 1   | 2213836 | Lê Thanh Tuyển             | Dev FE  |
| 2   | 2213132 | Nguyễn Công Thành          | Dev BE |
| 3   | 2212880 | Đặng Thị Quỳnh             | Dev FE |
| 4   | 2212631 | Nguyễn Ngọc Châu Phúc      | Dev FE |
| 5   | 2210805 | Nguyễn Việt Đức            | Dev FE |
| 6   | 2210768 | Nguyễn Văn Đoàn            | Dev FE |
| 7   | 2213609 | Phan Duệ Triết             | Dev BE |
| 8   | 2212307 | Nguyễn Lâm Nguyên          | Dev BE |
| 9   | 2211721 | Lê Hữu Kiên                | Dev BE |
| 10  | 2210871 | Quách Khải Hào             | Dev BE |
