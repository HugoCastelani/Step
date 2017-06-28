package com.enoughspam.step.database.domains;

/**
 * Created by Hugo Castelani
 * Date: 27/06/17
 * Time: 17:05
 */

public class Phone {
    private long id;
    private String number;
    private String areaCode;
    private User user;

    public Phone() {}

    public Phone(String number, String areaCode, User user) {
        this.number = number;
        this.areaCode = areaCode;
        this.user = user;
    }

    public Phone(long id, String number, String areaCode, User user) {
        this.id = id;
        this.number = number;
        this.areaCode = areaCode;
        this.user = user;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
