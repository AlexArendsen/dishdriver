-- This file describes two scripts. If you can write then as plaintext files,
-- that would be great! Don't worry about testing them. Thanks!


-- Script 1: The Create Script (sql-create.sql)

-- This script will create a fresh instance of our DB, and will just contain a
-- "CREATE TABLE" statement for each entity defined in our ER specification. It
-- doesn't actually put any data in-- it only defines the tables, their
-- colunms, and the various constraints among them. Running the "Drop script"
-- will undo everything this script does.

CREATE TABLE Restaurants (
  Id            INT          NOT NULL AUTO_INCREMENT,
  Name          VARCHAR(255) NOT NULL,
  DT_Opened     DATETIME     NOT NULL DEFAULT NOW(),
  DT_Closed     DATETIME     DEFAULT NULL,
  PRIMARY_KEY(Id)
);

CREATE TABLE Positions (
  Id            INT         NOT NULL AUTO_INCREMENT,
  Employee_ID   INT         NOT NULL,
  Role_ID       INT         NOT NULL,
  Restaurant_ID INT         NOT NULL,
  DT_Hired      DATETIME    NOT NULL DEFAULT NOW(),
  DT_Unhired    DATETIME    DEFAULT NULL,
  PRIMARY_KEY(Id),
  FOREIGN KEY FK_Positions_Employees(Employee_ID) REFERENCES Employees(Id),
  FOREIGN KEY FK_Positions_Restaurants(Restaurant_ID) REFERENCES Restaurants(Id),
  UNIQUE (Employee_ID, Restaurant_ID)

);

-- For each primary key column, please make sure that "AUTO_INCREMENT" is
-- defined (I forgot to put this in the DB spec-- oops!).

-- Some explanation for the FOREIGN_KEY part: FOREIGN_KEY constraints define
-- which columns must contain values (Id numbers, in our case) from other
-- tables. The parts of the constraint notation are as follows:
-- -------
--   FOREIGN KEY <Name of key constraint>(<column in this table>) REFERENCES <referenced table>(<referenced column>)
-- -------
-- You can name the foreign key constraint whatever you'd like, but it's
-- conventional that such constraints begin with "FK"


-- Script 2: The Drop Script (sql-drop.sql)

-- Much simpler, it just deletes ("drops") all of the tables created by the
-- create script.

DROP TABLE Positions;

-- That's all! ^^ The only complicated part is deleting them in the correct
-- order, but that's as simple as rearranging the lines in the script.
