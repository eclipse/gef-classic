package org.eclipse.graph.demo;

import java.util.*;
import java.util.List;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.graph.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.widgets.Button;

/**
 * @author delee
 */
public class RandomGraphDemo extends AbstractExample {

static int HEIGHT = 100;
public Random random = new Random(0);
public int numberOfRows = 10;
public int numberOfNodes = 10;

public static void main(String[] args) {
	final RandomGraphDemo demo = new RandomGraphDemo();
	Shell choiceShell = new Shell();
	choiceShell.setSize(200,200);
	choiceShell.setLayoutData(new GridData(GridData.FILL_VERTICAL));
	
	choiceShell.setLayout(new GridLayout());
	
	
	final org.eclipse.swt.widgets.Label rowLabel 
			= new org.eclipse.swt.widgets.Label(choiceShell, SWT.NONE);
	rowLabel.setText("Maximum Rank");
	final Text rowsText = new Text(choiceShell, SWT.BORDER);
	
	final org.eclipse.swt.widgets.Label nodesLabel 
			= new org.eclipse.swt.widgets.Label(choiceShell, SWT.NONE);
	nodesLabel.setText("Number of Nodes");
	final Text nodesText = new Text(choiceShell, SWT.BORDER);
	
	final org.eclipse.swt.widgets.Label seedLabel 
			= new org.eclipse.swt.widgets.Label(choiceShell, SWT.NONE);
	seedLabel.setText("Random seed");
	final Text seedText = new Text(choiceShell, SWT.BORDER);
	seedText.setSize(50,50);
	
	Button button = new Button(choiceShell,SWT.PUSH);
	button.setText("Generate");
	button.addSelectionListener(new SelectionListener() {
		public void widgetSelected(SelectionEvent e) {
			try {
				int rows = Integer.parseInt(rowsText.getText());
				if (rows > 1)
					demo.numberOfRows = rows;
				else
					rowsText.setText(Integer.toString(demo.numberOfRows));
				
				int nodes = Integer.parseInt(nodesText.getText());
				if (nodes > 0)
					demo.numberOfNodes = nodes;
				else
					nodesText.setText(Integer.toString(demo.numberOfNodes));
				demo.random = new Random(Integer.parseInt(seedText.getText()));
				demo.getFigureCanvas().setContents(demo.getContents());
				}
			catch (NumberFormatException exception) {
			}
		}
		public void widgetDefaultSelected(SelectionEvent e) {
		}
	});
	choiceShell.setVisible(true);
	choiceShell.pack();
	rowsText.setText("10");
	nodesText.setText("10");
	seedText.setText("0");
	demo.run();
}

static public Figure buildGraph(DirectedGraph graph) {
	Figure contents = new Panel();
	contents.setFont(new Font(null, "Tahoma", 10, 0));
	contents.setBackgroundColor(ColorConstants.white);
	contents.setLayoutManager(new XYLayout());
	
	Label label;
	
//	for (int r = 0; r<graph.ranks.ranks.size(); r++) {
//		int x = 0;
//		Rank rank = graph.ranks.getRank(r);
//		for (int i = 0; i < rank.size(); i++) {
//			Node n = rank.getNode(i);
//			n.x = x;
//			x += n.width + 8;
//		}
//	}
	
	for (int i=0; i<graph.nodes.size(); i++) {
		Node node = graph.nodes.getNode(i);
		label = new Label();
		label.setBackgroundColor(ColorConstants.lightGray);
		label.setOpaque(true);
		label.setBorder(new LineBorder());
		if (node.incoming.isEmpty())
			label.setBorder(new LineBorder(2));
		String text = node.data.toString();// + "(" + node.min + ", " + node.max + ")";
		label.setText(text +" "+ node.sortValue);
		if (text.indexOf("Java") >= 0 || text.indexOf("JDI") >= 0)
			label.setBackgroundColor(ColorConstants.lightGreen);
		if (text.indexOf("Graph") >= 0
			|| text.indexOf("2d") >= 0
			|| text.indexOf("Dependen") >= 0
			|| text.indexOf("GEF")>= 0)
			label.setBackgroundColor(ColorConstants.cyan);
		node.data = label;
		contents.add(label,
			new Rectangle(node.x, node.rank * HEIGHT, node.width, 40));
	}
	
	for (int i=0; i<graph.edges.size(); i++) {
		Edge edge = graph.edges.getEdge(i);
		PolylineConnection conn = connection(edge);
		conn.setForegroundColor(ColorConstants.gray);
		conn.setLineWidth(2);
		if (edge.tree) {
			Label l = new Label ("(" + edge.cut + ")");
			l.setOpaque(true);
			//conn.add(l, new ConnectionLocator(conn));
			conn.setLineWidth(1);
//			PolygonDecoration dec = new PolygonDecoration();
//			dec.setLineWidth(3);
//			if (edge.head() == edge.target)
//				conn.setSourceDecoration(dec);
//			else
//				conn.setTargetDecoration(dec);
		} else {
//			conn.setLineStyle(Graphics.LINE_DASHDOT);
//			Label l = new Label (Integer.toString(edge.getSlack()));
//			l.setOpaque(true);
//			conn.add(l, new ConnectionEndpointLocator(conn, false));
		}
		Figure s = (Figure)edge.source.data;
		Figure t = (Figure)edge.target.data;
		conn.setSourceAnchor(new TopOrBottomAnchor(s));
		conn.setTargetAnchor(new TopOrBottomAnchor(t));
		contents.add(conn);
	}
//	buildPrimeGraph(graph.gPrime, contents);
	return contents;
}

static PolylineConnection connection(Edge e) {
	PolylineConnection conn = new PolylineConnection();
	conn.setConnectionRouter(new BendpointConnectionRouter());
	List bends = new ArrayList();
	NodeList nodes = e.vNodes;
	if (nodes != null) {
		for (int i=0; i<nodes.size(); i++){
			Node n = nodes.getNode(i);
			int x = n.x;
			int y = n.rank * HEIGHT;
			bends.add(new AbsoluteBendpoint(x+70, y));
			bends.add(new AbsoluteBendpoint(x+70, y+40));
		}
	}
	conn.setRoutingConstraint(bends);
	return conn;
}

private DirectedGraph generateRandomGraph() {
	DirectedGraph randomGraph = new DirectedGraph();
	
	// Randomly select number of nodes. 
	// This is a number between 10 and 50.
	//int numberOfNodes = random.nextInt(50);
	//numberOfNodes += 10;
	
	// Randomly select number of rows.
	// This is a number between 3 and 10
//	int numberOfRows = random.nextInt(10);
//	numberOfRows += 7;
	
	// Create the nodes
	for (int i = 0; i < numberOfNodes; i++) {
		Node newNode = new Node("Node " + i);
		newNode.width = 100;
		randomGraph.nodes.add(newNode);
	}
	
	// Assign each node to a random row
	for (int i = 0; i < numberOfNodes; i++) {
		((Node)(randomGraph.nodes.get(i))).rank = random.nextInt(numberOfRows);
	}
	
 	NodeList unreachableNodes = new NodeList();
 	unreachableNodes = randomGraph.nodes;
 	
	NodeList reachableNodes = new NodeList();
	EdgeList allEdges = new EdgeList();
	
	// Connect the graph
	while (unreachableNodes.size() > 0) {
		Node unreachableNode = unreachableNodes.getNode(random.nextInt(unreachableNodes.size()));
			if (reachableNodes.size() > 0) {
				Node randomNode = reachableNodes.getNode(random.nextInt(reachableNodes.size()));

				// If ranks are the same, re-pick to prohibit cycles
				while(randomNode.rank == unreachableNode.rank) {
					randomNode = reachableNodes.getNode(random.nextInt(reachableNodes.size()));
					unreachableNode = unreachableNodes.getNode(random.nextInt(unreachableNodes.size()));
				}
				
				Edge newEdge;
				if (randomNode.rank > unreachableNode.rank)
					newEdge = new Edge(unreachableNode, randomNode);
				else
					newEdge = new Edge(randomNode, unreachableNode);
				allEdges.add(newEdge);
			}
			findAllReachableNodesFrom(unreachableNode, reachableNodes, unreachableNodes);
	}
	
	randomGraph.nodes = reachableNodes;
	randomGraph.edges = allEdges;

	new InitialRankSolver()
		.visit(randomGraph);
	new TightSpanningTreeSolver()
		.visit(randomGraph);
	new RankAssigmentSolver()
		.visit(randomGraph);
	new PopulateRanks()
		.visit(randomGraph);
	new MinCross()
		.visit(randomGraph);
	new HorizontalPlacement()
		.visit(randomGraph);
		
	return randomGraph;
}

private void findAllReachableNodesFrom(Node root, NodeList reached, NodeList unreached) {
	reached.add(root);
	unreached.remove(root);
	EdgeList incoming = root.incoming;
	EdgeList outgoing = root.outgoing;
	
	for(int i = 0; i < incoming.size(); i++) {
		Node incomingNode = incoming.getEdge(i).source;
		if (!reached.contains(incomingNode))
			findAllReachableNodesFrom(incomingNode, reached, unreached);
	}
	
	for(int i = 0; i < outgoing.size(); i++) {
			Node outgoingNode = outgoing.getEdge(i).source;
			if (!reached.contains(outgoingNode))
				findAllReachableNodesFrom(outgoingNode, reached, unreached);
	}
	
	
}

/**
 * @see org.eclipse.graph.AbstractExample#getContents()
 */
protected IFigure getContents() {
	DirectedGraph graph = generateRandomGraph();
	Figure contents = buildGraph(graph);
	return contents;
}

private static void printGraphVisNodes(EdgeList edges) {
	for (int i=0; i < edges.size(); i++) {
		System.out.println("\"" + edges.getEdge(i).source.data + "\" -> \"" + 
												edges.getEdge(i).target.data + "\"");
	}
}

static class TopOrBottomAnchor extends ChopboxAnchor {
	public TopOrBottomAnchor(IFigure owner) {
		super(owner);
	}
	public Point getLocation(Point reference) {
		Point p;
		p = getOwner().getBounds().getCenter();
		getOwner().translateToAbsolute(p);
		if (reference.y < p.y)
			p = getOwner().getBounds().getTop();
		else
			p =getOwner().getBounds().getBottom();
		getOwner().translateToAbsolute(p);
		return p;
	}
}

}
