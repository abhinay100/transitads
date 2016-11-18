package com.transit_ads.tabs.model;

/**
 * Created by admin on 9/1/2016.
 */
public class Categories {
    int id;
    String name;
    String icon;
    String color;
    String created_at;
    String updated_at;
    String deleted_at;

    public Categories() {
    }

    public Categories(String name, String icon, String color) {
        this.name = name;
        this.icon = icon;
        this.color = color;
    }

    public Categories(int id, String name, String icon, String color) {
        this.id = id;
        this.name = name;
        this.icon = icon;
        this.color = color;
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

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
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
