package entity;

public class Rap {
    private String maRap;
    private int soLuongGhe;
    private String tenRap;

    public Rap(String maRap) {
        setMaRap(maRap);
        this.soLuongGhe = 0;
        this.tenRap = "";
    }

    public Rap(String maRap, int soLuongGhe, String tenRap) {
        setMaRap(maRap);
        setSoLuongGhe(soLuongGhe);
        setTenRap(tenRap);
    }

    public String getMaRap() { return maRap; }
    public void setMaRap(String maRap) {
        if (maRap == null || maRap.isEmpty()) throw new IllegalArgumentException("Mã rạp rỗng!");
        this.maRap = maRap;
    }

    public int getSoLuongGhe() { return soLuongGhe; }
    public void setSoLuongGhe(int soLuongGhe) {
        if (soLuongGhe <= 0) throw new IllegalArgumentException("Số lượng ghế phải > 0!");
        this.soLuongGhe = soLuongGhe;
    }

    public String getTenRap() { return tenRap; }
    public void setTenRap(String tenRap) {
        if (tenRap == null || tenRap.isEmpty()) throw new IllegalArgumentException("Tên rạp rỗng!");
        this.tenRap = tenRap;
    }

	@Override
	public String toString() {
		return "Rap [maRap=" + maRap + ", soLuongGhe=" + soLuongGhe + ", tenRap=" + tenRap + "]";
	}

    
}
