CREATE OR REPLACE FUNCTION agrawal_outlinks_test() RETURNS INTEGER AS $$
declare
   varid integer;
   varauthor integer;
   varlink text;
   varcontent text;
   varlinkcontent text;
   posts_ids integer;
   comm_count integer;
   inlinks_links integer;
   inlinks_users integer;
   outlinks_articles integer;
   outlinks_news integer;
   outlinks_photo_video integer;
   link_content integer;
   counter integer;
   post_len  integer;
   content_len integer;
   currentword text;
   currentword_len integer;
   rest text;
BEGIN

--select id, author_id, link into varid, varauthor, varlink from todo_posts where agraval_done = 'f' limit 1;
select id, author_id, link, content, link_from_content into varid, varauthor, varlink, varcontent, varlinkcontent from posts where id = 48575;
--todo_posts where agraval_done = 'f' limit 1;
--post length -done
--select char_lenght(content) into post_len from posts where id = varid;
select char_length(varcontent) into post_len;

--comments -done
select count(id) into comm_count from comments where id = varid;

--outlinks -add categories to like 
content_len=0;
counter = 1;
outlinks_articles = 0;
select char_length(varlinkcontent) into content_len;
rest = varlinkcontent;
while counter < content_len loop
	select split_part(rest, ';', 1) into currentword;
	select char_length(currentword) into currentword_len;
	select substring(rest from currentword_len+2 for content_len-currentword_len) into rest; --+2?
	if (select currentword like '%msnbc.msn.com%') is not null then
		outlinks_articles = outlinks_articles + 1;
	end if;
	counter = counter+currentword_len+1;
end loop;


return outlinks_articles; 
END;
$$ LANGUAGE plpgsql;
