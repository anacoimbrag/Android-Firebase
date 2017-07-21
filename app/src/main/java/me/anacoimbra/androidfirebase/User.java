package me.anacoimbra.androidfirebase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by anacoimbra on 20/07/17.
 */

public class User {
    private String uid;
    private String name;
    private String email;
    private String picture;
    private List<String> interests;

    public User() {
    }

    public User(String uid, String name, String email, String picture, List<String> interests) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.picture = picture;
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

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public List<String> getInterests() {
        return interests;
    }

    public void setInterests(List<String> interests) {
        this.interests = interests;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("email", email);
        map.put("picture", picture);
        map.put("interests", interests);

        return map;
    }
}
