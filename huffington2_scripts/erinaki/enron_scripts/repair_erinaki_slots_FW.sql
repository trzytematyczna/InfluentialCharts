
delimiter $$
create function repair_erinaki_slots_FW(given_date text, varuser text) returns text
begin
declare posts_count int;
declare comments_count int;
declare responders_count int;
declare cc int;
declare rc int;
declare varsubject text;
declare vardate datetime;
DECLARE isdone INT;
declare cur1 cursor for select distinct(date), subject from message_info
where sender = varuser and re=false and date like concat(given_date,'%')
and subject not like 'FW:%';
DECLARE CONTINUE HANDLER FOR NOT FOUND SET isdone = 1;


#select email into varuser from repair_todo where done='f' limit 1;
#select email into varuser from employeeinfo limit 1;#repair_todo where done='f' limit 1

set posts_count = 0;
set comments_count = 0;
set responders_count = 0;
set cc = 0;
set rc = 0;		
set isdone = 0;

open cur1;
set posts_count = (select found_rows());

if varuser is null then return 666;
else
simple_loop: loop
	fetch cur1 into vardate, varsubject;
		IF isdone>0 THEN
		  LEAVE simple_loop;
		END IF;
    
		#rozni respondenci dla danego maila (czyli komentujacy dany watek)
		select count(distinct sender) into rc from message_info where recipient = varuser 
		and re=true and core_subject = varsubject and date > vardate;
		
		#liczba odpowiedzi dla danego watku
		select count(*) into cc from message_info where recipient = varuser 
		and re=true and core_subject = varsubject and date > vardate;
		
		set responders_count = responders_count + rc;
		set comments_count = comments_count + cc;
   
end loop;
close cur1;

update count_slots set posts = posts_count, comments = comments_count, responders = responders_count
where email = varuser and date = given_date;

return given_date;
end if;

end
$$

delimiter ;