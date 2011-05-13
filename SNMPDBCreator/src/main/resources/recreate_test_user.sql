drop user ${javax.persistence.jdbc.testuser} cascade;
create user ${javax.persistence.jdbc.testuser} identified by ${javax.persistence.jdbc.password};
grant all privileges to ${javax.persistence.jdbc.testuser};

