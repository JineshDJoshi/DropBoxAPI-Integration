package com.cloudeagle.dropbox.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Origin model for Dropbox Business API events
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Origin {
    @JsonProperty("geo_location")
    private GeoLocation geoLocation;
    
    @JsonProperty("host")
    private HostInfo host;
    
    // Getters and Setters
    public GeoLocation getGeoLocation() { return geoLocation; }
    public void setGeoLocation(GeoLocation geoLocation) { this.geoLocation = geoLocation; }
    
    public HostInfo getHost() { return host; }
    public void setHost(HostInfo host) { this.host = host; }
}