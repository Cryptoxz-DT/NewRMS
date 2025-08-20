# Sign-Up API Documentation

## Overview
The NewRMS Sign-Up API provides secure user registration functionality with comprehensive validation, rate limiting, and security features.

## Endpoints

### 1. User Registration
**POST** `/api/auth/signup`

Registers a new staff member with secure password hashing and comprehensive validation.

#### Request Body
```json
{
  "name": "John Doe",
  "username": "johndoe",
  "password": "SecurePass123!",
  "confirmPassword": "SecurePass123!",
  "roles": "WAITER"
}
```

#### Field Validation Rules

| Field | Type | Required | Min Length | Max Length | Pattern | Description |
|-------|------|----------|------------|------------|---------|-------------|
| name | String | Yes | 2 | 100 | `^[a-zA-Z\s'-]+$` | Letters, spaces, hyphens, apostrophes only |
| username | String | Yes | 3 | 50 | `^[a-zA-Z0-9_]+$` | Letters, numbers, underscores only |
| password | String | Yes | 8 | 128 | Complex pattern | Must contain uppercase, lowercase, digit, special char |
| confirmPassword | String | Yes | - | - | Must match password | Password confirmation |
| roles | String | Yes | - | - | Valid role pattern | ADMIN, MANAGER, WAITER, CHEF, CASHIER |

#### Password Requirements
- Minimum 8 characters, maximum 128 characters
- At least one lowercase letter (a-z)
- At least one uppercase letter (A-Z)
- At least one digit (0-9)
- At least one special character (@$!%*?&)
- Cannot contain common weak patterns

#### Success Response (201 Created)
```json
{
  "id": 1,
  "name": "John Doe",
  "username": "johndoe",
  "roles": "WAITER",
  "createdAt": "2025-01-20T10:30:00",
  "message": "User registered successfully"
}
```

#### Error Responses

**400 Bad Request - Validation Error**
```json
{
  "timestamp": "2025-01-20T10:30:00",
  "status": 400,
  "error": "Validation Failed",
  "errors": {
    "password": "Password must contain at least one uppercase letter"
  }
}
```

**409 Conflict - Username Exists**
```json
{
  "timestamp": "2025-01-20T10:30:00",
  "status": 409,
  "error": "User Already Exists",
  "message": "Username is already taken"
}
```

**429 Too Many Requests - Rate Limited**
```json
{
  "error": "Too Many Requests",
  "message": "Too many sign-up attempts. Please try again in 12 minutes.",
  "retryAfterMinutes": 12
}
```

### 2. Username Availability Check
**GET** `/api/auth/check-username?username=johndoe`

Checks if a username is available for registration.

#### Success Response (200 OK)
```json
{
  "available": true
}
```

### 3. Form Structure (Development Helper)
**GET** `/api/test/signup-form`

Returns the complete form structure and validation rules for frontend development.

#### Success Response (200 OK)
```json
{
  "fields": {
    "name": {
      "type": "text",
      "required": true,
      "minLength": 2,
      "maxLength": 100,
      "pattern": "^[a-zA-Z\\s'-]+$",
      "description": "Full name (letters, spaces, hyphens, apostrophes only)"
    }
    // ... other fields
  },
  "endpoint": "/api/auth/signup",
  "method": "POST",
  "rateLimiting": {
    "maxAttempts": 5,
    "windowMinutes": 15,
    "description": "Maximum 5 attempts per 15 minutes per IP"
  }
}
```

## Security Features

### 1. Password Security
- **BCrypt Hashing**: Passwords are hashed using BCrypt with strength 12
- **Complex Requirements**: Enforced password complexity rules
- **Common Pattern Detection**: Prevents use of common weak passwords

### 2. Rate Limiting
- **IP-based Limiting**: Maximum 5 attempts per IP address
- **Time Window**: 15-minute sliding window
- **Automatic Reset**: Counters reset after the time window expires

### 3. Input Validation & Sanitization
- **Server-side Validation**: All inputs validated using Jakarta Bean Validation
- **XSS Prevention**: Input sanitization to prevent cross-site scripting
- **SQL Injection Prevention**: Parameterized queries through JPA

### 4. Security Headers
- **X-Frame-Options**: Prevents clickjacking attacks
- **X-Content-Type-Options**: Prevents MIME type sniffing
- **Strict-Transport-Security**: Enforces HTTPS connections
- **Referrer-Policy**: Controls referrer information

### 5. Audit Logging
- **Registration Attempts**: All registration attempts are logged
- **Security Events**: Failed attempts and rate limiting events are logged
- **IP Tracking**: Client IP addresses are tracked for security monitoring

## Error Handling

The API implements comprehensive error handling with:
- **Structured Error Responses**: Consistent error format across all endpoints
- **Detailed Validation Messages**: Clear field-specific validation errors
- **Security-conscious Messages**: Non-descriptive error messages to prevent information leakage
- **Proper HTTP Status Codes**: Appropriate status codes for different error types

## Usage Examples

### Frontend Integration
```javascript
// Registration request
const signUpData = {
  name: "John Doe",
  username: "johndoe",
  password: "SecurePass123!",
  confirmPassword: "SecurePass123!",
  roles: "WAITER"
};

try {
  const response = await fetch('/api/auth/signup', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(signUpData)
  });
  
  if (response.ok) {
    const result = await response.json();
    console.log('Registration successful:', result);
  } else {
    const error = await response.json();
    console.error('Registration failed:', error);
  }
} catch (error) {
  console.error('Network error:', error);
}
```

### Username Availability Check
```javascript
const checkUsername = async (username) => {
  const response = await fetch(`/api/auth/check-username?username=${username}`);
  const result = await response.json();
  return result.available;
};
```

## Testing

Use the test endpoints to validate your implementation:

1. **GET** `/api/test/signup-form` - Get form structure
2. **POST** `/api/test/validate-signup` - Validate data without saving

## Security Considerations

1. **HTTPS Only**: Always use HTTPS in production
2. **Rate Limiting**: Monitor and adjust rate limiting based on usage patterns
3. **Password Policies**: Consider implementing additional password policies as needed
4. **Audit Logs**: Regularly review audit logs for suspicious activity
5. **Database Security**: Ensure database connections are secure and encrypted

## Monitoring

Key metrics to monitor:
- Registration success/failure rates
- Rate limiting triggers
- Password validation failures
- Username collision attempts
- Response times and system performance