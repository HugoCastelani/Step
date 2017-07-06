package com.enoughspam.step.database.domains;

/**
 * Created by Hugo Castelani
 * Date: 27/06/17
 * Time: 17:02
 */

public class User {
    private long id;
    private String idSocial;
    private String name;

    public User(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public User(String idSocial, String name) {
        this.idSocial = idSocial;
        this.name = name;
    }

    public User(long id, String idSocial, String name) {
        this.id = id;
        this.idSocial = idSocial;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getIdSocial() {
        return idSocial;
    }

    public void setIdSocial(String idSocial) {
        this.idSocial = idSocial;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
