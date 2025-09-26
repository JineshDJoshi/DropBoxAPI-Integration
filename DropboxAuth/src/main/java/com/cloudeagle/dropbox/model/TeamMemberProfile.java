package com.cloudeagle.dropbox.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Team member profile model for Dropbox Business API
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TeamMemberProfile {
    @JsonProperty("team_member_id")
    private String teamMemberId;
    
    @JsonProperty("external_id")
    private String externalId;
    
    @JsonProperty("account_id")
    private String accountId;
    
    @JsonProperty("email")
    private String email;
    
    @JsonProperty("email_verified")
    private boolean emailVerified;
    
    @JsonProperty("name")
    private NameInfo name;
    
    @JsonProperty("membership_type")
    private String membershipType;
    
    @JsonProperty("status")
    private String status;
    
    @JsonProperty("joined_on")
    private String joinedOn;
    
    // Getters and Setters
    public String getTeamMemberId() { return teamMemberId; }
    public void setTeamMemberId(String teamMemberId) { this.teamMemberId = teamMemberId; }
    
    public String getExternalId() { return externalId; }
    public void setExternalId(String externalId) { this.externalId = externalId; }
    
    public String getAccountId() { return accountId; }
    public void setAccountId(String accountId) { this.accountId = accountId; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public boolean isEmailVerified() { return emailVerified; }
    public void setEmailVerified(boolean emailVerified) { this.emailVerified = emailVerified; }
    
    public NameInfo getName() { return name; }
    public void setName(NameInfo name) { this.name = name; }
    
    public String getMembershipType() { return membershipType; }
    public void setMembershipType(String membershipType) { this.membershipType = membershipType; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public String getJoinedOn() { return joinedOn; }
    public void setJoinedOn(String joinedOn) { this.joinedOn = joinedOn; }
    
    @Override
    public String toString() {
        return String.format("TeamMember{id='%s', email='%s', name='%s', status='%s'}", 
                teamMemberId, email, name != null ? name.getDisplayName() : "N/A", status);
    }
}