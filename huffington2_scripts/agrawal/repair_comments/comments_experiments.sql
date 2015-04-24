CREATE OR REPLACE FUNCTION comments_experiments() RETURNS INTEGER AS $$
declare
      counter integer;
BEGIN
counter = 0;
while counter < 100 loop
	perform comment_count_repair();
	counter=counter+1;
	end loop;
return counter; 
END;
$$ LANGUAGE plpgsql;
