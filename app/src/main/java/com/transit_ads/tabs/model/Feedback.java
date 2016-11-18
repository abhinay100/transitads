package com.transit_ads.tabs.model;

/**
 * Created by admin on 7/15/2016.
 */
public class Feedback {

    int id;
    int tablet_id;
    int company_id;
    int ad_id;
    String action;
    String created_at;
    String updated_at;
    String deleted_at;

    public Feedback() {
    }

    public Feedback(int tablet_id, int company_id, int ad_id, String action) {
        this.tablet_id = tablet_id;
        this.company_id = company_id;
        this.ad_id = ad_id;
        this.action = action;
    }

    public Feedback(int id, int tablet_id, int company_id, int ad_id, String action) {
        this.id = id;
        this.tablet_id = tablet_id;
        this.company_id = company_id;
        this.ad_id = ad_id;
        this.action = action;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTablet_id() {
        return tablet_id;
    }

    public void setTablet_id(int tablet_id) {
        this.tablet_id = tablet_id;
    }

    public int getCompany_id() {
        return company_id;
    }

    public void setCompany_id(int company_id) {
        this.company_id = company_id;
    }

    public int getAd_id() {
        return ad_id;
    }

    public void setAd_id(int ad_id) {
        this.ad_id = ad_id;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
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
