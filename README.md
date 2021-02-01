<h1 align="center">Baseweb</h1>

<div align="center">

Base infrastructure for web application.

[![License](https://img.shields.io/badge/License-BSD%203--Clause-blue.svg)](https://opensource.org/licenses/BSD-3-Clause)

</div>

## Installation on Window

Bạn cần có những thứ sau được cài đặt và cấu hình sẵn trước khi bắt đầu cài đặt project: [Apache Maven](https://maven.apache.org/), [PostgreSQL](https://www.postgresql.org/), [MongoDB](https://www.mongodb.com/), [Redis](https://redis.io/). Nếu chưa cài đặt, vui lòng xem hướng dẫn sau: 
  * [Hướng dẫn cài đặt Apache Maven]()
  * [Hướng dẫn cài đặt PostgreSQL](https://drive.google.com/file/d/1o15E-QNNgHeZK5F1N7h4FfxYpT3B9S92/view?usp=sharing)
  * [Hướng dẫn cài đặt MongoDB](https://drive.google.com/file/d/1pNgZmw8TBU3uSzaAwZiXiVW5dp6Pjw7i/view?usp=sharing)

Khi đã sẵn sàng cho quá trình cài đặt project, thực hiện lần lượt các bước sau:
### 1. Cài đặt Database
### 1.1. PostgreSQL
* Từ <b>Searchbar</b> trên thanh <b>Taskbar</b>, gõ <b>pgAdmin4</b> để tìm kiếm, chọn <b>pgAdmin4</b> từ danh sách kết quả để khởi động <b>pgAdmin</b>
* Trong <b>pgAdmin</b>, tạo mới một Database với tên tuỳ ý
* Sau khi tạo xong Database, click chuột phải vào Database vừa tạo, chọn <b>restore</b>, một hộp thoại sẽ mở ra
* Ở trường <b>Filename</b>, browse đến nơi tải xuống và chọn [file backup](https://drive.google.com/file/d/1GN1iLdSqfZSNO1LLeMpfl8q6PkXYmdFk/view?usp=sharing) (chú ý: chọn Format là <b>All Files</b> khi browse)
* Ở trường <b>Role name</b>, chọn <b>postgres</b> (option nằm ở cuối), sau đó chọn <b>Restore</b>
### 1.2. Cấu hình Mongo Replica Set
Với `{version}` là phiên bản MongoDB được cài đặt, ví dụ: 4.2, thực hiện lần lượt các bước sau:
* Click chuột phải vào thanh <b>Taskbar</b> → chọn <b>Task Manager</b> → chọn <b>Services</b> → tìm và click chuột phải vào <b>MongoDB</b> → chọn <b>Stop</b>
* Tạo thư mục: <b>C:\data\db</b> trong ổ C 
* Trong thư mục <b>db</b> tạo lần lượt 3 thư mục con: <b>mongo27017</b>, <b>mongo27018</b>, <b>mongo27019</b>
* Mở một Command Prompt (cmd), chạy lần lượt 2 lệnh: <br/>
`cd C:\Program Files\MongoDB\Server\{version}\bin` <br/>
`mongod --port 27017 --dbpath C:\data\db\mongo27017 --replSet rs0`
* Mở thêm một cmd mới, chạy lần lượt 2 lệnh: <br/>
`cd C:\Program Files\MongoDB\Server\{version}\bin` <br/>
`mongod --port 27018 --dbpath C:\data\db\mongo27018 --replSet rs0`
* Mở thêm một cmd mới, chạy lần lượt 2 lệnh: <br/>
`cd C:\Program Files\MongoDB\Server\{version}\bin` <br/>
`mongod --port 27019 --dbpath C:\data\db\mongo27019 --replSet rs0`
* Mở thêm một cmd mới, chạy lần lượt 5 lệnh: <br/>
`cd C:\Program Files\MongoDB\Server\{version}\bin` <br/>
`mongo` <br/>
`rs.initiate()` <br/>
`rs.add(“localhost:27018”)` <br/>
`rs.add(“localhost:27019”)` <br/>
### 2. Cấu hình project
* Import project vào <b>IntelliJ IDEA</b> hoặc <b>Eclipse</b> (optional, có thể không làm tại bước này)
* Copy [file cấu hình](https://drive.google.com/file/d/1cxurrBoNn6cNgOx_Q9i22meYtMP02iJN/view?usp=sharing) vào thư mục: <b>src\main\resources</b>
* Trong file cấu hình, điền mật khẩu và tên PostgreSQL Database được tạo ở 1.1 tương ứng cho các thuộc tính <b>SQL_DB_PASS</b> và <b>POSTGRES_DB</b>
### 3. Build project
### 3.1. Khởi động các dịch vụ (redis, mongo replica set)
* Chạy file <b>redis-server.exe</b> trong thư mục [redis-2.4.5](https://drive.google.com/drive/folders/1WilP451UfPN33uM1RSUreCX9rJmVVbMK?usp=sharing)<b>\64bit</b> để khởi động redis
* Chạy file [sscm.bat](https://drive.google.com/file/d/1D5ZRsY0S8-hAPjEZX6x2DwDrjZs7NqLQ/view?usp=sharing) để khởi động mongo replica set (lưu ý: nếu phiên bản MongoDB được cài đặt khác 4.2 thì cần thay thế tất cả 4.2 trong nội dung file thành phiên bản đươc cài, ví dụ: 4.4)
### 3.2. Build
* Mở Git Bash và `cd` đến thư mục project
* Chạy lệnh: `mvn clean package`
* Chờ đến khi quá trình build thành công và xuất hiện thông báo <b>BUILD_SUCCESS</b>
### 4. Chạy project
* Trong thư mục: <b>src\main\java\com\hust\baseweb</b>, chạy file <b>BasewebApplication.java</b>

Sau lần chạy thành công đầu tiên, ở các lần chạy sau chỉ cần thực hiện lần lượt bước 3.1 và 4
### 5. Cài đặt OR-Tools
* Tải về cài đặt bộ cài: windows: [https://github.com/google/or-tools/releases/download/v8.0/or-tools_VisualStudio2019-64bit_v8.0.8283.zip] và giải nén
* Vào thư mục đã giải nén chạy các lệnh sau (yêu cầu đã có Apache Maven):
```
mvn install:install-file -Dfile=ortools-win32-x86-64-8.0.8283.jar -DpomFile=pom-runtime.xml
mvn install:install-file -Dfile=ortools-java-8.0.8283.jar -DpomFile=pom-local.xml
```

Sau lần chạy thành công đầu tiên, ở các lần chạy sau chỉ cần thực hiện lần lượt bước 3.1 và 4
### 6. Tài nguyên
* [Installers](https://drive.google.com/drive/folders/1r4VCwCz2JZGg9-LxQFPNw1aTZJl9gYp3?usp=sharing)
