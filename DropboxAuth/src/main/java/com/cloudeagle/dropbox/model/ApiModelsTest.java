package com.cloudeagle.dropbox.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.jupiter.api.Assertions.*;
class ApiModelsTest {
    
    private ObjectMapper objectMapper;
    
    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }
    
    @Test
    void testTeamInfoDeserialization() throws Exception {
        String json = """
            {
                "name": "Test Team",
                "team_id": "dbtid:123",
                "num_licensed_users": 25,
                "num_provisioned_users": 20,
                "policies": {
                    "sharing": {
                        "shared_folder_member_policy": "team",
                        "shared_folder_join_policy": "from_anyone",
                        "shared_link_create_policy": "team_only"
                    },
                    "emm_state": "disabled",
                    "office_addin": "enabled"
                }
            }
            """;
        
        TeamInfo teamInfo = objectMapper.readValue(json, TeamInfo.class);
        
        assertNotNull(teamInfo, "TeamInfo should not be null");
        assertEquals("Test Team", teamInfo.getName(), "Team name should match");
        assertEquals("dbtid:123", teamInfo.getTeamId(), "Team ID should match");
        assertEquals(25, teamInfo.getNumLicensedUsers().intValue(), "Licensed users should match");
        assertEquals(20, teamInfo.getNumProvisionedUsers().intValue(), "Provisioned users should match");
        
        // Test nested policies
        TeamPolicies policies = teamInfo.getPolicies();
        assertNotNull(policies, "Policies should not be null");
        assertEquals("disabled", policies.getEmmState(), "EMM state should match");
        assertEquals("enabled", policies.getOfficeAddin(), "Office add-in should match");
        
        // Test nested sharing policies
        SharingPolicies sharing = policies.getSharing();
        assertNotNull(sharing, "Sharing policies should not be null");
        assertEquals("team", sharing.getSharedFolderMemberPolicy(), "Shared folder member policy should match");
        assertEquals("from_anyone", sharing.getSharedFolderJoinPolicy(), "Shared folder join policy should match");
        assertEquals("team_only", sharing.getSharedLinkCreatePolicy(), "Shared link create policy should match");
    }
    
    @Test
    void testTeamInfoSerialization() throws Exception {
        TeamInfo teamInfo = new TeamInfo();
        teamInfo.setName("Serialization Test Team");
        teamInfo.setTeamId("dbtid:serialize123");
        teamInfo.setNumLicensedUsers(30);
        teamInfo.setNumProvisionedUsers(28);
        
        TeamPolicies policies = new TeamPolicies();
        policies.setEmmState("enabled");
        policies.setOfficeAddin("disabled");
        
        SharingPolicies sharing = new SharingPolicies();
        sharing.setSharedFolderMemberPolicy("admin_only");
        sharing.setSharedFolderJoinPolicy("from_team_only");
        sharing.setSharedLinkCreatePolicy("admin_only");
        
        policies.setSharing(sharing);
        teamInfo.setPolicies(policies);
        
        String json = objectMapper.writeValueAsString(teamInfo);
        
        assertNotNull(json, "JSON should not be null");
        assertTrue(json.contains("Serialization Test Team"), "Should contain team name");
        assertTrue(json.contains("dbtid:serialize123"), "Should contain team ID");
        assertTrue(json.contains("\"num_licensed_users\":30"), "Should contain licensed users");
        assertTrue(json.contains("\"num_provisioned_users\":28"), "Should contain provisioned users");
        assertTrue(json.contains("admin_only"), "Should contain sharing policies");
    }
    
    @Test
    void testMembersListRequestSerialization() throws Exception {
        MembersListRequest request = new MembersListRequest();
        request.setLimit(50);
        request.setIncludeRemoved(true);
        
        String json = objectMapper.writeValueAsString(request);
        
        assertNotNull(json, "JSON should not be null");
        assertTrue(json.contains("\"limit\":50"), "Should contain limit");
        assertTrue(json.contains("\"include_removed\":true"), "Should contain include_removed");
    }
    
    @Test
    void testMembersListResponseDeserialization() throws Exception {
        String json = """
            {
                "members": [
                    {
                        "profile": {
                            "team_member_id": "dbmid:member1",
                            "external_id": "ext123",
                            "account_id": "dbid:account1",
                            "email": "test@example.com",
                            "email_verified": true,
                            "name": {
                                "given_name": "John",
                                "surname": "Doe",
                                "familiar_name": "John",
                                "display_name": "John Doe",
                                "abbreviated_name": "JD"
                            },
                            "membership_type": "full",
                            "status": "active",
                            "joined_on": "2024-01-01T00:00:00Z"
                        }
                    }
                ],
                "cursor": "test_cursor_123",
                "has_more": true
            }
            """;
        
        MembersListResponse response = objectMapper.readValue(json, MembersListResponse.class);
        
        assertNotNull(response, "Response should not be null");
        assertNotNull(response.getMembers(), "Members list should not be null");
        assertEquals(1, response.getMembers().size(), "Should have one member");
        assertEquals("test_cursor_123", response.getCursor(), "Cursor should match");
        assertTrue(response.isHasMore(), "Should have more results");
        
        TeamMember member = response.getMembers().get(0);
        assertNotNull(member, "Member should not be null");
        
        TeamMemberProfile profile = member.getProfile();
        assertNotNull(profile, "Profile should not be null");
        assertEquals("dbmid:member1", profile.getTeamMemberId(), "Team member ID should match");
        assertEquals("ext123", profile.getExternalId(), "External ID should match");
        assertEquals("dbid:account1", profile.getAccountId(), "Account ID should match");
        assertEquals("test@example.com", profile.getEmail(), "Email should match");
        assertTrue(profile.isEmailVerified(), "Email should be verified");
        assertEquals("full", profile.getMembershipType(), "Membership type should match");
        assertEquals("active", profile.getStatus(), "Status should match");
        assertEquals("2024-01-01T00:00:00Z", profile.getJoinedOn(), "Join date should match");
        
        NameInfo name = profile.getName();
        assertNotNull(name, "Name should not be null");
        assertEquals("John", name.getGivenName(), "Given name should match");
        assertEquals("Doe", name.getSurname(), "Surname should match");
        assertEquals("John", name.getFamiliarName(), "Familiar name should match");
        assertEquals("John Doe", name.getDisplayName(), "Display name should match");
        assertEquals("JD", name.getAbbreviatedName(), "Abbreviated name should match");
    }
    
    @Test
    void testTimeRangeCreation() throws Exception {
        TimeRange timeRange = new TimeRange("2024-01-01T00:00:00Z", "2024-01-02T00:00:00Z");
        
        assertEquals("2024-01-01T00:00:00Z", timeRange.getStartTime(), "Start time should match");
        assertEquals("2024-01-02T00:00:00Z", timeRange.getEndTime(), "End time should match");
        
        String json = objectMapper.writeValueAsString(timeRange);
        assertTrue(json.contains("2024-01-01T00:00:00Z"), "Should contain start time");
        assertTrue(json.contains("2024-01-02T00:00:00Z"), "Should contain end time");
    }
    
    @Test
    void testTeamEventsRequestSerialization() throws Exception {
        TimeRange timeRange = new TimeRange("2024-01-01T00:00:00Z", "2024-01-02T00:00:00Z");
        
        TeamEventsRequest request = new TeamEventsRequest();
        request.setLimit(100);
        request.setCategory("logins");
        request.setTime(timeRange);
        
        String json = objectMapper.writeValueAsString(request);
        
        assertNotNull(json, "JSON should not be null");
        assertTrue(json.contains("\"limit\":100"), "Should contain limit");
        assertTrue(json.contains("\"category\":\"logins\""), "Should contain category");
        assertTrue(json.contains("start_time"), "Should contain time range");
    }
    
    @Test
    void testTeamEventsResponseDeserialization() throws Exception {
        String json = """
            {
                "events": [
                    {
                        "timestamp": "2024-01-01T12:00:00Z",
                        "event_category": "logins",
                        "event_type": "sign_in_as_admin",
                        "details": {
                            "session_id": "session123"
                        },
                        "actor": {
                            "user": {
                                "team_member_id": "dbmid:user1",
                                "display_name": "Admin User",
                                "email": "admin@example.com"
                            }
                        },
                        "origin": {
                            "geo_location": {
                                "city": "San Francisco",
                                "region": "California", 
                                "country": "US"
                            },
                            "host": {
                                "host": "192.168.1.100"
                            }
                        }
                    }
                ],
                "cursor": "events_cursor_456",
                "has_more": false
            }
            """;
        
        TeamEventsResponse response = objectMapper.readValue(json, TeamEventsResponse.class);
        
        assertNotNull(response, "Response should not be null");
        assertNotNull(response.getEvents(), "Events list should not be null");
        assertEquals(1, response.getEvents().size(), "Should have one event");
        assertEquals("events_cursor_456", response.getCursor(), "Cursor should match");
        assertFalse(response.isHasMore(), "Should not have more results");
        
        TeamEvent event = response.getEvents().get(0);
        assertNotNull(event, "Event should not be null");
        assertEquals("2024-01-01T12:00:00Z", event.getTimestamp(), "Timestamp should match");
        assertEquals("logins", event.getEventCategory(), "Event category should match");
        assertEquals("sign_in_as_admin", event.getEventType(), "Event type should match");
        assertNotNull(event.getDetails(), "Details should not be null");
        
        Actor actor = event.getActor();
        assertNotNull(actor, "Actor should not be null");
        
        UserInfo user = actor.getUser();
        assertNotNull(user, "User should not be null");
        assertEquals("dbmid:user1", user.getTeamMemberId(), "Team member ID should match");
        assertEquals("Admin User", user.getDisplayName(), "Display name should match");
        assertEquals("admin@example.com", user.getEmail(), "Email should match");
        
        Origin origin = event.getOrigin();
        assertNotNull(origin, "Origin should not be null");
        
        GeoLocation geoLocation = origin.getGeoLocation();
        assertNotNull(geoLocation, "Geo location should not be null");
        assertEquals("San Francisco", geoLocation.getCity(), "City should match");
        assertEquals("California", geoLocation.getRegion(), "Region should match");
        assertEquals("US", geoLocation.getCountry(), "Country should match");
        
        HostInfo host = origin.getHost();
        assertNotNull(host, "Host should not be null");
        assertEquals("192.168.1.100", host.getHost(), "Host IP should match");
    }
    
    @Test
    void testTeamInfoWithNullValues() throws Exception {
        String json = """
            {
                "name": "Test Team",
                "team_id": null,
                "num_licensed_users": null,
                "num_provisioned_users": null,
                "policies": null
            }
            """;
        
        TeamInfo teamInfo = objectMapper.readValue(json, TeamInfo.class);
        
        assertNotNull(teamInfo, "TeamInfo should not be null");
        assertEquals("Test Team", teamInfo.getName(), "Team name should match");
        assertNull(teamInfo.getTeamId(), "Team ID should be null");
        assertNull(teamInfo.getNumLicensedUsers(), "Licensed users should be null");
        assertNull(teamInfo.getNumProvisionedUsers(), "Provisioned users should be null");
        assertNull(teamInfo.getPolicies(), "Policies should be null");
    }
    
    @Test
    void testTeamMemberProfileToString() {
        TeamMemberProfile profile = new TeamMemberProfile();
        profile.setTeamMemberId("dbmid:test123");
        profile.setEmail("test@example.com");
        profile.setStatus("active");
        
        NameInfo nameInfo = new NameInfo();
        nameInfo.setDisplayName("Test User");
        profile.setName(nameInfo);
        
        String result = profile.toString();
        
        assertNotNull(result, "toString should not return null");
        assertTrue(result.contains("dbmid:test123"), "Should contain team member ID");
        assertTrue(result.contains("test@example.com"), "Should contain email");
        assertTrue(result.contains("Test User"), "Should contain display name");
        assertTrue(result.contains("active"), "Should contain status");
    }
    
    @Test
    void testTeamMemberProfileToStringWithNullName() {
        TeamMemberProfile profile = new TeamMemberProfile();
        profile.setTeamMemberId("dbmid:test123");
        profile.setEmail("test@example.com");
        profile.setStatus("active");
        profile.setName(null);
        
        String result = profile.toString();
        
        assertNotNull(result, "toString should not return null");
        assertTrue(result.contains("N/A"), "Should contain N/A for null name");
    }
    
    @Test
    void testTeamEventToString() {
        TeamEvent event = new TeamEvent();
        event.setTimestamp("2024-01-01T12:00:00Z");
        event.setEventCategory("logins");
        event.setEventType("sign_in");
        
        String result = event.toString();
        
        assertNotNull(result, "toString should not return null");
        assertTrue(result.contains("2024-01-01T12:00:00Z"), "Should contain timestamp");
        assertTrue(result.contains("logins"), "Should contain event category");
        assertTrue(result.contains("sign_in"), "Should contain event type");
    }
    
    @Test
    void testIgnoreUnknownProperties() throws Exception {
        String jsonWithExtraFields = """
            {
                "name": "Test Team",
                "team_id": "dbtid:123",
                "num_licensed_users": 25,
                "num_provisioned_users": 20,
                "unknown_field": "should_be_ignored",
                "another_unknown": {
                    "nested": "value"
                }
            }
            """;
        
        // Should not throw an exception despite unknown fields
        TeamInfo teamInfo = objectMapper.readValue(jsonWithExtraFields, TeamInfo.class);
        
        assertNotNull(teamInfo, "TeamInfo should not be null");
        assertEquals("Test Team", teamInfo.getName(), "Known fields should still be parsed");
        assertEquals("dbtid:123", teamInfo.getTeamId(), "Team ID should be parsed correctly");
    }
    
    @Test
    void testEmptyJsonObjects() throws Exception {
        String emptyJson = "{}";
        
        // Should handle empty JSON objects gracefully
        TeamInfo teamInfo = objectMapper.readValue(emptyJson, TeamInfo.class);
        assertNotNull(teamInfo, "TeamInfo should not be null for empty JSON");
        
        MembersListRequest request = objectMapper.readValue(emptyJson, MembersListRequest.class);
        assertNotNull(request, "MembersListRequest should not be null for empty JSON");
        // Should use default values
        assertEquals(100, request.getLimit(), "Should use default limit");
        assertFalse(request.isIncludeRemoved(), "Should use default include_removed");
        
        TeamEventsRequest eventsRequest = objectMapper.readValue(emptyJson, TeamEventsRequest.class);
        assertNotNull(eventsRequest, "TeamEventsRequest should not be null for empty JSON");
        assertEquals(50, eventsRequest.getLimit(), "Should use default limit");
    }
}