/*******************************************************************************
 * Copyright (c) 2009 Fabian Steeg. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * <p/>
 * Contributors: Fabian Steeg - initial API and implementation; see bug 277380
 *******************************************************************************/
package org.eclipse.zest.dot.test_data;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.GraphConnection;
import org.eclipse.zest.core.widgets.GraphNode;
import org.eclipse.zest.core.widgets.ZestStyles;
import org.eclipse.zest.layouts.LayoutStyles;
import org.eclipse.zest.layouts.algorithms.TreeLayoutAlgorithm;

/**
 * Zest graph sample input for the Zest-To-Dot transformation demonstrating
 * directed graph support.
 */
public class SimpleDigraph extends Graph {
    /**
     * {@link Graph#Graph(Composite, int)}
     * @param parent The parent
     * @param style The style bits
     */
    public SimpleDigraph(final Composite parent, final int style) {
        super(parent, style);

        /* Global settings, here we set the directed property: */
        setConnectionStyle(ZestStyles.CONNECTIONS_DIRECTED);
        setLayoutAlgorithm(new TreeLayoutAlgorithm(
                LayoutStyles.NO_LAYOUT_NODE_RESIZING), true);

        /* Nodes: */
        GraphNode n1 = new GraphNode(this, SWT.NONE, "1");
        GraphNode n2 = new GraphNode(this, SWT.NONE, "2");
        GraphNode n3 = new GraphNode(this, SWT.NONE, "3");

        /* Connection from n1 to n2: */
        new GraphConnection(this, SWT.NONE, n1, n2);

        /* Connection from n2 to n3: */
        new GraphConnection(this, SWT.NONE, n2, n3);

    }
    /**
     * Displays this graph in a shell.
     * @param args Not used
     */
    public static void main(final String[] args) {
        Display d = new Display();
        Shell shell = new Shell(d);
        shell.setText(SimpleDigraph.class.getSimpleName());
        shell.setLayout(new FillLayout());
        shell.setSize(200, 250);
        new SimpleDigraph(shell, SWT.NONE);
        shell.open();
        while (!shell.isDisposed()) {
            while (!d.readAndDispatch()) {
                d.sleep();
            }
        }
    }
}
