package com.cloudeagle.dropbox.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Request model for team events
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TeamEventsRequest {
    @JsonProperty("limit")
    private int limit = 50;
    
    @JsonProperty("category")
    private String category;
    
    @JsonProperty("time")
    private TimeRange time;
    
    // Getters and Setters
    public int getLimit() { return limit; }
    public void setLimit(int limit) { this.limit = limit; }
    
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    
    public TimeRange getTime() { return time; }
    public void setTime(TimeRange time) { this.time = time; }
}