CREATE TABLE states (
	name VARCHAR(100),
	PRIMARY KEY (name)
);


CREATE TABLE regions (
	state VARCHAR(100),
	name VARCHAR(100),
	PRIMARY KEY(name, state),
	FOREIGN KEY (state) REFERENCES states(name)
);


CREATE TABLE cities (
	state VARCHAR(100),
	region VARCHAR(100),
	name VARCHAR(100),
	PRIMARY KEY(name, region, state),
	FOREIGN KEY (region, state) REFERENCES regions(name, state)
);

CREATE TABLE USERS (
    ID INTEGER NOT NULL GENERATED BY DEFAULT AS IDENTITY,
    NAME VARCHAR(63) NOT NULL,
    SURNAME VARCHAR(63) NOT NULL,
    PASSWORD VARCHAR(32) NOT NULL,
    EMAIL VARCHAR(255) UNIQUE NOT NULL,
    TYPE CHAR DEFAULT 'u',
    PRIMARY KEY (ID)
);

CREATE TABLE RESTAURANTS (
    ID INTEGER NOT NULL GENERATED BY DEFAULT AS IDENTITY,
    NAME VARCHAR(255) NOT NULL,
    DESCRIPTION VARCHAR(4096),
    WEB_SITE_URL VARCHAR(255),
    
    REVIEW_COUNTER INT DEFAULT 0,
    GLOBAL_REVIEW DOUBLE DEFAULT 0,
    FOOD_REVIEW DOUBLE DEFAULT 0,
    ATMOSPHERE_REVIEW DOUBLE DEFAULT 0,
    SERVICE_REVIEW DOUBLE DEFAULT 0,
    VALUE_FOR_MONEY_REVIEW DOUBLE DEFAULT 0,
    
    ID_OWNER INTEGER,
    ID_CREATOR INTEGER NOT NULL,

    ADDRESS VARCHAR(255) NOT NULL,
    LATITUDE DOUBLE,
    LONGITUDE DOUBLE,
    
    MIN_PRICE INT,
    MAX_PRICE INT,

    state VARCHAR(100),
    region VARCHAR(100),
    city VARCHAR(100),


    CONSTRAINT PRICE_RANGE_CHECK CHECK ((MIN_PRICE IS NULL AND MAX_PRICE IS NOT NULL) OR (MIN_PRICE IS NOT NULL AND MAX_PRICE IS NULL) OR (MIN_PRICE < MAX_PRICE)),
    PRIMARY KEY (ID),
    FOREIGN KEY (ID_OWNER) REFERENCES USERS (ID),
    FOREIGN KEY (ID_CREATOR) REFERENCES USERS (ID),
    FOREIGN KEY(city, region, state) REFERENCES CITIES (name, region, state)

);


CREATE TABLE ORARIO (
	id INTEGER,
	giorno  VARCHAR(10),
	apertura TIME,
	chiusura TIME,
	PRIMARY KEY (ID , giorno, apertura, chiusura),
	FOREIGN KEY (ID) REFERENCES RESTAURANTS (ID)

);

CREATE TABLE PHOTOS (
    ID INTEGER NOT NULL GENERATED BY DEFAULT AS IDENTITY,
    NAME VARCHAR(25) NOT NULL,
    PATH VARCHAR(1024) NOT NULL UNIQUE,
    ID_RESTAURANT INTEGER NOT NULL,
    PRIMARY KEY (ID),
    FOREIGN KEY (ID_RESTAURANT) REFERENCES RESTAURANTS (ID)
);


CREATE TABLE REVIEWS (
	GLOBAL_VALUE INTEGER NOT NULL CONSTRAINT REVIEW_GLOBAL_VALUE_CHECK 
		      CHECK (GLOBAL_VALUE >= 1 AND GLOBAL_VALUE <= 5),
	FOOD INTEGER CONSTRAINT REVIEW_FOOD_CHECK 
		    CHECK (FOOD>= 1 AND FOOD <=5),
	ATMOSPHERE INTEGER CONSTRAINT REVIEW_ATMOSPHERE_CHECK 
		   CHECK (ATMOSPHERE >= 1 AND ATMOSPHERE <= 5),
	SERVICE INTEGER CONSTRAINT REVIEW_SERVICE_CHECK 
		       CHECK (SERVICE >= 1 AND SERVICE <= 5),
	VALUE_FOR_MONEY INTEGER CONSTRAINT REVIEW_VALUE_FOR_MONEY_CHECK 
		        CHECK (VALUE_FOR_MONEY >= 1 AND VALUE_FOR_MONEY <= 5),
	
	TITLE VARCHAR(25),
	DESCRIPTION VARCHAR(4096),
	DATE_CREATION TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	ID_RESTAURANT INTEGER NOT NULL,
	ID_CREATOR INTEGER NOT NULL,
	ID_PHOTO INTEGER,
	PRIMARY KEY (ID_RESTAURANT, ID_CREATOR, DATE_CREATION),
	FOREIGN KEY (ID_RESTAURANT) REFERENCES RESTAURANTS (ID),
	FOREIGN KEY (ID_CREATOR) REFERENCES USERS (ID),
	FOREIGN KEY (ID_PHOTO) REFERENCES PHOTOS (ID)
);



CREATE TABLE CUISINES (
    ID INTEGER NOT NULL GENERATED BY DEFAULT AS IDENTITY,
    NAME VARCHAR(32) UNIQUE NOT NULL,
    PRIMARY KEY(ID)
);



CREATE TABLE RESTAURANTS_CUISINES (
    ID_RESTAURANT INTEGER NOT NULL,
    ID_CUISINE INTEGER NOT NULL,
    PRIMARY KEY(ID_RESTAURANT,ID_CUISINE),
    FOREIGN KEY(ID_RESTAURANT) REFERENCES RESTAURANTS(ID),
    FOREIGN KEY(ID_CUISINE) REFERENCES CUISINES(ID)

);




CREATE TABLE TOKENS (
    ID INTEGER NOT NULL,
    TOKEN VARCHAR(1024) NOT NULL,
    EXPIRATION_TIME TIMESTAMP NOT NULL,
    
    PRIMARY KEY (TOKEN),
    FOREIGN KEY (ID) REFERENCES USERS(ID)
);


CREATE TRIGGER delete_old_tokens_trigger
    AFTER INSERT ON TOKENS
    DELETE FROM TOKENS WHERE expiration_time < CURRENT_TIMESTAMP;




CREATE TABLE names_term (
    id INT NOT NULL,
    
    term VARCHAR(255),

    PRIMARY KEY (id, term),
    FOREIGN KEY(id) REFERENCES RESTAURANTS(ID) ON DELETE CASCADE

);

CREATE TABLE NOTIFICATIONS_PHOTO (
	id_target INTEGER,
	id_photo INTEGER NOT NULL,
	
	PRIMARY KEY (id_photo),
	FOREIGN KEY (id_photo) REFERENCES PHOTOS(ID),
	FOREIGN KEY (id_target) REFERENCES USERS(ID)
);

CREATE TABLE NOTIFICATIONS_RESTAURANT (
	id_restaurant INTEGER NOT NULL,
	id_user INTEGER NOT NULL,
	
	PRIMARY KEY (id_restaurant, id_user),
	FOREIGN KEY (id_restaurant) REFERENCES restaurants(ID),
	FOREIGN KEY (id_user) REFERENCES USERS(ID)
);



CREATE INDEX name_term_index ON names_term (term);



