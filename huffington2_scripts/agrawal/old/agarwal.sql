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
   linkcontent_len integer;
   currentword text;
   currentword_len integer;
   rest text;
BEGIN

select id, author_id, link, content, link_from_content into varid, varauthor, varlink, varcontent, varlinkcontent from todo_posts where done = 'f' limit 1;

--post length -done
select char_lenght(varcontent) into post_len;

--comments -done
select count(id) into comm_count from comments where id = varid;

--outlinks 
select char_length(varlinkcontent) into linkcontent_len;
counter = 1;
outlinks_articles = 0;
outlinks_news = 0;
outlinks_photo_video = 0;
rest = varlinkcontent;
while counter < linkcontent_len loop
	select split_part(rest, ';', 1) into currentword;
	select char_length(currentword) into currentword_len;
	select substring(rest from currentword_len+2 for linkcontent_len-currentword_len) into rest;
	if (select currentword like '%msnbc.msn.com%') is not null then --huffington
		outlinks_articles = outlinks_articles + 1;
	end if;
	if (select currentword like '%msnbc.msn.com%') is not null then -- news
		outlinks_news = outlinks_news + 1;
	end if;
	if (select currentword like '%msnbc.msn.com%') is not null then -- photo/video
		outlinks_photo_video = outlinks_photo_video + 1;
	end if;
	if (select currentword like '%msnbc.msn.com%') is not null then -- photo/video
		outlinks_others = outlinks_others + 1;
	end if;	
	counter = counter+currentword_len+1;
end loop;

--inlinks - artykuly linkujace -done
select count(link_from_content) into inlinks_links from posts where author_id!=varauthor and link_from_content like '%'||varlink||'%';

--inlinks - autorzy komentujacy cale drzewo komentarzy -done
select count(distinct author_id) into inlinks_users from comments where author_id!=varauthor and post_id = varid;

insert into agarwal_posts_stats values(varauthor, varid, post_len,comm_count,inlinks_links, inlinks_users,outlinks_articles, outlinks_news,outlinks_photo_video,outlinks_others );
update todo_posts set done='t' where id = varid; 
update todo set agraval_done='t' where id = varauthor; 
return varid; 
END;
$$ LANGUAGE plpgsql;
