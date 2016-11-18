package com.transit_ads.tabs.model;

/**
 * Created by admin on 7/15/2016.
 */

public class Advertisements {

    int id;
    String name;
    int company_id;
    int group_id;
    String type;
    String content;
    String categories;
    String thumbnail;
    String created_at;
    String updated_at;
    String deleted_at;


    // constructors
    public Advertisements() {
    }

    public Advertisements(int id, String name, int company_id,int group_id, String type, String content, String categories, String thumbnail) {
        this.id = id;
        this.name = name;
        this.company_id = company_id;
        this.group_id = group_id;
        this.type = type;
        this.content = content;
        this.categories = categories;
        this.thumbnail = thumbnail;
    }

    public Advertisements(String name, int company_id,int group_id, String type, String content, String categories, String thumbnail) {
        this.name = name;
        this.company_id = company_id;
        this.group_id = group_id;
        this.type = type;
        this.content = content;
        this.categories = categories;
        this.thumbnail = thumbnail;
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

    public int getCompany_id() {
        return company_id;
    }

    public int getGroup_id() {
        return group_id;
    }

    public void setGroup_id(int group_id) {
        this.group_id = group_id;
    }

    public void setCompany_id(int company_id) {
        this.company_id = company_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
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
