package com.enoughspam.step.database.domains;

/**
 * Created by Hugo Castelani
 * Date: 27/06/17
 * Time: 17:05
 */

public class Phone {
    private int id;
    private long number;
    private int areaCode;
    private User user;

    public Phone(long number, int areaCode, User user) {
        this.number = number;
        this.areaCode = areaCode;
        this.user = user;
    }

    public Phone(int id, long number, int areaCode, User user) {
        this.id = id;
        this.number = number;
        this.areaCode = areaCode;
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getNumber() {
        return number;
    }

    public void setNumber(long number) {
        this.number = number;
    }

    public int getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(int areaCode) {
        this.areaCode = areaCode;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
