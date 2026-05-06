package entity;

public class TaiKhoan {
	private NhanVien nhanVien;
	private String taiKhoan;
	private String matKhau;
    private VaiTro vaiTro;
	
	public TaiKhoan() {
		
	}

	public TaiKhoan(NhanVien nhanVien, String taiKhoan, String matKhau, VaiTro vaiTro) {
		this.nhanVien = nhanVien;
		this.taiKhoan = taiKhoan;
		this.matKhau = matKhau;
        this.vaiTro = vaiTro;
	}

	public NhanVien getNhanVien() {
		return nhanVien;
	}

	public void setNhanVien(NhanVien nhanVien) {
		this.nhanVien = nhanVien;
	}

	public String getTaiKhoan() {
		return taiKhoan;
	}

	public void setTaiKhoan(String taiKhoan) {
		this.taiKhoan = taiKhoan;
	}

	public String getMatKhau() {
		return matKhau;
	}

	public void setMatKhau(String matKhau) {
		this.matKhau = matKhau;
	}

    public VaiTro getVaiTro() {
        return vaiTro;
    }

    public void setVaiTro(VaiTro vaiTro) {
        this.vaiTro = vaiTro;
    }

    @Override
	public String toString() {
		return "TaiKhoan [nhanVien=" + nhanVien + ", taiKhoan=" + taiKhoan + ", matKhau=" + matKhau + "]";
	}
	
	
	
	
}
