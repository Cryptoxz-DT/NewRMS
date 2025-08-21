# Enhanced Authentication System Documentation

## Overview

This document describes the enhanced authentication and security system implemented for the NewRMS (Restaurant Management System). The system provides robust user authentication, password security, account protection, and comprehensive audit logging.

## Key Security Features

### 1. Secure Password Management
- **BCrypt Hashing**: Passwords are hashed using BCrypt with strength 12
- **Password Complexity Requirements**:
  - Minimum 8 characters, maximum 128 characters
  - At least one lowercase letter
  - At least one uppercase letter
  - At least one digit
  - At least one special character (@$!%*?&)
  - Protection against common weak passwords

### 2. Account Protection
- **Account Lockout**: Accounts are automatically locked after 5 failed login attempts
- **Failed Attempt Tracking**: System tracks and logs all failed login attempts
- **Account Unlock**: Locked accounts can be unlocked by administrators
- **Login Attempt Monitoring**: All login attempts are logged with timestamps and IP addresses

### 3. Rate Limiting
- **IP-based Rate Limiting**: Prevents brute force attacks by limiting requests per IP
- **Configurable Cooldown**: Automatic cooldown periods for excessive requests
- **Multiple Endpoint Protection**: Rate limiting applied to both login and signup endpoints

### 4. Comprehensive Audit Logging
- **Security Event Logging**: All authentication events are logged
- **IP Address Tracking**: Client IP addresses are captured and logged
- **Suspicious Activity Detection**: Automated detection and logging of suspicious activities
- **Audit Trail**: Complete audit trail for compliance and security monitoring

### 5. Input Validation and Sanitization
- **Server-side Validation**: Comprehensive validation using Jakarta Bean Validation
- **XSS Protection**: Input sanitization to prevent cross-site scripting attacks
- **SQL Injection Prevention**: Prepared statements and JPA queries prevent SQL injection
- **Email Validation**: Proper email format validation and uniqueness checks

## API Endpoints

### Authentication Endpoints

#### 1. User Registration
```http
POST /api/auth/signup
Content-Type: application/json

{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com",
  "username": "johndoe",
  "password": "SecurePass123!",
  "confirmPassword": "SecurePass123!",
  "roles": "STAFF"
}
```

**Response (Success - 201 Created):**
```json
{
  "id": 1,
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com",
  "username": "johndoe",
  "roles": "STAFF",
  "createdAt": "2025-01-21T10:30:00",
  "message": "User registered successfully"
}
```

#### 2. User Login
```http
POST /api/auth/login
Content-Type: application/json

{
  "usernameOrEmail": "johndoe",
  "password": "SecurePass123!"
}
```

**Response (Success - 200 OK):**
```json
{
  "id": 1,
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com",
  "username": "johndoe",
  "roles": ["STAFF"],
  "lastLogin": "2025-01-21T10:30:00",
  "message": "Login successful"
}
```

#### 3. Check Username Availability
```http
GET /api/auth/check-username?username=johndoe
```

**Response:**
```json
{
  "available": false
}
```

#### 4. Get Current User
```http
GET /api/auth/user
Authorization: Basic <credentials>
```

#### 5. Logout
```http
POST /api/auth/logout
Authorization: Basic <credentials>
```

## Database Schema

### Staff Table Structure
```sql
CREATE TABLE staff (
    id BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    roles VARCHAR(255) NOT NULL,
    account_locked BOOLEAN NOT NULL DEFAULT FALSE,
    failed_login_attempts INTEGER NOT NULL DEFAULT 0,
    last_login_attempt TIMESTAMP,
    password_changed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### Security Audit Log Table
```sql
CREATE TABLE security_audit_log (
    id BIGSERIAL PRIMARY KEY,
    timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    username VARCHAR(100),
    ip_address INET,
    action VARCHAR(50) NOT NULL,
    status VARCHAR(20) NOT NULL,
    details TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

## Security Configuration

### Spring Security Configuration
- **CORS**: Configured for frontend integration
- **CSRF**: Disabled for API endpoints
- **Session Management**: Stateless session creation policy
- **Security Headers**: HSTS, Content-Type Options, Frame Options
- **Authentication**: HTTP Basic authentication for API access

### Password Encoding
- **Algorithm**: BCrypt
- **Strength**: 12 (high security)
- **Salt**: Automatically generated per password

## Error Handling

### Common Error Responses

#### 1. Validation Errors (400 Bad Request)
```json
{
  "error": "WeakPassword",
  "message": "Password must contain at least one uppercase letter"
}
```

#### 2. User Already Exists (409 Conflict)
```json
{
  "error": "UserAlreadyExists",
  "message": "Username is already taken"
}
```

#### 3. Account Locked (403 Forbidden)
```json
{
  "error": "Account Locked",
  "message": "Account is locked due to multiple failed login attempts"
}
```

#### 4. Invalid Credentials (401 Unauthorized)
```json
{
  "error": "Invalid Credentials",
  "message": "Invalid username/email or password"
}
```

#### 5. Rate Limit Exceeded (429 Too Many Requests)
```json
{
  "error": "Too Many Requests",
  "message": "Too many login attempts. Please try again in 15 minutes.",
  "retryAfterMinutes": 15
}
```

## Security Best Practices Implemented

### 1. Password Security
- ✅ Strong password hashing (BCrypt with high cost)
- ✅ Password complexity requirements
- ✅ Protection against common passwords
- ✅ Password change tracking

### 2. Account Protection
- ✅ Account lockout after failed attempts
- ✅ Failed attempt tracking and logging
- ✅ IP-based monitoring
- ✅ Suspicious activity detection

### 3. Input Security
- ✅ Server-side validation
- ✅ Input sanitization
- ✅ SQL injection prevention
- ✅ XSS protection

### 4. Monitoring and Auditing
- ✅ Comprehensive audit logging
- ✅ Security event tracking
- ✅ IP address logging
- ✅ Failed attempt monitoring

### 5. Rate Limiting
- ✅ IP-based rate limiting
- ✅ Configurable thresholds
- ✅ Automatic cooldown periods
- ✅ Multiple endpoint protection

## Frontend Integration

### Signup Form Fields
- First Name (required, 2-50 characters)
- Last Name (required, 2-50 characters)
- Email (required, valid email format)
- Username (required, 3-50 characters, alphanumeric + underscore)
- Password (required, meets complexity requirements)
- Confirm Password (required, must match password)
- Role (required, dropdown selection)

### Login Form Fields
- Username or Email (required)
- Password (required)

### Error Handling
- Client-side validation for immediate feedback
- Server-side error display for security violations
- Rate limiting notifications
- Account lockout notifications

## Deployment Considerations

### Environment Variables
```bash
DB_URL=jdbc:postgresql://localhost:5432/restaurant_db
DB_USERNAME=postgres
DB_PASSWORD=your_secure_password
JWT_SECRET=your_jwt_secret_key
JWT_EXPIRATION=86400000
```

### Database Migration
1. Run the provided `database_migration.sql` script
2. Or rely on Hibernate's `ddl-auto=update` for automatic schema updates

### Security Headers
The application automatically sets security headers:
- HSTS (HTTP Strict Transport Security)
- Content-Type Options
- Frame Options (DENY)
- Referrer Policy

## Monitoring and Maintenance

### Log Monitoring
Monitor application logs for:
- Failed login attempts
- Account lockouts
- Rate limit violations
- Suspicious activities

### Regular Maintenance
- Review audit logs regularly
- Monitor for unusual patterns
- Update password policies as needed
- Review and update rate limiting thresholds

### Performance Considerations
- Database indexes on username and email fields
- Connection pooling with HikariCP
- Efficient query patterns
- Audit log cleanup procedures

## Compliance and Standards

This implementation follows security best practices and standards:
- OWASP Top 10 protection
- NIST password guidelines
- Industry-standard encryption
- Comprehensive audit trails
- Data protection principles

## Future Enhancements

Potential future security enhancements:
- Multi-factor authentication (MFA)
- JWT token-based authentication
- OAuth2 integration
- Advanced threat detection
- Automated security scanning
- Password expiration policies
- Role-based access control (RBAC) refinements