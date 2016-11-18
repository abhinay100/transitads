package com.transit_ads.tabs.model;

/**
 * Created by admin on 7/15/2016.
 */
public class PassengerHasFeedback {
    String passenger_id;
    String feedback_id;

    public PassengerHasFeedback() {
    }

    public PassengerHasFeedback(String passenger_id, String feedback_id) {
        this.passenger_id = passenger_id;
        this.feedback_id = feedback_id;
    }

    public PassengerHasFeedback(String passenger_id) {
        this.passenger_id = passenger_id;
    }

    public String getPassenger_id() {
        return passenger_id;
    }

    public void setPassenger_id(String passenger_id) {
        this.passenger_id = passenger_id;
    }

    public String getFeedback_id() {
        return feedback_id;
    }

    public void setFeedback_id(String feedback_id) {
        this.feedback_id = feedback_id;
    }
}
