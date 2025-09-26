package com.cloudeagle.dropbox.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Geographical location model for Dropbox Business API
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GeoLocation {
    @JsonProperty("city")
    private String city;
    
    @JsonProperty("region")
    private String region;
    
    @JsonProperty("country")
    private String country;
    
    // Getters and Setters
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    
    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }
    
    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }
}