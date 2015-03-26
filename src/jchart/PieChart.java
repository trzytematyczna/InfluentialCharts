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

public class PieChart
{
   public static void main( String[ ] args )throws Exception
   {
     
      
	  JFreeChart barchart = BarChart(
		  "Top 10 users - all data", 
		  "User ID", 
		  "Profile Rank Score");
	  JFreeChart xychart = XYChart(
			  "yt", 
			  "User ID", 
			  "Profile Rank Score");
//	  XYPlot plot = xychart.getXYPlot();
//	  NumberAxis domainAxis = new NumberAxis("x");
//	  plot.setDomainAxis(domainAxis);	  
	  JFreeChart chart = ChartFactory.createLineChart("Slot score for User", 
			  " ", 
			  "Profile Rank Score",
			  getDatasetUs(), 
    		  PlotOrientation.VERTICAL, true, true, false);   
	  CategoryPlot plot = (CategoryPlot)chart.getPlot();
      CategoryAxis xAxis = (CategoryAxis)plot.getDomainAxis();
      xAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_90);
      xAxis.setFixedDimension(4.0);
      CategoryPlot categoryplot = (CategoryPlot)chart.getPlot(); 
      LineAndShapeRenderer rend = (LineAndShapeRenderer)categoryplot.getRenderer();
      rend.setShapesVisible(true);
      
//	  XYPlot plot = (XYPlot) xychart.getPlot();
//	  DateAxis axis = (DateAxis) plot.getDomainAxis();
	  
      int width = 700; /* Width of the image */
      int height = 370; /* Height of the image */ 
      saveToFile("ex1.jpg", chart, width, height);
   }
   
   
   public static JFreeChart BarChart(String title, String category, String score) throws ClassNotFoundException, SQLException{
	      JFreeChart chart = ChartFactory.createXYLineChart(title, category, score, getXYDataset(),
	    		  PlotOrientation.VERTICAL, true, true, false);   
	      return chart;
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
   
   
   public static DefaultCategoryDataset getDataset() throws SQLException, ClassNotFoundException{
	      /* Create MySQL Database Connection */
	      Class.forName( "com.mysql.jdbc.Driver" );
	      Connection connect = DriverManager.getConnection(
	      "jdbc:mysql://localhost:3306/test" ,     
	      "root",     
	      "gozofewi");
	      
	      Statement statement = connect.createStatement( );
	      ResultSet resultSet = statement.executeQuery("select eid, email, profilerank from EmployeeInfo order by profilerank desc limit 10" );

	      DefaultCategoryDataset dataset = new DefaultCategoryDataset( );
	      while( resultSet.next( ) ) 
	      {
	          dataset.setValue(
	        		  Double.parseDouble( resultSet.getString( "profilerank" )),
	        		  resultSet.getString( "profilerank" ),
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
   