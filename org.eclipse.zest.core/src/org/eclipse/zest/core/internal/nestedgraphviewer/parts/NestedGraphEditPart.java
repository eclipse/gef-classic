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
package org.eclipse.mylar.zest.core.internal.nestedgraphviewer.parts;

import org.eclipse.draw2d.ActionEvent;
import org.eclipse.draw2d.ActionListener;
import org.eclipse.draw2d.Clickable;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.editpolicies.RootComponentEditPolicy;
import org.eclipse.mylar.zest.core.ZestColors;
import org.eclipse.mylar.zest.core.internal.graphmodel.nested.NestedGraphModel;
import org.eclipse.mylar.zest.core.internal.graphmodel.nested.NestedGraphModelNode;
import org.eclipse.mylar.zest.core.internal.graphviewer.parts.GraphEditPart;
import org.eclipse.mylar.zest.core.internal.nestedgraphviewer.NestedGraphViewerImpl;
import org.eclipse.mylar.zest.core.internal.nestedgraphviewer.policies.GraphXYLayoutEditPolicy;
import org.eclipse.mylar.zest.core.internal.viewers.figures.NestedFigure;
import org.eclipse.mylar.zest.core.internal.viewers.figures.NestedFreeformLayer;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;


/**
 * Extends GraphEditPart to allow moving and resizing of nodes.
 * 
 * @author Chris Callendar
 */
public class NestedGraphEditPart extends GraphEditPart implements ActionListener {

	private boolean allowOverlap;
	private boolean enforceBounds;
	
	/**
	 * Initializes the edit part.
	 * @param allowOverlap If nodes are allowed to overlap
	 * @param enforceBounds If nodes can be moved outside the bounds  If this is set to false
	 * then scrollbars will appear.
	 */
	public NestedGraphEditPart(boolean allowOverlap, boolean enforceBounds) {
		super();
		this.allowOverlap = allowOverlap;
		this.enforceBounds = enforceBounds;
	}	

	/* (non-Javadoc)
	 * @see ca.uvic.cs.zest.internal.viewers.parts.GraphEditPart#createEditPolicies()
	 */
	protected void createEditPolicies() {
		// disallows the removal of this edit part from its parent
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new RootComponentEditPolicy());
		
		// handles constraint changes (e.g. moving and/or resizing) of model elements
		// and creation of new model elements
		installEditPolicy(EditPolicy.LAYOUT_ROLE,  new GraphXYLayoutEditPolicy(allowOverlap, enforceBounds));
	}
	
	//TODO: should we change this to getNestedGraphModel?
	// and make the same method in GraphEditPart called getGraphModel?
	public NestedGraphModel getCastedModel() {
		return (NestedGraphModel)getModel();
	}	
		
	/**
	 * Creates a FreeformLayer which contains the root NestedFigure.
	 * This NestedFigure will have an up button in the top left if the
	 * current node isn't the root node.
	 */
	protected IFigure createFigure() {		
		NestedGraphModel model = getCastedModel();
		NestedGraphModelNode current = model.getCurrentNode();
		
		Label label;
		if (current == null) {
			label = new Label("Root");	// shouldn't get here
		} else {
			label = new Label(current.getText(), current.getImage());
			label.setFont(current.getFont());
		}
		label.setBackgroundColor(ZestColors.DARK_BLUE);
		label.setForegroundColor(ColorConstants.white);
		
		// add an up button in the top left corner of the figure
		Clickable upButton = null;
		if (current != model.getRootNode()) {
			ISharedImages sharedImages = PlatformUI.getWorkbench().getSharedImages();
			Label upLabel = new Label(sharedImages.getImage(ISharedImages.IMG_TOOL_UP));
			upLabel.setBorder(new MarginBorder(1));
			upButton = new Clickable(upLabel);
			upButton.addActionListener(this);
			upButton.setToolTip(new Label(" Up "));
		}
		
		NestedFigure figure = new NestedFigure(label, upButton, !enforceBounds);
		figure.getScaledFigure().setVisible(true);

		NestedFreeformLayer layer = new NestedFreeformLayer(figure);
		layer.setBorder(new MarginBorder(2, 2, 2, 2));
		
		if (getViewer() instanceof NestedGraphViewerImpl) {
			NestedGraphViewerImpl viewer = (NestedGraphViewerImpl)getViewer();
			Dimension dim = viewer.getCanvasSize();
			layer.resize(dim.width, dim.height);
		}
		return layer;
	}
	
	/**
	 * Handle the up button being clicked.
	 * @see org.eclipse.draw2d.ActionListener#actionPerformed(org.eclipse.draw2d.ActionEvent)
	 */
	public void actionPerformed(ActionEvent event) {
		NestedGraphModel model = getCastedModel();
		model.goUp();
		if (getViewer() instanceof NestedGraphViewerImpl) {
			((NestedGraphViewerImpl)getViewer()).fireModelUpdate();
		}		
	}
	
}
