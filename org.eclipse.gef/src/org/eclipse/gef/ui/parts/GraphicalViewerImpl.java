/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.ui.parts;

import java.util.*;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.widgets.*;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.action.*;
import org.eclipse.jface.util.Assert;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.Point;

import org.eclipse.gef.*;
import org.eclipse.gef.editparts.LayerManager;
import org.eclipse.gef.editparts.ScalableRootEditPart;

/**
 * An EditPartViewer implementation based on {@link org.eclipse.draw2d.IFigure Figures}.
 * @author hudsonr
 */
public class GraphicalViewerImpl
	extends AbstractEditPartViewer
	implements GraphicalViewer
{

private final LightweightSystem lws = createLightweightSystem();
IFigure rootFigure;
private DomainEventDispatcher eventDispatcher;
private FocusListener lFocus;

/**
 * Constructs a GraphicalViewerImpl with the default root editpart.
 */
public GraphicalViewerImpl() {
	createDefaultRoot();
}

/**
 * @see org.eclipse.gef.EditPartViewer#createControl(org.eclipse.swt.widgets.Composite)
 */
public Control createControl(Composite composite) {
	setControl(new Canvas(composite, SWT.NO_BACKGROUND));
	return getControl();
}

/**
 * Creates the default root editpart. Called during construction.
 */
protected void createDefaultRoot() {
	setRootEditPart(new ScalableRootEditPart());
}

/**
 * Creates the lightweight system used to host figures. Subclasses should not need to
 * override this method.
 * @return the lightweight system
 */
protected LightweightSystem createLightweightSystem() {
	return new LightweightSystem();
}

/**
 * @see AbstractEditPartViewer#handleDispose(org.eclipse.swt.events.DisposeEvent)
 */
protected void handleDispose(DisposeEvent e) {
	super.handleDispose(e);
	getLightweightSystem().getUpdateManager().dispose();
}

protected void handleFocusGained(FocusEvent fe) {
	if (focusPart != null)
		focusPart.setFocus(true);
}

protected void handleFocusLost(FocusEvent fe) {
	if (focusPart != null)
		focusPart.setFocus(false);
}

/**
 * @see GraphicalViewer#findHandleAt(org.eclipse.draw2d.geometry.Point)
 */
public Handle findHandleAt(Point p) {
	LayerManager layermanager = (LayerManager)getEditPartRegistry().get(LayerManager.ID);
	if (layermanager == null)
		return null;
	List list = new ArrayList(3);
	list.add(layermanager.getLayer(LayerConstants.PRIMARY_LAYER));
	list.add(layermanager.getLayer(LayerConstants.CONNECTION_LAYER));
	list.add(layermanager.getLayer(LayerConstants.FEEDBACK_LAYER));
	IFigure handle = getLightweightSystem().getRootFigure()
		.findFigureAtExcluding(p.x, p.y, list);
	if (handle instanceof Handle)
		return (Handle)handle;
	return null;
}

/**
 * @see EditPartViewer#findObjectAtExcluding(Point, Collection, EditPartViewer.Conditional)
 */
public EditPart findObjectAtExcluding(
	Point pt,
	Collection exclude,
	final Conditional condition) {
	class ConditionalTreeSearch extends ExclusionSearch {
		ConditionalTreeSearch (Collection coll) {
			super(coll);
		}
		public boolean accept(IFigure figure) {
			EditPart editpart = null;
			while (editpart == null && figure != null) {
				editpart = (EditPart)getVisualPartMap().get(figure);
				figure = figure.getParent();
			}
			return editpart != null
				&& (condition == null || condition.evaluate(editpart));
		}
	}
	IFigure figure = getLightweightSystem()
		.getRootFigure()
		.findFigureAt(pt.x, pt.y, new ConditionalTreeSearch(exclude));
	EditPart part = null;
	while (part == null && figure != null) {
		part = (EditPart)getVisualPartMap().get(figure);
		figure = figure.getParent();
	}
	if (part == null)
		return getContents();
	return part;
}

/**
 * Flushes and pending layouts and paints in the lightweight system.
 * @see org.eclipse.gef.EditPartViewer#flush()
 */
public void flush() {
	getLightweightSystem().getUpdateManager().performUpdate();
}

/**
 * Returns the event dispatcher
 * @deprecated This method should not be called by subclasses
 * @return the event dispatcher
 */
protected DomainEventDispatcher getEventDispatcher() {
	return eventDispatcher;
}

/**
 * Convenience method for finding the layer manager.
 * @return the LayerManager
 */
protected LayerManager getLayerManager() {
	return (LayerManager)getEditPartRegistry().get(LayerManager.ID);
}

/**
 * Returns the lightweight system.
 * @return the system
 */
protected LightweightSystem getLightweightSystem() {
	return lws;
}

/**
 * Returns the root figure
 * @deprecated There is no reason to call this method
 * $TODO delete this method
 * @return the root figure
 */
protected IFigure getRootFigure() {
	return rootFigure;
}

/**
 * Extended to flush paints during drop callbacks.
 * @see org.eclipse.gef.ui.parts.AbstractEditPartViewer#hookDropTarget()
 */
protected void hookDropTarget() {
	//Allow the real drop targets to make their changes first.
	super.hookDropTarget();
	
	//Then force and update since async paints won't occurs during a Drag operation
	getDropTarget().addDropListener(new DropTargetAdapter() {
		public void dragEnter(DropTargetEvent event) {
			flush();
		}
		public void dragLeave(DropTargetEvent event) {
			flush();
		}
		public void dragOver(DropTargetEvent event) {
			flush();
		}
	});
}

/**
 * 
 * Extended to tell the lightweight system what its control is.
 * @see org.eclipse.gef.ui.parts.AbstractEditPartViewer#hookControl()
 */
protected void hookControl() {
	super.hookControl();
	getLightweightSystem().setControl((Canvas)getControl());
	getControl().addFocusListener(lFocus = new FocusListener() {
		public void focusGained(FocusEvent e) {
			handleFocusGained(e);
		}
		public void focusLost(FocusEvent e) {
			handleFocusLost(e);
		}
	});
}

/**
 * Registers the accessible editpart with the event dispatcher.
 * @param acc the accessible
 */
public void registerAccessibleEditPart(AccessibleEditPart acc) {
	Assert.isNotNull(acc);
	getEventDispatcher().putAccessible(acc);
}

/**
 * Reveals the specified editpart by using {@link ExposeHelper}s. A bottom-up scan through
 * the parent-chain is performed, looking for expose helpers along the way, and asking
 * them to expose the given editpart.
 * @see org.eclipse.gef.EditPartViewer#reveal(EditPart)
 */
public void reveal(EditPart part) {
	if (part == null)
		return;
	EditPart current = part.getParent();
	while (current != null) {
		if (current instanceof IAdaptable) {
			IAdaptable adaptable = (IAdaptable) current;
			ExposeHelper helper = (ExposeHelper)adaptable.getAdapter(ExposeHelper.class);
			if (helper != null)
				helper.exposeDescendant(part);
		}
		current = current.getParent();
	}
	AccessibleEditPart acc = (AccessibleEditPart)part.getAdapter(AccessibleEditPart.class);
	if (acc != null)
		getControl().getAccessible().setFocus(acc.getAccessibleID());
}

/**
 * Extended implementation to flush the viewer as the context menu is shown.
 * @see EditPartViewer#setContextMenu(org.eclipse.jface.action.MenuManager)
 */
public void setContextMenu(MenuManager contextMenu) {
	super.setContextMenu(contextMenu);
	if (contextMenu != null)
		contextMenu.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				flush();
			}
		});
}

/**
 * @see org.eclipse.gef.EditPartViewer#setCursor(org.eclipse.swt.graphics.Cursor)
 */
public void setCursor(Cursor newCursor) {
	if (getEventDispatcher() != null)
		getEventDispatcher().setOverrideCursor(newCursor);
}

/**
 * Extends the drag source to handle figures which handle MouseDown events, thereby
 * aborting any DragDetect callbacks.
 * @see AbstractEditPartViewer#setDragSource(org.eclipse.swt.dnd.DragSource)
 */
protected void setDragSource(DragSource source) {
	super.setDragSource(source);

	class TheLastListener extends DragSourceAdapter {
		public void dragStart(DragSourceEvent event) {
			// If the EventDispatcher has captured the mouse, don't perform native drag.
			if (getEventDispatcher().isCaptured())
				event.doit = false;
			if (event.doit) {
				//A drag is going to occur, tell the EditDomain
				getEventDispatcher().dispatchNativeDragStarted(event,
					GraphicalViewerImpl.this);
				/* 
				 * The mouse down that came before the dragstart, or the dragstart event
				 * itself, may have caused selection or something that needs to be
				 * painted. paints will not get processed during DND, so flush.
				 */
				flush();
			}
		}
		public void dragFinished(DragSourceEvent event) {
			getEventDispatcher()
				.dispatchNativeDragFinished(event, GraphicalViewerImpl.this);
		}
	}
	
	/*
	 * The DragSource may be set to null if there are no listeners.  If there are
	 * listeners, this should be *the* last listener because all other listeners are
	 * hooked in super().
	 */
	if (source != null)
		getDragSource().addDragListener(new TheLastListener());
}

/**
 * @see org.eclipse.gef.EditPartViewer#setEditDomain(org.eclipse.gef.EditDomain)
 */
public void setEditDomain(EditDomain domain) {
	super.setEditDomain(domain);
	/*
	 * @TODO:Pratik    what if the new edit domain is null?  skip the following step?
	 */
	getLightweightSystem()
		.setEventDispatcher(eventDispatcher = new DomainEventDispatcher(domain, this));
}

/**
 * @see org.eclipse.gef.EditPartViewer#setRootEditPart(org.eclipse.gef.RootEditPart)
 */
public void setRootEditPart(RootEditPart editpart) {
	super.setRootEditPart(editpart);
	setRootFigure(((GraphicalEditPart)editpart).getFigure());
}

/**
 * Sets the lightweight system's root figure.
 * @param figure the root figure
 */
protected void setRootFigure(IFigure figure) {
	rootFigure = figure;
	getLightweightSystem().setContents(rootFigure);
}

/**
 * @see org.eclipse.gef.EditPartViewer#setRouteEventsToEditDomain(boolean)
 */
public void setRouteEventsToEditDomain(boolean value) {
	getEventDispatcher().setRouteEventsToEditor(value);
}

/**
 * @see org.eclipse.gef.ui.parts.AbstractEditPartViewer#unhookControl()
 */
protected void unhookControl() {
	super.unhookControl();
	if (lFocus != null) {
		getControl().removeFocusListener(lFocus);
		lFocus = null;
	}
}

/**
 * @see EditPartViewer#unregisterAccessibleEditPart(org.eclipse.gef.AccessibleEditPart)
 */
public void unregisterAccessibleEditPart(AccessibleEditPart acc) {
	Assert.isNotNull(acc);
	getEventDispatcher().removeAccessible(acc);
}

}