package org.eclipse.graph.demo;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.graph.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.widgets.Button;

/**
 * @author Daniel Lee
 */
public abstract class AbstractGraphDemo extends AbstractExample {
	
static boolean buildPrime = false;

static String graphMethod;

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
			bends.add(new AbsoluteBendpoint(x, y + 40));
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
 * Returns an array of strings that represent the names of the methods which build
 * graphs for this graph demo
 * @return array of graph building method names 
 */
protected abstract String[] getGraphMethods();

/**
 * @see org.eclipse.graph.AbstractExample#run()
 */
protected void run() {
	Display d = Display.getDefault();
	Shell shell = new Shell(d);
	String appName = getClass().getName();
	appName = appName.substring(appName.lastIndexOf('.') + 1);
	
	Shell choiceShell = new Shell();
	choiceShell.setSize(200, 200);
	choiceShell.setLayoutData(new GridData(GridData.FILL_VERTICAL));
	
	choiceShell.setLayout(new GridLayout());
	
	final org.eclipse.swt.widgets.Label nodesLabel 
			= new org.eclipse.swt.widgets.Label(choiceShell, SWT.NONE);
	nodesLabel.setText("Graph");
	final Combo graphList = new Combo(choiceShell, SWT.DROP_DOWN);
	
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
			= new org.eclipse.swt.widgets.Label(choiceShell, SWT.NONE);
	seedLabel.setText("Build Prime Graph");
	final Button primeGraphButton = new Button(choiceShell, SWT.CHECK);
	
	primeGraphButton.addSelectionListener(new SelectionListener() {
		public void widgetSelected(SelectionEvent e) {
			buildPrime = !buildPrime;
			getFigureCanvas().setContents(getContents());
		}
		public void widgetDefaultSelected(SelectionEvent e) {
		}
	});

	shell.setText(appName);
	shell.setLayout(new FillLayout());
	setFigureCanvas(new FigureCanvas(shell));
	getFigureCanvas().setContents(getContents());
	shell.setSize(1100, 700);
	shell.setLocation(100, 100);
	shell.open();
	
	choiceShell.setText("Graph chooser");
	choiceShell.setLocation(0, 0);
	choiceShell.setVisible(true);
	choiceShell.pack();

	while (!shell.isDisposed())
		while (!d.readAndDispatch())
			d.sleep();
}

/**
 * Sets the name of the method to call to build the graph
 * @param method name of the method used to build the graph
 */
public static void setGraphMethod(String method) {
	graphMethod = method;
}

}
