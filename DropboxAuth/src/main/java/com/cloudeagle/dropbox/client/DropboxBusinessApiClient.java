package com.cloudeagle.dropbox.client;

import com.cloudeagle.dropbox.auth.DropboxAuthenticator;
import com.cloudeagle.dropbox.model.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Main client for Dropbox Business API operations
 */
public class DropboxBusinessApiClient {
    private static final Logger logger = LoggerFactory.getLogger(DropboxBusinessApiClient.class);
    private static final String BASE_URL = "https://api.dropboxapi.com/2";
    
    private final DropboxAuthenticator authenticator;
    private final OkHttpClient httpClient;
    private final ObjectMapper objectMapper;
    
    public DropboxBusinessApiClient(DropboxAuthenticator authenticator) {
        if (authenticator == null) {
            throw new IllegalArgumentException("Authenticator cannot be null");
        }
        this.authenticator = authenticator;
        this.httpClient = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build();
        this.objectMapper = new ObjectMapper();
    }
    
    /**
     * Gets team information including name and license details
     * @return TeamInfo containing team details
     * @throws IOException if the API request fails
     */
    public TeamInfo getTeamInfo() throws IOException {
        String url = BASE_URL + "/team/get_info";
        
        RequestBody requestBody = RequestBody.create(
            MediaType.parse("application/json"), "{}"
        );
        
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .addHeader("Authorization", "Bearer " + authenticator.getValidAccessToken())
                .addHeader("Content-Type", "application/json")
                .build();
        
        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                String errorBody = response.body() != null ? response.body().string() : "No error body";
                throw new IOException("Failed to get team info: " + response.code() + " " + errorBody);
            }
            
            ResponseBody responseBody = response.body();
            if (responseBody == null) {
                throw new IOException("Empty response body");
            }
            
            String responseBodyString = responseBody.string();
            logger.debug("Team info response: {}", responseBodyString);
            
            return objectMapper.readValue(responseBodyString, TeamInfo.class);
        }
    }
    
    /**
     * Gets list of all team members
     * @param limit Maximum number of members to retrieve (max 1000)
     * @param includeRemoved Whether to include removed members
     * @return MembersListResponse containing team members
     * @throws IOException if the API request fails
     */
    public MembersListResponse getTeamMembers(int limit, boolean includeRemoved) throws IOException {
        if (limit <= 0 || limit > 1000) {
            throw new IllegalArgumentException("Limit must be between 1 and 1000");
        }
        
        String url = BASE_URL + "/team/members/list_v2";
        
        MembersListRequest request = new MembersListRequest();
        request.setLimit(limit);
        request.setIncludeRemoved(includeRemoved);
        
        String requestJson = objectMapper.writeValueAsString(request);
        RequestBody requestBody = RequestBody.create(
            MediaType.parse("application/json"), requestJson
        );
        
        Request httpRequest = new Request.Builder()
                .url(url)
                .post(requestBody)
                .addHeader("Authorization", "Bearer " + authenticator.getValidAccessToken())
                .addHeader("Content-Type", "application/json")
                .build();
        
        try (Response response = httpClient.newCall(httpRequest).execute()) {
            if (!response.isSuccessful()) {
                String errorBody = response.body() != null ? response.body().string() : "No error body";
                throw new IOException("Failed to get team members: " + response.code() + " " + errorBody);
            }
            
            ResponseBody responseBody = response.body();
            if (responseBody == null) {
                throw new IOException("Empty response body");
            }
            
            String responseBodyString = responseBody.string();
            logger.debug("Team members response: {}", responseBodyString);
            
            return objectMapper.readValue(responseBodyString, MembersListResponse.class);
        }
    }
    
    /**
     * Gets team events (including sign-in events)
     * @param limit Maximum number of events to retrieve
     * @param category Event category to filter by (e.g., "logins")
     * @param timeRange Time range for the events
     * @return TeamEventsResponse containing team events
     * @throws IOException if the API request fails
     */
    public TeamEventsResponse getTeamEvents(int limit, String category, TimeRange timeRange) throws IOException {
        if (limit <= 0) {
            throw new IllegalArgumentException("Limit must be greater than 0");
        }
        
        String url = BASE_URL + "/team_log/get_events";
        
        TeamEventsRequest request = new TeamEventsRequest();
        request.setLimit(limit);
        request.setCategory(category);
        request.setTime(timeRange);
        
        String requestJson = objectMapper.writeValueAsString(request);
        RequestBody requestBody = RequestBody.create(
            MediaType.parse("application/json"), requestJson
        );
        
        Request httpRequest = new Request.Builder()
                .url(url)
                .post(requestBody)
                .addHeader("Authorization", "Bearer " + authenticator.getValidAccessToken())
                .addHeader("Content-Type", "application/json")
                .build();
        
        try (Response response = httpClient.newCall(httpRequest).execute()) {
            if (!response.isSuccessful()) {
                String errorBody = response.body() != null ? response.body().string() : "No error body";
                throw new IOException("Failed to get team events: " + response.code() + " " + errorBody);
            }
            
            ResponseBody responseBody = response.body();
            if (responseBody == null) {
                throw new IOException("Empty response body");
            }
            
            String responseBodyString = responseBody.string();
            logger.debug("Team events response: {}", responseBodyString);
            
            return objectMapper.readValue(responseBodyString, TeamEventsResponse.class);
        }
    }
    
    /**
     * Gets sign-in events specifically
     * @param limit Maximum number of events to retrieve
     * @param timeRange Time range for the events
     * @return TeamEventsResponse containing sign-in events
     * @throws IOException if the API request fails
     */
    public TeamEventsResponse getSignInEvents(int limit, TimeRange timeRange) throws IOException {
        return getTeamEvents(limit, "logins", timeRange);
    }
    
    /**
     * Closes the HTTP client and releases resources
     */
    public void close() {
        if (httpClient != null) {
            httpClient.dispatcher().executorService().shutdown();
            httpClient.connectionPool().evictAll();
        }
    }
}