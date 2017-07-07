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

    public Country(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public Country(int id, int code, String name) {
        this.id = id;
        this.code = code;
        this.name = name;
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
}
