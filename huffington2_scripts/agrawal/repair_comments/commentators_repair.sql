CREATE OR REPLACE FUNCTION commentators_repair() RETURNS INTEGER AS $$
declare
   varid integer;
   varauthor integer;
  
BEGIN
select id, author_id into varid, varauthor from copy_posts2 where agarwal_done=false limit 1;
if varid is null then return 666;
else 
update agarwal_posts_stats set inlinks_commentators = (select count(distinct author_id) from comments where author_id!=varauthor and post_id = varid) where author_id=varauthor and post_id = varid;
update copy_posts2 set agarwal_done=true where id = varid;
end if;
return varid; 
END;
$$ LANGUAGE plpgsql;
