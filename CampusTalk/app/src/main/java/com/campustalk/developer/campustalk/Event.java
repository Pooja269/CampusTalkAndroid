package com.campustalk.developer.campustalk;

/**
 * Created by khushali on 20/04/2016.
 */
public class Event {

    String eventId;
    String eventTitle;
    String eventTime;
    String eventStartDate;
    String eventDescription;
    String eventEndDate;
    String eventImage;

    public Event(){}

    public Event(String eventId, String eventTitle, String eventTime, String eventStartDate, String eventDescription, String eventEndDate, String eventImage) {
        this.eventId = eventId;
        this.eventTitle = eventTitle;
        this.eventTime = eventTime;
        this.eventStartDate = eventStartDate;
        this.eventDescription = eventDescription;
        this.eventEndDate = eventEndDate;
        this.eventImage = eventImage;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public String getEventTime() {
        return eventTime;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

    public String getEventStartDate() {
        return eventStartDate;
    }

    public void setEventStartDate(String eventStartDate) {
        this.eventStartDate = eventStartDate;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public String getEventEndDate() {
        return eventEndDate;
    }

    public void setEventEndDate(String eventEndDate) {
        this.eventEndDate = eventEndDate;
    }

    public String getEventImage() {
        return eventImage;
    }

    public void setEventImage(String eventImage) {
        this.eventImage = eventImage;
    }
}
