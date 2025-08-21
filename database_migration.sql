-- Database migration script for enhanced Staff authentication system
-- This script updates the staff table to support the new authentication features

-- Step 1: Add new columns as nullable first
ALTER TABLE staff 
ADD COLUMN IF NOT EXISTS first_name VARCHAR(50),
ADD COLUMN IF NOT EXISTS last_name VARCHAR(50),
ADD COLUMN IF NOT EXISTS email VARCHAR(100),
ADD COLUMN IF NOT EXISTS account_locked BOOLEAN DEFAULT FALSE,
ADD COLUMN IF NOT EXISTS failed_login_attempts INTEGER DEFAULT 0,
ADD COLUMN IF NOT EXISTS last_login_attempt TIMESTAMP,
ADD COLUMN IF NOT EXISTS password_changed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;

-- Create indexes for better performance
CREATE INDEX IF NOT EXISTS idx_staff_username ON staff(username);
CREATE INDEX IF NOT EXISTS idx_staff_email ON staff(email);
CREATE INDEX IF NOT EXISTS idx_staff_account_locked ON staff(account_locked);

-- Step 2: Migrate existing data (if any)
-- Update existing records to split name into first_name and last_name
UPDATE staff 
SET 
    first_name = CASE 
        WHEN name IS NOT NULL AND position(' ' in name) > 0 
        THEN substring(name from 1 for position(' ' in name) - 1)
        WHEN name IS NOT NULL
        THEN name
        ELSE 'Unknown'
    END,
    last_name = CASE 
        WHEN name IS NOT NULL AND position(' ' in name) > 0 
        THEN substring(name from position(' ' in name) + 1)
        WHEN name IS NOT NULL
        THEN 'User'
        ELSE 'User'
    END,
    email = CASE 
        WHEN email IS NULL 
        THEN username || '@newrms.local'
        ELSE email
    END,
    account_locked = COALESCE(account_locked, FALSE),
    failed_login_attempts = COALESCE(failed_login_attempts, 0),
    password_changed_at = COALESCE(password_changed_at, created_at, CURRENT_TIMESTAMP)
WHERE first_name IS NULL OR last_name IS NULL OR email IS NULL;

-- Step 3: Add NOT NULL constraints after data migration
ALTER TABLE staff 
ALTER COLUMN first_name SET NOT NULL,
ALTER COLUMN last_name SET NOT NULL,
ALTER COLUMN account_locked SET NOT NULL,
ALTER COLUMN failed_login_attempts SET NOT NULL;

-- Step 4: Add unique constraint on email after ensuring no duplicates
-- First, handle any potential duplicate emails
UPDATE staff 
SET email = username || '_' || id || '@newrms.local'
WHERE email IN (
    SELECT email 
    FROM staff 
    GROUP BY email 
    HAVING COUNT(*) > 1
);

-- Now add the unique constraint
ALTER TABLE staff 
ADD CONSTRAINT uk_staff_email UNIQUE (email);

-- Update roles to include STAFF if needed
UPDATE staff 
SET roles = REPLACE(roles, 'WAITER', 'STAFF') 
WHERE roles LIKE '%WAITER%' AND roles NOT LIKE '%STAFF%';

-- Create audit log table for security monitoring
CREATE TABLE IF NOT EXISTS security_audit_log (
    id BIGSERIAL PRIMARY KEY,
    timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    username VARCHAR(100),
    ip_address INET,
    action VARCHAR(50) NOT NULL,
    status VARCHAR(20) NOT NULL,
    details TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create index on audit log for better query performance
CREATE INDEX IF NOT EXISTS idx_security_audit_timestamp ON security_audit_log(timestamp);
CREATE INDEX IF NOT EXISTS idx_security_audit_username ON security_audit_log(username);
CREATE INDEX IF NOT EXISTS idx_security_audit_action ON security_audit_log(action);

-- Create a view for recent security events
CREATE OR REPLACE VIEW recent_security_events AS
SELECT 
    timestamp,
    username,
    ip_address,
    action,
    status,
    details
FROM security_audit_log 
WHERE timestamp >= CURRENT_TIMESTAMP - INTERVAL '30 days'
ORDER BY timestamp DESC;

COMMENT ON TABLE staff IS 'Staff members with enhanced authentication and security features';
COMMENT ON TABLE security_audit_log IS 'Security audit log for authentication and authorization events';
COMMENT ON VIEW recent_security_events IS 'Recent security events within the last 30 days';