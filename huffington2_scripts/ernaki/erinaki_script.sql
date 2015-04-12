
begin;
select distinct author_id into todo from posts;
alter table todo add column erinaki_done character varying(10);
update todo set erinaki_done='f';

-- creation of erinaki_authors_stats (stats for algorithm)
CREATE TABLE erinaki_authors_stats
(
  id integer NOT NULL,
  friends_count integer,
  comments_count integer,
  posts_count integer,
  CONSTRAINT erinaki_authors_stats_pkey PRIMARY KEY (id)
)
commit;