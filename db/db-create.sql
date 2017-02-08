-- Group 2: MYSQL Resturant Database

-- Notes:
-- ------
-- Changed from tabs to spaces (personal preference)
-- Added spacing (personal preference as well)
-- Capitalized tables names
-- Added AUTO_INCREMENT (forgot to put these in the spec-- oops!)
-- Added NOT NULL to PKs
-- Added "CREATE DATABASE" and "USE" statements to the top
-- Changed 'NOW' values to NOW()
-- Removed quotes from INT default values
-- Added missing word "KEY" to `Reviews` table
-- Changed `Positions` primary key to ID field
-- Added note about the TEXT type, see `Dishes` table
-- Changed BIT to BINARY (my bad-- BIT is an MSSQL thing; BINARY is the MySQL equivalent)
-- Added semicolons to end of CREATE TABLE statements
-- Added commas after integrity constraints
-- Had to adjust the lengths of VARCHAR fields involved with UNIQUE contraints (the limit for keys is 767 bytes)
-- For the foreign key on `Positions`, Waiter_ID and Cook_ID each needed separate FK constraints


CREATE DATABASE dishdriver;
USE dishdriver;

CREATE USER 'dishdriver'@'localhost' IDENTIFIED BY 'yummo';
GRANT ALL PRIVILEGES ON dishdriver.* TO 'dishdriver'@'localhost' WITH GRANT OPTION;

CREATE TABLE Users (
  ID           INT          NOT NULL AUTO_INCREMENT, 
  Email        VARCHAR(127) NOT NULL,
  Password     VARCHAR(255) NOT NULL,
  FirstName    VARCHAR(255) NOT NULL,
  LastName     VARCHAR(255) NOT NULL,
  DT_Created   DATETIME     NOT NULL DEFAULT NOW(),
  DT_LastLogin DATETIME     NULL DEFAULT NULL,
  DT_Cancelled DATETIME     NULL DEFAULT NULL, 
  PRIMARY KEY (ID),
  UNIQUE (Email)
);

CREATE TABLE Roles (
  ID           INT          NOT NULL AUTO_INCREMENT,
  Name         VARCHAR(32)  NOT NULL, 
  PRIMARY KEY (ID),
  UNIQUE (Name) 
); -- This was a right curly brace


CREATE TABLE Restaurants (
  ID           INT          NOT NULL AUTO_INCREMENT, 
  Name         VARCHAR(255) NOT NULL,
  DT_Opened    DATETIME     NOT NULL DEFAULT NOW(),
  DT_Closed    DATETIME     DEFAULT NULL,
  PRIMARY KEY (ID) 
);

CREATE TABLE Positions (
  ID           INT          AUTO_INCREMENT, 
  Employee_ID  INT          NOT NULL, /* Reference Users */
  Role_ID      INT          NOT NULL, 
  Restaurant_ID INT         NOT NULL, /* Reference Restaurant */
  DT_Hired     DATETIME     NOT NULL DEFAULT NOW(),
  DT_Unhired   DATETIME     DEFAULT NULL,
  PRIMARY KEY (ID),
  FOREIGN KEY (Role_ID) REFERENCES Roles (ID),
  FOREIGN KEY (Employee_ID) REFERENCES Users (ID),
  FOREIGN KEY (Restaurant_ID) REFERENCES Restaurants (ID),
  UNIQUE(Employee_ID, Restaurant_ID)
  -- ^^ I think this is correct for doing the composite key, but I can't vouch
  -- for that
);

CREATE TABLE Orders (
  ID          INT           NOT NULL AUTO_INCREMENT,
  Waiter_ID   INT           NOT NULL,
  Cook_ID     INT           DEFAULT NULL,
  DT_Placed   DATETIME      NOT NULL,
  DT_Rejected DATETIME      DEFAULT NULL,
  DT_Cancelled DATETIME     DEFAULT NULL,
  DT_Cooked   DATETIME      DEFAULT NULL,
  DT_Payed    DATETIME      DEFAULT NULL,
  Discount    INT           NOT NULL DEFAULT 0,
  Payment     INT           NOT NULL DEFAULT 0,
  Instructions VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (ID),
  FOREIGN KEY (Cook_ID) REFERENCES Positions (ID),
  FOREIGN KEY (Waiter_ID) REFERENCES Positions (ID)
  -- ^^ Question was "which ID?", we just need FKs for both of them individually
);

CREATE TABLE Dishes (
  ID          INT           NOT NULL AUTO_INCREMENT,
  Restaurant_ID INT         NOT NULL,
  Price       INT           NOT NULL DEFAULT 0,
  Name        VARCHAR(255)  NOT NULL,
  Description TEXT          DEFAULT NULL, -- See below*
  DT_Deleted  DATETIME      DEFAULT NULL,
  PRIMARY KEY (ID),
  FOREIGN KEY (Restaurant_ID) REFERENCES Restaurants (ID)
  -- *MySQL and other DBMSes use different types for CLOBs (character-large
  -- objects), so the syntax highlighting of your editor might not catch the type
  -- properly.
);

CREATE TABLE Order_Dishes (
  ID          INT           NOT NULL AUTO_INCREMENT,
  Order_ID    INT           NOT NULL, 
  Dish_ID     INT           NOT NULL,
  IsRejected  BINARY        NOT NULL DEFAULT 0,
  IsVoided    BINARY        NOT NULL DEFAULT 0,
  NotesFromKitchen VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (ID),
  /* WE ARE ADDING THESE IN BC IT FOLLOWS THE ER DIAGRAM */
  FOREIGN KEY (Order_ID) REFERENCES Orders (ID),
  FOREIGN KEY (Dish_ID) REFERENCES Dishes (ID) 
);

CREATE TABLE Reviews (
  ID          INT           NOT NULL AUTO_INCREMENT,
  Customer_ID INT           NOT NULL,
  Restaurant_ID INT         NOT NULL,
  Waiter_ID   INT           NOT NULL,
  Service_Rating INT        NOT NULL, -- 0 - 5
  Food_Rating INT           NOT NULL, -- 0 - 5
  Comments    VARCHAR(4096) DEFAULT NULL,
  PRIMARY KEY (ID), -- Missing word "KEY" here
  FOREIGN KEY (Customer_ID) REFERENCES Users (ID),
  FOREIGN KEY (Restaurant_ID) REFERENCES Restaurants (ID),
  FOREIGN KEY (Waiter_ID) REFERENCES Positions (ID)
  -- Note: Yep, I meant Users instead of Customers. Oops!
);
