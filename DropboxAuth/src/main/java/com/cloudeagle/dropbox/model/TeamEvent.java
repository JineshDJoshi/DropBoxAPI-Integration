package com.cloudeagle.dropbox.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Team event model for Dropbox Business API
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TeamEvent {
    @JsonProperty("timestamp")
    private String timestamp;
    
    @JsonProperty("event_category")
    private String eventCategory;
    
    @JsonProperty("event_type")
    private String eventType;
    
    @JsonProperty("details")
    private Object details;
    
    @JsonProperty("actor")
    private Actor actor;
    
    @JsonProperty("origin")
    private Origin origin;
    
    // Getters and Setters
    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
    
    public String getEventCategory() { return eventCategory; }
    public void setEventCategory(String eventCategory) { this.eventCategory = eventCategory; }
    
    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }
    
    public Object getDetails() { return details; }
    public void setDetails(Object details) { this.details = details; }
    
    public Actor getActor() { return actor; }
    public void setActor(Actor actor) { this.actor = actor; }
    
    public Origin getOrigin() { return origin; }
    public void setOrigin(Origin origin) { this.origin = origin; }
    
    @Override
    public String toString() {
        return String.format("TeamEvent{timestamp='%s', category='%s', type='%s'}", 
                timestamp, eventCategory, eventType);
    }
}