package org.eclipse.draw2d.examples.graph;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.graph.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.widgets.Button;

/**
 * @author Daniel Lee
 */
public abstract class AbstractGraphDemo {

/** Indicates whether the prime graph should be built */
protected static boolean buildPrime = false;
/** Contents of the demo */
protected IFigure contents;
/** Name of graph test method to run */
protected static String graphMethod;
/** Demo shell */
protected Shell shell;

private FigureCanvas fc;

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
			p = getOwner().getBounds().getBottom();
		getOwner().translateToAbsolute(p);
		return p;
	}
}

static class LeftOrRightAnchor extends ChopboxAnchor {
	public LeftOrRightAnchor(IFigure owner) {
		super(owner);
	}
	public Point getLocation(Point reference) {
		Point p;
		p = getOwner().getBounds().getCenter();
		getOwner().translateToAbsolute(p);
		if (reference.x < p.x)
			p = getOwner().getBounds().getLeft();
		else
			p = getOwner().getBounds().getRight();
		getOwner().translateToAbsolute(p);
		return p;
	}
}

/**
 * Builds a figure for the given edge and adds it to contents
 * @param contents the parent figure to add the edge to
 * @param edge the edge
 */
static void buildEdgeFigure(Figure contents, Edge edge) {
	PolylineConnection conn = connection(edge);
	conn.setForegroundColor(ColorConstants.gray);
	PolygonDecoration dec = new PolygonDecoration();
	conn.setTargetDecoration(dec);
	Node s = edge.source;
	Node t = edge.target;
		
	conn.setSourceAnchor(new XYAnchor(edge.start));
	conn.setTargetAnchor(new XYAnchor(edge.end));
	conn.setConnectionRouter(null);
	NodeList nodes = edge.vNodes;
	if (nodes != null) {
			List bends = new ArrayList();
			conn.setConnectionRouter(new BendpointConnectionRouter());
			for (int i = 0; i < nodes.size(); i++) {
				Node vn = nodes.getNode(i);
				int x = vn.x;
				int y = vn.y;
				if (edge.isFeedback) {
					bends.add(new AbsoluteBendpoint(x, y + vn.height));
					bends.add(new AbsoluteBendpoint(x, y));

				} else {
					bends.add(new AbsoluteBendpoint(x, y));
					bends.add(new AbsoluteBendpoint(x, y + vn.height));
				}
			}
		conn.setRoutingConstraint(bends);
	}	
	contents.add(conn);
}

/**
 * Builds a Figure for the given node and adds it to contents
 * @param contents the parent Figure to add the node to
 * @param node the node to add
 */
static void buildNodeFigure(Figure contents, Node node) {
	Label label;
	label = new Label();
	label.setBackgroundColor(ColorConstants.lightGray);
	label.setOpaque(true);
	label.setBorder(new LineBorder());
	if (node.incoming.isEmpty())
		label.setBorder(new LineBorder(2));
	String text = node.data.toString();// + "(" + node.index +","+node.sortValue+ ")";
	label.setText(text);
	node.data = label;
	contents.add(label, new Rectangle(node.x, node.y, node.width, node.height));
}

/**
 * Builds a Figure for the given prime edge
 * @param e the prime edge
 * @return the Figure for the prime edge
 */
static PolylineConnection buildPrimeEdge(Edge e) {
	PolylineConnection line = new PolylineConnection();
	
	if (e.tree) {
		PolygonDecoration dec = new PolygonDecoration();
		dec.setLineWidth(2);
	
		line.setLineWidth(3);
		Label l = new Label (e.cut + "");
		l.setOpaque(true);
		line.add(l, new ConnectionLocator(line));
	} else {
		line.setLineStyle(Graphics.LINE_DOT);
		Label l = new Label (e.getSlack() + "");
		l.setOpaque(true);
		line.add(l, new ConnectionLocator(line));
	}
	return line;
}

/**
 * Builds a connection for the given edge
 * @param e the edge
 * @return the connection
 */
static PolylineConnection connection(Edge e) {
	PolylineConnection conn = new PolylineConnection();
	conn.setConnectionRouter(new BendpointConnectionRouter());
	List bends = new ArrayList();
	NodeList nodes = e.vNodes;
	if (nodes != null) {
		for (int i = 0; i < nodes.size(); i++) {
			Node n = nodes.getNode(i);
			int x = n.x;
			int y = n.y;
			bends.add(new AbsoluteBendpoint(x, y));
			bends.add(new AbsoluteBendpoint(x, y + n.height));
		}
	}
	conn.setRoutingConstraint(bends);
	return conn;
}

/**
 * @see org.eclipse.graph.AbstractExample#getContents()
 */
protected IFigure getContents() {
	return null;
}

/**
 * Returns the FigureCanvas
 * @return this demo's FigureCanvas
 */
protected FigureCanvas getFigureCanvas() {
	return fc;
}

/**
 * Returns an array of strings that represent the names of the methods which build
 * graphs for this graph demo
 * @return array of graph building method names 
 */
protected abstract String[] getGraphMethods();


/**
 * @see org.eclipse.graph.AbstractExample#hookShell()
 */
protected void hookShell() {
	Composite composite = new Composite(shell, 0);
	composite.setLayoutData(new GridData(GridData.FILL_VERTICAL));
	
	composite.setLayout(new GridLayout());	
	final org.eclipse.swt.widgets.Label nodesLabel 
			= new org.eclipse.swt.widgets.Label(composite, SWT.NONE);
	nodesLabel.setText("Graph");
	final Combo graphList = new Combo(composite, SWT.DROP_DOWN);
	
	String[] graphMethods = getGraphMethods();
	for (int i = 0; i < graphMethods.length; i++) {
		if (graphMethods[i] != null)
			graphList.add(graphMethods[i]);
	}
	setGraphMethod(graphMethods[0]);
	graphList.setText(graphMethod);
	graphList.addSelectionListener(new SelectionListener() {
		public void widgetSelected(SelectionEvent e) {
			setGraphMethod(graphList.getItem(graphList.getSelectionIndex()));
			getFigureCanvas().setContents(getContents());
		}
		public void widgetDefaultSelected(SelectionEvent e) {
			graphList.setText(graphMethod);
		}
	});
	
	final org.eclipse.swt.widgets.Label seedLabel 
			= new org.eclipse.swt.widgets.Label(composite, SWT.NONE);
	seedLabel.setText("Build Prime Graph");
	final Button primeGraphButton = new Button(composite, SWT.CHECK);
	
	primeGraphButton.addSelectionListener(new SelectionListener() {
		public void widgetSelected(SelectionEvent e) {
			buildPrime = !buildPrime;
			getFigureCanvas().setContents(getContents());
		}
		public void widgetDefaultSelected(SelectionEvent e) {
		}
	});
}

/**
 * Runs the demo.
 */
protected void run() {
	Display d = Display.getDefault();
	shell = new Shell(d);
	String appName = getClass().getName();
	appName = appName.substring(appName.lastIndexOf('.') + 1);
	hookShell();
	shell.setText(appName);
	shell.setLayout(new GridLayout(2, false));
	setFigureCanvas(new FigureCanvas(shell));
	getFigureCanvas().setContents(contents = getContents());
	getFigureCanvas().getViewport().setContentsTracksHeight(true);
	getFigureCanvas().getViewport().setContentsTracksWidth(true);
	getFigureCanvas().setLayoutData(new GridData(GridData.FILL_BOTH));
	shell.setSize(1100, 700);
	shell.open();
	while (!shell.isDisposed())
		while (!d.readAndDispatch())
			d.sleep();
}

/**
 * Sets this demo's FigureCanvas
 * @param canvas this demo's FigureCanvas
 */
protected void setFigureCanvas(FigureCanvas canvas) {
	this.fc = canvas;
}

/**
 * Sets the name of the method to call to build the graph
 * @param method name of the method used to build the graph
 */
public static void setGraphMethod(String method) {
	graphMethod = method;
}

}
