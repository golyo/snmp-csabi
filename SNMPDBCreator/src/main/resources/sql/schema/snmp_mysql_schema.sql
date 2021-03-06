  CREATE TABLE ROLE
   (
        ROLE VARCHAR(20),
        PRIMARY KEY (ROLE)
   );

INSERT INTO ROLE values ('ADMIN');
INSERT INTO ROLE values ('USER');

  CREATE TABLE USERS
   (	ID INT(11) AUTO_INCREMENT,
	NAME VARCHAR(255) NOT NULL,
	PASSWORD VARCHAR(255),
	EMAIL VARCHAR(255) NOT NULL,
      CONSTRAINT PK_USERS PRIMARY KEY (ID),
      CONSTRAINT UK_PLAYER_NAME UNIQUE (NAME)
   ) ;

  CREATE TABLE USERROLE
   (	USERID INT(11),
	ROLE VARCHAR(20),
      CONSTRAINT PK_USERROLE PRIMARY KEY (USERID, ROLE),
      CONSTRAINT FK_USERROLE_PLAYER FOREIGN KEY (USERID) REFERENCES USERS (ID) ON DELETE CASCADE,
      CONSTRAINT FK_USERROLE_ROLE FOREIGN KEY (ROLE) REFERENCES ROLE (ROLE)
   ) ;

  CREATE TABLE DEVICECONFIG
   (	CODE VARCHAR(20) NOT NULL,
	NAME VARCHAR(255),
        SNMPDESCRIPTOR MEDIUMTEXT NOT NULL,
        ACTIVE TINYINT(1) NOT NULL,
      CONSTRAINT PK_DEVICECONFIG PRIMARY KEY (CODE)
   ) ;

  CREATE TABLE DEVICE
   (	DEVICEID VARCHAR(20) NOT NULL,
        NODEID VARCHAR(20),
	IPADDRESS VARCHAR(20) NOT NULL,
	MACADDRESS VARCHAR(20),	
	CONFIGCODE VARCHAR(20) NOT NULL,
	CONFIGSTATE VARCHAR(20) NOT NULL,
        DEVICEMAP MEDIUMTEXT NOT NULL,
      CONSTRAINT PK_DEVICE PRIMARY KEY (DEVICEID),
      CONSTRAINT FK_DEVICE_DEVICECONFIG FOREIGN KEY (CONFIGCODE) REFERENCES DEVICECONFIG (CODE)
   ) ;

  CREATE TABLE DEVICECHANGE
   (	ID INT(11) AUTO_INCREMENT,
        DEVICEID VARCHAR(20) NOT NULL,
        SERVICE VARCHAR(20),
        STATEBEFORE VARCHAR(20),
        STATEAFTER VARCHAR(20),
        UPDATETIME DATETIME NOT NULL,
        USERNAME VARCHAR(20) NOT NULL,
        DESCRIPTION VARCHAR(255),
      CONSTRAINT PK_CHANGELOG PRIMARY KEY (ID),
      CONSTRAINT FK_CHANGELOG_DEVICEID FOREIGN KEY (DEVICEID) REFERENCES DEVICE (DEVICEID)
   ) ;

