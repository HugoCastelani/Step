package com.enoughspam.step.database.domains;

/**
 * Created by Hugo Castelani
 * Date: 27/06/17
 * Time: 17:02
 */

public class User {
    private int id;
    private String socialId;
    private String name;

    public User(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public User(String socialId, String name) {
        this.socialId = socialId;
        this.name = name;
    }

    public User(int id, String socialId, String name) {
        this.id = id;
        this.socialId = socialId;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSocialId() {
        return socialId;
    }

    public void setSocialId(String socialId) {
        this.socialId = socialId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
