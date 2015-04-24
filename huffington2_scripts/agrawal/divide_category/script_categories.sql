update links_divided set category = 'photovideo' where link like '%.jpg' or link like '%.png' or link like '%.jpeg' or link like '%.gif' or link like '%.bmp'
update links_divided set category = domain_category.category where links_divided.domain = domain_category.domain;
update links_divided set category = 'unclassified' where category is ;

alter table copy_posts add column agarwal_done boolean;
update copy_posts set agarwal_done=false;

create table agarwal_posts_stats(
author_id int, 
post_id int , 
inlinks_links int, 
inlinks_linking_authors int, 
inlinks_commentators int, 
outlinks_opinions int, 
outlinks_news int,
outlinks_entertainment int,
outlinks_photovideo int,
outlinks_socialmedia int,
outlinks_others int,
outlinks_unclassified int, 
comments_count int, 
post_length int
);
