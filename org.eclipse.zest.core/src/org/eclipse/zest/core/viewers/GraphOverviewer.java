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
package org.eclipse.mylar.zest.core.viewers;

import java.util.Collections;
import java.util.List;

import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.mylar.zest.core.internal.graphviewer.overview.GraphOverviewerImpl;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IPartService;
import org.eclipse.ui.IWorkbenchPart;

/**
 * A viewer that can be used as an overview of graphs. It provides a small view that shows
 * only rectangles representing the nodes of a graph that exists in the most recently selected
 * workbench part that contains a graph. Does not show connections, and does not
 * provide any selections or items. No interaction can be done directly through this viewer.
 * @author Del Myers
 *
 */
public class GraphOverviewer extends StructuredViewer implements DisposeListener {
	private GraphOverviewerImpl viewer;
	private AbstractZoomableViewer currentHookedViewer;
	private IPartService service;
	private ZoomablePartListener partListener;
	private class ZoomablePartListener implements IPartListener {
		public void partActivated(IWorkbenchPart part) {
			if (part instanceof IZoomableWorkbenchPart) {
				AbstractZoomableViewer viewer = 
					((IZoomableWorkbenchPart)part).getZoomableViewer();
				if (viewer != currentHookedViewer && viewer instanceof AbstractStructuredGraphViewer) {
					GraphOverviewer.this.hookViewer((AbstractStructuredGraphViewer) viewer);
				}
			}
		}
		public void partDeactivated(IWorkbenchPart part) {

		}
	
		public void partBroughtToTop(IWorkbenchPart part) {
			if (part instanceof IZoomableWorkbenchPart) {
				AbstractZoomableViewer viewer = 
					((IZoomableWorkbenchPart)part).getZoomableViewer();
				if (viewer != currentHookedViewer) {
					GraphOverviewer.this.unhookViewer();
				}
			}
		}
		public void partClosed(IWorkbenchPart part) {
			if (part instanceof IZoomableWorkbenchPart) {
				AbstractZoomableViewer viewer = 
					((IZoomableWorkbenchPart)part).getZoomableViewer();
				if (viewer == currentHookedViewer) {
					GraphOverviewer.this.unhookViewer();
				}
			}
		}
		
		public void partOpened(IWorkbenchPart part) {}
		
	}
	
	/**
	 * 
	 */
	public GraphOverviewer(Composite parent, IPartService service) {
		this.viewer = new GraphOverviewerImpl();
		viewer.createControl(parent);
		this.service = service;
		partListener = new ZoomablePartListener();
		service.addPartListener(partListener);
		parent.addDisposeListener(this);
	}
	
	
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.StructuredViewer#doFindInputItem(java.lang.Object)
	 */
	protected Widget doFindInputItem(Object element) {
		return null;
	}

	/**
	 * @param viewer2
	 */
	public void hookViewer(AbstractStructuredGraphViewer zoomViewer) {
		viewer.setContents(zoomViewer.getEditPartViewer());
		currentHookedViewer = zoomViewer;
	}

	/**
	 * 
	 */
	public void unhookViewer() {
		//viewer.unhookViewer();		
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.StructuredViewer#doFindItem(java.lang.Object)
	 */
	protected Widget doFindItem(Object element) {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.StructuredViewer#doUpdateItem(org.eclipse.swt.widgets.Widget, java.lang.Object, boolean)
	 */
	protected void doUpdateItem(Widget item, Object element, boolean fullMap) {
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.StructuredViewer#getSelectionFromWidget()
	 */
	protected List getSelectionFromWidget() {
		return Collections.EMPTY_LIST;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.StructuredViewer#internalRefresh(java.lang.Object)
	 */
	protected void internalRefresh(Object element) {
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.StructuredViewer#reveal(java.lang.Object)
	 */
	public void reveal(Object element) {
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.StructuredViewer#setSelectionToWidget(java.util.List, boolean)
	 */
	protected void setSelectionToWidget(List l, boolean reveal) {
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.Viewer#getControl()
	 */
	public Control getControl() {
		return viewer.getControl();
	}



	/* (non-Javadoc)
	 * @see org.eclipse.swt.events.DisposeListener#widgetDisposed(org.eclipse.swt.events.DisposeEvent)
	 */
	public void widgetDisposed(DisposeEvent e) {
		service.removePartListener(partListener);
		partListener = null;
	}

}
