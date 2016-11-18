package com.transit_ads.tabs.model;

/**
 * Created by admin on 14-05-2016.
 */
public class DbGetterSetter {

    //private variables
    int ID;
    String ADD_ID;
    String VIDEOCLICKTXT;
    String CLIENT;
    String VIDEO_PATH;
    String IMAGE;
    String TEXT_ADD;
    String GROUPS;
    String CATEGORY;

    // Empty constructor
    public DbGetterSetter(){

    }


    // constructor
    public DbGetterSetter(int ID, String ADD_ID, String CLIENT,String VIDEO_PATH, String IMAGE, String TEXT_ADD,String GROUPS, String CATEGORY, String VIDEOCLICKTXT){

        this.ID = ID;
        this.ADD_ID = ADD_ID;
        this.CLIENT = CLIENT;
        this.VIDEO_PATH =VIDEO_PATH;
        this.IMAGE=IMAGE;
        this.TEXT_ADD=TEXT_ADD;
        this.GROUPS=GROUPS;
        this.CATEGORY=CATEGORY;
        this.VIDEOCLICKTXT=VIDEOCLICKTXT;

    }

    // constructor
    public DbGetterSetter(String ADD_ID, String CLIENT,String VIDEO_PATH, String IMAGE, String TEXT_ADD,String GROUPS, String CATEGORY, String VIDEOCLICKTXT){

        this.ADD_ID = ADD_ID;
        this.CLIENT = CLIENT;
        this.VIDEO_PATH =VIDEO_PATH;
        this.IMAGE=IMAGE;
        this.TEXT_ADD=TEXT_ADD;
        this.GROUPS=GROUPS;
        this.CATEGORY=CATEGORY;
        this.VIDEOCLICKTXT=VIDEOCLICKTXT;

    }
    public String getADD_ID() {
        return ADD_ID;
    }

    public void setADD_ID(String ADD_ID) {
        this.ADD_ID = ADD_ID;
    }

    public String getCLIENT() {
        return CLIENT;
    }

    public void setCLIENT(String CLIENT) {
        this.CLIENT = CLIENT;
    }

    public String getVIDEO_PATH() {
        return VIDEO_PATH;
    }

    public void setVIDEO_PATH(String VIDEO_PATH) {
        this.VIDEO_PATH = VIDEO_PATH;
    }

    public String getIMAGE() {
        return IMAGE;
    }

    public void setIMAGE(String IMAGE) {
        this.IMAGE = IMAGE;
    }

    public String getTEXT_ADD() {
        return TEXT_ADD;
    }

    public void setTEXT_ADD(String TEXT_ADD) {
        this.TEXT_ADD = TEXT_ADD;
    }

    public String getGROUPS() {
        return GROUPS;
    }

    public void setGROUPS(String GROUPS) {
        this.GROUPS = GROUPS;
    }

    public String getCATEGORY() {
        return CATEGORY;
    }

    public void setCATEGORY(String CATEGORY) {
        this.CATEGORY = CATEGORY;
    }

    public String getVIDEOCLICKTXT() {
        return VIDEOCLICKTXT;
    }

    public void setVIDEOCLICKTXT(String VIDEOCLICKTXT) {
        this.VIDEOCLICKTXT = VIDEOCLICKTXT;
    }


    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }


}