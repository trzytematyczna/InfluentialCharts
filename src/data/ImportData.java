package data;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.gephi.data.attributes.api.AttributeColumn;
import org.gephi.data.attributes.api.AttributeController;
import org.gephi.data.attributes.api.AttributeModel;
import org.gephi.graph.api.DirectedGraph;
import org.gephi.graph.api.GraphController;
import org.gephi.graph.api.GraphModel;
import org.gephi.graph.api.Node;
import org.gephi.graph.api.UndirectedGraph;
import org.gephi.io.database.drivers.MySQLDriver;
import org.gephi.io.database.drivers.SQLUtils;
import org.gephi.io.importer.api.Container;
import org.gephi.io.importer.api.EdgeDefault;
import org.gephi.io.importer.api.ImportController;
import org.gephi.io.importer.plugin.database.EdgeListDatabaseImpl;
import org.gephi.io.importer.plugin.database.ImporterEdgeList;
import org.gephi.io.processor.plugin.DefaultProcessor;
import org.gephi.layout.plugin.force.StepDisplacement;
import org.gephi.layout.plugin.force.yifanHu.YifanHuLayout;
import org.gephi.project.api.ProjectController;
import org.gephi.project.api.Workspace;
import org.gephi.statistics.plugin.PageRank;
import org.openide.util.Lookup;


public class ImportData {

	public void go(){
		
		ProjectController pc = Lookup.getDefault().lookup(ProjectController.class);
		pc.newProject();
		Workspace workspace = pc.getCurrentWorkspace();

		//Get controllers and models
		ImportController importController = Lookup.getDefault().lookup(ImportController.class);
		GraphModel graphModel = Lookup.getDefault().lookup(GraphController.class).getModel();
		AttributeModel attributeModel = Lookup.getDefault().lookup(AttributeController.class).getModel();

		//Import database
		EdgeListDatabaseImpl db = new EdgeListDatabaseImpl();
		db.setDBName("test");
		db.setHost("localhost");
		db.setUsername("root");
		db.setPasswd("gozofewi");
		db.setSQLDriver(new MySQLDriver());
		//db.setSQLDriver(new PostgreSQLDriver());
		//db.setSQLDriver(new SQLServerDriver());
		db.setPort(3306);
		db.setNodeQuery("SELECT employeeinfo.email  AS id, employeeinfo.eid as label FROM employeeinfo where profilerank>0");
		db.setEdgeQuery("SELECT sender_recipient.sender AS source, sender_recipient.rvalue AS target FROM sender_recipient");
		ImporterEdgeList edgeListImporter = new ImporterEdgeList();
		Container container = importController.importDatabase(db, edgeListImporter);
		container.setAllowAutoNode(true);      //Don't create missing nodes
		container.getLoader().setEdgeDefault(EdgeDefault.DIRECTED);   //Force UNDIRECTED

		//Append imported data to GraphAPI
		importController.process(container, new DefaultProcessor(), workspace);

		//See if graph is well imported
		DirectedGraph graph = graphModel.getDirectedGraph();
		
		System.out.println("Nodes: " + graph.getNodeCount());
		System.out.println("Edges: " + graph.getEdgeCount());

		PageRank pagerank = new PageRank();
		pagerank.setProbability(0.85);
		pagerank.setEpsilon(0.001);
		pagerank.setDirected(true);
		pagerank.execute(graphModel, attributeModel);
		AttributeColumn col = attributeModel.getNodeTable().getColumn(PageRank.PAGERANK);

		//Iterate over values
//		   Double max = 0.0;
//		for (Node n : graph.getNodes()) {
//		   
//			Double centrality5 = (Double)n.getNodeData().getAttributes().getValue(col.getIndex());
//		   
////			System.out.println(centrality5);
//			if(max < centrality5){
//				max = centrality5;
//			}
//		}
//		
//		System.out.println("max: "+max);
//		
//		//Layout - 100 Yifan Hu passes
//		YifanHuLayout layout = new YifanHuLayout(null, new StepDisplacement(1f));
//		layout.setGraphModel(graphModel);
//		layout.resetPropertiesValues();
//		layout.initAlgo();
//		for (int i = 0; i < 100 && layout.canAlgo(); i++) {
//		    layout.goAlgo();
//		}
//		layout.endAlgo();

		//Export X, Y position to the DB
		//Connect database
		String url = SQLUtils.getUrl(db.getSQLDriver(), db.getHost(), db.getPort(), db.getDBName());
		Connection connection = null;
		try {
		    //System.err.println("Try to connect at " + url);
		    connection = db.getSQLDriver().getConnection(url, db.getUsername(), db.getPasswd());
		    //System.err.println("Database connection established");
		} catch (SQLException ex) {
		    if (connection != null) {
		try {
		    connection.close();
		    System.err.println("Database connection terminated");
		} catch (Exception e) { /* ignore close errors */ }
		    }
		    System.err.println("Failed to connect at " + url);
		    ex.printStackTrace(System.err);
		}
		if (connection == null) {
		    System.err.println("Failed to connect at " + url);
		}

//		//Update
//		int count = 0;
//		for (Node nodes : graph.getNodes().toArray()) {
//		    String id = nodes.getNodeData().getId();
////		    float y = nodes.getNodeData().y();
//			Double pgrank = (Double)nodes.getNodeData().getAttributes().getValue(col.getIndex());
//
//		    String query = "UPDATE " + db.getDBName() + ".employeeinfo "
//		    		+ "SET pagerank = "+pgrank+" WHERE employeeinfo.email='" + id + "'";
//		    try {
//		        Statement s = connection.createStatement();
//		        count += s.executeUpdate(query);
//		        s.close();
//
//		    } catch (SQLException e) {
//		        System.err.println("Failed to update line node id = " + id);
//		    }
//		}
//		System.err.println(count + " rows were updated");

		//Close connection
		if (connection != null) {
		    try {
		        connection.close();
		        //System.err.println("Database connection terminated");
		    } catch (Exception e) { /* ignore close errors */ }
		}
	}
}
