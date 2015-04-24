CREATE OR REPLACE FUNCTION divide_category() RETURNS INTEGER AS $$
declare
   varid integer;
   varauthor integer;
   varlinkcontent text;
   counter integer;
   content_len integer;
   linkcurrent text;
   linkdomain text;
   currentword_len integer;
   linkdomainfull text;
   rest text;
BEGIN

select id, author_id, link_from_content into varid, varauthor, varlinkcontent from copy_posts where linked_done = 'f';

content_len=0;
counter = 1;
select char_length(varlinkcontent) into content_len;
rest = varlinkcontent;
while counter < content_len loop
	select split_part(rest, ';', 1) into linkcurrent;
	select substring(linkcurrent from '.*://([^/]*)') into linkdomainfull;
	if linkdomainfull is null then
		select substring(linkcurrent from '(www.[^/]*)') into linkdomainfull;	
	end if;
	select substring(linkdomainfull from  '[^www.].*$') into linkdomain;
	
	select char_length(linkcurrent) into currentword_len;
	select substring(rest from currentword_len+2 for content_len-currentword_len) into rest; --+2?
	
	insert into links_divided values(varid, linkcurrent, linkdomain, varauthor);

	counter = counter+currentword_len+1;
end loop;

update copy_posts set linked_done='t' where id = varid;

return varid; 
END;
$$ LANGUAGE plpgsql;
