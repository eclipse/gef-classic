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

import java.util.List;

import org.eclipse.draw2d.ActionEvent;
import org.eclipse.draw2d.ActionListener;
import org.eclipse.draw2d.Clickable;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseListener;
import org.eclipse.draw2d.MouseMotionListener;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.mylar.zest.core.ZestColors;
import org.eclipse.mylar.zest.core.internal.graphmodel.nested.NestedGraphModel;
import org.eclipse.mylar.zest.core.internal.graphmodel.nested.NestedGraphModelNode;
import org.eclipse.mylar.zest.core.internal.graphviewer.parts.GraphNodeEditPart;
import org.eclipse.mylar.zest.core.internal.nestedgraphviewer.NestedGraphViewerImpl;
import org.eclipse.mylar.zest.core.internal.nestedgraphviewer.policies.NestedGraphXYLayoutEditPolicy;
import org.eclipse.mylar.zest.core.internal.viewers.figures.NestedFigure;
import org.eclipse.mylar.zest.core.internal.viewers.figures.NestedFreeformLayer;
import org.eclipse.mylar.zest.core.internal.viewers.figures.PlusMinusFigure;
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
	private boolean currentEditPart = false;

	public NestedGraphNodeEditPart(boolean enforceBounds) {
		super();
		this.enforceBounds = enforceBounds;
		currentEditPart = false;
	}

	/**
	 * Gets the bounds of the figure and translates it to absolute coordinates.
	 * 
	 * @return Rectangle in absolute coordinates
	 */
	public Rectangle getScreenBounds() {
		Rectangle bounds = getFigure().getBounds().getCopy();
		
		//Point p = bounds.getLocation();
		//getFigure().translateToParent(bounds);
		getFigure().translateToAbsolute(bounds);
		//bounds.setLocation(p);
		return bounds;
	}

	protected void createEditPolicies() {
		super.createEditPolicies();

		installEditPolicy(EditPolicy.LAYOUT_ROLE, new NestedGraphXYLayoutEditPolicy(true));
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
		if ( currentEditPart ) {
			NestedFreeformLayer nestedFreeformLayer = (NestedFreeformLayer)getFigure();
			return nestedFreeformLayer.getClientArea();
		}
		else {
			NestedFigure nestedFigure = (NestedFigure)getFigure();
			return nestedFigure.getClientArea();
		}	
	}

	
	protected IFigure createFigureForModel() {

		if (((NestedGraphModel) getCastedModel().getGraphModel()).getCurrentNode() == getCastedModel()) {
			currentEditPart = true;
			NestedGraphModel model = (NestedGraphModel) getCastedModel().getGraphModel();
			NestedGraphModelNode current = model.getCurrentNode();
			// current.setBackgroundColor(ZestColors.DARK_BLUE);
			// current.setForegroundColor(ColorConstants.white);

			Label label;
			if (current == null) {
				label = new Label("Root"); // shouldn't get here
			} else {
				label = new Label(current.getText(), current.getImage());
				label.setFont(current.getFont());
			}
			label.setBackgroundColor(ZestColors.DARK_BLUE);
			label.setForegroundColor(ColorConstants.white);

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

			NestedFreeformLayer layer = new NestedFreeformLayer(figure);
			layer.setBorder(new MarginBorder(2, 2, 2, 2));

			if (getViewer() instanceof NestedGraphViewerImpl) {
				NestedGraphViewerImpl viewer = (NestedGraphViewerImpl) getViewer();
				Dimension dim = viewer.getCanvasSize();
				layer.resize(dim.width, dim.height);
			}
			layer.addMouseListener(new MouseListener() {

				public void mousePressed(MouseEvent me) {
					// TODO Auto-generated method stub

				}

				public void mouseReleased(MouseEvent me) {
					// TODO Auto-generated method stub

				}

				public void mouseDoubleClicked(MouseEvent me) {
					// TODO Auto-generated method stub

				}

			});
			layer.addMouseMotionListener(new MouseMotionListener() {

				public void mouseDragged(MouseEvent me) {
					// TODO Auto-generated method stub

				}

				public void mouseEntered(MouseEvent me) {
					// TODO Auto-generated method stub

				}

				public void mouseExited(MouseEvent me) {
					// TODO Auto-generated method stub

				}

				public void mouseHover(MouseEvent me) {
					// TODO Auto-generated method stub

				}

				public void mouseMoved(MouseEvent me) {
					// TODO Auto-generated method stub

				}

			});
			
			return layer;
		}

		else {
			currentEditPart = true;
			if (getCastedModel() != null) {
				NestedGraphModelNode node = getCastedModel();
				Label label = new Label(node.getText(), node.getImage());
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
			} else {
				figure = super.createFigureForModel();
			}

			return figure;
		}
	}

	/**
	 * Returns a list of the children GraphModelNode object.
	 * 
	 * @see org.eclipse.mylar.zest.core.internal.graphviewer.parts.GraphNodeEditPart#getModelChildren()
	 */
	protected List getModelChildren() {
		return getCastedModel().getChildren();
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
				((NestedGraphViewerImpl)getViewer()).fireModelUpdate();
			}
			
		}
		
		// check connections
		if (getViewer() instanceof NestedGraphViewerImpl) {
			NestedFigure fig = (NestedFigure) getFigure();
			boolean vis = !fig.getScaledFigure().isVisible();
			fig.setNestedFiguresVisible(vis);
			NestedGraphModelNode node = getCastedModel();
			node.setChildrenVisible(vis);
			
			NestedGraphViewerImpl viewer = (NestedGraphViewerImpl) getViewer();
			//viewer.hideConnections();
			viewer.expandTreeItem(getCastedModel().getData(), vis);
			if (vis) {
				// do a grid layout on the children using the full area and then
				// scale
				//Rectangle bounds = getGraphModel().getMainArea();
				viewer.doLayout(node, 1200, 1200);
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
