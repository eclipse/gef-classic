package org.eclipse.mylyn.zest.examples.swt;

import org.eclipse.mylyn.zest.core.widgets.Graph;
import org.eclipse.mylyn.zest.core.widgets.GraphConnection;
import org.eclipse.mylyn.zest.core.widgets.GraphContainer;
import org.eclipse.mylyn.zest.core.widgets.GraphNode;
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
		shell.setSize(400, 400);

		Graph g = new Graph(shell, SWT.NONE);

		GraphContainer p1 = new GraphContainer(g, SWT.NONE, ".more.more.more.more\n.more.\nmore.more.more", image1);
		GraphContainer p2 = new GraphContainer(g, SWT.NONE, "org.example.plugin.more.more.more.more", image1);
		GraphContainer p3 = new GraphContainer(g, SWT.NONE, "org.example.ui.more.more.more.more", image1);
		GraphContainer p4 = new GraphContainer(g, SWT.NONE, "org.example\n.views\n.more.\nmore.more.\nmore", image1);

		GraphNode c1 = new GraphNode(p1, SWT.NONE, "DefaultAction.java", classImage);
		GraphNode c2 = new GraphNode(p1, SWT.NONE, "UIAction.java", classImage);
		GraphNode c3 = new GraphNode(p1, SWT.NONE, "ToolBarAction.java", classImage);
		GraphNode c4 = new GraphNode(p1, SWT.NONE, "DeleteAction.java", classImage);
		GraphNode c5 = new GraphNode(p1, SWT.NONE, "CreateAction.java", classImage);

		new GraphConnection(g, SWT.NONE, c1, c2);
		new GraphConnection(g, SWT.NONE, c1, c3);
		new GraphConnection(g, SWT.NONE, c1, c4);
		new GraphConnection(g, SWT.NONE, c1, c5);

		GraphNode c6 = new GraphNode(p2, SWT.NONE, "Activator.java", classImage);
		new GraphConnection(g, SWT.NONE, c1, c6);

		new GraphConnection(g, SWT.NONE, c6, p3);
		new GraphConnection(g, SWT.NONE, c6, p4);
		new GraphConnection(g, SWT.NONE, p3, p4);

		//g.setLayoutAlgorithm(new GridLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING), true);

		shell.open();
		while (!shell.isDisposed()) {
			while (!d.readAndDispatch()) {
				d.sleep();
			}
		}
		image1.dispose();
	}
}
