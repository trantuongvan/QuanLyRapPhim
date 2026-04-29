package entity;

public enum TheLoaiPhim {
    HANH_DONG("Hành động"),
    TINH_CAM("Tình cảm"),
    HAI_HUOC("Hài hước"),
    KINH_DI("Kinh dị"),
    HOAT_HINH("Hoạt hình"),
    TAM_LY("Tâm lý"),
    VIEN_TUONG("Viễn tưởng"),
    CHUA_RO("Chưa rõ");

    private final String tenHienThi;

    TheLoaiPhim(String tenHienThi) {
        this.tenHienThi = tenHienThi;
    }

    public String getTenHienThi() {
        return tenHienThi;
    }

    @Override
    public String toString() {
        return tenHienThi;
    }

    public static TheLoaiPhim fromTenHienThi(String ten) {
        for (TheLoaiPhim tl : TheLoaiPhim.values()) {
            if (tl.tenHienThi.equalsIgnoreCase(ten)) {
                return tl;
            }
        }
        return CHUA_RO;
    }
}
