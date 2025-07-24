package Model;


public class Currency {
    private int id;
    private String code;
    private String fullName;
    private String sign;

    public int getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public Currency(String code, String fullName, String sign) {
        this.code = code;
        this.fullName = fullName;
        this.sign = sign;
    }

    public String getSign() {
        return sign;
    }

    public Currency(int ID, String code, String fullName, String sign) {
        this.id = ID;
        this.code = code;
        this.fullName = fullName;
        this.sign = sign;
    }
}
