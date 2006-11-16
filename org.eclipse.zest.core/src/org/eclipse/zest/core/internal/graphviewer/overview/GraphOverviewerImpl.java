/*******************************************************************************
 * Copyright 2005-2006, CHISEL Group, University of Victoria, Victoria, BC, Canada.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     The Chisel Group, University of Victoria
 *******************************************************************************/
package org.eclipse.mylar.zest.core.internal.graphviewer.overview;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditDomain;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.RootEditPart;
import org.eclipse.gef.Tool;
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;
import org.eclipse.gef.editparts.ScalableRootEditPart;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.gef.ui.parts.DomainEventDispatcher;
import org.eclipse.gef.ui.parts.GraphicalViewerImpl;
import org.eclipse.mylar.zest.core.internal.gefx.IZestViewerProperties;
import org.eclipse.mylar.zest.core.internal.graphviewer.overview.parts.OverviewEditPart;
import org.eclipse.mylar.zest.core.internal.graphviewer.overview.parts.OverviewImageEditPartFactory;
import org.eclipse.mylar.zest.core.internal.graphviewer.overview.parts.OverviewRootEditPart;
import org.eclipse.mylar.zest.core.internal.graphviewer.parts.tools.OverviewRectangleTool;
import org.eclipse.swt.graphics.Cursor;

/**
 * A viewer that simply draws an "overview" graph of nodes only.
 * @author Del Myers
 *
 */
public class GraphOverviewerImpl extends GraphicalViewerImpl implements PropertyChangeListener  {

			
	private EditPartViewer model;

	/**
	 * 
	 */
	public GraphOverviewerImpl() {
		setEditPartFactory(new OverviewImageEditPartFactory());
		EditDomain domain = new EditDomain();
		//ToolEventDispatcher dispatcher = new ToolEventDispatcher(this);
		domain.setDefaultTool(new OverviewRectangleTool());
		domain.setActiveTool(domain.getDefaultTool());
		DomainEventDispatcher dispatcher = new DomainEventDispatcher(domain, this);
		setEditDomain(domain);
		//dispatcher.setTool(new OverviewRectangleTool());
		getLightweightSystem().setEventDispatcher(dispatcher);
	}
	
	protected void createDefaultRoot() {
		ScalableRootEditPart root = new OverviewRootEditPart();
		this.setRootEditPart(root);
	}
	
	private ScalableRootEditPart getCastedRoot() {
		return (ScalableRootEditPart)getRootEditPart();
	}

		
	public void setContents(Object model) {
		if (model instanceof EditPartViewer) {
			EditPartViewer newModel = (EditPartViewer) model;
			if (this.model != model) {
				if (this.model != null) 
					this.model.removePropertyChangeListener(this);
				this.model = newModel;
				super.setContents(newModel);
				this.model.addPropertyChangeListener(this);
				Tool tool = getEditDomain().getActiveTool();
				if (tool instanceof OverviewRectangleTool) {
					tool.setViewer(this);
					((OverviewRectangleTool)tool).setZoomManager(getZoomManager(model));
				}
			} 
		} else {
			
		}
	}
	
	/**
	 * @param model2
	 * @return
	 */
	private ZoomManager getZoomManager(Object model) {
		if (model instanceof EditPartViewer) {
			RootEditPart modelRoot = ((EditPartViewer)model).getRootEditPart();
			if (modelRoot instanceof ScalableFreeformRootEditPart) {
				return ((ScalableFreeformRootEditPart)modelRoot).getZoomManager();
			} else if (modelRoot instanceof ScalableRootEditPart) {
				return ((ScalableRootEditPart)modelRoot).getZoomManager();
			}
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.ui.parts.GraphicalViewerImpl#setCursor(org.eclipse.swt.graphics.Cursor)
	 */
	public void setCursor(Cursor newCursor) {
		if (getControl() != null && !getControl().isDisposed()) {
			getControl().setCursor(newCursor);
		}
	}
	
	public void setZoom(Rectangle zoom) {
		OverviewEditPart content = (OverviewEditPart) getCastedRoot().getContents();
		Rectangle copy = zoom.getCopy();
		//((GraphicalEditPart)getRootEditPart()).getFigure().translateToRelative(copy);
		if (content==null) return; //not yet initialized.
		content.translateToViewer(copy);
		RootEditPart modelRoot = this.model.getRootEditPart();
		if (modelRoot instanceof ScalableFreeformRootEditPart) {
			if (!zoom.isEmpty()) {
			((ScalableFreeformRootEditPart)modelRoot).getZoomManager().zoomTo(copy);
			} else {
				//ensure that the rectangle has a zero width and height so that 
				//it is centered and the view is not scaled.
				copy.width = 0;
				copy.height = 0;
				((ScalableFreeformRootEditPart)modelRoot).getZoomManager().zoomTo(copy);
			}
		} else if (modelRoot instanceof ScalableRootEditPart) {
			if (!zoom.isEmpty()) {
				((ScalableRootEditPart)modelRoot).getZoomManager().zoomTo(copy);
			} else {
//				ensure that the rectangle has a zero width and height so that 
				//it is centered and the view is not scaled.
				copy.width = 0;
				copy.height = 0;
				((ScalableRootEditPart)modelRoot).getZoomManager().zoomTo(copy);
			}
		}
	}

	
	/* (non-Javadoc)
	 * @see org.eclipse.gef.ui.parts.GraphicalViewerImpl#unhookControl()
	 */
	protected void unhookControl() {
		Tool tool = getEditDomain().getActiveTool();
		if (tool instanceof OverviewRectangleTool) {
			((OverviewRectangleTool)tool).setZoomManager(null);
		}
		super.unhookControl();
	}
	
	/* (non-Javadoc)
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	public void propertyChange(PropertyChangeEvent evt) {
		if (IZestViewerProperties.GRAPH_VIEWER_CONTENTS.equals(evt.getPropertyName())) {
			//set the zoom to nothing.
			//setZoom(new Rectangle());
			//have to refresh the whole thing.
			//super.setContents(this.model);
		}
	}
	
	

}
