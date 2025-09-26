// DropboxAuthenticator.java
package com.cloudeagle.dropbox.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.concurrent.TimeUnit;

/**
 * Handles OAuth2 authentication for Dropbox Business API
 */
public class DropboxAuthenticator {
    private static final Logger logger = LoggerFactory.getLogger(DropboxAuthenticator.class);
    
    private final String clientId;
    private final String clientSecret;
    private final String redirectUri;
    private final OkHttpClient httpClient;
    private final ObjectMapper objectMapper;
    
    private String accessToken;
    private String refreshToken;
    private LocalDateTime tokenExpiry;
    
    public DropboxAuthenticator(String clientId, String clientSecret, String redirectUri) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.redirectUri = redirectUri;
        this.httpClient = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
        this.objectMapper = new ObjectMapper();
    }
    
    /**
     * Generates the authorization URL for OAuth2 flow
     */
    public String getAuthorizationUrl() {
        String scopes = "team_info.read members.read events.read";
        return String.format(
            "https://www.dropbox.com/oauth2/authorize?client_id=%s&redirect_uri=%s&response_type=code&scope=%s&token_access_type=offline",
            clientId, redirectUri, scopes.replace(" ", "%20")
        );
    }
    
    /**
     * Exchanges authorization code for access token
     */
    public void exchangeCodeForToken(String authorizationCode) throws IOException {
        RequestBody requestBody = new FormBody.Builder()
                .add("client_id", clientId)
                .add("client_secret", clientSecret)
                .add("redirect_uri", redirectUri)
                .add("code", authorizationCode)
                .add("grant_type", "authorization_code")
                .build();
        
        Request request = new Request.Builder()
                .url("https://api.dropboxapi.com/oauth2/token")
                .post(requestBody)
                .build();
        
        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Failed to exchange code for token: " + response.body().string());
            }
            
            String responseBody = response.body().string();
            TokenResponse tokenResponse = objectMapper.readValue(responseBody, TokenResponse.class);
            
            this.accessToken = tokenResponse.accessToken;
            this.refreshToken = tokenResponse.refreshToken;
            
            // Calculate expiry time
            if (tokenResponse.expiresIn != null) {
                this.tokenExpiry = LocalDateTime.now().plusSeconds(tokenResponse.expiresIn - 300); // 5 min buffer
            }
            
            logger.info("Successfully obtained access token");
        }
    }
    
    /**
     * Refreshes the access token using refresh token
     */
    public void refreshAccessToken() throws IOException {
        if (refreshToken == null) {
            throw new IllegalStateException("No refresh token available");
        }
        
        RequestBody requestBody = new FormBody.Builder()
                .add("client_id", clientId)
                .add("client_secret", clientSecret)
                .add("refresh_token", refreshToken)
                .add("grant_type", "refresh_token")
                .build();
        
        Request request = new Request.Builder()
                .url("https://api.dropboxapi.com/oauth2/token")
                .post(requestBody)
                .build();
        
        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Failed to refresh token: " + response.body().string());
            }
            
            String responseBody = response.body().string();
            TokenResponse tokenResponse = objectMapper.readValue(responseBody, TokenResponse.class);
            
            this.accessToken = tokenResponse.accessToken;
            
            // Update expiry time
            if (tokenResponse.expiresIn != null) {
                this.tokenExpiry = LocalDateTime.now().plusSeconds(tokenResponse.expiresIn - 300);
            }
            
            logger.info("Successfully refreshed access token");
        }
    }
    
    /**
     * Gets a valid access token, refreshing if necessary
     */
    public String getValidAccessToken() throws IOException {
        if (accessToken == null) {
            throw new IllegalStateException("No access token available. Please authenticate first.");
        }
        
        // Check if token is expired or will expire soon
        if (tokenExpiry != null && LocalDateTime.now().isAfter(tokenExpiry)) {
            logger.info("Access token expired, refreshing...");
            refreshAccessToken();
        }
        
        return accessToken;
    }
    
    // Token response model
    private static class TokenResponse {
        @JsonProperty("access_token")
        public String accessToken;
        
        @JsonProperty("refresh_token")
        public String refreshToken;
        
        @JsonProperty("expires_in")
        public Long expiresIn;
        
        @JsonProperty("token_type")
        public String tokenType;
        
        @JsonProperty("scope")
        public String scope;
    }
}