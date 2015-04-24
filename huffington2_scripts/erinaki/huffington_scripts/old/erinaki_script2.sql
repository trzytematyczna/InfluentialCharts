CREATE OR REPLACE FUNCTION erinaki () RETURNS VARCHAR AS $$
declare
   --cur1 cursor is select A1, A2 from A;
   cur1 refcursor;
   varid integer;
   posts_ids integer;
   comments_count integer;
   posts_count integer;
   friends_count integer;
   cc integer;
   fc integer;
BEGIN
  --open cur1;
  
comments_count = 0;
friends_count = 0;
select id into varid from todo where erinaki_done = 'f' limit 1;
select count(id) into posts_count from posts where author_id = varid;
OPEN cur1 FOR execute('select id from posts where author_id = varid');
	loop
	  fetch cur1 into posts_ids;
		select count(id) into cc from comments where post_id = posts_ids;
		select count (distinct author_id) into fc from comments where post_id = posts_ids;
		comments_count = comments_count + cc;
		friends_count = friends_count + fc;
	end loop;

insert into erinaki_authors_stats values(varid, friends_count,comments_count,posts_count);
update todo set erinaki_done='t'; 
	
 end loop;
close cur1;
return "done";
END;
$$ LANGUAGE plpgsql