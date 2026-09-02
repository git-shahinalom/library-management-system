-- Run this ONCE on VM 103 (Database Server), e.g.:
--   sudo mysql < init-vm103.sql
-- Edit the password below before running, then keep this file out of git
-- (or blank the password out again before committing).

CREATE DATABASE IF NOT EXISTS library_db;

-- Restrict the user to connections coming from VM 101 only (192.168.88.254).
CREATE USER IF NOT EXISTS 'library_user'@'192.168.88.254' IDENTIFIED BY 'CHANGE_ME';
GRANT ALL PRIVILEGES ON library_db.* TO 'library_user'@'192.168.88.254';
FLUSH PRIVILEGES;
