CREATE OR REPLACE FUNCTION erinaki2 () RETURNS INTEGER AS $$
declare
   varid integer;
   posts_ids integer;
   comments_count integer;
   posts_count integer;
   friends_count integer;
   cc integer;
   fc integer;
BEGIN
comments_count = 0;
friends_count = 0;
select id into varid from todo where erinaki_done = 'f' limit 1;
select count(id) into posts_count from posts where author_id = varid;

select count(id) into comments_count from comments where post_id in (select id from posts where author_id = varid);

select count (distinct author_id) into friends_count from comments where post_id in (select id from posts where author_id = varid);

insert into erinaki_authors_stats values(varid, friends_count,comments_count,posts_count);
update todo set erinaki_done='t'; 
return varid; 
END;
$$ LANGUAGE plpgsql;
