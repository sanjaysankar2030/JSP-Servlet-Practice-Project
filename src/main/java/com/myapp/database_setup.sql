-- ============================================================
-- database_setup.sql
-- Run this file in MySQL Workbench or MySQL CLI to set up
-- the database and table required for this application.
-- ============================================================

-- Step 1: Create the database
CREATE DATABASE IF NOT EXISTS myapp_db;

-- Step 2: Select the database
USE myapp_db;

-- Step 3: Create the users table
CREATE TABLE IF NOT EXISTS users (
    id          INT AUTO_INCREMENT PRIMARY KEY,   -- Unique ID for each user
    name        VARCHAR(100)  NOT NULL,            -- Full name
    email       VARCHAR(150)  NOT NULL UNIQUE,     -- Email (must be unique)
    password    VARCHAR(255)  NOT NULL,            -- BCrypt hashed password
    token       VARCHAR(255)  DEFAULT NULL,        -- Email verification token
    is_verified BOOLEAN       DEFAULT FALSE,       -- FALSE until they click verify link
    created_at  TIMESTAMP     DEFAULT CURRENT_TIMESTAMP  -- When they signed up
);

-- Step 4: (Optional) View the table structure
DESCRIBE users;

-- Step 5: (Optional) Check all users
-- SELECT * FROM users;

-- ============================================================
-- To run this file in MySQL CLI:
--   mysql -u root -p < database_setup.sql
--
-- Or paste it into MySQL Workbench and click Run (Ctrl+Shift+Enter)
-- ============================================================
