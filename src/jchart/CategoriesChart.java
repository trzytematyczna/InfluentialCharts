package jchart;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.*; 

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

public class CategoriesChart {
	 public static void main( String[ ] args )throws Exception
	   {
	     
	      one_chart();
	   }

	   private static void one_chart() throws ClassNotFoundException, SQLException, IOException {
//		   DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		   	Class.forName( "org.postgresql.Driver" );
		      Connection connect = DriverManager.getConnection(
		      "jdbc:postgresql://localhost:5432/huffington2" ,     
		      "postgres",     
		      "postgres");
		      
		      Statement statement = connect.createStatement( );
			      DefaultCategoryDataset data = getDataset();
			      JFreeChart chart = ChartFactory.createLineChart("Categories designated by domain with usage count", 
						  " ", 
						  "Usage count",
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
			      int height = 500; /* Height of the image */ 
			      saveToFile("Categories_Count_100.jpg", chart, width, height);

	   }  
	   private static DefaultCategoryDataset getDataset() throws ClassNotFoundException, SQLException {
		   DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		   	Class.forName( "org.postgresql.Driver" );
		      Connection connect = DriverManager.getConnection(
		      "jdbc:postgresql://localhost:5432/huffington2" ,     
		      "postgres",     
		      "postgres");
		      
		      Statement statement = connect.createStatement( );
		      ResultSet resultSet = statement.executeQuery("select count, lower from outlinks_categories_count order by count desc limit 100");
//		      resultSet.setFetchSize(65);

		      while( resultSet.next( ) ) 
		      {
		    	  dataset.addValue(
		        		  Double.parseDouble( resultSet.getString( "count" )),
		        		  "Domain count",
		        		  resultSet.getString( "lower" )
		         );
		      }
		   return dataset;
	}
	   
		public static void saveToFile(String filename, JFreeChart chart, int width, int height) throws IOException{
		    File file = new File( filename);
		    ChartUtilities.saveChartAsJPEG( file, chart , width , height );
		}
}
