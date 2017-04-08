-- This script will populate the database with dummy data for us to test
-- against.

-- Some test users. All of their passwords are "password"

USE dishdriver;

INSERT INTO Users
  (Id, Email, Password, FirstName, LastName, DT_LastLogin)
VALUES
  (1, 'tj@dishdriver.com', '$2a$04$qj9nwY0MzPcXIqUOpNCRU.zdgjIsQco7.ibIKgXwDWfz.P4nICqwq', '', '', CURRENT_TIMESTAMP()),
  (2, 'ashton@dishdriver.com', '$2a$04$qj9nwY0MzPcXIqUOpNCRU.zdgjIsQco7.ibIKgXwDWfz.P4nICqwq', '', '', CURRENT_TIMESTAMP()),
  (3, 'chad_dude@dishdriver.com', '$2a$04$qj9nwY0MzPcXIqUOpNCRU.zdgjIsQco7.ibIKgXwDWfz.P4nICqwq', '', '', CURRENT_TIMESTAMP()),
  (4, 'alexander-mark-arendsen@dishdriver.com', '$2a$04$qj9nwY0MzPcXIqUOpNCRU.zdgjIsQco7.ibIKgXwDWfz.P4nICqwq', '', '', CURRENT_TIMESTAMP()),
  (5, 'melissa@dishdriver.com', '$2a$04$qj9nwY0MzPcXIqUOpNCRU.zdgjIsQco7.ibIKgXwDWfz.P4nICqwq', '', '', CURRENT_TIMESTAMP()),
  (6, 'rebecca@dishdriver.com', '$2a$04$qj9nwY0MzPcXIqUOpNCRU.zdgjIsQco7.ibIKgXwDWfz.P4nICqwq', '', '', CURRENT_TIMESTAMP()),
  (7, 'vivienne@dishdriver.com', '$2a$04$qj9nwY0MzPcXIqUOpNCRU.zdgjIsQco7.ibIKgXwDWfz.P4nICqwq', '', '', CURRENT_TIMESTAMP());

INSERT INTO Restaurants
  (Id, Name, DT_Opened, DT_Closed)
VALUES
  (1, 'Grandma\'s Pie House', CURRENT_TIMESTAMP(), NULL),
  (2, 'Pie Day Pie', '2017-01-13 14:13:00', NULL),
  (3, 'Chad Thai (Vivienne\'s Vegan)', '2017-01-13 14:13:00', NULL),
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
  (2, 2, 3, CURRENT_TIMESTAMP(), NULL),
  (3, 1, 3, CURRENT_TIMESTAMP(), NULL),
  (4, 3, 3, CURRENT_TIMESTAMP(), NULL),
  (5, 1, 3, CURRENT_TIMESTAMP(), NULL),
  (6, 2, 3, CURRENT_TIMESTAMP(), NULL);

INSERT INTO Dishes
  (Id, Restaurant_ID, Price, Name, Description, DT_Deleted)
VALUES
  (1, 3, 499, 'A Slice of Pie', 'It\'s gourmet pies, guys.', NULL),
  (2, 3, 727, 'A Buger!', '100% Beefcow', NULL),
  (3, 3, 1400, '1-Foot Long Hot Dog (Kids Menu Version)', 'It has to be at least six dollars, so says Melissa', NULL),
  (4, 3, 99, 'Nachos (w/ Cream)', 'So cheap, you\'re gonna ask "Why is it so cheap?" o-o', NULL),
  (5, 3, 3799, 'Buffalo Wings', 'I think they\'re from New York? Also: expensive as hell lolol', NULL),
  (6, 3, -200, 'Viv\'s Vejan Tiramisu', 'CHAD DUDE PLEAse eat this we\'ll pay u', NULL);

INSERT INTO Tables
  (ID, Restaurant_ID, Name, Position_X, Position_Y, Capacity)
VALUES
  (1, 3, 'A1', 32, 32, 4),
  (2, 3, 'A2', 64, 32, 4),
  (3, 3, 'A3', 96, 32, 4),
  (4, 3, 'A4', 128, 32, 4),
  (5, 3, 'A5', 32, 64, 4),
  (6, 3, 'A6', 64, 64, 4);

INSERT INTO Table_Reservations
  (ID, Table_ID, Party_Name, Party_Size, Deposit, DepositPayed, DT_Requested, DT_Accepted)
VALUES
  (1, 3, 'TJ\'s Super Duper Pirate Party', 30, 8, 0, CURRENT_TIMESTAMP(), NULL);

INSERT INTO Orders
  (Id, Waiter_ID, Cook_ID, Table_ID, DT_Created, DT_Placed, DT_Rejected, DT_Accepted, DT_Cancelled, DT_Cooked, DT_Payed, Discount, Payment, Instructions)
VALUES
  (1, 1, 1, 3, '2017-01-03 13:00:00', '2017-01-03 13:15:47', NULL, NULL, NULL, NULL, NULL, 220, 1299, 'None pizza left beef');

INSERT INTO Ordered_Dishes
  (Id, Order_ID, Dish_ID, IsRejected, IsVoided, OrderedPrice, NotesFromKitchen)
VALUES
  (1, 1, 2, false, false, 10000, 'Make sure that it\'s a burger.'),
  (2, 1, 1, false, false, 9999,  'This is for desert.'),
  (3, 1, 4, false, false, 12345, 'They NOTCH-yos (omg!)'),
  (4, 1, 5, false, false, 71771, 'Customer requests that they are made from buffalos and not from chickens or whatever.');

INSERT INTO Reviews
  (Id, Customer_ID, Restaurant_ID, Waiter_ID, Service_Rating, Food_Rating, Comments)
VALUES
  (1, 3, 3, 1, 3, 0, 'My food was on fire and also was everything');
