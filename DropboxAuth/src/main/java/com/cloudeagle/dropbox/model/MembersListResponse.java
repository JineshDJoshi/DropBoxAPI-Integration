package com.cloudeagle.dropbox.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Response model for listing team members
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MembersListResponse {
    @JsonProperty("members")
    private List<TeamMember> members;
    
    @JsonProperty("cursor")
    private String cursor;
    
    @JsonProperty("has_more")
    private boolean hasMore;
    
    // Getters and Setters
    public List<TeamMember> getMembers() { return members; }
    public void setMembers(List<TeamMember> members) { this.members = members; }
    
    public String getCursor() { return cursor; }
    public void setCursor(String cursor) { this.cursor = cursor; }
    
    public boolean isHasMore() { return hasMore; }
    public void setHasMore(boolean hasMore) { this.hasMore = hasMore; }
}