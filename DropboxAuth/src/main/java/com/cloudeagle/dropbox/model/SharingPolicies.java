package com.cloudeagle.dropbox.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Sharing policies model for Dropbox Business API
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SharingPolicies {
    @JsonProperty("shared_folder_member_policy")
    private String sharedFolderMemberPolicy;
    
    @JsonProperty("shared_folder_join_policy")
    private String sharedFolderJoinPolicy;
    
    @JsonProperty("shared_link_create_policy")
    private String sharedLinkCreatePolicy;
    
    // Getters and Setters
    public String getSharedFolderMemberPolicy() { return sharedFolderMemberPolicy; }
    public void setSharedFolderMemberPolicy(String sharedFolderMemberPolicy) { this.sharedFolderMemberPolicy = sharedFolderMemberPolicy; }
    
    public String getSharedFolderJoinPolicy() { return sharedFolderJoinPolicy; }
    public void setSharedFolderJoinPolicy(String sharedFolderJoinPolicy) { this.sharedFolderJoinPolicy = sharedFolderJoinPolicy; }
    
    public String getSharedLinkCreatePolicy() { return sharedLinkCreatePolicy; }
    public void setSharedLinkCreatePolicy(String sharedLinkCreatePolicy) { this.sharedLinkCreatePolicy = sharedLinkCreatePolicy; }
}