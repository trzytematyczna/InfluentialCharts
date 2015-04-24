
delimiter $$
create function wrapup_wrong_comments() returns text
begin
declare varuser text;
declare vardate text;
declare var text;
DECLARE isdone INT;
declare cur1 cursor for select distinct date from count_slots;
DECLARE CONTINUE HANDLER FOR NOT FOUND SET isdone = 1;

#select email into varuser from repair_todo where done='f' limit 1;
#select email into varuser from employeeinfo limit 1;#repair_todo where done='f' limit 1
set varuser = 'harry.arora@enron.com';

set isdone = 0;
open cur1;

if varuser is null then return 666;
else
simple_loop: loop
	fetch cur1 into vardate;
     IF isdone>0 THEN
      LEAVE simple_loop;
    END IF;
    select wrong_comments_fun(vardate,varuser) into var;    
   
end loop;
close cur1;

#update repair_todo set done = 't' where email = varuser;
return varuser;
end if;

end
$$

delimiter ;