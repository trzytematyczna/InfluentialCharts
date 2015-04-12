begin transaction;
set enable_seqscan to off;
select erinaki2();
commit;


while true; do psql -U postgres -d huffington2 -c "select erinaki2()"; done
