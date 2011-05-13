drop user snmp_test cascade;
create user snmp_test identified by snmp;
grant all privileges to snmp_test;

drop user snmp cascade;
create user snmp identified by snmp;
grant all privileges to snmp;

grant select on sys.dba_pending_transactions to snmp;
grant select on sys.pending_trans$ to snmp;
grant select on sys.dba_2pc_pending to snmp;
grant execute on sys.dbms_system to snmp;
