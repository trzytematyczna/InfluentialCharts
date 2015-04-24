CREATE OR REPLACE FUNCTION loop_erase_www (num integer) RETURNS INTEGER AS $$
declare
   vardomain text;
   varauthor integer;
   varid integer;
   varlink text;
   linkdomain text;
   counter integer;
BEGIN
counter=1;
while counter < num loop
	select domain, link, author_id, post_id into vardomain, varlink, varauthor, varid from copy_links_divided where done = 'f' limit 1;
	select substring(vardomain from  '[^www.].*$') into linkdomain;
	update copy_links_divided set domain = linkdomain, done = 't' where link=varlink and author_id=varauthor and post_id=varid;
end loop;
	
return counter; 
END;
$$ LANGUAGE plpgsql;


update copy_links_divided set domain = substring(domain from  '[^www.].*$'), done = 't' where link='http://www.facebook.com/home.php' and author_id=776426 and post_id=775163;
