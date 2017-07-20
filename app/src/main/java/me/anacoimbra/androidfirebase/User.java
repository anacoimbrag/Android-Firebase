package me.anacoimbra.androidfirebase;

import java.util.List;

/**
 * Created by anacoimbra on 20/07/17.
 */

public class User {
    private String uid;
    private String name;
    private String email;
    private List<String> interests;

    public User() {
    }

    public User(String uid, String name, String email, List<String> interests) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.interests = interests;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getInterests() {
        return interests;
    }

    public void setInterests(List<String> interests) {
        this.interests = interests;
    }
}
