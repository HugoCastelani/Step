package com.enoughspam.step.database.domains;

/**
 * Created by Hugo Castelani
 * Date: 04/07/17
 * Time: 17:26
 */

public class Country {
    private int id;
    private int code;
    private String name;
    private String mask;

    public Country(int code, String name, String mask) {
        this.code = code;
        this.name = name;
        this.mask = mask;
    }

    public Country(int id, int code, String name, String mask) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.mask = mask;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMask() {
        return mask;
    }

    public void setMask(String mask) {
        this.mask = mask;
    }
}
