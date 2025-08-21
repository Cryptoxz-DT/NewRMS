# JWT Authentication Migration Guide

## 🔐 **Overview**

This document provides a comprehensive guide for the migration from Basic Authentication to JSON Web Token (JWT) authentication in the NewRMS application. This migration enhances security by implementing a stateless, token-based authentication mechanism.

## 📋 **Table of Contents**

1. [Migration Overview](#migration-overview)
2. [JWT Implementation Details](#jwt-implementation-details)
3. [Security Enhancements](#security-enhancements)
4. [API Changes](#api-changes)
5. [Frontend Integration](#frontend-integration)
6. [Configuration](#configuration)
7. [Testing Guide](#testing-guide)
8. [Troubleshooting](#troubleshooting)
9. [Security Considerations](#security-considerations)
10. [Deployment Guide](#deployment-guide)

## 🚀 **Migration Overview**

### **What Changed**
- **Replaced**: HTTP Basic Authentication
- **Implemented**: JWT-based authentication with refresh tokens
- **Added**: Token rotation and automatic cleanup
- **Enhanced**: Security audit logging and monitoring

### **Key Benefits**
- ✅ **Stateless Authentication**: No server-side session storage required
- ✅ **Enhanced Security**: Tokens expire automatically and can be revoked
- ✅ **Better Performance**: Reduced database queries for authentication
- ✅ **Mobile-Friendly**: Perfect for mobile app integration
- ✅ **Scalability**: Supports horizontal scaling without session affinity

## 🔧 **JWT Implementation Details**

### **Core Components**

#### 1. **JwtService** (`src/main/java/.../Service/JwtService.java`)
- **Purpose**: JWT token generation, validation, and parsing
- **Key Features**:
  - Token generation with custom claims
  - Signature validation using HMAC-SHA256
  - Expiration checking
  - Authority extraction

```java
// Example: Generate JWT token
String token = jwtService.generateToken(userDetails);

// Example: Validate JWT token
boolean isValid = jwtService.isTokenValid(token, userDetails);
```

#### 2. **JwtAuthenticationFilter** (`src/main/java/.../Config/JwtAuthenticationFilter.java`)
- **Purpose**: Intercepts HTTP requests and validates JWT tokens
- **Key Features**:
  - Automatic token extraction from Authorization header
  - Security context population
  - Comprehensive error handling
  - Audit logging for security events

#### 3. **RefreshTokenService** (`src/main/java/.../Service/RefreshTokenService.java`)
- **Purpose**: Manages refresh token lifecycle
- **Key Features**:
  - Token rotation for enhanced security
  - Automatic cleanup of expired tokens
  - Rate limiting (max 5 active tokens per user)
  - Comprehensive audit logging

### **Token Structure**

#### **Access Token (JWT)**
```json
{
  "sub": "username",
  "authorities": ["ROLE_ADMIN", "ROLE_MANAGER"],
  "iat": 1640995200,
  "exp": 1641081600
}
```

#### **Refresh Token**
- **Format**: UUID-based random string
- **Storage**: Database with metadata (IP, User-Agent, expiration)
- **Lifetime**: 7 days (configurable)
- **Security**: Automatic rotation on use

## 🛡️ **Security Enhancements**

### **Token Security**
- **Algorithm**: HMAC-SHA256 with 256-bit secret key
- **Expiration**: Access tokens expire in 24 hours
- **Refresh**: Refresh tokens expire in 7 days
- **Rotation**: Refresh tokens are rotated on each use

### **Protection Mechanisms**
- **Rate Limiting**: Prevents brute force attacks
- **Account Lockout**: Automatic lockout after 5 failed attempts
- **Audit Logging**: Comprehensive security event logging
- **Token Revocation**: Immediate token invalidation on logout

### **Security Headers**
```http
Strict-Transport-Security: max-age=31536000; includeSubDomains
X-Content-Type-Options: nosniff
X-Frame-Options: DENY
Referrer-Policy: strict-origin-when-cross-origin
```

## 🔌 **API Changes**

### **Authentication Endpoints**

#### **1. Login** `POST /api/auth/login`

**Request:**
```json
{
  "usernameOrEmail": "john.doe@example.com",
  "password": "SecurePass123!"
}
```

**Response:**
```json
{
  "id": 1,
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com",
  "username": "johndoe",
  "roles": ["STAFF"],
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refreshToken": "550e8400-e29b-41d4-a716-446655440000",
  "tokenType": "Bearer",
  "expiresIn": 86400000,
  "message": "Login successful"
}
```

#### **2. Token Refresh** `POST /api/auth/refresh`

**Request:**
```json
{
  "refreshToken": "550e8400-e29b-41d4-a716-446655440000"
}
```

**Response:**
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refreshToken": "660e8400-e29b-41d4-a716-446655440001",
  "tokenType": "Bearer",
  "expiresIn": 86400000,
  "message": "Token refreshed successfully"
}
```

#### **3. Logout** `POST /api/auth/logout`

**Headers:**
```http
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

**Response:**
```json
{
  "message": "Logged out successfully"
}
```

### **Protected Endpoints**

All protected endpoints now require the JWT token in the Authorization header:

```http
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

## 🌐 **Frontend Integration**

### **Token Storage**
```javascript
// Store tokens after successful login
localStorage.setItem('accessToken', response.accessToken);
localStorage.setItem('refreshToken', response.refreshToken);

// Include token in API requests
const token = localStorage.getItem('accessToken');
fetch('/api/protected-endpoint', {
  headers: {
    'Authorization': `Bearer ${token}`,
    'Content-Type': 'application/json'
  }
});
```

### **Automatic Token Refresh**
```javascript
// Interceptor for automatic token refresh
axios.interceptors.response.use(
  (response) => response,
  async (error) => {
    if (error.response?.status === 401) {
      const refreshToken = localStorage.getItem('refreshToken');
      if (refreshToken) {
        try {
          const response = await refreshAccessToken(refreshToken);
          localStorage.setItem('accessToken', response.accessToken);
          localStorage.setItem('refreshToken', response.refreshToken);
          // Retry original request
          return axios.request(error.config);
        } catch (refreshError) {
          // Redirect to login
          window.location.href = '/login';
        }
      }
    }
    return Promise.reject(error);
  }
);
```

## ⚙️ **Configuration**

### **Application Properties**
```properties
# JWT Configuration
app.security.jwt.secret=${JWT_SECRET:404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970}
app.security.jwt.expiration=${JWT_EXPIRATION:86400000}
app.security.jwt.refresh-expiration=${JWT_REFRESH_EXPIRATION:604800000}

# CORS Configuration
app.cors.allowed-origins=http://localhost:3000,http://localhost:3001
```

### **Environment Variables**
```bash
# Production Environment
JWT_SECRET=your-256-bit-secret-key-here
JWT_EXPIRATION=86400000
JWT_REFRESH_EXPIRATION=604800000

# Database Configuration
DB_URL=jdbc:postgresql://localhost:5432/restaurant_db
DB_USERNAME=postgres
DB_PASSWORD=your-secure-password
```

## 🧪 **Testing Guide**

### **1. Login Test**
```bash
curl -X POST http://localhost:8085/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "usernameOrEmail": "admin",
    "password": "password"
  }'
```

### **2. Protected Endpoint Test**
```bash
curl -X GET http://localhost:8085/api/auth/user \
  -H "Authorization: Bearer YOUR_JWT_TOKEN_HERE"
```

### **3. Token Refresh Test**
```bash
curl -X POST http://localhost:8085/api/auth/refresh \
  -H "Content-Type: application/json" \
  -d '{
    "refreshToken": "YOUR_REFRESH_TOKEN_HERE"
  }'
```

### **4. Logout Test**
```bash
curl -X POST http://localhost:8085/api/auth/logout \
  -H "Authorization: Bearer YOUR_JWT_TOKEN_HERE"
```

## 🔍 **Troubleshooting**

### **Common Issues**

#### **1. "Invalid JWT token" Error**
- **Cause**: Token expired or malformed
- **Solution**: Refresh token or re-authenticate
- **Check**: Token format and expiration time

#### **2. "Token Refresh Failed" Error**
- **Cause**: Refresh token expired or revoked
- **Solution**: User must log in again
- **Check**: Refresh token validity in database

#### **3. "Account Locked" Error**
- **Cause**: Too many failed login attempts
- **Solution**: Wait for automatic unlock or admin intervention
- **Check**: `failed_login_attempts` and `account_locked` fields

#### **4. CORS Issues**
- **Cause**: Frontend origin not in allowed origins
- **Solution**: Update `app.cors.allowed-origins` property
- **Check**: Browser console for CORS errors

### **Debug Logging**
```properties
# Enable debug logging for JWT
logging.level.com.DevanshNewRMS.NewRMS.Config.JwtAuthenticationFilter=DEBUG
logging.level.com.DevanshNewRMS.NewRMS.Service.JwtService=DEBUG
```

## 🛡️ **Security Considerations**

### **Critical Security Measures**

#### **1. Secret Key Management**
- ⚠️ **NEVER** commit JWT secrets to version control
- ✅ Use environment variables for production
- ✅ Generate cryptographically secure 256-bit keys
- ✅ Rotate keys periodically

#### **2. Token Transmission**
- ✅ Always use HTTPS in production
- ✅ Never log JWT tokens
- ✅ Implement proper CORS policies
- ✅ Use secure HTTP headers

#### **3. Token Storage**
- ✅ Store tokens securely on client side
- ✅ Consider using httpOnly cookies for web apps
- ✅ Implement automatic token cleanup
- ✅ Monitor for suspicious token usage

#### **4. Vulnerability Protection**
- ✅ Implement rate limiting
- ✅ Monitor for brute force attacks
- ✅ Log all authentication events
- ✅ Regular security audits

### **Security Checklist**
- [ ] JWT secret is 256-bit and environment-specific
- [ ] HTTPS is enabled in production
- [ ] Token expiration times are appropriate
- [ ] Refresh token rotation is enabled
- [ ] Account lockout is configured
- [ ] Audit logging is comprehensive
- [ ] Rate limiting is active
- [ ] CORS is properly configured

## 🚀 **Deployment Guide**

### **Pre-Deployment Checklist**
1. [ ] Update JWT secret for production environment
2. [ ] Configure HTTPS certificates
3. [ ] Set appropriate token expiration times
4. [ ] Enable comprehensive logging
5. [ ] Configure monitoring and alerting
6. [ ] Test all authentication flows
7. [ ] Verify CORS configuration
8. [ ] Run security scans

### **Database Migration**
The application automatically creates the required tables:
- `refresh_tokens` - Stores refresh token metadata
- Updated `staff` table with security fields

### **Environment Setup**
```bash
# Production Environment Variables
export JWT_SECRET="your-production-256-bit-secret"
export JWT_EXPIRATION="86400000"
export JWT_REFRESH_EXPIRATION="604800000"
export DB_URL="jdbc:postgresql://prod-db:5432/restaurant_db"
export DB_USERNAME="prod_user"
export DB_PASSWORD="secure_production_password"
```

### **Health Checks**
```bash
# Check application health
curl http://localhost:8085/actuator/health

# Verify JWT endpoint
curl -X POST http://localhost:8085/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"usernameOrEmail":"test","password":"test"}'
```

## 📊 **Monitoring and Metrics**

### **Key Metrics to Monitor**
- Authentication success/failure rates
- Token refresh frequency
- Account lockout incidents
- Suspicious activity alerts
- Token cleanup statistics

### **Log Analysis**
```bash
# Monitor authentication events
grep "Authentication" application.log

# Monitor security events
grep "SECURITY_AUDIT" application.log

# Monitor JWT errors
grep "JWT" application.log
```

## 🔄 **Migration Rollback Plan**

In case of issues, the system can be rolled back by:
1. Reverting to previous application version
2. Disabling JWT filter temporarily
3. Re-enabling Basic Authentication
4. Cleaning up JWT-related database tables

## 📞 **Support and Maintenance**

### **Regular Maintenance Tasks**
- Monitor token cleanup job execution
- Review security audit logs
- Update JWT secrets periodically
- Monitor authentication metrics
- Review and update security policies

### **Emergency Procedures**
- Token revocation for compromised accounts
- Mass token invalidation procedures
- Security incident response protocols
- System recovery procedures

---

## 🎯 **Conclusion**

The JWT authentication migration provides a robust, scalable, and secure authentication mechanism for the NewRMS application. This implementation follows industry best practices and provides comprehensive security features including token rotation, audit logging, and protection against common attacks.

For additional support or questions, please refer to the security team or create an issue in the project repository.

**⚠️ SECURITY WARNING**: This authentication system handles sensitive user data. Always follow security best practices and conduct regular security audits.