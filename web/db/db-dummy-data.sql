-- This script will populate the database with dummy data for us to test
-- against.

-- Some test users. All of their passwords are "password"

USE dishdriver;

INSERT INTO Users
  (Id, Email, Password, FirstName, LastName, DT_LastLogin)
VALUES
  (1, 'tj@dishdriver.com', '$2a$04$qj9nwY0MzPcXIqUOpNCRU.zdgjIsQco7.ibIKgXwDWfz.P4nICqwq', '', '', CURRENT_TIMESTAMP()),
  (2, 'ashton@dishdriver.com', '$2a$04$qj9nwY0MzPcXIqUOpNCRU.zdgjIsQco7.ibIKgXwDWfz.P4nICqwq', '', '', CURRENT_TIMESTAMP()),
  (3, 'chris@dishdriver.com', '$2a$04$qj9nwY0MzPcXIqUOpNCRU.zdgjIsQco7.ibIKgXwDWfz.P4nICqwq', '', '', CURRENT_TIMESTAMP()),
  (4, 'alexander-mark-arendsen@dishdriver.com', '$2a$04$qj9nwY0MzPcXIqUOpNCRU.zdgjIsQco7.ibIKgXwDWfz.P4nICqwq', '', '', CURRENT_TIMESTAMP()),
  (5, 'melissa@dishdriver.com', '$2a$04$qj9nwY0MzPcXIqUOpNCRU.zdgjIsQco7.ibIKgXwDWfz.P4nICqwq', '', '', CURRENT_TIMESTAMP()),
  (6, 'rebecca@dishdriver.com', '$2a$04$qj9nwY0MzPcXIqUOpNCRU.zdgjIsQco7.ibIKgXwDWfz.P4nICqwq', '', '', CURRENT_TIMESTAMP()),
  (7, 'vivienne@dishdriver.com', '$2a$04$qj9nwY0MzPcXIqUOpNCRU.zdgjIsQco7.ibIKgXwDWfz.P4nICqwq', '', '', CURRENT_TIMESTAMP());

INSERT INTO Restaurants
  (Id, Name, DT_Opened, DT_Closed)
VALUES
  (1, 'Grandma\'s Pie House', CURRENT_TIMESTAMP(), NULL),
  (2, 'Pie Day Pie', '2017-01-13 14:13:00', NULL),
  (3, 'Chad Thai (Doesn\'t Actually Serve Thai Food)', '2017-01-13 14:13:00', NULL),
  (4, 'pom.xml (Priceless Object Management by Ashton)', '2017-01-13 14:13:00', NULL),
  (5, 'Mama Ling-Lings Teahouse and Sandwicheria', '2017-01-13 14:13:00', NULL);


INSERT INTO Roles
  (Id, Name)
VALUES
  (1, 'Admin'),
  (2, 'Waiter'),
  (3, 'Cook');


INSERT INTO Positions
  (Employee_ID, Role_ID, Restaurant_ID, DT_Hired, DT_Unhired)
VALUES
  (1, 1, 4, CURRENT_TIMESTAMP(), NULL);

INSERT INTO Dishes
  (Id, Restaurant_ID, Price, Name, Description, DT_Deleted)
VALUES
  (1, 1, 499, 'A Slice of Pie', 'It\'s gourmet pies, guys.', NULL),
  (2, 3, 727, 'A Buger!', '100% Beefcow', NULL),
  (3, 3, 1400, '1-Foot Long Hot Dog (Kids Menu Version)', 'It has to be at least six dollars, so says Melissa', NULL),
  (4, 3, 99, 'Nachos (w/ Cream)', 'So cheap, you\'re gonna ask "Why is it so cheap?" o-o', NULL),
  (5, 3, 3799, 'Buffalo Wings', 'I think they\'re from New York? Also: expensive as hell lolol', NULL);
