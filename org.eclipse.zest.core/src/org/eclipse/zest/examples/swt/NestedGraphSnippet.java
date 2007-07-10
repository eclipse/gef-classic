package org.eclipse.mylyn.zest.examples.swt;

import org.eclipse.mylyn.zest.core.widgets.Graph;
import org.eclipse.mylyn.zest.core.widgets.GraphConnection;
import org.eclipse.mylyn.zest.core.widgets.GraphContainer;
import org.eclipse.mylyn.zest.core.widgets.GraphNode;
import org.eclipse.mylyn.zest.core.widgets.ZestStyles;
import org.eclipse.mylyn.zest.layouts.LayoutAlgorithm;
import org.eclipse.mylyn.zest.layouts.LayoutStyles;
import org.eclipse.mylyn.zest.layouts.algorithms.CompositeLayoutAlgorithm;
import org.eclipse.mylyn.zest.layouts.algorithms.HorizontalShift;
import org.eclipse.mylyn.zest.layouts.algorithms.RadialLayoutAlgorithm;
import org.eclipse.mylyn.zest.layouts.algorithms.TreeLayoutAlgorithm;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class NestedGraphSnippet {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// Create the shell
		Display d = new Display();

		Image image1 = new Image(Display.getDefault(), NestedGraphSnippet.class.getResourceAsStream("package_obj.gif"));
		Image classImage = new Image(Display.getDefault(), NestedGraphSnippet.class.getResourceAsStream("class_obj.gif"));

		Shell shell = new Shell(d);
		shell.setText("GraphSnippet1");
		shell.setLayout(new FillLayout());
		shell.setSize(800, 400);

		Graph g = new Graph(shell, SWT.NONE);
		g.setNodeStyle(ZestStyles.NODES_HIGHLIGHT_ADJACENT);

		GraphNode n1 = new GraphNode(g, SWT.NONE, "non container node", classImage);

		GraphContainer p1 = new GraphContainer(g, SWT.NONE, "A Container with\nMultiple Lines", image1);
		GraphContainer p2 = new GraphContainer(g, SWT.NONE, "org.example.plugin", image1);
		GraphContainer p3 = new GraphContainer(g, SWT.NONE, "org.example.ui", image1);
		GraphContainer p4 = new GraphContainer(g, SWT.NONE, "org.example.action", image1);

		GraphNode a = new GraphNode(p1, ZestStyles.NODES_FISHEYE | ZestStyles.NODES_HIDE_TEXT, "SomeClass.java", classImage);
		for (int i = 0; i < 4; i++) {
			GraphNode b = new GraphNode(p1, ZestStyles.NODES_FISHEYE | ZestStyles.NODES_HIDE_TEXT, "SomeNestedClass.java", classImage);
			new GraphConnection(g, SWT.NONE, a, b);
			for (int j = 0; j < 4; j++) {
				GraphNode c = new GraphNode(p1, ZestStyles.NODES_FISHEYE | ZestStyles.NODES_HIDE_TEXT, "DefaultAction.java", classImage);
				new GraphConnection(g, SWT.NONE, b, c);
			}
		}

		GraphNode aa = new GraphNode(p3, ZestStyles.NODES_FISHEYE | ZestStyles.NODES_HIDE_TEXT, "SomeClass.java", classImage);
		for (int i = 0; i < 4; i++) {
			GraphNode b = new GraphNode(p3, ZestStyles.NODES_FISHEYE | ZestStyles.NODES_HIDE_TEXT, "SomeNestedClass.java", classImage);
			new GraphConnection(g, SWT.NONE, aa, b);
			for (int j = 0; j < 4; j++) {
				GraphNode c = new GraphNode(p3, ZestStyles.NODES_FISHEYE | ZestStyles.NODES_HIDE_TEXT, "DefaultAction.java", classImage);
				new GraphConnection(g, SWT.NONE, b, c);
			}
		}
		GraphNode c6 = new GraphNode(p2, SWT.NONE, "Activator.java", classImage);

		new GraphConnection(g, SWT.NONE, n1, p1);
		new GraphConnection(g, SWT.NONE, n1, p3);
		new GraphConnection(g, SWT.NONE, n1, p4);
		new GraphConnection(g, SWT.NONE, n1, p2);
		new GraphConnection(g, SWT.NONE, p1, p3);
		new GraphConnection(g, SWT.NONE, p2, p4);

		new GraphConnection(g, SWT.NONE, c6, a);

		CompositeLayoutAlgorithm compositeLayoutAlgorithm = new CompositeLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING, new LayoutAlgorithm[] { new TreeLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING), new HorizontalShift(LayoutStyles.NO_LAYOUT_NODE_RESIZING) });
		g.setLayoutAlgorithm(compositeLayoutAlgorithm, true);
		p1.setLayoutAlgorithm(new RadialLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING), true);
		p2.setLayoutAlgorithm(new RadialLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING), true);
		p3.setLayoutAlgorithm(new RadialLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING), true);

		shell.open();
		while (!shell.isDisposed()) {
			while (!d.readAndDispatch()) {
				d.sleep();
			}
		}
		image1.dispose();
	}
}
