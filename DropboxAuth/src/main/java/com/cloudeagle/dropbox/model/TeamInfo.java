package com.cloudeagle.dropbox.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Team information model for Dropbox Business API
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TeamInfo {
    @JsonProperty("name")
    private String name;
    
    @JsonProperty("team_id")
    private String teamId;
    
    @JsonProperty("num_licensed_users")
    private Integer numLicensedUsers;
    
    @JsonProperty("num_provisioned_users")
    private Integer numProvisionedUsers;
    
    @JsonProperty("policies")
    private TeamPolicies policies;
    
    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getTeamId() { return teamId; }
    public void setTeamId(String teamId) { this.teamId = teamId; }
    
    public Integer getNumLicensedUsers() { return numLicensedUsers; }
    public void setNumLicensedUsers(Integer numLicensedUsers) { this.numLicensedUsers = numLicensedUsers; }
    
    public Integer getNumProvisionedUsers() { return numProvisionedUsers; }
    public void setNumProvisionedUsers(Integer numProvisionedUsers) { this.numProvisionedUsers = numProvisionedUsers; }
    
    public TeamPolicies getPolicies() { return policies; }
    public void setPolicies(TeamPolicies policies) { this.policies = policies; }
    
    @Override
    public String toString() {
        return String.format("TeamInfo{name='%s', teamId='%s', licensedUsers=%d, provisionedUsers=%d}", 
                name, teamId, numLicensedUsers, numProvisionedUsers);
    }
}