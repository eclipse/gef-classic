package org.eclipse.zest.tests.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.GraphConnection;
import org.eclipse.zest.core.widgets.GraphNode;
import org.eclipse.zest.layouts.LayoutEntity;
import org.eclipse.zest.layouts.algorithms.AbstractLayoutAlgorithm;
import org.eclipse.zest.layouts.dataStructures.InternalNode;
import org.eclipse.zest.layouts.dataStructures.InternalRelationship;

/**
 * This snippet shows how to create a custom layout. This layout simply lays the nodes out vertically
 * on the same Y-Axis as they currently have.  All the work is done in the applyLayoutInternal Method.
 * 
 * @author irbull
 *
 */
public class CustomLayout {
	
	public static void main(String[] args) {
		Display d = new Display();
		Shell shell = new Shell(d);
		shell.setText("Custom Layout Example");
		shell.setLayout(new FillLayout());
		shell.setSize(400, 400);

		Graph g = new Graph(shell, SWT.NONE);

		GraphNode n = new GraphNode(g, SWT.NONE, "Paper");
		GraphNode n2 = new GraphNode(g, SWT.NONE, "Rock");
		GraphNode n3 = new GraphNode(g, SWT.NONE, "Scissors");
		new GraphConnection(g, SWT.NONE, n, n2);
		new GraphConnection(g, SWT.NONE, n2, n3);
		new GraphConnection(g, SWT.NONE, n3, n);
		g.setLayoutAlgorithm(new AbstractLayoutAlgorithm(SWT.NONE) {

			
			private int totalSteps;
			private int currentStep;

			protected void applyLayoutInternal(InternalNode[] entitiesToLayout,
					InternalRelationship[] relationshipsToConsider, double boundsX, double boundsY, double boundsWidth,
					double boundsHeight) {
				
				totalSteps = entitiesToLayout.length;
				double distance = boundsWidth / totalSteps;
				int xLocation = 0;
			
				fireProgressStarted(totalSteps);
				
				for (currentStep = 0; currentStep < entitiesToLayout.length; currentStep++) {
					LayoutEntity layoutEntity = entitiesToLayout[currentStep].getLayoutEntity();
					layoutEntity.setLocationInLayout(xLocation, layoutEntity.getYInLayout());
					xLocation+= distance;
					fireProgressEvent(currentStep, totalSteps);
				}
				fireProgressEnded(totalSteps);
			}

			protected int getCurrentLayoutStep() {
				return 0;
			}

			protected int getTotalNumberOfLayoutSteps() {
				return totalSteps;
			}

			protected boolean isValidConfiguration(boolean asynchronous, boolean continuous) {
				return true;
			}

			protected void postLayoutAlgorithm(InternalNode[] entitiesToLayout,
					InternalRelationship[] relationshipsToConsider) {
				// Do nothing
			}

			protected void preLayoutAlgorithm(InternalNode[] entitiesToLayout,
					InternalRelationship[] relationshipsToConsider, double x, double y, double width, double height) {
				// do nothing
			}

			public void setLayoutArea(double x, double y, double width, double height) {
				// do nothing
			}
			
		}, true);

		shell.open();
		while (!shell.isDisposed()) {
			while (!d.readAndDispatch()) {
				d.sleep();
			}
		}
	}
}
