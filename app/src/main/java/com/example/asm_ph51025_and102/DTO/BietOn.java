package com.example.asm_ph51025_and102.DTO;

public class BietOn {
    private String id;
    private String tenTD;
    private String ND;

    // Phải có constructor mặc định cho Firestore deserialization
    public BietOn() {}

    public BietOn(String tenTD, String ND) {
        this.tenTD = tenTD;
        this.ND = ND;
    }

    // Getter và Setter cho các thuộc tính
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTenTD() {
        return tenTD;
    }

    public void setTenTD(String tenTD) {
        this.tenTD = tenTD;
    }

    public String getND() {
        return ND;
    }

    public void setND(String ND) {
        this.ND = ND;
    }
}
