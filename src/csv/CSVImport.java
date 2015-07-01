package csv;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.gephi.data.attributes.api.AttributeColumn;
import org.gephi.data.attributes.api.AttributeController;
import org.gephi.data.attributes.api.AttributeModel;
import org.gephi.data.attributes.api.AttributeType;
import org.gephi.datalab.api.AttributeColumnsController;
import org.gephi.datalab.impl.AttributeColumnsControllerImpl;
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


public class CSVImport {

	public void go(String filepath, String outpath) throws IOException{
		
		ProjectController pc = Lookup.getDefault().lookup(ProjectController.class);
		pc.newProject();
		Workspace workspace = pc.getCurrentWorkspace();

		ImportController importController = Lookup.getDefault().lookup(ImportController.class);
		GraphModel graphModel = Lookup.getDefault().lookup(GraphController.class).getModel();
		AttributeModel attributeModel = Lookup.getDefault().lookup(AttributeController.class).getModel();
		
		File file = new File(filepath);
		AttributeColumnsControllerImpl acci = new AttributeColumnsControllerImpl();
		String[] s = {"Source","Target"};
		java.nio.charset.Charset.defaultCharset();
		String coma = ",";
		coma.toCharArray();
		Character c = coma.charAt(0);
		AttributeType[] type = {AttributeType.INT,AttributeType.INT};
		acci.importCSVToEdgesTable(file, c,java.nio.charset.Charset.defaultCharset(),
				s, type, true);
		Container container = importController.importFile(file);
		container.setAllowAutoNode(true);      //create missing nodes
		container.getLoader().setEdgeDefault(EdgeDefault.DIRECTED);   

		//Append imported data to GraphAPI
		importController.process(container, new DefaultProcessor(), workspace);

		//See if graph is well imported
		DirectedGraph graph = graphModel.getDirectedGraph();
		
		System.out.println("Nodes: " + graph.getNodeCount());
		System.out.println("Edges: " + graph.getEdgeCount());

		PageRank pagerank = new PageRank();
		pagerank.setProbability(0.85);
		pagerank.setDirected(true);
		pagerank.execute(graphModel, attributeModel);
		AttributeColumn col = attributeModel.getNodeTable().getColumn(PageRank.PAGERANK);

		FileWriter writer = new FileWriter(outpath);
		for (Node nodes : graph.getNodes().toArray()) {
			String id = nodes.getNodeData().getId();
			Double pgrank = (Double)nodes.getNodeData().getAttributes().getValue(col.getIndex());
			writer.write(id+","+pgrank+"\n");
//			System.out.println(id+","+pgrank);
		}
		writer.flush();
	    writer.close();
	}
}
