package com.cloudeagle.dropbox.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Team member model for Dropbox Business API
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TeamMember {
    @JsonProperty("profile")
    private TeamMemberProfile profile;
    
    // Getters and Setters
    public TeamMemberProfile getProfile() { return profile; }
    public void setProfile(TeamMemberProfile profile) { this.profile = profile; }
}