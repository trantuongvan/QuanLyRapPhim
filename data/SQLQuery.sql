IF NOT EXISTS (SELECT name FROM sys.databases WHERE name = N'QLRapChieuPhim')
BEGIN
    CREATE DATABASE QLRapChieuPhim;
END
GO

USE QLRapChieuPhim;
GO

DROP TABLE IF EXISTS ChiTietHoaDon;
DROP TABLE IF EXISTS TaiKhoan;
DROP TABLE IF EXISTS HoaDon;
DROP TABLE IF EXISTS Ve;
DROP TABLE IF EXISTS SuatChieu;
DROP TABLE IF EXISTS Ghe;
DROP TABLE IF EXISTS Rap;
DROP TABLE IF EXISTS Phim;
DROP TABLE IF EXISTS NhanVien;
DROP TABLE IF EXISTS KhachHang;
GO

CREATE TABLE Rap (
    maRap NVARCHAR(10) PRIMARY KEY,
    tenRap NVARCHAR(100) NOT NULL,
    soLuongGhe INT CHECK (soLuongGhe >= 0)
);
GO

CREATE TABLE Ghe (
    maGhe NVARCHAR(20) PRIMARY KEY,
	tenGhe NVARCHAR(20) NOT NULL,
    maRap NVARCHAR(10) NOT NULL,
    tinhTrang BIT DEFAULT 0, 
    
    CONSTRAINT FK_GHE_RAP FOREIGN KEY (maRap) REFERENCES Rap(maRap) ON DELETE CASCADE
);
GO

CREATE TABLE NhanVien (
    maNV NVARCHAR(10) PRIMARY KEY,
    tenNV NVARCHAR(100) NOT NULL,
    diaChi NVARCHAR(200),
    soDienThoai NVARCHAR(15),
    ngaySinh DATE,
    email NVARCHAR(100),
    gioiTinh NVARCHAR(10)
);
GO

CREATE TABLE KhachHang (
    maKH NVARCHAR(50) PRIMARY KEY,
    hoTen NVARCHAR(100) NOT NULL,
    gioiTinh NVARCHAR(10),
    soDT NVARCHAR(15),
    diaChi NVARCHAR(200)
);
GO

CREATE TABLE Phim (
    maPhim NVARCHAR(10) PRIMARY KEY,
    tenPhim NVARCHAR(200) NOT NULL,
    nhaSanXuat NVARCHAR(100),
    theLoai NVARCHAR(100),
    thoiLuong INT CHECK (thoiLuong > 0),
    quocGia NVARCHAR(50)
);
GO

CREATE TABLE SuatChieu (
    maSuatChieu NVARCHAR(10) PRIMARY KEY,
    maPhim NVARCHAR(10) NOT NULL,
    maRap NVARCHAR(10) NOT NULL,
    ngayChieu DATE NOT NULL,
    gioChieu TIME NOT NULL,
    giaVe FLOAT CHECK (giaVe > 0),

    CONSTRAINT FK_SUATCHIEU_PHIM FOREIGN KEY (maPhim) REFERENCES Phim(maPhim) ON DELETE CASCADE,
    CONSTRAINT FK_SUATCHIEU_RAP FOREIGN KEY (maRap) REFERENCES Rap(maRap) ON DELETE CASCADE
);
GO

CREATE TABLE Ve (
    maVe NVARCHAR(50) PRIMARY KEY,
    maGhe NVARCHAR(20) NOT NULL,
    ngayBan DATE NOT NULL DEFAULT GETDATE(),
    maSuatChieu NVARCHAR(10) NOT NULL,
    daThanhToan BIT DEFAULT 0,

    CONSTRAINT FK_VE_GHE FOREIGN KEY (maGhe) REFERENCES Ghe(maGhe),
    CONSTRAINT FK_VE_SUATCHIEU FOREIGN KEY (maSuatChieu) REFERENCES SuatChieu(maSuatChieu)
);
GO

CREATE TABLE HoaDon (
    maHoaDon NVARCHAR(50) PRIMARY KEY,
    ngayLap DATE DEFAULT GETDATE(),
    maNV NVARCHAR(10) NOT NULL,
    maKH NVARCHAR(50) NOT NULL,
	soLuongVe INT NOT NULL,
    tongTien FLOAT CHECK (tongTien >= 0),

    CONSTRAINT FK_HOADON_NV FOREIGN KEY (maNV) REFERENCES NhanVien(maNV),
    CONSTRAINT FK_HOADON_KH FOREIGN KEY (maKH) REFERENCES KhachHang(maKH) ON DELETE CASCADE
);
GO

CREATE TABLE ChiTietHoaDon (
    maHoaDon NVARCHAR(50) NOT NULL,
    maVe NVARCHAR(50) NOT NULL,
    soLuong INT CHECK (soLuong > 0),
    giaVe FLOAT CHECK (giaVe > 0),

    CONSTRAINT PK_CTHD PRIMARY KEY (maHoaDon, maVe),
    CONSTRAINT FK_CTHD_HD FOREIGN KEY (maHoaDon) REFERENCES HoaDon(maHoaDon) ON DELETE CASCADE,
    CONSTRAINT FK_CTHD_VE FOREIGN KEY (maVe) REFERENCES Ve(maVe)
);
GO

CREATE TABLE TaiKhoan (
    maNV NVARCHAR(10) PRIMARY KEY,
    taiKhoan NVARCHAR(50) UNIQUE NOT NULL,
    matKhau NVARCHAR(100) NOT NULL,

    CONSTRAINT FK_TAIKHOAN_NV FOREIGN KEY (maNV) REFERENCES NhanVien(maNV) ON DELETE CASCADE
);
GO

INSERT INTO Phim (maPhim, tenPhim, nhaSanXuat, theLoai, thoiLuong, quocGia) VALUES
('P001', N'Cuộc chiến vĩ đại', N'Studio A', N'Hành động', 120, N'Mỹ'),
('P002', N'Tình yêu mùa hè', N'Studio B', N'Tình cảm', 105, N'Hàn Quốc'),
('P003', N'Cười thả ga', N'Studio C', N'Hài hước', 95, N'Mỹ'),
('P004', N'Ngôi nhà ma', N'Studio D', N'Kinh dị', 110, N'Anh'),
('P005', N'Chú mèo tinh nghịch', N'Studio E', N'Hoạt hình', 80, N'Nhật Bản'),
('P006', N'Bí mật tâm lý', N'Studio F', N'Tâm lý', 115, N'Pháp'),
('P007', N'Hành tinh xa xôi', N'Studio G', N'Viễn tưởng', 130, N'Mỹ'),
('P008', N'Sát thủ bóng đêm', N'Studio H', N'Hành động', 125, N'Mỹ'),
('P009', N'Chuyện tình Paris', N'Studio I', N'Tình cảm', 100, N'Pháp'),
('P010', N'Hội bạn vui nhộn', N'Studio J', N'Hài hước', 90, N'Hàn Quốc'),
('P011', N'Người ngoài hành tinh', N'Studio K', N'Viễn tưởng', 140, N'Mỹ'),
('P012', N'Ngôi làng ma quái', N'Studio L', N'Kinh dị', 105, N'Anh'),
('P013', N'Bức tranh tâm lý', N'Studio M', N'Tâm lý', 110, N'Nhật Bản'),
('P014', N'Chú heo phiêu lưu', N'Studio N', N'Hoạt hình', 85, N'Mỹ'),
('P015', N'Cuộc đua tốc độ', N'Studio O', N'Hành động', 115, N'Mỹ'),
('P016', N'Tình yêu tuổi học trò', N'Studio P', N'Tình cảm', 95, N'Hàn Quốc'),
('P017', N'Cười rụng rốn', N'Studio Q', N'Hài hước', 100, N'Mỹ'),
('P018', N'Bóng ma trong rừng', N'Studio R', N'Kinh dị', 120, N'Anh'),
('P019', N'Tâm lý gia đình', N'Studio S', N'Tâm lý', 110, N'Pháp'),
('P020', N'Cuộc phiêu lưu của gấu', N'Studio T', N'Hoạt hình', 90, N'Nhật Bản'),
('P021', N'Hành trình cứu thế giới', N'Studio U', N'Viễn tưởng', 135, N'Mỹ'),
('P022', N'Sát thủ đường phố', N'Studio V', N'Hành động', 125, N'Mỹ'),
('P023', N'Mối tình ngọt ngào', N'Studio W', N'Tình cảm', 105, N'Hàn Quốc'),
('P024', N'Hội bạn siêu hài', N'Studio X', N'Hài hước', 95, N'Mỹ'),
('P025', N'Ngôi nhà kỳ bí', N'Studio Y', N'Kinh dị', 115, N'Anh'),
('P026', N'Tâm lý học sinh', N'Studio Z', N'Tâm lý', 100, N'Pháp'),
('P027', N'Chú chó tinh nghịch', N'Studio AA', N'Hoạt hình', 85, N'Mỹ'),
('P028', N'Cuộc chiến vũ trụ', N'Studio BB', N'Viễn tưởng', 140, N'Mỹ'),
('P029', N'Anh hùng đường phố', N'Studio CC', N'Hành động', 120, N'Mỹ'),
('P030', N'Chuyện tình mùa đông', N'Studio DD', N'Tình cảm', 100, N'Hàn Quốc');
GO

INSERT INTO Rap (maRap, tenRap, soLuongGhe) VALUES
('RAP001', N'Phòng 1', 25),
('RAP002', N'Phòng 2', 30),
('RAP003', N'Phòng 3', 30),
('RAP004', N'Phòng 4', 30);
GO

INSERT INTO SuatChieu (maSuatChieu, maPhim, maRap, ngayChieu, gioChieu, giaVe) VALUES
('SC001', 'P001', 'RAP001', '2025-11-03', '10:00', 45000),
('SC002', 'P002', 'RAP002', '2025-11-03', '12:30', 45000),
('SC003', 'P003', 'RAP003', '2025-11-03', '14:00', 45000),
('SC004', 'P004', 'RAP004', '2025-11-03', '16:00', 45000),
('SC005', 'P005', 'RAP001', '2025-11-03', '18:30', 45000),
('SC006', 'P006', 'RAP001', '2025-11-03', '20:00', 45000),
('SC007', 'P007', 'RAP001', '2025-11-04', '10:30', 45000),
('SC008', 'P008', 'RAP001', '2025-11-04', '13:00', 45000),
('SC009', 'P009', 'RAP001', '2025-11-04', '15:30', 45000),
('SC010', 'P010', 'RAP001', '2025-11-04', '18:00', 45000),
('SC011', 'P011', 'RAP001', '2025-11-05', '10:00', 50000),
('SC012', 'P012', 'RAP002', '2025-11-05', '12:30', 50000),
('SC013', 'P013', 'RAP003', '2025-11-05', '14:30', 55000),
('SC014', 'P014', 'RAP002', '2025-11-05', '17:00', 55000),
('SC015', 'P015', 'RAP002', '2025-11-05', '19:00', 55000),
('SC016', 'P016', 'RAP004', '2025-11-06', '10:30', 55000),
('SC017', 'P017', 'RAP004', '2025-11-06', '13:00', 55000),
('SC018', 'P018', 'RAP003', '2025-11-06', '15:30', 60000),
('SC019', 'P019', 'RAP003', '2025-11-06', '18:00', 60000),
('SC020', 'P020', 'RAP003', '2025-11-06', '20:00', 60000);
GO

INSERT INTO Ghe (maGhe, tenGhe, maRap, tinhTrang) VALUES
('RAP001_G1', N'Ghế 1', 'RAP001', 1), ('RAP001_G2', N'Ghế 2', 'RAP001', 1), ('RAP001_G3', N'Ghế 3', 'RAP001', 0), ('RAP001_G4', N'Ghế 4', 'RAP001', 0), ('RAP001_G5', N'Ghế 5', 'RAP001', 0),
('RAP001_G6', N'Ghế 6', 'RAP001', 0), ('RAP001_G7', N'Ghế 7', 'RAP001', 0), ('RAP001_G8', N'Ghế 8', 'RAP001', 0), ('RAP001_G9', N'Ghế 9', 'RAP001', 0), ('RAP001_G10', N'Ghế 10', 'RAP001', 0),
('RAP001_G11', N'Ghế 11', 'RAP001', 0), ('RAP001_G12', N'Ghế 12', 'RAP001', 0), ('RAP001_G13', N'Ghế 13', 'RAP001', 0), ('RAP001_G14', N'Ghế 14', 'RAP001', 0), ('RAP001_G15', N'Ghế 15', 'RAP001', 0),
('RAP001_G16', N'Ghế 16', 'RAP001', 0), ('RAP001_G17', N'Ghế 17', 'RAP001', 0), ('RAP001_G18', N'Ghế 18', 'RAP001', 0), ('RAP001_G19', N'Ghế 19', 'RAP001', 0), ('RAP001_G20', N'Ghế 20', 'RAP001', 0),
('RAP001_G21', N'Ghế 21', 'RAP001', 0), ('RAP001_G22', N'Ghế 22', 'RAP001', 0), ('RAP001_G23', N'Ghế 23', 'RAP001', 0), ('RAP001_G24', N'Ghế 24', 'RAP001', 0), ('RAP001_G25', N'Ghế 25', 'RAP001', 0);
GO
INSERT INTO Ghe (maGhe, tenGhe, maRap, tinhTrang) VALUES
('RAP002_G1', N'Ghế 1', 'RAP002', 0), ('RAP002_G2', N'Ghế 2', 'RAP002', 0), ('RAP002_G3', N'Ghế 3', 'RAP002', 0), ('RAP002_G4', N'Ghế 4', 'RAP002', 0), ('RAP002_G5', N'Ghế 5', 'RAP002', 0),
('RAP002_G6', N'Ghế 6', 'RAP002', 0), ('RAP002_G7', N'Ghế 7', 'RAP002', 0), ('RAP002_G8', N'Ghế 8', 'RAP002', 0), ('RAP002_G9', N'Ghế 9', 'RAP002', 0), ('RAP002_G10', N'Ghế 10', 'RAP002', 1),
('RAP002_G11', N'Ghế 11', 'RAP002', 1), ('RAP002_G12', N'Ghế 12', 'RAP002', 0), ('RAP002_G13', N'Ghế 13', 'RAP002', 0), ('RAP002_G14', N'Ghế 14', 'RAP002', 0), ('RAP002_G15', N'Ghế 15', 'RAP002', 0),
('RAP002_G16', N'Ghế 16', 'RAP002', 0), ('RAP002_G17', N'Ghế 17', 'RAP002', 0), ('RAP002_G18', N'Ghế 18', 'RAP002', 0), ('RAP002_G19', N'Ghế 19', 'RAP002', 0), ('RAP002_G20', N'Ghế 20', 'RAP002', 0),
('RAP002_G21', N'Ghế 21', 'RAP002', 0), ('RAP002_G22', N'Ghế 22', 'RAP002', 0), ('RAP002_G23', N'Ghế 23', 'RAP002', 0), ('RAP002_G24', N'Ghế 24', 'RAP002', 0), ('RAP002_G25', N'Ghế 25', 'RAP002', 0),
('RAP002_G26', N'Ghế 26', 'RAP002', 0), ('RAP002_G27', N'Ghế 27', 'RAP002', 0), ('RAP002_G28', N'Ghế 28', 'RAP002', 0),
('RAP002_G29', N'Ghế 29', 'RAP002', 0), ('RAP002_G30', N'Ghế 30', 'RAP002', 0);
GO
INSERT INTO Ghe (maGhe, tenGhe, maRap, tinhTrang) VALUES
('RAP003_G1', N'Ghế 1', 'RAP003', 0), ('RAP003_G2', N'Ghế 2', 'RAP003', 0), ('RAP003_G3', N'Ghế 3', 'RAP003', 0), ('RAP003_G4', N'Ghế 4', 'RAP003', 0), ('RAP003_G5', N'Ghế 5', 'RAP003', 0),
('RAP003_G6', N'Ghế 6', 'RAP003', 0), ('RAP003_G7', N'Ghế 7', 'RAP003', 0), ('RAP003_G8', N'Ghế 8', 'RAP003', 0), ('RAP003_G9', N'Ghế 9', 'RAP003', 0), ('RAP003_G10', N'Ghế 10', 'RAP003', 0),
('RAP003_G11', N'Ghế 11', 'RAP003', 0), ('RAP003_G12', N'Ghế 12', 'RAP003', 0), ('RAP003_G13', N'Ghế 13', 'RAP003', 0), ('RAP003_G14', N'Ghế 14', 'RAP003', 0), ('RAP003_G15', N'Ghế 15', 'RAP003', 1),
('RAP003_G16', N'Ghế 16', 'RAP003', 1), ('RAP003_G17', N'Ghế 17', 'RAP003', 1), ('RAP003_G18', N'Ghế 18', 'RAP003', 0), ('RAP003_G19', N'Ghế 19', 'RAP003', 0), ('RAP003_G20', N'Ghế 20', 'RAP003', 0),
('RAP003_G21', N'Ghế 21', 'RAP003', 0), ('RAP003_G22', N'Ghế 22', 'RAP003', 0), ('RAP003_G23', N'Ghế 23', 'RAP003', 0), ('RAP003_G24', N'Ghế 24', 'RAP003', 0), ('RAP003_G25', N'Ghế 25', 'RAP003', 0),
('RAP003_G26', N'Ghế 26', 'RAP003', 0), ('RAP003_G27', N'Ghế 27', 'RAP003', 0), ('RAP003_G28', N'Ghế 28', 'RAP003', 0),
('RAP003_G29', N'Ghế 29', 'RAP003', 0), ('RAP003_G30', N'Ghế 30', 'RAP003', 0), ('RAP003_G31', N'Ghế 31', 'RAP003', 0);
GO
INSERT INTO Ghe (maGhe, tenGhe, maRap, tinhTrang) VALUES
('RAP004_G1', N'Ghế 1', 'RAP004', 0), ('RAP004_G2', N'Ghế 2', 'RAP004', 0), ('RAP004_G3', N'Ghế 3', 'RAP004', 0), ('RAP004_G4', N'Ghế 4', 'RAP004', 0), ('RAP004_G5', N'Ghế 5', 'RAP004', 0),
('RAP004_G6', N'Ghế 6', 'RAP004', 0), ('RAP004_G7', N'Ghế 7', 'RAP004', 0), ('RAP004_G8', N'Ghế 8', 'RAP004', 0), ('RAP004_G9', N'Ghế 9', 'RAP004', 0), ('RAP004_G10', N'Ghế 10', 'RAP004', 0),
('RAP004_G11', N'Ghế 11', 'RAP004', 0), ('RAP004_G12', N'Ghế 12', 'RAP004', 0), ('RAP004_G13', N'Ghế 13', 'RAP004', 0), ('RAP004_G14', N'Ghế 14', 'RAP004', 0), ('RAP004_G15', N'Ghế 15', 'RAP004', 0),
('RAP004_G16', N'Ghế 16', 'RAP004', 0), ('RAP004_G17', N'Ghế 17', 'RAP004', 0), ('RAP004_G18', N'Ghế 18', 'RAP004', 0), ('RAP004_G19', N'Ghế 19', 'RAP004', 0), ('RAP004_G20', N'Ghế 20', 'RAP004', 0),
('RAP004_G21', N'Ghế 21', 'RAP004', 0), ('RAP004_G22', N'Ghế 22', 'RAP004', 0), ('RAP004_G23', N'Ghế 23', 'RAP004', 0), ('RAP004_G24', N'Ghế 24', 'RAP004', 0), ('RAP004_G25', N'Ghế 25', 'RAP004', 0),
('RAP004_G26', N'Ghế 26', 'RAP004', 0), ('RAP004_G27', N'Ghế 27', 'RAP004', 0), ('RAP004_G28', N'Ghế 28', 'RAP004', 0), ('RAP004_G29', N'Ghế 29', 'RAP004', 1), ('RAP004_G30', N'Ghế 30', 'RAP004', 1);
GO

INSERT INTO NhanVien (maNV, tenNV, diaChi, soDienThoai, ngaySinh, email, gioiTinh)
VALUES
('NV01', N'Lê Minh Tân', N'123 Lê Lợi, Quận 1, TP.HCM', '0905123456', '1998-03-15', 'an.nguyen@example.com', N'Nam'),
('NV02', N'Nguyễn Chí Tâm', N'45 Hai Bà Trưng, Hà Nội', '0987654321', '2000-07-22', 'binh.tran@example.com', N'Nữ'),
('NV03', N'Đỗ Thanh Tường', N'78 Nguyễn Huệ, Đà Nẵng', '0912345678', '1995-11-09', 'phuc.le@example.com', N'Nam');
GO

INSERT INTO TaiKhoan (maNV, taiKhoan, matKhau)
VALUES
('NV01', N'leminhtan', N'123456'),
('NV02', N'nguyenchitam', N'123456'),
('NV03', N'dothanhtuong', N'123456');
GO

INSERT INTO KhachHang (maKH, hoTen, gioiTinh, soDT, diaChi)
VALUES
('KH001', N'Nguyễn Văn An', N'Nam', '0987123456', N'Quận 1, TP HCM'),
('KH002', N'Trần Thị Bích', N'Nữ', '0912345678', N'Quận 3, TP HCM'),
('KH003', N'Lê Hoàng Minh', N'Nam', '0978123456', N'Quận 5, TP HCM'),
('KH004', N'Phạm Ngọc Lan', N'Nữ', '0932456789', N'Quận 7, TP HCM'),
('KH005', N'Võ Thanh Hùng', N'Nam', '0905678123', N'Thủ Đức, TP HCM'),

('KH006', N'Đỗ Thị Mai', N'Nữ', '0987765432', N'Quận Bình Thạnh, TP HCM'),
('KH007', N'Bùi Văn Khánh', N'Nam', '0912555666', N'Quận Gò Vấp, TP HCM'),
('KH008', N'Huỳnh Mỹ Duyên', N'Nữ', '0937888999', N'Quận Tân Bình, TP HCM'),
('KH009', N'Ngô Quốc Trí', N'Nam', '0977444333', N'Quận Tân Phú, TP HCM'),
('KH010', N'Dương Thùy Trang', N'Nữ', '0909777666', N'Quận 10, TP HCM'),

('KH011', N'Nguyễn Nhật Tuấn', N'Nam', '0965432345', N'Quận 11, TP HCM'),
('KH012', N'Trần Thanh Hằng', N'Nữ', '0913459988', N'Quận 12, TP HCM'),
('KH013', N'Lý Minh Phúc', N'Nam', '0936778899', N'Bình Tân, TP HCM'),
('KH014', N'Cao Ngọc Hà', N'Nữ', '0988665544', N'Hóc Môn, TP HCM'),
('KH015', N'Phan Quốc Việt', N'Nam', '0905443322', N'Củ Chi, TP HCM'),

('KH016', N'Vũ Thị Hương', N'Nữ', '0912998877', N'Biên Hòa, Đồng Nai'),
('KH017', N'Tạ Minh Khoa', N'Nam', '0978665522', N'Thuận An, Bình Dương'),
('KH018', N'Đinh Thị Yến', N'Nữ', '0935442211', N'Dĩ An, Bình Dương'),
('KH019', N'Hồ Tuấn Kiệt', N'Nam', '0966778899', N'Vũng Tàu, Bà Rịa Vũng Tàu'),
('KH020', N'La Mỹ Linh', N'Nữ', '0989554433', N'Tân Uyên, Bình Dương'),

('KH021', N'Châu Nhật Long', N'Nam', '0913445566', N'Long An'),
('KH022', N'Lâm Thị Kim Chi', N'Nữ', '0935112233', N'Tiền Giang'),
('KH023', N'Kha Đức Thịnh', N'Nam', '0988112233', N'Bến Tre'),
('KH024', N'Đoàn Mỹ Hạnh', N'Nữ', '0977442211', N'Vĩnh Long'),
('KH025', N'Hoàng Minh Khôi', N'Nam', '0908111222', N'Cần Thơ'),

('KH026', N'Lương Thị Phương', N'Nữ', '0912777333', N'Hà Nội'),
('KH027', N'Nguyễn Hoàng Sơn', N'Nam', '0965223344', N'Đà Nẵng'),
('KH028', N'Phạm Thùy Dương', N'Nữ', '0935223344', N'Huế'),
('KH029', N'Đặng Hữu Nghĩa', N'Nam', '0975223344', N'Hải Phòng'),
('KH030', N'Trịnh Nhật Vy', N'Nữ', '0985223344', N'Nam Định'),

('KH031', N'Trần Văn An', N'Nam', '0909111222', N'123 CMT8, P10, Q3, TP HCM'),
('KH032', N'Nguyễn Thị Bê', N'Nữ', '0909333444', N'456 Lê Lợi, Q1, TP HCM'),
('KH033', N'Lý Văn Cường', N'Nam', '0909555666', N'789 Hóc Môn, TP HCM');

GO

INSERT INTO Ve (maVe, maGhe, ngayBan, maSuatChieu, daThanhToan)
VALUES
('VE0001', 'RAP001_G1', '2025-11-03', 'SC001', 1),
('VE0002', 'RAP001_G2', '2025-11-03', 'SC001', 1),

('VE0003', 'RAP002_G10', '2025-11-03', 'SC002', 1),

('VE0004', 'RAP003_G15', '2025-11-03', 'SC003', 1),
('VE0005', 'RAP003_G16', '2025-11-03', 'SC003', 1),

('VE0006',  'RAP001_G3',  '2025-01-01', 'SC003', 1),
('VE0007',  'RAP001_G4',  '2025-01-01', 'SC007', 1),

('VE0008',  'RAP001_G10',  '2025-01-02', 'SC001', 1),

('VE0009',  'RAP001_G11',  '2025-01-03', 'SC004', 1),
('VE0010',  'RAP001_G12',  '2025-01-03', 'SC009', 1),

('VE0011',  'RAP001_G13',  '2025-01-04', 'SC002', 1),
('VE0012',  'RAP001_G14',  '2025-01-04', 'SC008', 1),

('VE0013',  'RAP001_G15',  '2025-01-05', 'SC006', 1),
('VE0014',  'RAP001_G20',  '2025-01-05', 'SC010', 1),

('VE0015', 'RAP002_G11',  '2025-01-06', 'SC003', 1),
('VE0016', 'RAP002_G12',  '2025-01-06', 'SC005', 1),

('VE0017', 'RAP002_G13',  '2025-01-07', 'SC001', 1),

('VE0018', 'RAP002_G1',  '2025-01-08', 'SC007', 1),

('VE0019', 'RAP002_G3',  '2025-01-09', 'SC008', 1),

('VE0020', 'RAP002_G17',  '2025-01-10', 'SC009', 1),
('VE0021', 'RAP003_G17',  '2025-01-10', 'SC004', 1),

('VE0022', 'RAP003_G30',  '2025-01-11', 'SC002', 1),

('VE0023', 'RAP003_G29',  '2025-01-12', 'SC003', 1),

('VE0024', 'RAP003_G25',  '2025-01-13', 'SC010', 1),

('VE0025', 'RAP003_G24',  '2025-01-14', 'SC008', 1),

('VE0026', 'RAP004_G1',  '2025-01-15', 'SC006', 1),

('VE0027', 'RAP004_G2',  '2025-01-16', 'SC004', 1),

('VE0028', 'RAP004_G3',  '2025-01-17', 'SC001', 1),

('VE0029', 'RAP004_G4',  '2025-01-18', 'SC009', 1),

('VE0030', 'RAP004_G5',  '2025-01-19', 'SC002', 1),

('VE0031', 'RAP004_G6',  '2025-01-20', 'SC005', 1),

('VE0032', 'RAP004_G7',  '2025-01-21', 'SC003', 1),
('VE0033', 'RAP004_G11',  '2025-01-21', 'SC006', 1),
('VE0034', 'RAP004_G12',  '2025-01-21', 'SC009', 1),

('VE0035', 'RAP004_G23',  '2025-01-22', 'SC010', 1),

('VE0036', 'RAP004_G14',  '2025-01-23', 'SC008', 1),
('VE0037', 'RAP004_G15',  '2025-01-23', 'SC002', 1),

('VE0038', 'RAP004_G16',  '2025-01-24', 'SC005', 1),

('VE0039', 'RAP004_G17',  '2025-01-25', 'SC004', 1),
('VE0040', 'RAP004_G18',  '2025-01-25', 'SC001', 1),

('VE0041', 'RAP004_G19',  '2025-01-26', 'SC007', 1),
('VE0042', 'RAP004_G20',  '2025-01-26', 'SC003', 1),
('VE0043', 'RAP004_G21',  '2025-01-26', 'SC002', 1),

('VE0044', 'RAP004_G22',  '2025-01-27', 'SC006', 1),

('VE0045', 'RAP004_G23',  '2025-01-28', 'SC010', 1),

('VE0046', 'RAP004_G24',  '2025-01-29', 'SC009', 1),
('VE0047', 'RAP004_G25',  '2025-01-29', 'SC002', 1),

('VE0048', 'RAP004_G26',  '2025-01-30', 'SC001', 1),
('VE0049', 'RAP004_G27',  '2025-01-30', 'SC008', 1);


GO

INSERT INTO HoaDon (maHoaDon, ngayLap, maNV, maKH, soLuongVe, tongTien) 
VALUES
('HD001', '2025-01-01', 'NV01', 'KH001', 2, 90000),
('HD002', '2025-01-02', 'NV02', 'KH002', 1, 45000),
('HD003', '2025-01-03', 'NV03', 'KH003', 2, 180000),
('HD004', '2025-01-04', 'NV01', 'KH004', 2, 135000),
('HD005', '2025-01-05', 'NV02', 'KH005', 2, 225000),

('HD006', '2025-01-06', 'NV03', 'KH006', 2, 90000),
('HD007', '2025-01-07', 'NV01', 'KH007', 1, 45000),
('HD008', '2025-01-08', 'NV02', 'KH008', 1, 270000),
('HD009', '2025-01-09', 'NV03', 'KH009', 1, 135000),
('HD010', '2025-01-10', 'NV01', 'KH010', 2, 90000),

('HD011', '2025-01-11', 'NV02', 'KH011', 1, 180000),
('HD012', '2025-01-12', 'NV03', 'KH012', 1, 225000),
('HD013', '2025-01-13', 'NV01', 'KH013', 1, 315000),
('HD014', '2025-01-14', 'NV02', 'KH014', 1, 135000),
('HD015', '2025-01-15', 'NV03', 'KH015', 1, 360000),

('HD016', '2025-01-16', 'NV01', 'KH016', 1, 90000),
('HD017', '2025-01-17', 'NV02', 'KH017', 1, 405000),
('HD018', '2025-01-18', 'NV03', 'KH018', 1, 45000),
('HD019', '2025-01-19', 'NV01', 'KH019', 1, 270000),
('HD020', '2025-01-20', 'NV02', 'KH020', 1, 180000),

('HD021', '2025-01-21', 'NV03', 'KH021', 3, 135000),
('HD022', '2025-01-22', 'NV01', 'KH022', 1, 450000),
('HD023', '2025-01-23', 'NV02', 'KH023', 2, 90000),
('HD024', '2025-01-24', 'NV03', 'KH024', 1, 225000),
('HD025', '2025-01-25', 'NV01', 'KH025', 2, 180000),

('HD026', '2025-01-26', 'NV02', 'KH026', 3, 135000),
('HD027', '2025-01-27', 'NV03', 'KH027', 1, 270000),
('HD028', '2025-01-28', 'NV01', 'KH028', 1, 45000),
('HD029', '2025-01-29', 'NV02', 'KH029', 2, 315000),
('HD030', '2025-01-30', 'NV03', 'KH030', 2, 90000),

('HD031', '2025-11-03', 'NV01', 'KH001', 2, 90000),
('HD032', '2025-11-03', 'NV02', 'KH002', 1, 45000),
('HD033', '2025-11-03', 'NV01', 'KH003', 2, 90000);
GO

INSERT INTO ChiTietHoaDon (maHoaDon, maVe, soLuong, giaVe) 
VALUES
-- HD001: 2 vé
('HD001', 'VE0006', 1, 45000),
('HD001', 'VE0007', 1, 45000),

-- HD002: 1 vé
('HD002', 'VE0008', 1, 45000),

-- HD003: 2 vé
('HD003', 'VE0009', 1, 45000),
('HD003', 'VE0010', 1, 45000),

-- HD004: 2 vé
('HD004', 'VE0011', 1, 45000),
('HD004', 'VE0012', 1, 45000),

-- HD005: 2 vé
('HD005', 'VE0013', 1, 45000),
('HD005', 'VE0014', 1, 45000),

-- HD006: 2 vé
('HD006', 'VE0015', 1, 45000),
('HD006', 'VE0016', 1, 45000),

-- HD007: 1 vé
('HD007', 'VE0017', 1, 45000),

-- HD008: 1 vé
('HD008', 'VE0018', 1, 45000),

-- HD009: 1 vé
('HD009', 'VE0019', 1, 45000),

-- HD010: 2 vé
('HD010', 'VE0020', 1, 45000),
('HD010', 'VE0021', 1, 45000),

-- HD011: 1 vé
('HD011', 'VE0022', 1, 45000),

-- HD012: 1 vé
('HD012', 'VE0023', 1, 45000),

-- HD013: 1 vé
('HD013', 'VE0024', 1, 45000),

-- HD014: 1 vé
('HD014', 'VE0025', 1, 45000),

-- HD015: 1 vé
('HD015', 'VE0026', 1, 45000),

-- HD016: 1 vé
('HD016', 'VE0027', 1, 45000),

-- HD017: 1 vé
('HD017', 'VE0028', 1, 45000),

-- HD018: 1 vé
('HD018', 'VE0029', 1, 45000),

-- HD019: 1 vé
('HD019', 'VE0030', 1, 45000),

-- HD020: 1 vé
('HD020', 'VE0031', 1, 45000),

-- HD021: 3 vé
('HD021', 'VE0032', 1, 45000),
('HD021', 'VE0033', 1, 45000),
('HD021', 'VE0034', 1, 45000),

-- HD022: 1 vé
('HD022', 'VE0035', 1, 45000),

-- HD023: 2 vé
('HD023', 'VE0036', 1, 45000),
('HD023', 'VE0037', 1, 45000),

-- HD024: 1 vé
('HD024', 'VE0038', 1, 45000),

-- HD025: 2 vé
('HD025', 'VE0039', 1, 45000),
('HD025', 'VE0040', 1, 45000),

-- HD026: 3 vé
('HD026', 'VE0041', 1, 45000),
('HD026', 'VE0042', 1, 45000),
('HD026', 'VE0043', 1, 45000),

-- HD027: 1 vé
('HD027', 'VE0044', 1, 45000),

-- HD028: 1 vé
('HD028', 'VE0045', 1, 45000),

-- HD029: 2 vé
('HD029', 'VE0046', 1, 45000),
('HD029', 'VE0047', 1, 45000),

-- HD030: 2 vé
('HD030', 'VE0048', 1, 45000),
('HD030', 'VE0049', 1, 45000),

-- HD031: 2 vé
('HD031', 'VE0001', 1, 50000),
('HD031', 'VE0002', 1, 50000),

--HD032: 1 vé
('HD032', 'VE0003', 1, 55000),

--HD033: 2 vé
('HD033', 'VE0004', 1, 45000),
('HD033', 'VE0005', 1, 45000);
GO
Use master