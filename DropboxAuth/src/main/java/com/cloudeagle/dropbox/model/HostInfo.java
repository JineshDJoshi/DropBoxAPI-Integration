package com.cloudeagle.dropbox.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Host information model for Dropbox Business API
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class HostInfo {
    @JsonProperty("host")
    private String host;
    
    // Getters and Setters
    public String getHost() { return host; }
    public void setHost(String host) { this.host = host; }
}

