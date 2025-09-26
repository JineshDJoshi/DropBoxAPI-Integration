package com.cloudeagle.dropbox.example;

import com.cloudeagle.dropbox.auth.DropboxAuthenticator;
import com.cloudeagle.dropbox.client.DropboxBusinessApiClient;
import com.cloudeagle.dropbox.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

/**
 * Example application demonstrating Dropbox Business API usage
 */
public class DropboxApiExample {
    private static final Logger logger = LoggerFactory.getLogger(DropboxApiExample.class);
    
    // Replace with your actual app credentials
    private static final String CLIENT_ID = "uk9nfvrmrflkr1j";
    private static final String CLIENT_SECRET = "qnk580zrxivqwqe";
    private static final String REDIRECT_URI = "http://localhost:8080/callback";
    
    public static void main(String[] args) {
        DropboxBusinessApiClient apiClient = null;
        Scanner scanner = null;
        
        try {
            // Initialize authenticator
            DropboxAuthenticator authenticator = new DropboxAuthenticator(CLIENT_ID, CLIENT_SECRET, REDIRECT_URI);
            
            // Step 1: Get authorization URL
            String authUrl = authenticator.getAuthorizationUrl();
            System.out.println("Please visit this URL to authorize the application:");
            System.out.println(authUrl);
            System.out.println();
            
            // Step 2: Get authorization code from user
            scanner = new Scanner(System.in);
            System.out.print("Enter the authorization code from the callback URL: ");
            String authCode = scanner.nextLine().trim();
            
            if (authCode.isEmpty()) {
                System.err.println("Authorization code cannot be empty");
                return;
            }
            
            // Step 3: Exchange code for token
            authenticator.exchangeCodeForToken(authCode);
            System.out.println("Authentication successful!");
            System.out.println();
            
            // Initialize API client
            apiClient = new DropboxBusinessApiClient(authenticator);
            
            // Demonstrate all required APIs
            demonstrateTeamInfo(apiClient);
            demonstrateUsersList(apiClient);
            demonstrateSignInEvents(apiClient);
            
        } catch (Exception e) {
            logger.error("Application error", e);
            System.err.println("Error: " + e.getMessage());
        } finally {
            // Clean up resources
            if (scanner != null) {
                scanner.close();
            }
            if (apiClient != null) {
                apiClient.close();
            }
        }
    }
    
    private static void demonstrateTeamInfo(DropboxBusinessApiClient apiClient) {
        try {
            System.out.println("=== TEAM INFORMATION ===");
            TeamInfo teamInfo = apiClient.getTeamInfo();
            
            System.out.println("Team Name: " + (teamInfo.getName() != null ? teamInfo.getName() : "N/A"));
            System.out.println("Team ID: " + (teamInfo.getTeamId() != null ? teamInfo.getTeamId() : "N/A"));
            System.out.println("Licensed Users: " + (teamInfo.getNumLicensedUsers() != null ? teamInfo.getNumLicensedUsers() : "N/A"));
            System.out.println("Provisioned Users: " + (teamInfo.getNumProvisionedUsers() != null ? teamInfo.getNumProvisionedUsers() : "N/A"));
            
            TeamPolicies policies = teamInfo.getPolicies();
            if (policies != null) {
                SharingPolicies sharing = policies.getSharing();
                if (sharing != null) {
                    System.out.println("Shared Folder Member Policy: " + (sharing.getSharedFolderMemberPolicy() != null ? sharing.getSharedFolderMemberPolicy() : "N/A"));
                    System.out.println("Shared Folder Join Policy: " + (sharing.getSharedFolderJoinPolicy() != null ? sharing.getSharedFolderJoinPolicy() : "N/A"));
                    System.out.println("Shared Link Create Policy: " + (sharing.getSharedLinkCreatePolicy() != null ? sharing.getSharedLinkCreatePolicy() : "N/A"));
                }
                System.out.println("EMM State: " + (policies.getEmmState() != null ? policies.getEmmState() : "N/A"));
                System.out.println("Office Add-in: " + (policies.getOfficeAddin() != null ? policies.getOfficeAddin() : "N/A"));
            }
            
            // Determine plan type based on licensed users
            String planType = determinePlanType(teamInfo.getNumLicensedUsers());
            System.out.println("Estimated Plan Type: " + planType);
            
            System.out.println();
            
        } catch (IOException e) {
            logger.error("Failed to get team info", e);
            System.err.println("Failed to get team info: " + e.getMessage());
        }
    }
    
    private static void demonstrateUsersList(DropboxBusinessApiClient apiClient) {
        try {
            System.out.println("=== TEAM MEMBERS LIST ===");
            MembersListResponse membersResponse = apiClient.getTeamMembers(100, false);
            
            if (membersResponse.getMembers() == null) {
                System.out.println("No members data received");
                return;
            }
            
            System.out.println("Total members found: " + membersResponse.getMembers().size());
            System.out.println("Has more pages: " + membersResponse.isHasMore());
            System.out.println("Cursor: " + (membersResponse.getCursor() != null ? membersResponse.getCursor() : "N/A"));
            System.out.println();
            
            System.out.println("Members:");
            int displayCount = Math.min(membersResponse.getMembers().size(), 10);
            for (int i = 0; i < displayCount; i++) {
                TeamMember member = membersResponse.getMembers().get(i);
                if (member == null) continue;
                
                TeamMemberProfile profile = member.getProfile();
                if (profile == null) continue;
                
                String displayName = "N/A";
                NameInfo nameInfo = profile.getName();
                if (nameInfo != null && nameInfo.getDisplayName() != null) {
                    displayName = nameInfo.getDisplayName();
                }
                
                System.out.printf("%d. %s (%s) - Status: %s - Member Type: %s%n",
                    i + 1,
                    displayName,
                    profile.getEmail() != null ? profile.getEmail() : "N/A",
                    profile.getStatus() != null ? profile.getStatus() : "N/A",
                    profile.getMembershipType() != null ? profile.getMembershipType() : "N/A"
                );
                
                if (profile.getJoinedOn() != null) {
                    System.out.printf("   Joined: %s%n", profile.getJoinedOn());
                }
                if (profile.getTeamMemberId() != null) {
                    System.out.printf("   Team Member ID: %s%n", profile.getTeamMemberId());
                }
            }
            
            if (membersResponse.getMembers().size() > 10) {
                System.out.println("... and " + (membersResponse.getMembers().size() - 10) + " more members");
            }
            
            System.out.println();
            
        } catch (IOException e) {
            logger.error("Failed to get team members", e);
            System.err.println("Failed to get team members: " + e.getMessage());
        }
    }
    
    private static void demonstrateSignInEvents(DropboxBusinessApiClient apiClient) {
        try {
            System.out.println("=== SIGN-IN EVENTS ===");
            
            // Get events from last 30 days
            String endTime = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) + "Z";
            String startTime = LocalDateTime.now().minusDays(30).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) + "Z";
            
            TimeRange timeRange = new TimeRange(startTime, endTime);
            TeamEventsResponse eventsResponse = apiClient.getSignInEvents(50, timeRange);
            
            if (eventsResponse.getEvents() == null) {
                System.out.println("No events data received");
                return;
            }
            
            System.out.println("Sign-in events found: " + eventsResponse.getEvents().size());
            System.out.println("Has more pages: " + eventsResponse.isHasMore());
            System.out.println("Cursor: " + (eventsResponse.getCursor() != null ? eventsResponse.getCursor() : "N/A"));
            System.out.println();
            
            if (!eventsResponse.getEvents().isEmpty()) {
                System.out.println("Recent sign-in events:");
                int displayCount = Math.min(eventsResponse.getEvents().size(), 5);
                for (int i = 0; i < displayCount; i++) {
                    TeamEvent event = eventsResponse.getEvents().get(i);
                    if (event == null) continue;
                    
                    String userName = "Unknown User";
                    String userEmail = "N/A";
                    String location = "Unknown Location";
                    
                    Actor actor = event.getActor();
                    if (actor != null) {
                        UserInfo user = actor.getUser();
                        if (user != null) {
                            userName = user.getDisplayName() != null ? user.getDisplayName() : "N/A";
                            userEmail = user.getEmail() != null ? user.getEmail() : "N/A";
                        }
                    }
                    
                    Origin origin = event.getOrigin();
                    if (origin != null) {
                        GeoLocation geo = origin.getGeoLocation();
                        if (geo != null) {
                            location = String.format("%s, %s, %s", 
                                geo.getCity() != null ? geo.getCity() : "Unknown",
                                geo.getRegion() != null ? geo.getRegion() : "Unknown", 
                                geo.getCountry() != null ? geo.getCountry() : "Unknown"
                            );
                        }
                        
                        HostInfo host = origin.getHost();
                        if (host != null && host.getHost() != null) {
                            location += " (Host: " + host.getHost() + ")";
                        }
                    }
                    
                    System.out.printf("%d. [%s] %s (%s)%n", 
                        i + 1,
                        event.getTimestamp() != null ? event.getTimestamp() : "N/A",
                        userName,
                        userEmail
                    );
                    System.out.printf("   Event: %s - %s%n",
                        event.getEventCategory() != null ? event.getEventCategory() : "N/A",
                        event.getEventType() != null ? event.getEventType() : "N/A"
                    );
                    System.out.printf("   Location: %s%n", location);
                    System.out.println();
                }
                
                if (eventsResponse.getEvents().size() > 5) {
                    System.out.println("... and " + (eventsResponse.getEvents().size() - 5) + " more events");
                }
            } else {
                System.out.println("No sign-in events found in the last 30 days.");
            }
            
            System.out.println();
            
        } catch (IOException e) {
            logger.error("Failed to get sign-in events", e);
            System.err.println("Failed to get sign-in events: " + e.getMessage());
        }
    }
    
    private static String determinePlanType(Integer licensedUsers) {
        if (licensedUsers == null) {
            return "Unknown";
        }
        
        if (licensedUsers <= 3) {
            return "Dropbox Business Basic (1-3 users)";
        } else if (licensedUsers <= 100) {
            return "Dropbox Business Standard (3+ users)";
        } else if (licensedUsers <= 300) {
            return "Dropbox Business Advanced (3+ users)";
        } else {
            return "Dropbox Business Enterprise (300+ users)";
        }
    }
}