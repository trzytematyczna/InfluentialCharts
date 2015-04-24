CREATE OR REPLACE FUNCTION commentators_experiments() RETURNS INTEGER AS $$
declare
      counter integer;
BEGIN
counter = 0;
while counter < 100 loop
	perform commentators_repair();
	counter=counter+1;
	end loop;
return counter; 
END;
$$ LANGUAGE plpgsql;
