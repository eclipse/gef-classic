package org.eclipse.gef.dnd;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.util.*;

import org.eclipse.swt.dnd.*;

import org.eclipse.gef.GEF;

/**
 * A DragSourceListener that allows many {@link TransferDragSourceListener}s to be added 
 * to it.  When the DragSource fires a DragSourceEvent, this listener calculates which 
 * one of the added listeners is able to complete the drag operation and forwards the 
 * event to that listener.
 */
public class DelegatingDragAdapter 
	implements DragSourceListener 
{

private List listeners = new ArrayList();
private List activeListeners = new ArrayList();
private TransferDragSourceListener currentListener;

/**
 * Adds the given TransferDragSourceListener to the list of listeners.
 */
public void addDragSourceListener(TransferDragSourceListener listener) {
	listeners.add(listener);
}

/**
 * Forwards this event to the current listener.  Doesn't 
 * update the current listener, since the current listener 
 * is already the one that completed the drag operation.
 * 
 * @see DragSourceListener#dragFinished(DragSourceEvent)
 */
public void dragFinished(DragSourceEvent event) {
	if (GEF.DebugDND)
		GEF.debug("Drag Finished: " + toString()); //$NON-NLS-1$
	// if there is a listener that can handle the drop, delegate the event
	if (currentListener != null)
		currentListener.dragFinished(event);
}

/**
 * Updates the current listener and then forwards the event to it.
 * 
 * @see DragSourceListener#dragSetData(DragSourceEvent)
 */
public void dragSetData(DragSourceEvent event) {
	if (GEF.DebugDND)
		GEF.debug("Drag Set Data: " + toString()); //$NON-NLS-1$
	// find a listener that can handle a drop of the given data type
	updateCurrentListener(event);
	// if a listener was found, delegate the event
	if (currentListener != null)
		currentListener.dragSetData(event);
}

/**
 * Forwards this event to each listener.  It is expected that a listener will set
 * <code>event.doit</code> to <code>false</code> if it cannot handle the drag operation.  
 * If a listener can handle the drag, it is added to the list of active listeners.  The 
 * drag is aborted if there are no listeners that can handle it.  
 * 
 * @see DragSourceListener#dragStart(DragSourceEvent)
 */
public void dragStart(DragSourceEvent event) {
	if (GEF.DebugDND)
		GEF.debug("Drag Start: " + toString()); //$NON-NLS-1$
	boolean origDoit = event.doit;  // the listeners may change event.doit, so save the original value
	boolean doit = false;  // true if any one of the listeners can handle the drag
	List transfers = new ArrayList(listeners.size());
	
	for (int i=0; i<listeners.size(); i++) {
		TransferDragSourceListener listener = (TransferDragSourceListener)listeners.get(i);
		event.doit = origDoit;  // restore event.doit to its original value
		listener.dragStart(event); 	// delegate the event
		// if listener can handle the drag, add it and its transfer to the appropriate lists
		if (event.doit) {  
			transfers.add(listener.getTransfer());
			activeListeners.add(listener);
		}
		// set doit to true if the current listener or any of 
		// the previous listeners can handle the drag
		doit = doit || event.doit;  
	}
	
	if (doit)  // if any of the listeners can handle the drag, set the transfers on the DragSource
		((DragSource)event.widget).setTransfer((Transfer[])transfers.toArray(new Transfer[transfers.size()]));
	
	event.doit = doit;  // event.doit should be true if any of the listeners can handle the drag
}

/**
 * Updates the current listener to one that can handle the drag.  There can be many listeners 
 * and each listener may be able to handle many TransferData types.  The first listener found 
 * that can handle a drag of one of the given TransferData types will be selected.
 */
private void updateCurrentListener(DragSourceEvent event) {
	currentListener = null;
	if (event.dataType == null)
		return;
	Iterator iter = activeListeners.iterator();
	while (iter.hasNext()) {
		TransferDragSourceListener listener = (TransferDragSourceListener)iter.next();

		if (listener.getTransfer().isSupportedType(event.dataType)) {
			currentListener = listener;
			return;
		}
	}
}

/**
 * Adds the Transfer from each listener to an array and returns that array.
 */
public Transfer[] getTransferTypes() {
	Transfer[] types = new Transfer[listeners.size()];
	for (int i=0; i<listeners.size(); i++) {
		TransferDragSourceListener listener = (TransferDragSourceListener)listeners.get(i);
		types[i] = listener.getTransfer();
	}
	return types;
}

/**
 * Returns <code>true</code> if there are no listeners to delegate the events to.
 */
public boolean isEmpty() {
	return listeners.isEmpty();
}

/**
 * Removes the given TransferDragSourceListener from the list of listeners.
 */
public void removeDragSourceListener(TransferDragSourceListener listener) {
	listeners.remove(listener);
}

}