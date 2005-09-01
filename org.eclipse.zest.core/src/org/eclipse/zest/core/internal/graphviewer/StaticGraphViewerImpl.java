/*******************************************************************************
 * Copyright 2005, CHISEL Group, University of Victoria, Victoria, BC, Canada.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     The Chisel Group, University of Victoria
 *******************************************************************************/
package org.eclipse.mylar.zest.core.internal.graphviewer;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.mylar.zest.core.internal.gefx.GraphRootEditPart;
import org.eclipse.mylar.zest.core.internal.gefx.IPanningListener;
import org.eclipse.mylar.zest.core.internal.gefx.NonThreadedGraphicalViewer;
import org.eclipse.mylar.zest.core.internal.graphmodel.GraphModel;
import org.eclipse.mylar.zest.core.internal.graphmodel.IGraphModelFactory;
import org.eclipse.mylar.zest.core.internal.graphviewer.parts.GraphEditPartFactory;
import org.eclipse.mylar.zest.layouts.InvalidLayoutConfiguration;
import org.eclipse.mylar.zest.layouts.LayoutAlgorithm;
import org.eclipse.mylar.zest.layouts.LayoutStyles;
import org.eclipse.mylar.zest.layouts.algorithms.TreeLayoutAlgorithm;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.widgets.Composite;

/**
 * @author irbull
 */
public class StaticGraphViewerImpl extends NonThreadedGraphicalViewer implements IPanningListener {
	
	private LayoutAlgorithm layoutAlgorithm = null;
	private GraphModel model = null;
	private IGraphModelFactory modelFactory = null;
	private boolean allowMarqueeSelection;
	private boolean allowPanning;
	private int style = 0;
	
	public StaticGraphViewerImpl(Composite composite, int style) {
		super(composite);
		this.style = style;
	}
	
	
	/**
	 * Gets the style on the SpringGraphViewer
	 * @return
	 */
	public int getStyle( ) {
		return this.style;
	}
	
	/**
	 * Sets the model and initializes the layout algorithm.
	 * @see org.eclipse.mylar.zest.core.internal.gefx.ThreadedGraphicalViewer#setContents(java.lang.Object)
	 */
	public void setContents(GraphModel model, IGraphModelFactory modelFactory) { 
		super.setContents( model );
		Dimension d = this.getCanvasSize();
		this.model = model;
		this.modelFactory = modelFactory;
		
		layoutAlgorithm = new TreeLayoutAlgorithm(LayoutStyles.NO_LAYOUT_SIZE);
	
		try {
			layoutAlgorithm.applyLayout(model.getNodesArray(), model.getConnectionsArray(), 5.0, 5.0, d.width, d.height,false, false );
		} catch (InvalidLayoutConfiguration e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		this.addControlListener(new ControlListener() {
			public void controlMoved(ControlEvent e) { }
			public void controlResized(ControlEvent e) {
				// handle minimized case
				Dimension d = StaticGraphViewerImpl.this.getCanvasSize();
				if (d.isEmpty()) {
					//layoutAlgorithm.applyLayout(StaticGraphViewerImpl.this.model.getNodesArray(), StaticGraphViewerImpl.this.model.getConnectionsArray(), 5.0, 5.0, d.width, d.height,false, false );
				} else {
					try {
						layoutAlgorithm.applyLayout(StaticGraphViewerImpl.this.model.getNodesArray(), StaticGraphViewerImpl.this.model.getConnectionsArray(), 5.0, 5.0, d.width, d.height,false, false );
					} catch (InvalidLayoutConfiguration e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		});	
		
	}

	protected void configureGraphicalViewer() {
		GraphRootEditPart root = new GraphRootEditPart(this, allowMarqueeSelection, allowPanning);
		this.setRootEditPart(root);
		this.setEditPartFactory(new GraphEditPartFactory());
	}

	public void panningStart() {
		// TODO Auto-generated method stub
		
	}

	public void panning(int dx, int dy) {
		// TODO Auto-generated method stub
		
	}

	public void panningEnd() {
		// TODO Auto-generated method stub
		
	}

}
