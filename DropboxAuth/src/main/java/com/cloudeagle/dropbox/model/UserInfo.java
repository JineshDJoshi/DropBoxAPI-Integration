package com.cloudeagle.dropbox.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * User information model for Dropbox Business API
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserInfo {
    @JsonProperty("team_member_id")
    private String teamMemberId;
    
    @JsonProperty("display_name")
    private String displayName;
    
    @JsonProperty("email")
    private String email;
    
    // Getters and Setters
    public String getTeamMemberId() { return teamMemberId; }
    public void setTeamMemberId(String teamMemberId) { this.teamMemberId = teamMemberId; }
    
    public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}