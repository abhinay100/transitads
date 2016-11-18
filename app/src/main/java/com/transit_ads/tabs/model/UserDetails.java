package com.transit_ads.tabs.model;

/**
 * Created by admin on 8/31/2016.
 */
public class UserDetails {
    int id;
    String name;
    String phone;
    String like;
    String like_ad_id;
    String created_at;
    String updated_at;
    String deleted_at;

    public UserDetails(int id, String name, String phone, String like, String like_ad_id) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.like = like;
        this.like_ad_id = like_ad_id;
    }

    public UserDetails(String name, String phone, String like, String like_ad_id) {
        this.name = name;
        this.phone = phone;
        this.like = like;
        this.like_ad_id = like_ad_id;
    }

    public UserDetails() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLike() {
        return like;
    }

    public void setLike(String like) {
        this.like = like;
    }

    public String getLike_ad_id() {
        return like_ad_id;
    }

    public void setLike_ad_id(String like_ad_id) {
        this.like_ad_id = like_ad_id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getDeleted_at() {
        return deleted_at;
    }

    public void setDeleted_at(String deleted_at) {
        this.deleted_at = deleted_at;
    }
}
