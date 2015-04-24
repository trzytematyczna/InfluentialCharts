CREATE OR REPLACE FUNCTION agarwal() RETURNS INTEGER AS $$
declare
   varid integer;
   varauthor integer;
   varlink text;
   varcontent text;
   post_len  integer; --#length
   comm_count integer; --#comments
   inlinks_links integer; --#inlinks linki
   inlinks_commentators integer; --#inlinks users
   inlinks_linking_authors integer;
   outlinks_opinions integer; --#outlinks
   outlinks_news integer;  --#outlinks
   outlinks_entertainment integer; --#outlinks
   outlinks_photovideo integer;  --#outlinks
   outlinks_socialmedia integer;
   outlinks_others integer;  --#outlinks
   outlinks_unclassified integer;  --#outlinks
BEGIN
select id, author_id, link, content into varid, varauthor, varlink, varcontent from copy_posts2 where agarwal_done=false limit 1;

--post length -done
select char_length(varcontent) into post_len;

--comments -done
select count(id) into comm_count from comments where post_id = varid;

--outlinks 
select count(category) into outlinks_opinions from links_divided where post_id=varid and category = 'opinions' group by category;
select count(category) into outlinks_news from links_divided where post_id=varid and category = 'news' group by category;
select count(category) into outlinks_entertainment from links_divided where post_id=varid and category = 'entertainment' group by category;
select count(category) into outlinks_photovideo from links_divided where post_id=varid and category = 'photovideo' group by category;
select count(category) into outlinks_socialmedia from links_divided where post_id=varid and category = 'socialmedia' group by category;
select count(category) into outlinks_others from links_divided where post_id=varid and category = 'others' group by category;
select count(category) into outlinks_unclassified from links_divided where post_id=varid and category = 'unclassified' group by category;
if outlinks_opinions is null then outlinks_opinions=0; end if;
if outlinks_news is null then outlinks_news = 0; end if;
if outlinks_entertainment is null then outlinks_entertainment = 0; end if;
if outlinks_photovideo is null then outlinks_photovideo = 0; end if;
if outlinks_socialmedia is null then outlinks_socialmedia = 0; end if;
if outlinks_others is null then outlinks_others = 0; end if;
if outlinks_unclassified is null then outlinks_unclassified = 0; end if;

--inlinks - artykuly linkujace -done
--select count(link_from_content) into inlinks_links from posts where author_id!=varauthor and link_from_content like '%'||varlink||'%';
select count(link) into inlinks_links from links_divided where author_id!=varauthor and link = varlink;

-- inlinks - users who linked ?
select count(distinct author_id) into inlinks_linking_authors from links_divided where author_id!=varauthor and link = varlink;

--inlinks - autorzy komentujacy cale drzewo komentarzy -done
select count(distinct author_id) into inlinks_commentators from comments where author_id!=varauthor and post_id = varid;

insert into agarwal_posts_stats values(varauthor, varid, inlinks_links, inlinks_linking_authors, inlinks_commentators, outlinks_opinions, outlinks_news,outlinks_entertainment,outlinks_photovideo,outlinks_socialmedia,outlinks_others,outlinks_unclassified, comm_count, post_len);

update copy_posts2 set agarwal_done=true where id = varid;

return varid; 
END;
$$ LANGUAGE plpgsql;
