package com.example.fiesta.models;

public class Event {

    private String eventName;
    private String eventDescription;
    private String eventContact;
    private String eventCoordinator;
    private String eventDate;
    private String festId;
    private String eventId;

    public Event(){}

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public String getEventContact() {
        return eventContact;
    }

    public void setEventContact(String eventContact) {
        this.eventContact = eventContact;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getFestId() {
        return festId;
    }

    public void setFestId(String festId) {
        this.festId = festId;
    }


    public String _getEventId() {
        return eventId;
    }

    public void _setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getEventCoordinator() {
        return eventCoordinator;
    }

    public void setEventCoordinator(String eventCoordinator) {
        this.eventCoordinator = eventCoordinator;
    }
}
