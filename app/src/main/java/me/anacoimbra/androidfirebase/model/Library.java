package me.anacoimbra.androidfirebase.model;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;

/**
 * Created by anacoimbra on 20/07/17.
 */

public class Library {

    private String uid;
    @SerializedName("name")
    private String name;
    @SerializedName("url")
    private String url;
    @SerializedName("date")
    private String date;
    @SerializedName("min_sdk")
    private int min_sdk;
    @SerializedName("liscence")
    private String liscence;
    @SerializedName("description")
    private String description;
    private HashMap<String, Boolean> users = new HashMap<>();

    public Library() {
    }

    public Library(String name, String url, String date, int min_sdk, String liscence, String description) {
        this.name = name;
        this.url = url;
        this.date = date;
        this.min_sdk = min_sdk;
        this.liscence = liscence;
        this.description = description;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getMin_sdk() {
        return min_sdk;
    }

    public void setMin_sdk(int minSdk) {
        this.min_sdk = minSdk;
    }

    public String getLiscence() {
        return liscence;
    }

    public void setLiscence(String liscence) {
        this.liscence = liscence;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public HashMap<String, Boolean> getUsers() {
        return users;
    }

    public void setUsers(HashMap<String, Boolean> users) {
        this.users = users;
    }
}
