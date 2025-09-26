package com.cloudeagle.dropbox.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Team policies model for Dropbox Business API
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TeamPolicies {
    @JsonProperty("sharing")
    private SharingPolicies sharing;
    
    @JsonProperty("emm_state")
    private String emmState;
    
    @JsonProperty("office_addin")
    private String officeAddin;
    
    // Getters and Setters
    public SharingPolicies getSharing() { return sharing; }
    public void setSharing(SharingPolicies sharing) { this.sharing = sharing; }
    
    public String getEmmState() { return emmState; }
    public void setEmmState(String emmState) { this.emmState = emmState; }
    
    public String getOfficeAddin() { return officeAddin; }
    public void setOfficeAddin(String officeAddin) { this.officeAddin = officeAddin; }
}