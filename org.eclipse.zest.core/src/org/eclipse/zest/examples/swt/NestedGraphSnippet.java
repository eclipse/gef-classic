package org.eclipse.mylyn.zest.examples.swt;

import org.eclipse.mylyn.zest.core.widgets.Graph;
import org.eclipse.mylyn.zest.core.widgets.GraphConnection;
import org.eclipse.mylyn.zest.core.widgets.GraphContainer;
import org.eclipse.mylyn.zest.core.widgets.GraphNode;
import org.eclipse.mylyn.zest.core.widgets.ZestStyles;
import org.eclipse.mylyn.zest.layouts.LayoutAlgorithm;
import org.eclipse.mylyn.zest.layouts.LayoutStyles;
import org.eclipse.mylyn.zest.layouts.algorithms.CompositeLayoutAlgorithm;
import org.eclipse.mylyn.zest.layouts.algorithms.GridLayoutAlgorithm;
import org.eclipse.mylyn.zest.layouts.algorithms.HorizontalShift;
import org.eclipse.mylyn.zest.layouts.algorithms.RadialLayoutAlgorithm;
import org.eclipse.mylyn.zest.layouts.algorithms.TreeLayoutAlgorithm;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class NestedGraphSnippet {

	private static Image image1;
	private static Image classImage;

	public static void createContainer(Graph g) {
		GraphContainer a = new GraphContainer(g, SWT.NONE, "SomeClass.java", classImage);
		int r = (int) ((Math.random() * 3) + 1);
		populateContainer(a, g, r, true);
		for (int i = 0; i < 4; i++) {
			GraphContainer b = new GraphContainer(g, SWT.NONE, "SomeNestedClass.java", classImage);
			r = (int) ((Math.random() * 3) + 1);
			populateContainer(b, g, r, false);
			new GraphConnection(g, SWT.NONE, a, b);
			for (int j = 0; j < 4; j++) {
				GraphContainer c = new GraphContainer(g, SWT.NONE, "DefaultAction.java", classImage);
				r = (int) ((Math.random() * 3) + 1);
				populateContainer(c, g, r, true);
				new GraphConnection(g, SWT.NONE, b, c);
			}
		}
	}

	public static void populateContainer(GraphContainer c, Graph g, int number, boolean radial) {
		GraphNode a = new GraphNode(c, ZestStyles.NODES_FISHEYE | ZestStyles.NODES_HIDE_TEXT, "SomeClass.java", classImage);
		for (int i = 0; i < 4; i++) {
			GraphNode b = new GraphNode(c, ZestStyles.NODES_FISHEYE | ZestStyles.NODES_HIDE_TEXT, "SomeNestedClass.java", classImage);
			new GraphConnection(g, SWT.NONE, a, b);
			for (int j = 0; j < 4; j++) {
				GraphNode d = new GraphNode(c, ZestStyles.NODES_FISHEYE | ZestStyles.NODES_HIDE_TEXT, "DefaultAction.java", classImage);
				new GraphConnection(g, SWT.NONE, b, d);
				if (number > 2) {
					for (int k = 0; k < 4; k++) {
						GraphNode e = new GraphNode(c, ZestStyles.NODES_FISHEYE | ZestStyles.NODES_HIDE_TEXT, "LastAction(Hero).java", classImage);
						new GraphConnection(g, SWT.NONE, d, e);
						if (number > 3) {
							for (int l = 0; l < 4; l++) {
								GraphNode f = new GraphNode(c, ZestStyles.NODES_FISHEYE | ZestStyles.NODES_HIDE_TEXT, "LastAction(Hero).java", classImage);
								new GraphConnection(g, SWT.NONE, e, f);
							}
						}
					}
				}
			}
		}
		if (number == 1) {
			c.setScale(0.75);
		} else if (number == 2) {
			c.setScale(0.50);
		} else {
			c.setScale(0.25);
		}
		if (radial) {
			c.setLayoutAlgorithm(new RadialLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING), true);
		} else {
			c.setLayoutAlgorithm(new TreeLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING), true);
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// Create the shell
		Display d = new Display();

		image1 = new Image(Display.getDefault(), NestedGraphSnippet.class.getResourceAsStream("package_obj.gif"));
		classImage = new Image(Display.getDefault(), NestedGraphSnippet.class.getResourceAsStream("class_obj.gif"));

		Shell shell = new Shell(d);
		shell.setText("GraphSnippet1");
		shell.setLayout(new FillLayout());
		shell.setSize(1000, 800);

		Graph g = new Graph(shell, SWT.NONE);
		g.setNodeStyle(ZestStyles.NODES_HIGHLIGHT_ADJACENT);
		createContainer(g);

		/*
		GraphNode n1 = new GraphNode(g, SWT.NONE, "non container node", classImage);

		GraphContainer p1 = new GraphContainer(g, SWT.NONE, "A Container with Multiple \nLines", image1);
		GraphContainer p2 = new GraphContainer(g, SWT.NONE, "org.example.plugin", image1);
		p2.setText("org\nexample\nplugin that is really really really really really really long");
		GraphContainer p3 = new GraphContainer(g, SWT.NONE, "org.example.ui", image1);
		GraphContainer p4 = new GraphContainer(g, SWT.NONE, "org.example.action", image1);

		GraphNode a = new GraphNode(p1, ZestStyles.NODES_FISHEYE | ZestStyles.NODES_HIDE_TEXT, "SomeClass.java", classImage);
		for (int i = 0; i < 4; i++) {
			GraphNode b = new GraphNode(p1, ZestStyles.NODES_FISHEYE | ZestStyles.NODES_HIDE_TEXT, "SomeNestedClass.java", classImage);
			new GraphConnection(g, SWT.NONE, a, b);
			for (int j = 0; j < 4; j++) {
				GraphNode c = new GraphNode(p1, ZestStyles.NODES_FISHEYE | ZestStyles.NODES_HIDE_TEXT, "DefaultAction.java", classImage);
				new GraphConnection(g, SWT.NONE, b, c);
				for (int k = 0; k < 4; k++) {
					GraphNode e = new GraphNode(p1, ZestStyles.NODES_FISHEYE | ZestStyles.NODES_HIDE_TEXT, "LastAction(Hero).java", classImage);
					new GraphConnection(g, SWT.NONE, c, e);
				}
			}
		}
		p1.setScale(0.25);

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
		*/
		CompositeLayoutAlgorithm compositeLayoutAlgorithm = new CompositeLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING, new LayoutAlgorithm[] { new GridLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING), new HorizontalShift(LayoutStyles.NO_LAYOUT_NODE_RESIZING) });
		//g.setLayoutAlgorithm(new GridLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING), true);
		g.setLayoutAlgorithm(compositeLayoutAlgorithm, true);
/*
		CompositeLayoutAlgorithm compositeLayoutAlgorithm = new CompositeLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING, new LayoutAlgorithm[] { new TreeLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING), new HorizontalShift(LayoutStyles.NO_LAYOUT_NODE_RESIZING) });
		g.setLayoutAlgorithm(compositeLayoutAlgorithm, true);
		p1.setLayoutAlgorithm(new RadialLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING), true);
		p2.setLayoutAlgorithm(new RadialLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING), true);
		p3.setLayoutAlgorithm(new RadialLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING), true);
		*/

		shell.open();
		while (!shell.isDisposed()) {
			while (!d.readAndDispatch()) {
				d.sleep();
			}
		}
		image1.dispose();
	}
}
