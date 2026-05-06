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

    CONSTRAINT FK_GHE_RAP
        FOREIGN KEY (maRap)
        REFERENCES Rap(maRap)
        ON DELETE CASCADE
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
    giaVe DECIMAL(10,2) CHECK (giaVe > 0),

    CONSTRAINT FK_SUATCHIEU_PHIM FOREIGN KEY (maPhim) REFERENCES Phim(maPhim) ON DELETE CASCADE,
    CONSTRAINT FK_SUATCHIEU_RAP FOREIGN KEY (maRap) REFERENCES Rap(maRap) ON DELETE CASCADE
);
GO

CREATE TABLE Ve (
    maVe NVARCHAR(50) PRIMARY KEY,
    maGhe NVARCHAR(20) NOT NULL,
    ngayBan DATETIME DEFAULT GETDATE(),
    maSuatChieu NVARCHAR(10) NOT NULL,
    daThanhToan BIT DEFAULT 0,

    CONSTRAINT FK_VE_GHE
        FOREIGN KEY (maGhe) REFERENCES Ghe(maGhe),

    CONSTRAINT FK_VE_SUATCHIEU
        FOREIGN KEY (maSuatChieu) REFERENCES SuatChieu(maSuatChieu),

    CONSTRAINT UQ_Ghe_Suat UNIQUE (maGhe, maSuatChieu)
);
GO

CREATE TABLE HoaDon (
    maHoaDon NVARCHAR(50) PRIMARY KEY,
    ngayLap DATETIME DEFAULT GETDATE(),
    maNV NVARCHAR(10) NOT NULL,
    maKH NVARCHAR(50) NOT NULL,
    tongTien DECIMAL(12,2) DEFAULT 0,

    CONSTRAINT FK_HOADON_NV FOREIGN KEY (maNV) REFERENCES NhanVien(maNV),
    CONSTRAINT FK_HOADON_KH FOREIGN KEY (maKH) REFERENCES KhachHang(maKH)
);
GO

CREATE TABLE ChiTietHoaDon (
    maHoaDon NVARCHAR(50) NOT NULL,
    maVe NVARCHAR(50) NOT NULL,
    soLuong INT CHECK (soLuong > 0),
    giaVe DECIMAL(10,2) CHECK (giaVe > 0),

    CONSTRAINT PK_CTHD PRIMARY KEY (maHoaDon, maVe),
    CONSTRAINT FK_CTHD_HD FOREIGN KEY (maHoaDon) REFERENCES HoaDon(maHoaDon) ON DELETE CASCADE,
    CONSTRAINT FK_CTHD_VE FOREIGN KEY (maVe) REFERENCES Ve(maVe)
);
GO

CREATE TABLE TaiKhoan (
    maNV NVARCHAR(10) PRIMARY KEY,
    taiKhoan NVARCHAR(50) UNIQUE NOT NULL,
    matKhau NVARCHAR(100) NOT NULL,
    vaiTro NVARCHAR(20) NOT NULL,

    CONSTRAINT FK_TAIKHOAN_NV FOREIGN KEY (maNV) REFERENCES NhanVien(maNV) ON DELETE CASCADE
);
GO

CREATE TRIGGER trg_UpdateTongTien
ON ChiTietHoaDon
AFTER INSERT, UPDATE, DELETE
AS
BEGIN
    UPDATE HoaDon
    SET tongTien = (
        SELECT SUM(soLuong * giaVe)
        FROM ChiTietHoaDon
        WHERE ChiTietHoaDon.maHoaDon = HoaDon.maHoaDon
    )
    WHERE maHoaDon IN (
        SELECT maHoaDon FROM inserted
        UNION
        SELECT maHoaDon FROM deleted
    );
END;
GO

CREATE TRIGGER trg_CheckGheHopLe
ON Ve
INSTEAD OF INSERT
AS
BEGIN
    INSERT INTO Ve (maVe, maGhe, ngayBan, maSuatChieu, daThanhToan)
    SELECT i.maVe, i.maGhe, i.ngayBan, i.maSuatChieu, i.daThanhToan
    FROM inserted i
    JOIN Ghe g ON i.maGhe = g.maGhe
    JOIN SuatChieu sc ON i.maSuatChieu = sc.maSuatChieu
    WHERE g.maRap = sc.maRap
END

GO

INSERT INTO Phim (maPhim, tenPhim, nhaSanXuat, theLoai, thoiLuong, quocGia) VALUES
('P001', N'Cậu bé mất tích', N'Studio A', N'Viễn tưởng', 120, N'Mỹ'),
('P002', N'Trung tâm chăm sóc chấn thương', N'Studio B', N'Chính kịch', 105, N'Hàn Quốc'),
('P003', N'Truy tìm Long Diên Hương', N'Studio C', N'Hài hước', 95, N'Việt Nam'),
('P004', N'Thỏ ơi', N'Studio D', N'Tâm lý', 110, N'Việt Nam'),
('P005', N'Nhà ba tôi một phòng', N'Studio E', N'Tình cảm', 80, N'Việt Nam'),
('P006', N'Chó săn công lý', N'Studio F', N'Hành động', 115, N'Hàn Quốc'),
('P007', N'Cô gái đến từ hư vô', N'Studio G', N'Chính kịch', 130, N'Thái Lan'),
('P008', N'Anh hùng yếu đuối', N'Studio H', N'Chính kịch', 125, N'Hàn Quốc'),
('P009', N'Trò choi con mực', N'Studio I', N'Chính kịch', 100, N'Hàn Quốc'),
('P010', N'Tiệm ăn của quỷ', N'Studio J', N'Chính kịch', 90, N'Việt Nam'),
('P011', N'Phía dưới sông Sein', N'Studio K', N'Kinh dị', 140, N'Pháp'),
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
('RAP001', N'Rạp 1', 25),
('RAP002', N'Rạp 2', 30),
('RAP003', N'Rạp 3', 30),
('RAP004', N'Rạp 4', 30);
GO

INSERT INTO SuatChieu (maSuatChieu, maPhim, maRap, ngayChieu, gioChieu, giaVe) VALUES
('SC001', 'P001', 'RAP001', '2026-05-09', '10:00', 45000),
('SC002', 'P002', 'RAP002', '2026-05-09', '12:30', 45000),
('SC003', 'P003', 'RAP003', '2026-05-09', '14:00', 45000),
('SC004', 'P004', 'RAP004', '2026-05-09', '16:00', 45000),
('SC005', 'P005', 'RAP001', '2026-05-09', '18:30', 45000),

('SC006', 'P006', 'RAP001', '2026-05-10', '20:00', 45000),
('SC007', 'P007', 'RAP001', '2026-05-10', '10:30', 45000),
('SC008', 'P008', 'RAP001', '2026-05-10', '13:00', 45000),
('SC009', 'P009', 'RAP001', '2026-05-10', '15:30', 45000),
('SC010', 'P010', 'RAP001', '2026-05-10', '18:00', 45000),

('SC011', 'P011', 'RAP001', '2026-05-11', '10:00', 50000),
('SC012', 'P012', 'RAP002', '2026-05-11', '12:30', 50000),
('SC013', 'P013', 'RAP003', '2026-05-11', '14:30', 55000),
('SC014', 'P014', 'RAP002', '2026-05-11', '17:00', 55000),
('SC015', 'P015', 'RAP002', '2026-05-11', '19:00', 55000),

('SC016', 'P016', 'RAP004', '2026-05-12', '10:30', 55000),
('SC017', 'P017', 'RAP004', '2026-05-12', '13:00', 55000),
('SC018', 'P018', 'RAP003', '2026-05-12', '15:30', 60000),
('SC019', 'P019', 'RAP003', '2026-05-12', '18:00', 60000),
('SC020', 'P020', 'RAP003', '2026-05-12', '20:00', 60000);
GO

-- Rạp 001
INSERT INTO Ghe (maGhe, tenGhe, maRap) VALUES
('RAP001_G1', N'Ghế 1', 'RAP001'), ('RAP001_G2', N'Ghế 2', 'RAP001'), ('RAP001_G3', N'Ghế 3', 'RAP001'), ('RAP001_G4', N'Ghế 4', 'RAP001'), ('RAP001_G5', N'Ghế 5', 'RAP001'),
('RAP001_G6', N'Ghế 6', 'RAP001'), ('RAP001_G7', N'Ghế 7', 'RAP001'), ('RAP001_G8', N'Ghế 8', 'RAP001'), ('RAP001_G9', N'Ghế 9', 'RAP001'), ('RAP001_G10', N'Ghế 10', 'RAP001'),
('RAP001_G11', N'Ghế 11', 'RAP001'), ('RAP001_G12', N'Ghế 12', 'RAP001'), ('RAP001_G13', N'Ghế 13', 'RAP001'), ('RAP001_G14', N'Ghế 14', 'RAP001'), ('RAP001_G15', N'Ghế 15', 'RAP001'),
('RAP001_G16', N'Ghế 16', 'RAP001'), ('RAP001_G17', N'Ghế 17', 'RAP001'), ('RAP001_G18', N'Ghế 18', 'RAP001'), ('RAP001_G19', N'Ghế 19', 'RAP001'), ('RAP001_G20', N'Ghế 20', 'RAP001'),
('RAP001_G21', N'Ghế 21', 'RAP001'), ('RAP001_G22', N'Ghế 22', 'RAP001'), ('RAP001_G23', N'Ghế 23', 'RAP001'), ('RAP001_G24', N'Ghế 24', 'RAP001'), ('RAP001_G25', N'Ghế 25', 'RAP001');
GO

-- Rạp 002
INSERT INTO Ghe (maGhe, tenGhe, maRap) VALUES
('RAP002_G1', N'Ghế 1', 'RAP002'), ('RAP002_G2', N'Ghế 2', 'RAP002'), ('RAP002_G3', N'Ghế 3', 'RAP002'), ('RAP002_G4', N'Ghế 4', 'RAP002'), ('RAP002_G5', N'Ghế 5', 'RAP002'),
('RAP002_G6', N'Ghế 6', 'RAP002'), ('RAP002_G7', N'Ghế 7', 'RAP002'), ('RAP002_G8', N'Ghế 8', 'RAP002'), ('RAP002_G9', N'Ghế 9', 'RAP002'), ('RAP002_G10', N'Ghế 10', 'RAP002'),
('RAP002_G11', N'Ghế 11', 'RAP002'), ('RAP002_G12', N'Ghế 12', 'RAP002'), ('RAP002_G13', N'Ghế 13', 'RAP002'), ('RAP002_G14', N'Ghế 14', 'RAP002'), ('RAP002_G15', N'Ghế 15', 'RAP002'),
('RAP002_G16', N'Ghế 16', 'RAP002'), ('RAP002_G17', N'Ghế 17', 'RAP002'), ('RAP002_G18', N'Ghế 18', 'RAP002'), ('RAP002_G19', N'Ghế 19', 'RAP002'), ('RAP002_G20', N'Ghế 20', 'RAP002'),
('RAP002_G21', N'Ghế 21', 'RAP002'), ('RAP002_G22', N'Ghế 22', 'RAP002'), ('RAP002_G23', N'Ghế 23', 'RAP002'), ('RAP002_G24', N'Ghế 24', 'RAP002'), ('RAP002_G25', N'Ghế 25', 'RAP002'),
('RAP002_G26', N'Ghế 26', 'RAP002'), ('RAP002_G27', N'Ghế 27', 'RAP002'), ('RAP002_G28', N'Ghế 28', 'RAP002'),
('RAP002_G29', N'Ghế 29', 'RAP002'), ('RAP002_G30', N'Ghế 30', 'RAP002');
GO

-- Rạp 003
INSERT INTO Ghe (maGhe, tenGhe, maRap) VALUES
('RAP003_G1', N'Ghế 1', 'RAP003'), ('RAP003_G2', N'Ghế 2', 'RAP003'), ('RAP003_G3', N'Ghế 3', 'RAP003'), ('RAP003_G4', N'Ghế 4', 'RAP003'), ('RAP003_G5', N'Ghế 5', 'RAP003'),
('RAP003_G6', N'Ghế 6', 'RAP003'), ('RAP003_G7', N'Ghế 7', 'RAP003'), ('RAP003_G8', N'Ghế 8', 'RAP003'), ('RAP003_G9', N'Ghế 9', 'RAP003'), ('RAP003_G10', N'Ghế 10', 'RAP003'),
('RAP003_G11', N'Ghế 11', 'RAP003'), ('RAP003_G12', N'Ghế 12', 'RAP003'), ('RAP003_G13', N'Ghế 13', 'RAP003'), ('RAP003_G14', N'Ghế 14', 'RAP003'), ('RAP003_G15', N'Ghế 15', 'RAP003'),
('RAP003_G16', N'Ghế 16', 'RAP003'), ('RAP003_G17', N'Ghế 17', 'RAP003'), ('RAP003_G18', N'Ghế 18', 'RAP003'), ('RAP003_G19', N'Ghế 19', 'RAP003'), ('RAP003_G20', N'Ghế 20', 'RAP003'),
('RAP003_G21', N'Ghế 21', 'RAP003'), ('RAP003_G22', N'Ghế 22', 'RAP003'), ('RAP003_G23', N'Ghế 23', 'RAP003'), ('RAP003_G24', N'Ghế 24', 'RAP003'), ('RAP003_G25', N'Ghế 25', 'RAP003'),
('RAP003_G26', N'Ghế 26', 'RAP003'), ('RAP003_G27', N'Ghế 27', 'RAP003'), ('RAP003_G28', N'Ghế 28', 'RAP003'),
('RAP003_G29', N'Ghế 29', 'RAP003'), ('RAP003_G30', N'Ghế 30', 'RAP003'), ('RAP003_G31', N'Ghế 31', 'RAP003');
GO

-- Rạp 004
INSERT INTO Ghe (maGhe, tenGhe, maRap) VALUES
('RAP004_G1', N'Ghế 1', 'RAP004'), ('RAP004_G2', N'Ghế 2', 'RAP004'), ('RAP004_G3', N'Ghế 3', 'RAP004'), ('RAP004_G4', N'Ghế 4', 'RAP004'), ('RAP004_G5', N'Ghế 5', 'RAP004'),
('RAP004_G6', N'Ghế 6', 'RAP004'), ('RAP004_G7', N'Ghế 7', 'RAP004'), ('RAP004_G8', N'Ghế 8', 'RAP004'), ('RAP004_G9', N'Ghế 9', 'RAP004'), ('RAP004_G10', N'Ghế 10', 'RAP004'),
('RAP004_G11', N'Ghế 11', 'RAP004'), ('RAP004_G12', N'Ghế 12', 'RAP004'), ('RAP004_G13', N'Ghế 13', 'RAP004'), ('RAP004_G14', N'Ghế 14', 'RAP004'), ('RAP004_G15', N'Ghế 15', 'RAP004'),
('RAP004_G16', N'Ghế 16', 'RAP004'), ('RAP004_G17', N'Ghế 17', 'RAP004'), ('RAP004_G18', N'Ghế 18', 'RAP004'), ('RAP004_G19', N'Ghế 19', 'RAP004'), ('RAP004_G20', N'Ghế 20', 'RAP004'),
('RAP004_G21', N'Ghế 21', 'RAP004'), ('RAP004_G22', N'Ghế 22', 'RAP004'), ('RAP004_G23', N'Ghế 23', 'RAP004'), ('RAP004_G24', N'Ghế 24', 'RAP004'), ('RAP004_G25', N'Ghế 25', 'RAP004'),
('RAP004_G26', N'Ghế 26', 'RAP004'), ('RAP004_G27', N'Ghế 27', 'RAP004'), ('RAP004_G28', N'Ghế 28', 'RAP004'), ('RAP004_G29', N'Ghế 29', 'RAP004'), ('RAP004_G30', N'Ghế 30', 'RAP004');
GO

INSERT INTO NhanVien (maNV, tenNV, diaChi, soDienThoai, ngaySinh, email, gioiTinh)
VALUES
('NV01', N'Trần Thị Tường Vân', N'71 Điện Biên Phủ, TP.HCM', '0905123456', '2006-11-30', 'vantran@example.com', N'Nữ'),
('NV02', N'Bùi Thái Vy', N'100 Quang Trung, Bạc Liêu', '0987654321', '2006-07-22', 'vybui@example.com', N'Nữ'),
('NV03', N'Nguyễn Xuân Việt Anh', N'67 Nguyễn Huệ, Đà Lạt', '0912345678', '2006-09-10', 'anhnguyen@example.com', N'Nam');
GO

INSERT INTO TaiKhoan (maNV, taiKhoan, matKhau, vaiTro)
VALUES
('NV01', N'tranthituongvan', N'123456', N'QUAN_LY'),
('NV02', N'buithaivy', N'123456', N'NHAN_VIEN'),
('NV03', N'nguyenxuanvietanh', N'123456', N'NHAN_VIEN');
GO

INSERT INTO KhachHang (maKH, hoTen, gioiTinh, soDT, diaChi)
VALUES
('KH001', N'Nguyễn Hải Đăng', N'Nam', '0911223344', N'Quận 1, TP HCM'),
('KH002', N'Trần Minh Châu', N'Nữ', '0922334455', N'Quận 2, TP HCM'),
('KH003', N'Lê Quốc Bảo', N'Nam', '0933445566', N'Quận 4, TP HCM'),
('KH004', N'Phạm Thu Hằng', N'Nữ', '0944556677', N'Quận 6, TP HCM'),
('KH005', N'Võ Thanh Tâm', N'Nam', '0955667788', N'Quận 8, TP HCM'),

('KH006', N'Đinh Mỹ Linh', N'Nữ', '0966778899', N'Quận 9, TP HCM'),
('KH007', N'Bùi Đức Anh', N'Nam', '0977889900', N'Quận Bình Thạnh, TP HCM'),
('KH008', N'Huỳnh Gia Hân', N'Nữ', '0988990011', N'Quận Gò Vấp, TP HCM'),
('KH009', N'Ngô Thành Đạt', N'Nam', '0909001122', N'Quận Tân Bình, TP HCM'),
('KH010', N'Dương Ngọc Mai', N'Nữ', '0910112233', N'Quận Tân Phú, TP HCM'),

('KH011', N'Nguyễn Tuấn Kiệt', N'Nam', '0921223344', N'Quận 10, TP HCM'),
('KH012', N'Trần Bảo Ngọc', N'Nữ', '0932334455', N'Quận 11, TP HCM'),
('KH013', N'Lý Hoàng Phúc', N'Nam', '0943445566', N'Quận 12, TP HCM'),
('KH014', N'Cao Thị Diễm', N'Nữ', '0954556677', N'Bình Tân, TP HCM'),
('KH015', N'Phan Quốc Khánh', N'Nam', '0965667788', N'Hóc Môn, TP HCM'),

('KH016', N'Vũ Thanh Hương', N'Nữ', '0976778899', N'Củ Chi, TP HCM'),
('KH017', N'Tạ Minh Hoàng', N'Nam', '0987889900', N'Biên Hòa, Đồng Nai'),
('KH018', N'Đoàn Thị Yến Nhi', N'Nữ', '0908990011', N'Dĩ An, Bình Dương'),
('KH019', N'Hồ Quốc Trung', N'Nam', '0919001122', N'Thuận An, Bình Dương'),
('KH020', N'La Ngọc Ánh', N'Nữ', '0920112233', N'Vũng Tàu'),

('KH021', N'Châu Minh Tài', N'Nam', '0931223344', N'Long An'),
('KH022', N'Lâm Thị Thanh', N'Nữ', '0942334455', N'Tiền Giang'),
('KH023', N'Kha Gia Huy', N'Nam', '0953445566', N'Bến Tre'),
('KH024', N'Đặng Mỹ Duyên', N'Nữ', '0964556677', N'Vĩnh Long'),
('KH025', N'Hoàng Minh Nhật', N'Nam', '0975667788', N'Cần Thơ'),

('KH026', N'Lương Thu Trang', N'Nữ', '0986778899', N'Hà Nội'),
('KH027', N'Nguyễn Hoàng Nam', N'Nam', '0907889900', N'Đà Nẵng'),
('KH028', N'Phạm Thảo Vy', N'Nữ', '0918990011', N'Huế'),
('KH029', N'Đỗ Hữu Phước', N'Nam', '0929001122', N'Hải Phòng'),
('KH030', N'Trịnh Ngọc Trâm', N'Nữ', '0930112233', N'Nam Định');

GO

INSERT INTO Ve (maVe, maGhe, ngayBan, maSuatChieu, daThanhToan)
VALUES
('VE0001', 'RAP001_G1', '2026-04-01', 'SC001', 1),
('VE0002', 'RAP001_G2', '2026-04-01', 'SC001', 1),

('VE0003', 'RAP002_G1', '2026-04-02', 'SC002', 1),

('VE0004', 'RAP003_G1', '2026-04-03', 'SC003', 1),
('VE0005', 'RAP003_G2', '2026-04-03', 'SC003', 1),

('VE0006', 'RAP004_G1', '2026-04-04', 'SC004', 1),
('VE0007', 'RAP001_G3', '2026-04-04', 'SC007', 1),

('VE0008', 'RAP001_G4', '2026-04-05', 'SC001', 1),

('VE0009', 'RAP001_G5', '2026-04-06', 'SC006', 1),
('VE0010', 'RAP001_G6', '2026-04-06', 'SC009', 1),

('VE0011', 'RAP002_G2', '2026-04-07', 'SC002', 1),
('VE0012', 'RAP001_G7', '2026-04-07', 'SC008', 1),

('VE0013', 'RAP001_G8', '2026-04-08', 'SC006', 1),
('VE0014', 'RAP001_G9', '2026-04-08', 'SC010', 1),

('VE0015', 'RAP003_G3', '2026-04-09', 'SC003', 1),
('VE0016', 'RAP001_G10', '2026-04-09', 'SC005', 1),

('VE0017', 'RAP001_G11', '2026-04-10', 'SC001', 1),

('VE0018', 'RAP001_G12', '2026-04-11', 'SC007', 1),

('VE0019', 'RAP001_G13', '2026-04-12', 'SC008', 1),

('VE0020', 'RAP001_G14', '2026-04-13', 'SC009', 1),
('VE0021', 'RAP004_G2', '2026-04-13', 'SC004', 1),

('VE0022', 'RAP002_G3', '2026-04-14', 'SC002', 1),

('VE0023', 'RAP003_G4', '2026-04-15', 'SC003', 1),

('VE0024', 'RAP001_G15', '2026-04-16', 'SC010', 1),

('VE0025', 'RAP001_G16', '2026-04-17', 'SC008', 1),

('VE0026', 'RAP001_G17', '2026-04-18', 'SC006', 1),

('VE0027', 'RAP004_G3', '2026-04-19', 'SC004', 1),

('VE0028', 'RAP001_G18', '2026-04-20', 'SC001', 1),

('VE0029', 'RAP001_G19', '2026-04-21', 'SC009', 1),

('VE0030', 'RAP002_G4', '2026-04-22', 'SC002', 1),

('VE0031', 'RAP001_G20', '2026-04-23', 'SC005', 1),

('VE0032', 'RAP003_G5', '2026-04-24', 'SC003', 1),
('VE0033', 'RAP001_G21', '2026-04-24', 'SC006', 1),
('VE0034', 'RAP001_G22', '2026-04-24', 'SC009', 1),

('VE0035', 'RAP001_G23', '2026-04-25', 'SC010', 1),

('VE0036', 'RAP001_G24', '2026-04-26', 'SC008', 1),
('VE0037', 'RAP002_G5', '2026-04-26', 'SC002', 1),

('VE0038', 'RAP001_G25', '2026-04-27', 'SC005', 1),

('VE0039', 'RAP004_G4', '2026-04-28', 'SC004', 1),
('VE0040', 'RAP001_G3', '2026-04-28', 'SC001', 1),

('VE0041', 'RAP001_G2', '2026-04-29', 'SC007', 1),
('VE0042', 'RAP003_G6', '2026-04-29', 'SC003', 1),
('VE0043', 'RAP002_G6', '2026-04-29', 'SC002', 1);
GO

INSERT INTO HoaDon (maHoaDon, ngayLap, maNV, maKH, tongTien)
VALUES
('HD001', '2026-04-01', 'NV01', 'KH001', 90000),
('HD002', '2026-04-02', 'NV02', 'KH002', 45000),
('HD003', '2026-04-03', 'NV03', 'KH003', 90000),
('HD004', '2026-04-04', 'NV01', 'KH004', 90000),
('HD005', '2026-04-05', 'NV02', 'KH005', 45000),

('HD006', '2026-04-06', 'NV03', 'KH006', 90000),
('HD007', '2026-04-07', 'NV01', 'KH007', 45000),
('HD008', '2026-04-08', 'NV02', 'KH008', 45000),
('HD009', '2026-04-09', 'NV03', 'KH009', 45000),
('HD010', '2026-04-10', 'NV01', 'KH010', 90000),

('HD011', '2026-04-11', 'NV02', 'KH011', 45000),
('HD012', '2026-04-12', 'NV03', 'KH012', 45000),
('HD013', '2026-04-13', 'NV01', 'KH013', 90000),
('HD014', '2026-04-14', 'NV02', 'KH014', 45000),
('HD015', '2026-04-15', 'NV03', 'KH015', 45000),

('HD016', '2026-04-16', 'NV01', 'KH016', 45000),
('HD017', '2026-04-17', 'NV02', 'KH017', 90000),
('HD018', '2026-04-18', 'NV03', 'KH018', 45000),
('HD019', '2026-04-19', 'NV01', 'KH019', 45000),
('HD020', '2026-04-20', 'NV02', 'KH020', 90000),

('HD021', '2026-04-21', 'NV03', 'KH021', 45000),
('HD022', '2026-04-22', 'NV01', 'KH022', 45000),
('HD023', '2026-04-23', 'NV02', 'KH023', 90000),
('HD024', '2026-04-24', 'NV03', 'KH024', 45000),
('HD025', '2026-04-25', 'NV01', 'KH025', 90000),

('HD026', '2026-04-26', 'NV02', 'KH026', 45000),
('HD027', '2026-04-27', 'NV03', 'KH027', 90000),
('HD028', '2026-04-28', 'NV01', 'KH028', 45000),
('HD029', '2026-04-29', 'NV02', 'KH029', 90000),
('HD030', '2026-05-03', 'NV03', 'KH030', 90000);
GO

INSERT INTO ChiTietHoaDon (maHoaDon, maVe, soLuong, giaVe) 
VALUES
('HD001', 'VE0001', 1, 45000),
('HD001', 'VE0002', 1, 45000),

('HD002', 'VE0003', 1, 45000),

('HD003', 'VE0004', 1, 45000),
('HD003', 'VE0005', 1, 45000),

('HD004', 'VE0006', 1, 45000),
('HD004', 'VE0007', 1, 45000),

('HD005', 'VE0008', 1, 45000),

('HD006', 'VE0009', 1, 45000),
('HD006', 'VE0010', 1, 45000),

('HD007', 'VE0011', 1, 45000),

('HD008', 'VE0012', 1, 45000),

('HD009', 'VE0013', 1, 45000),

('HD010', 'VE0014', 1, 45000),
('HD010', 'VE0015', 1, 45000),

('HD011', 'VE0016', 1, 45000),

('HD012', 'VE0017', 1, 45000),

('HD013', 'VE0018', 1, 45000),
('HD013', 'VE0019', 1, 45000),

('HD014', 'VE0020', 1, 45000),

('HD015', 'VE0021', 1, 45000),

('HD016', 'VE0022', 1, 45000),

('HD017', 'VE0023', 1, 45000),
('HD017', 'VE0024', 1, 45000),

('HD018', 'VE0025', 1, 45000),

('HD019', 'VE0026', 1, 45000),

('HD020', 'VE0027', 1, 45000),
('HD020', 'VE0028', 1, 45000),

('HD021', 'VE0029', 1, 45000),

('HD022', 'VE0030', 1, 45000),

('HD023', 'VE0031', 1, 45000),
('HD023', 'VE0032', 1, 45000),

('HD024', 'VE0033', 1, 45000),

('HD025', 'VE0034', 1, 45000),
('HD025', 'VE0035', 1, 45000),

('HD026', 'VE0036', 1, 45000),

('HD027', 'VE0037', 1, 45000),
('HD027', 'VE0038', 1, 45000),

('HD028', 'VE0039', 1, 45000),

('HD029', 'VE0040', 1, 45000),
('HD029', 'VE0041', 1, 45000),

('HD030', 'VE0042', 1, 45000),
('HD030', 'VE0043', 1, 45000);
GO
Use master