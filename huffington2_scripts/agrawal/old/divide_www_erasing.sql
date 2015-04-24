CREATE OR REPLACE FUNCTION erase_www() RETURNS INTEGER AS $$
declare
   vardomain text;
   varauthor integer;
   varid integer;
   varlink text;
   linkdomain text;
   recordvar record;
   cur1 cursor for select domain, link, author_id, post_id  from copy_links_divided;
 
BEGIN
 for recordvar in cur1
 loop
   fetch cur1 into vardomain, varlink, varauthor, varid;
   select substring(vardomain from  '[^www.].*$') into linkdomain;
   update copy_links_divided set domain = linkdomain where link=varlink and author_id=varauthor and post_id=varid;
 end loop;

return varid; 
END;
$$ LANGUAGE plpgsql;
