CREATE OR REPLACE FUNCTION divide_rest() RETURNS INTEGER AS $$
declare
   varid integer;
   varauthor integer;
   varlinkcontent text;
   linkdomain text;
   linkdomainfull text;
BEGIN

select post_id, author_id, link into varid, varauthor, varlinkcontent from links_divided where domain is null and bitly=true limit 1;


select substring(varlinkcontent from '.*://([^/]*)') into linkdomainfull;
if linkdomainfull is null then
	select substring(varlinkcontent from '(www.[^/]*)') into linkdomainfull;	
end if;
select substring(linkdomainfull from  '[^www.].*$') into linkdomain;
	
update links_divided set domain=linkdomain, bitly=false where post_id=varid and link=varlinkcontent and author_id=varauthor;


return varid; 
END;
$$ LANGUAGE plpgsql;

