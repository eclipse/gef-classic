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
package org.eclipse.gef.dnd;

import java.util.*;

import org.eclipse.swt.dnd.*;

import org.eclipse.gef.GEF;

/**
 * A DragSourceListener that maintains and delegates to a set of {@link
 * TransferDragSourceListener}s. Each TransferDragSourceListener can then be implemented
 * as if it were the DragSource's only DragSourceListener.
 * <P>
 * When a native Drag is started, a subset of all <code>TransferDragSourceListeners</code>
 * is generated and stored in a list of <i>active</i> listeners. This subset is calculated
 * by forwarding {@link DragSourceListener#dragStart(DragSourceEvent) to every listener,
 * and inspecting changes to the {@link DragSourceEvent#doit doit} field. The
 * <code>DragSource</code>'s set of supported Transfer types ({@link
 * DragSource#setTransfer(Transfer[])}) is updated to reflect the Transfer types
 * corresponding to the active listener subset.
 * <P>
 * If and when {@link #dragSetData(DragSourceEvent)} is called, a single
 * <code>TransferDragSourceListener</code> is chosen, and only it is allowed to set the
 * drag data. The chosen listener is the first listener in the subset of active listeners
 * whose Transfer supports ({@link Transfer#isSupportedType(TransferData)}) the dataType
 * on the <code>DragSourceEvent</code>.
 */
public class DelegatingDragAdapter 
	implements DragSourceListener 
{

private List listeners = new ArrayList();
private List activeListeners = new ArrayList();
private TransferDragSourceListener currentListener;

/**
 * Adds the given TransferDragSourceListener. The set of Transfer types is updated to
 * reflect the change.
 * @param listener the new listener
 */
public void addDragSourceListener(TransferDragSourceListener listener) {
	listeners.add(listener);
}

/**
 * The drop has successfully completed. This event is Forwarded to the current listener.
 * Doesn't update the current listener, since the current listener  is already the one
 * that completed the drag operation.
 * 
 * @see DragSourceListener#dragFinished(DragSourceEvent)
 */
public void dragFinished(DragSourceEvent event) {
	if (GEF.DebugDND)
		GEF.debug("Drag Finished: " + toString()); //$NON-NLS-1$
	// if there is a listener that can handle the drop, delegate the event
	if (currentListener != null)
		currentListener.dragFinished(event);
	else {
		// The drag was canceled and currentListener was never set, so send the
		// dragFinished event to all the active listeners. 
		Iterator iter = activeListeners.iterator();
		while (iter.hasNext())
			((TransferDragSourceListener)iter.next()).dragFinished(event);
	}
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
	// the listeners may change event.doit, so save the original value
	boolean doitOriginal = event.doit;
	boolean doitFinal = false;// true if any one of the listeners can handle the drag
	List transfers = new ArrayList(listeners.size());
	
	for (int i = 0; i < listeners.size(); i++) {
		TransferDragSourceListener listener = (TransferDragSourceListener)listeners.get(i);
		// restore event.doit to its original value
		event.doit = doitOriginal;
		listener.dragStart(event);
		if (event.doit) { //Equivalent to "The listener is enabled for this drag"
			transfers.add(listener.getTransfer());
			activeListeners.add(listener);
		}
		doitFinal |= event.doit;
	}
	
	if (doitFinal)
		((DragSource)event.widget).setTransfer(
			(Transfer[])transfers.toArray(new Transfer[transfers.size()]));
	
	event.doit = doitFinal;
}

/**
 * Combines the <code>Transfer<code>s from every TransferDragSourceListener.
 * @return the combined <code>Transfer</code>s
 */
public Transfer[] getTransferTypes() {
	Transfer[] types = new Transfer[listeners.size()];
	for (int i = 0; i < listeners.size(); i++) {
		TransferDragSourceListener listener = (TransferDragSourceListener)listeners.get(i);
		types[i] = listener.getTransfer();
	}
	return types;
}

/**
 * Returns <code>true</code> if there are no listeners to delegate the events to.
 * @return <code>true</code> if there are no <code>TransferDragSourceListeners</code>
 */
public boolean isEmpty() {
	return listeners.isEmpty();
}

/**
 * Adds the given TransferDragSourceListener. The set of Transfer types is updated to
 * reflect the change.
 * @param listener the listener being removed
 */
public void removeDragSourceListener(TransferDragSourceListener listener) {
	listeners.remove(listener);
	if (currentListener == listener)
		currentListener = null;
	if (activeListeners.contains(listener))
		activeListeners.remove(listener);
}

/**
 * Updates the current listener to one that can handle the drag. There can be many
 * listeners  and each listener may be able to handle many TransferData types.  The first
 * listener found  that can handle a drag of one of the given TransferData types will be
 * selected.
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

}