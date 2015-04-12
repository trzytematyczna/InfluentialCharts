CREATE OR REPLACE FUNCTION divide_experiments() RETURNS INTEGER AS $$
declare
      counter integer;
BEGIN
counter = 0;
while counter < 100 loop
	perform divide_category();
	counter=counter+1;
	end loop;
return counter; 
END;
$$ LANGUAGE plpgsql;
