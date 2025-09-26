package com.cloudeagle.dropbox.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Request model for listing team members
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MembersListRequest {
    @JsonProperty("limit")
    private int limit = 100;
    
    @JsonProperty("include_removed")
    private boolean includeRemoved = false;
    
    // Getters and Setters
    public int getLimit() { return limit; }
    public void setLimit(int limit) { this.limit = limit; }
    
    public boolean isIncludeRemoved() { return includeRemoved; }
    public void setIncludeRemoved(boolean includeRemoved) { this.includeRemoved = includeRemoved; }
}