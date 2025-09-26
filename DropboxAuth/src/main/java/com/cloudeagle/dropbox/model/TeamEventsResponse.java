package com.cloudeagle.dropbox.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Response model for team events
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TeamEventsResponse {
    @JsonProperty("events")
    private List<TeamEvent> events;
    
    @JsonProperty("cursor")
    private String cursor;
    
    @JsonProperty("has_more")
    private boolean hasMore;
    
    // Getters and Setters
    public List<TeamEvent> getEvents() { return events; }
    public void setEvents(List<TeamEvent> events) { this.events = events; }
    
    public String getCursor() { return cursor; }
    public void setCursor(String cursor) { this.cursor = cursor; }
    
    public boolean isHasMore() { return hasMore; }
    public void setHasMore(boolean hasMore) { this.hasMore = hasMore; }
}