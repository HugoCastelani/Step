package com.enoughspam.step.domain;

/**
 * Created by Hugo Castelani
 * Date: 25/07/17
 * Time: 22:34
 */

public class Call {
    private String name;
    private String number;

    public Call(String name, String number) {
        this.name = name;
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
