package jchart;

import java.io.*; 
import java.sql.*; 

import org.gephi.project.io.SaveTask;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.ChartFactory; 
import org.jfree.chart.JFreeChart; 
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel; 
import org.jfree.chart.JFreeChart; 
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.CategoryDataset; 
import org.jfree.data.category.DefaultCategoryDataset; 
import org.jfree.ui.ApplicationFrame; 
import org.jfree.ui.RefineryUtilities; 

public class PageRankCharts
{
   public static void main( String[ ] args )throws Exception
   {
     
      
	  JFreeChart barchart = BarChart(
		  "PageRank of top 10 users Profile Rank - all data", 
		  "User ID", 
		  "Page Rank Score");
      int width = 700; /* Width of the image */
      int height = 370; /* Height of the image */ 
      saveToFile("PageRank_from_Top10_ProfileRank_All.jpg", barchart, width, height);
    
//      charts();
   }
   
   
   public static JFreeChart BarChart(String title, String category, String score) throws ClassNotFoundException, SQLException{
	      JFreeChart chart = ChartFactory.createBarChart(title, category, score, getDatasetTop10(),
	    		  PlotOrientation.VERTICAL, true, true, false);   
	      return chart;
   }
   
   private static void charts() throws ClassNotFoundException, SQLException, IOException {
	   DefaultCategoryDataset dataset = new DefaultCategoryDataset();
	   	Class.forName( "com.mysql.jdbc.Driver" );
	      Connection connect = DriverManager.getConnection(
	      "jdbc:mysql://localhost:3306/test" ,     
	      "root",     
	      "gozofewi");
	      
	      Statement statement = connect.createStatement( );
	      ResultSet resultSet = statement.executeQuery("select eid from EmployeeInfo order by profilerank, pagerank desc limit 10");
	      
	      while( resultSet.next( ) ) 
	      {
	    	  int eid =  Integer.parseInt(resultSet.getString("eid"));
		      DefaultCategoryDataset data = getDatasetOneUser(eid);
		      JFreeChart chart = ChartFactory.createLineChart("Slot score for User "+eid, 
					  " ", 
					  "Profile Rank Score",
					  data, 
		    		  PlotOrientation.VERTICAL, true, true, false);   
			  CategoryPlot plot = (CategoryPlot)chart.getPlot();
		      CategoryAxis xAxis = (CategoryAxis)plot.getDomainAxis();
		      xAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_90);
		      xAxis.setFixedDimension(4.0);
		      CategoryPlot categoryplot = (CategoryPlot)chart.getPlot(); 
		      LineAndShapeRenderer rend = (LineAndShapeRenderer)categoryplot.getRenderer();
		      rend.setShapesVisible(true);
		      
		      int width = 700; /* Width of the image */
		      int height = 370; /* Height of the image */ 
		      saveToFile("ProfileRank_User_"+eid+".jpg", chart, width, height);
	      }

   }
   private static DefaultCategoryDataset getDatasetOneUser(int eid) throws ClassNotFoundException, SQLException {
	   DefaultCategoryDataset dataset = new DefaultCategoryDataset();
	   	Class.forName( "com.mysql.jdbc.Driver" );
	      Connection connect = DriverManager.getConnection(
	      "jdbc:mysql://localhost:3306/test" ,     
	      "root",     
	      "gozofewi");
	      
	      Statement statement = connect.createStatement( );
	      ResultSet resultSet = statement.executeQuery("select profilerank, date from count_slots where eid ="+eid);

	      while( resultSet.next( ) ) 
	      {
	    	  dataset.addValue(
	        		  Double.parseDouble( resultSet.getString( "profilerank" )),
	        		  "ProfileRank",
	        		  resultSet.getString( "date" )
	         );
	      }
	   return dataset;
}
   
   private static DefaultCategoryDataset getDatasetUs() throws ClassNotFoundException, SQLException {
	   DefaultCategoryDataset dataset = new DefaultCategoryDataset();
	   	Class.forName( "com.mysql.jdbc.Driver" );
	      Connection connect = DriverManager.getConnection(
	      "jdbc:mysql://localhost:3306/test" ,     
	      "root",     
	      "gozofewi");
	      
	      Statement statement = connect.createStatement( );
	      ResultSet resultSet = statement.executeQuery("select profilerank, date from count_slots where eid in ("
		      		+ "select * from "
		      		+ "(select eid from EmployeeInfo order by profilerank desc limit 1)"
		      		+ "as x)" );

//	      DefaultCategoryDataset dataset = new DefaultCategoryDataset( );
	      double i = 1.0;
//	      resultSet.next();
	      while( resultSet.next( ) ) 
	      {
//	          dataset.setValue(
	    	  dataset.addValue(
	        		  Double.parseDouble( resultSet.getString( "profilerank" )),
//	        		  "asd",
	        		  "ProfileRank",
//	        		  resultSet.getString( "date" ),
	        		  resultSet.getString( "date" )
	         );
	      }
	   return dataset;
}
   private static XYDataset getXYDataset() throws ClassNotFoundException, SQLException {
	   XYSeries series = new XYSeries("Data1");
	      Class.forName( "com.mysql.jdbc.Driver" );
	      Connection connect = DriverManager.getConnection(
	      "jdbc:mysql://localhost:3306/test" ,     
	      "root",     
	      "gozofewi");
	      
	      Statement statement = connect.createStatement( );
	      ResultSet resultSet = statement.executeQuery("select profilerank, date from count_slots where eid in ("
	      		+ "select * from "
	      		+ "(select eid from EmployeeInfo order by profilerank desc limit 1)"
	      		+ "as x)" );

//	      DefaultCategoryDataset dataset = new DefaultCategoryDataset( );
	      double i = 1.0;
	      while( resultSet.next( ) ) 
	      {
//	          dataset.setValue(
	    	  series.add(
	    			  i++,
	        		  Double.parseDouble( resultSet.getString( "profilerank" ))
	         );
	      }
	      XYSeriesCollection dataset = new XYSeriesCollection();
	      dataset.addSeries(series);
	   return dataset;
}


public static JFreeChart XYChart(String title, String category, String score) throws ClassNotFoundException, SQLException{
	      JFreeChart chart = ChartFactory.createXYLineChart(
	    		  title, category, score,
	    		  getXYDataset(), 
	    		  PlotOrientation.VERTICAL, 
	    		  true, true, false); 
	      return chart;
   }
   
   
   public static DefaultCategoryDataset getDatasetTop10() throws SQLException, ClassNotFoundException{
	      /* Create MySQL Database Connection */
	      Class.forName( "com.mysql.jdbc.Driver" );
	      Connection connect = DriverManager.getConnection(
	      "jdbc:mysql://localhost:3306/test" ,     
	      "root",     
	      "gozofewi");
	      
	      Statement statement = connect.createStatement( );
	      ResultSet resultSet = statement.executeQuery("select eid, email, pagerank from EmployeeInfo order by profilerank desc limit 10" );

	      DefaultCategoryDataset dataset = new DefaultCategoryDataset( );
	      while( resultSet.next( ) ) 
	      {
	          dataset.setValue(
	        		  Double.parseDouble( resultSet.getString( "pagerank" )),
	        		  resultSet.getString( "pagerank" ),
	        		  resultSet.getString( "eid" )
	         );
	      }
	      
	      return dataset;
   
   }
   
   
	public static void saveToFile(String filename, JFreeChart chart, int width, int height) throws IOException{
	    File file = new File( filename);
	    ChartUtilities.saveChartAsJPEG( file, chart , width , height );
	}
}
   