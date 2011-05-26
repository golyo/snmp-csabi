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
   (	ID INT(11) AUTO_INCREMENT,
	CODE VARCHAR(20) NOT NULL,
	NAME VARCHAR(255),
        DEVICETYPE VARCHAR(20) NOT NULL,
        SNMPDESCRIPTOR MEDIUMTEXT NOT NULL,
        ACTIVE TINYINT(1) NOT NULL,
      CONSTRAINT PK_DEVICECONFIG PRIMARY KEY (ID),
      CONSTRAINT UK_DEVICECONFIG_CODE UNIQUE (CODE)
   ) ;

  CREATE TABLE DEVICE
   (	ID INT(11) AUTO_INCREMENT,
	IPADDRESS VARCHAR(20) NOT NULL,
	MACADDRESS VARCHAR(20),
	NODEID VARCHAR(20),
	CONFIGID INT(11),
      CONSTRAINT PK_DEVICE PRIMARY KEY (ID),
      CONSTRAINT UK_DEVICE_IPADDRESS UNIQUE (IPADDRESS),
      CONSTRAINT FK_DEVICE_DEVICECONFIG FOREIGN KEY (CONFIGID) REFERENCES DEVICECONFIG (ID)
   ) ;

    
  CREATE TABLE HISTORY
   (	ID INT(11) AUTO_INCREMENT,
        DEVICEID INT(11) NOT NULL,
        OLDCONFIGID INT(11),
        NEWCONFIGID INT(11),
        USERID INT(11),
        UPDATETIME DATETIME NOT NULL,
      CONSTRAINT PK_CHANGELOG PRIMARY KEY (ID),
      CONSTRAINT FK_CHANGELOG_OLDCONFIGID FOREIGN KEY (OLDCONFIGID) REFERENCES DEVICECONFIG (ID),
      CONSTRAINT FK_CHANGELOG_NEWCONFIGID FOREIGN KEY (NEWCONFIGID) REFERENCES DEVICECONFIG (ID),
      CONSTRAINT FK_CHANGELOG_USERID FOREIGN KEY (USERID) REFERENCES USERS (ID),
      CONSTRAINT FK_CHANGELOG_DEVICEID FOREIGN KEY (DEVICEID) REFERENCES DEVICE (ID)
   ) ;

