-- This script will populate the database with dummy data for us to test
-- against.

-- Some test users. All of their passwords are "password"

USE dishdriver;

INSERT INTO Users
  (Email, Password, FirstName, LastName, DT_LastLogin)
VALUES
  ('someone@example.com', '$2a$04$qj9nwY0MzPcXIqUOpNCRU.zdgjIsQco7.ibIKgXwDWfz.P4nICqwq', '', '', CURRENT_TIMESTAMP());

