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

import java.util.Iterator;
import java.util.List;

import org.eclipse.draw2d.ActionEvent;
import org.eclipse.draw2d.ActionListener;
import org.eclipse.draw2d.Clickable;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.requests.LocationRequest;
import org.eclipse.gef.tools.MarqueeDragTracker;
import org.eclipse.mylar.zest.core.internal.graphmodel.nested.NestedGraphModel;
import org.eclipse.mylar.zest.core.internal.graphmodel.nested.NestedGraphModelNode;
import org.eclipse.mylar.zest.core.internal.graphviewer.parts.GraphNodeEditPart;
import org.eclipse.mylar.zest.core.internal.nestedgraphviewer.NestedGraphViewerImpl;
import org.eclipse.mylar.zest.core.internal.nestedgraphviewer.policies.NestedGraphXYLayoutEditPolicy;
import org.eclipse.mylar.zest.core.internal.viewers.figures.AspectRatioScaledFigure;
import org.eclipse.mylar.zest.core.internal.viewers.figures.NestedFigure;
import org.eclipse.mylar.zest.core.internal.viewers.figures.PlusMinusFigure;
import org.eclipse.mylar.zest.core.internal.viewers.trackers.SingleSelectionTracker;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

/**
 * Extends GraphNodeEditPart to add functionality specific to nested graph
 * nodes.
 * 
 * @author Chris Callendar
 */
public class NestedGraphNodeEditPart extends GraphNodeEditPart implements ActionListener {

	private boolean enforceBounds;
	private Clickable upButton = null;
	private Label label;

	public NestedGraphNodeEditPart(boolean enforceBounds) {
		super();
		this.enforceBounds = enforceBounds;
	}

	/**
	 * Gets the bounds of the figure and translates it to absolute coordinates.
	 * 
	 * @return Rectangle in absolute coordinates
	 */
	public Rectangle getAbsoluteBounds() {
		Rectangle bounds = getFigure().getBounds().getCopy();
		getFigure().translateToAbsolute(bounds);
		return bounds;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.mylar.zest.core.internal.graphviewer.parts.GraphNodeEditPart#createEditPolicies()
	 */
	protected void createEditPolicies() {
		super.createEditPolicies();
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new NestedGraphXYLayoutEditPolicy(true));
	}
	
	
	/**
	 * Gets the drag tracker for this edit part.
	 * If the selection occurs on the root node then a marquee tracker
	 * is used, otherwise the default tracker is used.
	 */
	public DragTracker getDragTracker(Request request) {
		if ( ((NestedFigure)getFigure()).isPointInLabel(((LocationRequest)request).getLocation()) ) {
			// If the location where this selection happens is in the label
			// The use a single selection tracker
			return new SingleSelectionTracker(this);
		}		
		else if ( this.getCastedModel().isCurrent() ) {
			//@tag zest(bug(153221-Marquee-Selection(fix))) : make sure that the location is inside the scaled figure.
			AspectRatioScaledFigure sfig = ((NestedFigure)getFigure()).getScaledFigure();
			Rectangle scaledBounds = ((NestedFigure)getFigure()).getScaledFigure().getBounds().getCopy();
			sfig.translateToAbsolute(scaledBounds);
			if (scaledBounds.contains(((LocationRequest)request).getLocation()))
					return new MarqueeDragTracker();
		}
		return super.getDragTracker(request);
	}

	// TODO rename to getNestedGraphModel() ?
	public NestedGraphModelNode getCastedModel() {
		return (NestedGraphModelNode) getModel();
	}
	
	
	/**
	 * Gets the scaled bounds for this nested graph edit part
	 * @return
	 */
	public Rectangle getBounds() {
		NestedFigure nestedFigure = (NestedFigure)getFigure();
		return nestedFigure.getClientArea();
	
	}

	
	protected IFigure createFigureForModel() {

		if (((NestedGraphModel) getCastedModel().getGraphModel()).getCurrentNode() == getCastedModel()) {
			NestedGraphModel model = (NestedGraphModel) getCastedModel().getGraphModel();
			NestedGraphModelNode current = model.getCurrentNode();

			if (current == null) {
				label = new Label("Root"); // shouldn't get here
			} else {
				label = new Label(current.getText(), current.getImage());
				label.setFont(current.getFont());
			}
			//@tag zest(bug(151332-Colors(fix)))
			//@tag bug(154256-ClientSupplySelect(fix)) : get the colors from the model
			label.setBackgroundColor(current.getBackgroundColor());
			label.setForegroundColor(current.getForegroundColor());

			// add an up button in the top left corner of the figure
			upButton = null;
			ISharedImages sharedImages = PlatformUI.getWorkbench().getSharedImages();
			Label upLabel = new Label(sharedImages.getImage(ISharedImages.IMG_TOOL_UP));
			upLabel.setBorder(new MarginBorder(1));
			upButton = new Clickable(upLabel);
			if (current != model.getRootNode()) {
				upButton.addActionListener(this);
			} else {
				upButton.setVisible(false);
			}
			upButton.setRolloverEnabled(false);
			upButton.setToolTip(new Label(" Up "));

			NestedFigure figure = new NestedFigure(label, upButton, !enforceBounds);
			figure.getScaledFigure().setVisible(true);

			if (getViewer() instanceof NestedGraphViewerImpl) {
				NestedGraphViewerImpl viewer = (NestedGraphViewerImpl) getViewer();
				Dimension dim = viewer.getCanvasSize();
				figure.setSize(dim);
			}
			return figure;
		}

		else {
			if (getCastedModel() != null) {
				NestedGraphModelNode node = getCastedModel();
				label = new Label(node.getText(), node.getImage());
				label.setFont(node.getFont());
				label.setForegroundColor(node.getForegroundColor());
				PlusMinusFigure plusMinus = null;
				if (node.hasChildren()) {

					plusMinus = new PlusMinusFigure(NestedGraphModelNode.PLUS_SIZE);
					plusMinus.setBorder(new MarginBorder(2));
					plusMinus.addActionListener(this);

				}
				NestedFigure nestedFigure = new NestedFigure(label, plusMinus);
				nestedFigure.setBackgroundColor(node.getBackgroundColor());
				nestedFigure.setNestedFiguresVisible(node.getChildrenVisible());
				nestedFigure.setScale(node.getWidthScale(), node.getHeightScale());
				figure = nestedFigure;
				((NestedFigure)figure).getScaledFigure().setVisible(true);
				figure.setBounds(new Rectangle(1,0,100,100));
			} else {
				figure = super.createFigureForModel();
			}
			return figure;
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#getContentPane()
	 */
	public IFigure getContentPane() {
		return ((NestedFigure)getFigure()).getScaledFigure();
	}

	/**
	 * Returns a list of the children GraphModelNode object.
	 * 
	 * @see org.eclipse.mylar.zest.core.internal.graphviewer.parts.GraphNodeEditPart#getModelChildren()
	 */
	protected List getModelChildren() {
		return getCastedModel().getChildren();
	}
	
	/**
	 * The scaled figure that child NestedFigures will be placed on.
	 * @return scaled figure that child NestedFigures will be placed on.
	 */
	public AspectRatioScaledFigure getScaledFigure() {
		for (Iterator i = getFigure().getChildren().iterator(); i.hasNext();) {
			Object next = i.next();
			if (next instanceof AspectRatioScaledFigure) return (AspectRatioScaledFigure) next;
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ca.uvic.cs.zest.internal.springgraphviewer.parts.GraphNodeEditPart#performRequest(org.eclipse.gef.Request)
	 */
	public void performRequest(Request req) {

		if (REQ_OPEN.equals(req.getType())) {
			// DebugPrint.println("performRequest() Bounds: " +
			// getFigure().getBounds() + "\t " + getCastedModel().getLocation()
			// + ", " + getCastedModel().getSize());
			// DebugPrint.println("performRequest() Scale: " +
			// getCastedModel().getScale() + ", " +
			// ((NestedFigure)getFigure()).getScale());
			// if(true) return;

			// If there are children, then go down into that layer
			NestedGraphModel model = getGraphModel();
			if ((model != null) && (getCastedModel().getChildren().size() > 0)) {
				model.setCurrentNode(getCastedModel());
				if (getViewer() instanceof NestedGraphViewerImpl) {
					((NestedGraphViewerImpl) getViewer()).fireModelUpdate();
				}
			}
		} else {
			super.performRequest(req);
		}
	}

	/**
	 * Handles the
	 * {@link org.eclipse.mylar.zest.core.internal.viewers.figures.PlusMinusFigure}
	 * click events. Toggles whether the children are shown.
	 * 
	 * @see org.eclipse.draw2d.ActionListener#actionPerformed(org.eclipse.draw2d.ActionEvent)
	 */
	public void actionPerformed(ActionEvent event) {
		if ( event.getSource() == upButton ) {
			NestedGraphModel model = (NestedGraphModel)getCastedModel().getGraphModel();
			model.goUp();
			if (getViewer() instanceof NestedGraphViewerImpl) {
				upButton.removeActionListener(this);
				Display.getCurrent().asyncExec(new Runnable() {

					public void run() {
						// TODO Auto-generated method stub
						((NestedGraphViewerImpl)getViewer()).fireModelUpdate();
						//openNode();
					}
					
				});
			}
			
		}
		else {
			openNode();
		}

	}

	private void openNode() {
		// check connections
		if (getViewer() instanceof NestedGraphViewerImpl) {
			NestedFigure fig = (NestedFigure) getFigure();
			boolean vis = !fig.getScaledFigure().isVisible();
			fig.setNestedFiguresVisible(vis);
			NestedGraphModelNode node = getCastedModel();
			node.setChildrenVisible(vis);

			NestedGraphViewerImpl viewer = (NestedGraphViewerImpl) getViewer();
			if (vis) {
				// do a grid layout on the children using the full area and
				// then
				// scale
				// Rectangle bounds = getGraphModel().getMainArea();
				viewer.doLayout(node, 500, 500);
				viewer.checkScaling(node);
				// if (node.getScale() != fig.getScale()) {
				fig.setScale(node.getWidthScale(), node.getHeightScale());
				// }
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ca.uvic.cs.zest.internal.springgraphviewer.parts.GraphNodeEditPart#refreshVisuals()
	 */
	protected void refreshVisuals() {
		super.refreshVisuals();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.mylar.zest.core.internal.graphviewer.parts.GraphNodeEditPart#refreshColors()
	 */
	protected void refreshColors() {
		//@tag zest(bug(152393(fix))) : Set the label color when the foreground changes.
		if (label != null) {
			label.setForegroundColor(getCastedModel().getForegroundColor());
			label.setBackgroundColor(getCastedModel().getBackgroundColor());
		}
		super.refreshColors();
	}

	/**
	 * Gets the NestedGraphModel from the NestedGraphModelNode
	 * 
	 * @return NestedGraphModel
	 */
	private NestedGraphModel getGraphModel() {
		NestedGraphModel model = (NestedGraphModel) getCastedModel().getGraphModel();
		return model;
	}

}
