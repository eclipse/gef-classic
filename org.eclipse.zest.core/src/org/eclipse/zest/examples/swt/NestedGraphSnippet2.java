package org.eclipse.mylyn.zest.examples.swt;

import org.eclipse.mylyn.zest.core.widgets.Graph;
import org.eclipse.mylyn.zest.core.widgets.GraphConnection;
import org.eclipse.mylyn.zest.core.widgets.GraphContainer;
import org.eclipse.mylyn.zest.core.widgets.GraphNode;
import org.eclipse.mylyn.zest.core.widgets.ZestStyles;
import org.eclipse.mylyn.zest.layouts.LayoutStyles;
import org.eclipse.mylyn.zest.layouts.algorithms.GridLayoutAlgorithm;
import org.eclipse.mylyn.zest.layouts.algorithms.SpringLayoutAlgorithm;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class NestedGraphSnippet2 {

	public static void main(String[] args) {
		// Create the shell
		Display d = new Display();
		Shell shell = new Shell(d);
		shell.setText("GraphSnippet1");
		shell.setLayout(new FillLayout());
		shell.setSize(400, 400);

		Graph g = new Graph(shell, SWT.NONE);

		/* Machines  */
		GraphContainer machine1 = new GraphContainer(g, SWT.NONE);
		machine1.setText("Machine 1 (prop:1)");
		GraphContainer machine2 = new GraphContainer(g, SWT.NONE);
		machine2.setText("Machine 2");
		GraphContainer machine3 = new GraphContainer(g, SWT.NONE);
		machine3.setText("Machine 3");

		/* Network */
		GraphConnection networkConnection = new GraphConnection(g, ZestStyles.CONNECTIONS_DIRECTED, machine1, machine2);
		networkConnection.setText("Network (bandwidth:1) ");
		new GraphConnection(g, SWT.NONE, machine2, machine3);

		/* Containers */
		GraphContainer container1 = new GraphContainer(machine1, SWT.NONE);
		container1.setText("Host 1");
		GraphContainer container2 = new GraphContainer(machine1, SWT.NONE);
		container2.setText("Host 2");

		GraphContainer container3 = new GraphContainer(machine2, SWT.NONE);
		container3.setText("Host 3");
		GraphContainer container4 = new GraphContainer(machine3, SWT.NONE);
		container4.setText("Host 4");

		/* Objects */
		GraphNode object1 = new GraphNode(container1, ZestStyles.NODES_FISHEYE, "JSP Object");
		GraphNode object2 = new GraphNode(container1, ZestStyles.NODES_FISHEYE, "JSP Object 2");
		GraphNode object3 = new GraphNode(container2, ZestStyles.NODES_FISHEYE, "JSP Object 3");
		GraphNode object4 = new GraphNode(container3, ZestStyles.NODES_FISHEYE, "JSP Object 4");
		GraphNode object5 = new GraphNode(container4, ZestStyles.NODES_FISHEYE, "JSP Object 5");

		/* Connections */
		new GraphConnection(g, ZestStyles.CONNECTIONS_DIRECTED, object1, object2);
		new GraphConnection(g, ZestStyles.CONNECTIONS_DIRECTED, object2, object3);
		new GraphConnection(g, ZestStyles.CONNECTIONS_DIRECTED, object3, object4);
		new GraphConnection(g, ZestStyles.CONNECTIONS_DIRECTED, object4, object5);

		container1.setLayoutAlgorithm(new GridLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING), true);
		container2.setLayoutAlgorithm(new GridLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING), true);
		container3.setLayoutAlgorithm(new GridLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING), true);
		container3.setLayoutAlgorithm(new GridLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING), true);
		g.setLayoutAlgorithm(new SpringLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING), true);

		shell.open();
		while (!shell.isDisposed()) {
			while (!d.readAndDispatch()) {
				d.sleep();
			}
		}
	}
}
