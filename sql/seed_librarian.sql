-- Creates the default librarian (admin) login account.
-- Safe to run at any time (doesn't assume the persons table is empty) -
-- it lets MySQL auto-assign the id and links the librarians row to it.
--
-- IMPORTANT: run this AFTER the app has started at least once
-- (docker compose up), because that first run is what creates the
-- persons/librarians tables (Hibernate ddl-auto=update).
--
-- Default login:
--   username: librarian
--   password: librarian123
--
-- Change this password after first login (see README.md, "Changing a password").

USE library_db;

INSERT INTO persons (name, email, phone, username, password)
VALUES ('Head Librarian', 'librarian@library-lab.edu.bd', '+8801700000000',
        'librarian', '$2b$10$pfz0ImCUpdWZrb72n1RiQ.oFc.fRxLWZGEY8zflb1gSGlBjzmf7S2');

INSERT INTO librarians (id, staff_id, department)
VALUES (LAST_INSERT_ID(), 'STAFF-0001', 'Library Administration');
