CREATE OR REPLACE FUNCTION agarwal () RETURNS INTEGER AS $$
declare
   varid integer;
   posts_ids integer;
   comm_count integer;
   inlinks_links integer;
   inlinks_users integer;
   outlinks_articles integer;
   outlinks_news integer;
   outlinks_photo_video integer;
   post_len
   cc integer;
   fc integer;
BEGIN
comments_count = 0;
friends_count = 0;

select id into varid from todo where agraval_done = 'f' limit 1;

--post length
select char_lenght(contetn) into post_len from 
--comments -done
select comments_count into comm_count from erinaki_authors_stats where id = varid;

--outlinks -add categories to like 
select count(link_from_content) into inlinks_articles from posts where id in (select id from posts where author_id = varid) and link_from_content like '%%';
select count(link_from_content) into inlinks_news from posts where id in (select id from posts where author_id = varid) and link_from_content like '%%';
select count(link_from_content) into inlinks_photo_video from posts where id in (select id from posts where author_id = varid) and link_from_content like '%.jpg%';

--inlinks - artykuly linkujace
--select count(link_from_content) into inlinks_links from posts where author_id!=varid and link_from_content in (select link from posts where author_id = varid); --> lapie tylko takie ktore maja jedynie jeden link w contents

cursor_link execute 'select '%'|| link || '%' from posts where author_id = varid';
open cursor_link;
fetch cursor_link into links;
while loop;
select count(link_from_content) into inlinks_links from posts where author_id!=varid and link_from_content like cursor_link;
end loop;

--inlinks - autorzy komentujacy
select count(distinct author_id) into inlinks_users from comments where post_id in (select id from posts where author_id = varid);
select count(distinct author_id) into inlinks_users from comments where comment_id in (
	select id from comments where post_id in (select id from posts where author_id = varid
);

select count(id) into posts_count from posts where author_id = varid;
if posts_count > 0 then
	select sum(case when id>0 then 1 else 0 end), count(distinct author_id) into comments_count, friends_count  from comments where post_id in (select id from posts where author_id = varid);
end if;
insert into erinaki_authors_stats values(varid, friends_count,comments_count,posts_count);
update todo set erinaki_done='t' where id = varid; 
return varid; 
END;
$$ LANGUAGE plpgsql;
