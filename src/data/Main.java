package data;

public class Main {

	
	public static void main(String[] args){
		ImportData im  = new ImportData();
		im.go();
		PageRankSlots prs = new PageRankSlots();
		
		String[] years= {"1999","2000","2001","2002"};
		String[] months = {"-01%","-02%","-03%","-04%","-05%","-06%","-07%","-08%",
				"-09%","-10%","-11%","-12%"};

//		for(String y : years){
//			for(String m : months){
//				System.out.println(y+m);
//					prs.go(y+m);
//			}
//		}
	}
}
