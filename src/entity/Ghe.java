package entity;

public class Ghe {
    private String maGhe;
    private String tenGhe;
    private Rap rap;

    public Ghe(String maGhe, String tenGhe, Rap rap) {
        setMaGhe(maGhe);
        setTenGhe(tenGhe);
        setRap(rap);
    }

    public String getMaGhe() {
        return maGhe;
    }

    public void setMaGhe(String maGhe) {
        if (maGhe == null || maGhe.isEmpty())
            throw new IllegalArgumentException("Mã ghế rỗng!");
        this.maGhe = maGhe;
    }

    public String getTenGhe() {
        return tenGhe;
    }

    public void setTenGhe(String tenGhe) {
        this.tenGhe = tenGhe;
    }

    public Rap getRap() {
        return rap;
    }

    public void setRap(Rap rap) {
        if (rap == null)
            throw new IllegalArgumentException("Rạp không được null!");
        this.rap = rap;
    }

    @Override
    public String toString() {
        return "Ghe [maGhe=" + maGhe + ", tenGhe=" + tenGhe + ", rap=" + rap.getMaRap() + "]";
    }
}