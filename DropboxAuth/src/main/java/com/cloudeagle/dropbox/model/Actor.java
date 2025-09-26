package com.cloudeagle.dropbox.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Actor model for Dropbox Business API events
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Actor {
    @JsonProperty("user")
    private UserInfo user;
    
    // Getters and Setters
    public UserInfo getUser() { return user; }
    public void setUser(UserInfo user) { this.user = user; }
}