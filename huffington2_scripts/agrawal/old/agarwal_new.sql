CREATE OR REPLACE FUNCTION agarwal () RETURNS INTEGER AS $$
declare
   varid integer;
   varauthor integer;
   varlink text;
   varcontent text;
   varlinkcontent text;
   post_len  integer; --#length
   comm_count integer; --#comments
   inlinks_links integer; --#inlinks linki
   inlinks_users integer; --#inlinks users
   outlinks_articles integer; --#outlinks
   outlinks_news integer;  --#outlinks
   outlinks_photo_video integer;  --#outlinks
   outlinks_others integer;  --#outlinks
   link_content integer;
   counter integer;
   category_count integer;
   varcategory text;
   linkcontent_len integer;
   currentword text;
   currentword_len integer;
   opinions_count integer;
   news_count integer;
   entertainment_count integer;
   photovideo_count integer;
   socialmedia_count integer;
   others_count integer;
   rest text;
   cur1 refcursor;
BEGIN

select id, author_id, link, content into varid, varauthor, varlink, varcontent from copy_posts where done = 'f' limit 1;

--select link info varlinkcontent from links_divided where post_id = varid and author_id = varauthor;
--post length -done
select char_length(varcontent) into post_len;

--comments -done
select count(id) into comm_count from comments where id = varid;


opinions_count = 0;
news_count = 0;
entertainment_count = 0;
photovideo_count = 0;
socialmedia_count = 0;
others_count = 0;
--outlinks 
open cur1 for select count(category), category from links_divided where post_id=varid group by category;
fetch cur1 into category_count, varcategory;
loop
	if category_count is null then 
		exit loop;
	if varcategory = 'opinions' then
		opinions_count = category_count;
	else if varcategory = 'news' then
		news_count = category_count;
	else if varcategory = 'entertainment' then
		entertainmnet_count = category_count;
	else if varcategory = 'socialmedia' then
		socialmedia_count = category_count;
	else if varcategory = 'photovideo' then
		photovideo = category_count
	else if varcategory = 'others' then
		others = category_count;
	end if;

end loop;

--inlinks - artykuly linkujace -done
select count(link_from_content) into inlinks_links from posts where author_id!=varauthor and link_from_content like '%'||varlink||'%';
--select count(link) into inlinks_links from links_divided where author_id!=varauthor and link = varlink;
-- inlinks - users who linked ?
--select count(distinct author_id) into inlinks_linking_authors from links_divided where author_id!=varauthor and link = varlink;

--inlinks - autorzy komentujacy cale drzewo komentarzy -done
select count(distinct author_id) into inlinks_users from comments where author_id!=varauthor and post_id = varid;

insert into agarwal_posts_stats values(varauthor, varid, post_len,comm_count,inlinks_links, inlinks_users,outlinks_articles, outlinks_news,outlinks_photo_video,outlinks_others );
update copy_posts set done='t' where id = varid; 

return varid; 
END;
$$ LANGUAGE plpgsql;
