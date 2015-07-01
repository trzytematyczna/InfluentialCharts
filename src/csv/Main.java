package csv;

import java.io.IOException;

public class Main {

	public static void main(String[] args) throws IOException{
		String path  ="C://Users//MZ//Documents//mra//agh//newfolder//wyniki//"
				+ "erinaki//huffington//pagerank_do_gephi//all_dataset";
//				+ "az//pagerank_do_gephi//comments";
				  
//		String name1 = "//pagerank_all_links.csv";
		String name2 = "//pagerank_all_links_comments.csv";
//		String out1 = "//out//pagerank_all_links.csv";
		String out2 = "//out//res_pagerank_all_links_comments.csv";

		
//		slots(61, 69, path+"//","comments_");
//		slots(path+"//links//","links_");
//		slots(62,65,path+"//links_comments//","links_comments_");
//		all(path,name1,out1);
		all(path,name2,out2);
		
	}
	
	public static void all(String path,String name, String out) throws IOException{
		
		CSVImport csvi;
		csvi = new CSVImport();
		csvi.go(path+name,path+out);
	}
	public static void slots(int start, int finish, String path, String c) throws IOException{
		
		CSVImport csvi;
		for (int i=start; i<=finish; i++){
			System.out.println(i);
			String name = c+"slot_"+i+".csv";
			String out = "//out//pagerank_slot"+i+".csv";

			csvi = new CSVImport();
			csvi.go(path+name,path+out);
		}
	}
}
