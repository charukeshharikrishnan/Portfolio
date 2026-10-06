CREATE DATABASE IF NOT EXISTS portfolio CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE portfolio;
CREATE TABLE IF NOT EXISTS contact_messages (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(80) NOT NULL,
  email VARCHAR(120) NOT NULL,
  subject VARCHAR(120) NOT NULL,
  message VARCHAR(2000) NOT NULL,
  created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6)
);
-- Least-privilege app user (change the password):
-- CREATE USER 'portfolio_app'@'localhost' IDENTIFIED BY 'CHANGE_ME';
-- GRANT INSERT ON portfolio.contact_messages TO 'portfolio_app'@'localhost';
