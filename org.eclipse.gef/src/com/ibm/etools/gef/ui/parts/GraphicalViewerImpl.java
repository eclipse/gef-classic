package com.ibm.etools.gef.ui.parts;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.util.*;

import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.dnd.*;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.action.IMenuManager;

import com.ibm.etools.draw2d.*;
import com.ibm.etools.draw2d.geometry.*;

import com.ibm.etools.gef.editparts.LayerManager;
import com.ibm.etools.gef.*;
import com.ibm.etools.gef.handles.*;
import com.ibm.etools.gef.dnd.*;

public class GraphicalViewerImpl
	extends AbstractEditPartViewer
	implements GraphicalViewer
{

private LightweightSystem system;
IFigure rootFigure;
private DomainEventDispatcher eventDispatcher;

{
	createDefaultRoot();
}

public Control createControl(Composite composite){
	setControl(new Canvas(composite, 0));
	return getControl();
}

protected void createDefaultRoot(){
	setRootEditPart(new GraphicalRootEditPart());
}

protected void expose(EditPart part){
	if (part == null)
		return;
	EditPart current = part.getParent();
	while (current != null){
		if (current instanceof IAdaptable) {
			IAdaptable adaptable = (IAdaptable) current;
			ExposeHelper helper = (ExposeHelper)adaptable.getAdapter(ExposeHelper.class);
			if (helper != null)
				helper.exposeDescendant(part);
		}
		current = current.getParent();
	}
}

public Handle findHandleAt(Point p){
	LayerManager layermanager = (LayerManager)getEditPartRegistry().get(LayerManager.ID);
	if (layermanager == null)
		return null;
	List list = new ArrayList(3);
	list.add(layermanager.getLayer(LayerConstants.PRIMARY_LAYER));
	list.add(layermanager.getLayer(LayerConstants.CONNECTION_LAYER));
	list.add(layermanager.getLayer(LayerConstants.FEEDBACK_LAYER));
	IFigure handle = getLightweightSystem().getRootFigure().findFigureAtExcluding(p.x, p.y, list);
	if (handle instanceof Handle)
		return (Handle)handle;
	return null;
}

public EditPart findObjectAt(Point pt) {
	return findObjectAtExcluding(pt, Collections.EMPTY_SET);
}

/** @deprecated should be passing EditParts, not Figures */
public EditPart findObjectAtExcluding(Point pt, Collection exclude){
	Collection c = new ArrayList(getInactiveLayers());
	c.addAll(exclude);

	IFigure figure = getLightweightSystem().getRootFigure().findFigureAtExcluding(pt.x, pt.y, c);
	EditPart part = (EditPart)getVisualPartMap().get(figure);
	while(figure != null && part == null) {
		figure = figure.getParent();
		part = (EditPart)getVisualPartMap().get(figure);
	}
	if (part == null)
		return getContents();
	return part;
}

public void flush(){
	getLightweightSystem().getUpdateManager().performUpdate();
}

/**@deprecated*/
protected DomainEventDispatcher getEventDispatcher(){
	return eventDispatcher;
}

/**@deprecated */
protected Collection getInactiveLayers(){
	List list = new ArrayList();
	LayerManager layers = getLayerManager();
	list.add(layers.getLayer(LayerConstants.HANDLE_LAYER));
	list.add(layers.getLayer(LayerConstants.FEEDBACK_LAYER));
	return list;
}

protected LayerManager getLayerManager(){
	return (LayerManager)getEditPartRegistry().get(LayerManager.ID);
}

protected LightweightSystem getLightweightSystem(){
	if (system == null)
		system = new LightweightSystem();
	return system;
}

/**@deprecated There is no reason to call this method*/
protected IFigure getRootFigure(){
	return rootFigure;
}

protected void hookDropTarget() {
	//Allow the real drop targets to make their changes first.
	super.hookDropTarget();
	
	//Then force and update since async paints won't occurs during a Drag operation
	getDropTarget().addDropListener(new DropTargetAdapter() {
		public void dragEnter(DropTargetEvent event){
			flush();
		}
		public void dragLeave(DropTargetEvent event){
			flush();
		}
		public void dragOver(DropTargetEvent event){
			flush();
		}
	});
}

public void menuAboutToShow(IMenuManager menu) {
	flush(); //Any async paints won't be dispatched while the menu is visible.
	super.menuAboutToShow(menu);
}

protected void hookControl(){
	super.hookControl();
	getLightweightSystem().setControl((Canvas)getControl());
}

public void setCursor(Cursor newCursor){
	if(getEventDispatcher() != null)
		getEventDispatcher().setOverrideCursor(newCursor);
}

public void setEditDomain(EditDomain domain){
	super.setEditDomain(domain);
	getLightweightSystem().setEventDispatcher(eventDispatcher = new DomainEventDispatcher(domain, this));
}

void setLightweightSystem(LightweightSystem lws){
	if (getControl() != null)
		throw new RuntimeException("The LightweightSystem cannot be changed once the Control has been set"); //$NON-NLS-1$
	system = lws;
	if (eventDispatcher != null)
		system.setEventDispatcher(eventDispatcher);
	if (rootFigure != null)
		system.setContents(rootFigure);
}

public void setRootEditPart(RootEditPart editpart){
	super.setRootEditPart(editpart);
	setRootFigure(((GraphicalEditPart)editpart).getFigure());
}

protected void setRootFigure(IFigure figure){
	getLightweightSystem().setContents( rootFigure = figure );
}

public void setRouteEventsToEditor(boolean value){
	getEventDispatcher().setRouteEventsToEditor(value);
}

}