CREATE OR REPLACE FUNCTION erinaki_experiments () RETURNS INTEGER AS $$
declare
      counter integer;
BEGIN
counter = 0;
while counter < 2 loop
	perform erinaki2();
	counter=counter+1;
	end loop;
return 1; 
END;
$$ LANGUAGE plpgsql;
