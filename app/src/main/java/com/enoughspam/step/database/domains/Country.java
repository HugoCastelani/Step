package com.enoughspam.step.database.domains;

/**
 * Created by Hugo Castelani
 * Date: 04/07/17
 * Time: 17:26
 */

public class Country {
    private long id;
    private String name;

    public Country(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Country(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
