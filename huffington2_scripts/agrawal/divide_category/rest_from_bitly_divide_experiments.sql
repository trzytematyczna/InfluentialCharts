CREATE OR REPLACE FUNCTION rest_divide_experiments() RETURNS INTEGER AS $$
declare
      counter integer;
BEGIN
counter = 0;
while counter < 100 loop
	perform divide_rest();
	counter=counter+1;
	end loop;
return counter; 
END;
$$ LANGUAGE plpgsql;
