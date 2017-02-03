/* Group 2: MYSQL Resturant Database */

CREATE TABLE users (
	ID INT NOT NULL, 
	Email VARCHAR(255) NOT NULL, UNIQUE, 
	Password VARCHAR(255) NOT NULL,
	FirstName VARCHAR(255) NOT NULL,
	LastName VARCHAR(255) NOT NULL,
	DT_Created DATETIME NOT NULL DEFAULT 'NOW',
	DT_LastLogin DATETIME NOT NULL DEFAULT NULL,
	DT_Cancelled DATETIME NOT NULL DEFAULT NULL, 
	UNIQUE (Email)
	PRIMARY KEY (ID) 
)

CREATE TABLE roles (
	ID  INT, 
	Name VARCHAR(255) NOT NULL, 
	UNIQUE (Name) 
	PRIMARY KEY (ID)
}

CREATE TABLE restaurants (
	ID INT, 
	Name VARCHAR(255) NOT NULL,
	DT_Opened DATETIME NOT NULL DEFAULT 'NOW',
	DT_Closed DATETIME DEFAULT NULL,
	PRIMARY KEY (ID) 
)

CREATE TABLE positions (
	ID INT, 
	Employee_ID INT NOT NULL, /* Reference Users */
	Role_ID INT NOT NULL, 
	Restaurant_ID INT NOT NULL, /* Reference Restaurant */
	DT_Hired DATETIME DEFAULT 'NOW',
	DT_Unhired DATETIME DEFAULT NULL,
	PRIMARY KEY (Employee_ID, Restaurant_ID)
	FOREIGN KEY (Role_ID) REFERENCES roles (ID)
	FOREIGN KEY (Employee_ID) REFERENCES users (ID)
	FOREIGN KEY (Restaurant_ID) REFERENCES restaurants (ID) 
)

CREATE TABLE orders (
	ID INT,
	Waiter_ID INT NOT NULL,
	Cook_ID INT DEFAULT NULL,
	DT_Placed DATETIME NOT NULL,
	DT_Rejected DATETIME DEFAULT NULL,
	DT_Cancelled DATETIME DEFAULT NULL,
	DT_Cooked DATETIME DEFAULT NULL,
	DT_Payed DATETIME DEFAULT NULL,
	Discount INT NOT NULL DEFAULT '0',
	Payment INT NOT NULL DEFAULT '0',
	Instructions VARCHAR(255) DEFAULT NULL,
	PRIMARY KEY (ID)
	FOREIGN KEY (Waiter_ID, Cook_ID) REFERENCES positions (ID) /*which ID?*/
)

CREATE TABLE dishes (
	ID INT,
	Restaurant_ID INT NOT NULL,
	Price INT NOT NULL DEFAULT '0',
	Name VARCHAR(255) NOT NULL,
	Description TEXT DEFAULT NULL, /* TEXT ISNT BEING RECOGNIZED */
	DT_Deleted DATETIME
	PRIMARY KEY (ID)
	FOREIGN KEY (Restaurant_ID) REFERENCES restaurants (ID)
) 

CREATE TABLE order_dishes (
	ID INT,
	Order_ID INT NOT NULL, 
	Dish_ID INT NOT NULL,
	IsRejected BIT NOT NULL DEFAULT 'False',
	IsVoided BIT NOT NULL DEFAULT 'False',
	NotesFromKitchen VARCHAR(255) DEFAULT NULL,
	PRIMARY KEY (ID)
	/* WE ARE ADDING THESE IN BC IT FOLLOWS THE ER DIAGRAM*/
	FOREIGN KEY (Order_ID) REFERENCES orders (ID)
	FOREIGN KEY (Dish_ID) REFERENCES dishes (ID) 
)

CREATE TABLE reviews (
	ID INT, 
	Customer_ID INT NOT NULL,
	Restaurant_ID INT NOT NULL,
	Waiter_ID INT NOT NULL,
	Service_Rating INT NOT NULL, /* 0 - 5 */
	Food_Rating INT NOT NULL, /* 0 - 5 */
	Comments VARCHAR(4096) DEFAULT NULL,
	PRIMARY (ID)
	FOREIGN KEY (Customer_ID) REFERENCES users (ID) /* Customers as in users???? */  
	FOREIGN KEY (Restaurant_ID) REFERENCES restaurants (ID) 
	FOREIGN KEY (Waiter_ID) REFERENCES positions (ID) 
)






