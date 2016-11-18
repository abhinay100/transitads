package com.transit_ads.tabs.model;

/**
 * Created by admin on 7/15/2016.
 */
public class Passenger {

    int id;
    String name;
    String phone;
    int wifi_used;
    String created_at;
    String updated_at;
    String deleted_at;

    public Passenger() {
    }

    public Passenger(String name, String phone, int wifi_used) {
        this.name = name;
        this.phone = phone;
        this.wifi_used = wifi_used;
    }

    public Passenger(int id, String name, String phone, int wifi_used) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.wifi_used = wifi_used;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDeleted_at() {
        return deleted_at;
    }

    public void setDeleted_at(String deleted_at) {
        this.deleted_at = deleted_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public int getWifi_used() {
        return wifi_used;
    }

    public void setWifi_used(int wifi_used) {
        this.wifi_used = wifi_used;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
