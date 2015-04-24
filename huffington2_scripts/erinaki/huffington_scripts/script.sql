begin transaction;
set enable_seqscan to off;
select erinaki2();
commit;


postresql: while true; do psql -U postgres -d huffington2 -c "select erinaki2()"; done
mysql: while true; do mysql -h localhost -u root -pgozofewi test -e "select repair_erinaki()"; done

SET ENABLE_SEQSCAN TO OFF