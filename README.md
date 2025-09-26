# Dropbox Business API Integration

## Project Overview

This project demonstrates the integration with Dropbox Business APIs as part of the CloudEagle SaaS platform assessment. The implementation includes OAuth2 authentication and various team management API endpoints to retrieve organizational data, user information, and activity logs.

## Features

- **OAuth2 Authentication**: Complete implementation of Dropbox OAuth2 flow
- **Team Information**: Retrieve organization/team details and license information
- **User Management**: Fetch list of all team members and their details
- **Activity Monitoring**: Access sign-in events and team activity logs
- **Java SDK Integration**: Native Java implementation with proper error handling
- **Postman Testing**: Complete test suite with documented API calls

## 🔧 Prerequisites

Before running this project, ensure you have:

- Java 11 or higher
- Maven 3.6+
- Dropbox Business account (trial/paid)
- Postman (for API testing)
- Git

## 🛠 Installation & Setup

### 1. Clone the Repository

```bash
git clone https://github.com/JineshDJoshi/DropBoxAPI-Integration.git
cd DropBoxAPI-Integration
```

### 2. Configure Dropbox App

1. Go to [Dropbox App Console](https://www.dropbox.com/developers/apps)
2. Create a new app with the following settings:
   - **API**: Dropbox Business API
   - **Type of access**: Team member file access
   - **Name**: Your app name (e.g., "CloudEagle-Integration")

3. Configure OAuth settings:
   - **Redirect URIs**: `http://localhost:8080/auth/callback`
   - **Permissions**: Enable required scopes (see API Documentation section)

### 3. Environment Configuration

Create a `config.properties` file in the `src/main/resources` directory:

```properties
dropbox.client.id=YOUR_CLIENT_ID
dropbox.client.secret=YOUR_CLIENT_SECRET
dropbox.redirect.uri=http://localhost:8080/auth/callback
```

### 4. Install Dependencies

```bash
mvn clean install
```

## 📚 API Documentation

### Authentication Configuration

| Parameter | Value |
|-----------|--------|
| **Authentication Type** | OAuth2 (Authorization Code Flow) |
| **Auth URL** | `https://www.dropbox.com/oauth2/authorize` |
| **Access Token URL** | `https://api.dropboxapi.com/oauth2/token` |
| **Refresh Token URL** | `https://api.dropboxapi.com/oauth2/token` |
| **Scopes** | `team_data.member`, `team_data.team`, `team_data.governance.read`, `events.read` |

### API Endpoints

#### 1. Get Team Information

**Purpose**: Retrieve organization/team name and details

```
GET https://api.dropboxapi.com/2/team/get_info
```

**Required Scopes**: `team_data.team`

**Response Fields**:
- `name`: Team name
- `team_id`: Unique team identifier
- `num_licensed_users`: Number of licensed users
- `num_provisioned_users`: Number of provisioned users

#### 2. Get Plan/License Information

**Purpose**: Fetch plan type and license details

```
GET https://api.dropboxapi.com/2/team/get_info
```

**Required Scopes**: `team_data.team`

**Response Fields**:
- `plan.name`: Plan name (e.g., "Business", "Advanced")
- `plan.space_quota`: Storage quota
- `plan.features`: Available features

#### 3. List Team Members

**Purpose**: Get all users in the organization

```
POST https://api.dropboxapi.com/2/team/members/list_v2
```

**Required Scopes**: `team_data.member`

**Request Body**:
```json
{
    "limit": 1000,
    "include_removed": false
}
```

**Response Fields**:
- `members`: Array of team members
- `email`: Member email address
- `status`: Member status (active/invited/suspended)
- `role`: Member role (admin/member)

#### 4. Get Sign-in Events

**Purpose**: Retrieve user sign-in activity logs

```
POST https://api.dropboxapi.com/2/team_log/get_events
```

**Required Scopes**: `events.read`

**Request Body**:
```json
{
    "limit": 1000,
    "account_id": "optional_account_id",
    "time": {
        "start_time": "2024-01-01T00:00:00Z",
        "end_time": "2024-12-31T23:59:59Z"
    }
}
```

## 🧪 Testing with Postman

### Import Collection

1. Import the Postman collection from `postman/Dropbox_API_Collection.json`
2. Import the environment from `postman/Dropbox_Environment.json`

### Configure Authentication

1. Go to Authorization tab in Postman
2. Select OAuth 2.0
3. Configure with the following:
   - **Grant Type**: Authorization Code
   - **Auth URL**: `https://www.dropbox.com/oauth2/authorize`
   - **Access Token URL**: `https://api.dropboxapi.com/oauth2/token`
   - **Client ID**: Your app's client ID
   - **Client Secret**: Your app's client secret
   - **Scope**: `team_data.member team_data.team events.read`

### Test Sequence

1. **Authenticate**: Get access token using OAuth2 flow
2. **Test Team Info**: Verify team information retrieval
3. **Test Members List**: Fetch all team members
4. **Test Activity Logs**: Retrieve sign-in events

## Java Implementation

### Key Components

#### 1. DropboxService Class

```java
public class DropboxService {
    private final DbxTeamClientV2 teamClient;
    
    public TeamGetInfoResult getTeamInfo() throws DbxException {
        return teamClient.team().getInfo();
    }
    
    public TeamMembersListResult getTeamMembers() throws DbxException {
        return teamClient.team().membersListV2(
            TeamMembersListArg.newBuilder().build()
        );
    }
}
```

#### 2. OAuth Handler

```java
public class OAuthHandler {
    public String getAuthorizationUrl() {
        return DbxWebAuth.newRequestBuilder(requestConfig, appInfo)
                .withRedirectUri(redirectUri, sessionStore)
                .start();
    }
    
    public DbxAuthFinish finishAuthentication(String code) {
        return webAuth.finishFromCode(code);
    }
}
```

### Running the Application

```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080` and provide endpoints for:
- `/auth/login` - Initiate OAuth flow
- `/auth/callback` - Handle OAuth callback
- `/api/team-info` - Get team information
- `/api/members` - List team members
- `/api/events` - Get activity events

## Project Structure

```
DropBoxAPI-Integration/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/cloudeagle/dropbox/
│   │   │       ├── DropboxApiApplication.java
│   │   │       ├── config/
│   │   │       │   └── DropboxConfig.java
│   │   │       ├── controller/
│   │   │       │   ├── AuthController.java
│   │   │       │   └── ApiController.java
│   │   │       ├── service/
│   │   │       │   └── DropboxService.java
│   │   │       └── model/
│   │   │           └── ApiResponse.java
│   │   └── resources/
│   │       ├── application.properties
│   │       └── config.properties
├── postman/
│   ├── Dropbox_API_Collection.json
│   └── Dropbox_Environment.json
├── docs/
│   ├── API_Documentation.md
│   └── screenshots/
├── pom.xml
└── README.md
```

##  Configuration

### Required Environment Variables

```bash
DROPBOX_CLIENT_ID=your_client_id
DROPBOX_CLIENT_SECRET=your_client_secret
DROPBOX_REDIRECT_URI=http://localhost:8080/auth/callback
```

### Application Properties

```properties
# Server Configuration
server.port=8080

# Dropbox Configuration
dropbox.api.timeout=30000
dropbox.api.retries=3

# Logging
logging.level.com.cloudeagle.dropbox=DEBUG
```

## 🖥️ Usage

### Authentication Flow

1. Navigate to `http://localhost:8080/auth/login`
2. You'll be redirected to Dropbox for authorization
3. Grant permissions to your application
4. You'll be redirected back with an access token

### API Calls

Once authenticated, you can make API calls:

```bash
# Get team information
curl -H "Authorization: Bearer YOUR_ACCESS_TOKEN" \
     http://localhost:8080/api/team-info

# List team members
curl -H "Authorization: Bearer YOUR_ACCESS_TOKEN" \
     http://localhost:8080/api/members

# Get activity events
curl -H "Authorization: Bearer YOUR_ACCESS_TOKEN" \
     http://localhost:8080/api/events
```

## 📸 Screenshots

Screenshots of Postman tests and API responses are available in the `docs/screenshots/` directory:

- `authentication_flow.png` - OAuth2 authentication process
- `team_info_response.png` - Team information API response
- `members_list_response.png` - Team members list response
- `activity_logs_response.png` - Sign-in events response

## 🔧 Troubleshooting

### Common Issues

#### Authentication Errors
- **Issue**: "Invalid client credentials"
- **Solution**: Verify your client ID and secret in the configuration

#### Permission Errors
- **Issue**: "Insufficient scope"
- **Solution**: Ensure all required scopes are configured in your Dropbox app

#### Rate Limiting
- **Issue**: "Too many requests"
- **Solution**: Implement proper retry logic with exponential backoff

### API Limitations

- **Team Events API**: Available only for Dropbox Business accounts
- **Rate Limits**: 120 requests per minute per app
- **Data Retention**: Activity logs available for 180 days


Email: jinesh3715@gmail.com
Documentation**: https://drive.google.com/drive/folders/16Rj-X0GdExuZJ3o2j-ngfeGXNfkImnCO?usp=sharing

