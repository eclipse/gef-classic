package com.ibm.etools.gef.dnd;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.util.*;

import org.eclipse.jface.util.SafeRunnable;
import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.dnd.*;

import com.ibm.etools.gef.GEF;

/**
 * A DropTargetListener that allows many {@link TransferDropTargetListener}s to be added 
 * to it.  When the DropTarget fires a DropTargetEvent, this listener calculates which 
 * one of the added listeners is able to complete the drop operation and forwards the 
 * event to that listener.
 */
public class DelegatingDropAdapter 
	implements DropTargetListener 
{

private List listeners = new ArrayList();
private TransferDropTargetListener currentListener;
private int origDropType;

/**
 * Adds the given TransferDropTargetListener to the list of listeners.
 */
public void addDropTargetListener(TransferDropTargetListener listener) {
	listeners.add(listener);
}

/**
 * Updates the current listener and forwards this event to that listener.
 * 
 * @see DropTargetListener#dragEnter(DropTargetEvent)
 */
public void dragEnter(final DropTargetEvent event) {
	if (GEF.DebugDND)
		GEF.debug("Drag Enter: " + toString()); //$NON-NLS-1$
	setOriginalDropType(event.detail);
	Platform.run(new SafeRunnable(){
		public void run() throws Exception {
			updateCurrentListener(event);
			if (getCurrentListener() != null)
				getCurrentListener().dragEnter(event);
		}
	});
}

/**
 * Forwards this event to the current listener.
 * 
 * @see DropTargetListener#dragLeave(DropTargetEvent)
 */
public void dragLeave(final DropTargetEvent event) {
	if (GEF.DebugDND)
		GEF.debug("Drag Leave: " + toString()); //$NON-NLS-1$
	if (getCurrentListener() != null)
		Platform.run(new SafeRunnable(){
			public void run() throws Exception {
				getCurrentListener().dragLeave(event);
			}
		});
//Cannot do this because dragLeave actually happens right before a Drop.
//	setCurrentListener(null);
}

/**
 * Updates the current listener and forwards this event to that listener.
 * 
 * @see DropTargetListener#dragOperationChanged(DropTargetEvent)
 */
public void dragOperationChanged(final DropTargetEvent event) {
	if (GEF.DebugDND)
		GEF.debug("Drag Operation Changed to: " + event.detail); //$NON-NLS-1$
	setOriginalDropType(event.detail);
	Platform.run(new SafeRunnable() {
		public void run() throws Exception {
			// Should we send the change even if the current listener
			// is new and came after the change?
			updateCurrentListener(event);
			if (getCurrentListener() != null)
				getCurrentListener().dragOperationChanged(event);
		}
	});
}

/**
 * Updates the current listener and forwards this event to that listener.  If
 * the current listener is <code>null</code>, <code>event.detail</code> is set
 * to <code>DND.DROP_NONE</code>, which causes the NOT cursor to appear.
 * 
 * @see DropTargetListener#dragOver(DropTargetEvent)
 */
public void dragOver(final DropTargetEvent event) {
	updateCurrentListener(event);
	if (getCurrentListener() != null){
		Platform.run(new SafeRunnable() {
			public void run() throws Exception {
				getCurrentListener().dragOver(event);
			}
		});
	} else
		event.detail = DND.DROP_NONE;
}

/**
 * Forwards this event to the current listener, if it is not <code>null</code>.
 * Then sets the current listener to <code>null</code>.
 * 
 * @see DropTargetListener#drop(DropTargetEvent)
 */
public void drop(final DropTargetEvent event) {
	if (GEF.DebugDND)
		GEF.debug("Drop: " + toString()); //$NON-NLS-1$
	if (getCurrentListener() != null){
		Platform.run(new SafeRunnable() {
			public void run() throws Exception {
				getCurrentListener().drop(event);
			}
		});
	}
	setCurrentListener(null);
}

/**
 * Forwards this event to the current listener.
 * 
 * @see DropTargetListener#dropAccept(DropTargetEvent)
 */
public void dropAccept(final DropTargetEvent event) {
	if (GEF.DebugDND)
		GEF.debug("Drop Accept: " + toString()); //$NON-NLS-1$
	if (getCurrentListener() != null){
		Platform.run(new SafeRunnable() {
			public void run() throws Exception {
				getCurrentListener().dropAccept(event);
			}
		});
	}
}

/*
 * Returns the current listener.
 */
private TransferDropTargetListener getCurrentListener() {
	return currentListener;
}

/*
 * Returns the original drop type.
 */
private int getOriginalDropType() {
	return origDropType;
}

/**
 * Adds the Transfer from each listener to an array and returns that array.
 */
public Transfer[] getTransferTypes() {
	Transfer[] types = new Transfer[listeners.size()];
	for (int i=0; i<listeners.size(); i++) {
		TransferDropTargetListener listener = (TransferDropTargetListener)listeners.get(i);
		types[i] = listener.getTransfer();
	}
	return types;
}

/**
 * Returns <code>true</code> if there are no listeners to delegate events to.
 */
public boolean isEmpty() {
	return listeners.isEmpty();
}

/**
 * Removes the given TransferDropTargetListener from the list of listeners.
 */
public void removeDropTargetListener(TransferDropTargetListener listener) {
	listeners.remove(listener);
}

/*
 * Returns <code>true</code> if the new listener is different than the previous
 */
private boolean setCurrentListener(TransferDropTargetListener listener) {
	if (currentListener == listener)
		return false;
	if (currentListener != null)
		currentListener.deactivate();
	currentListener = listener;
	if (GEF.DebugDND)
		GEF.debug("Current listener " + listener); //$NON-NLS-1$
	if (currentListener != null)
		currentListener.activate();
	return true;
}

/*
 * Caches the original drop type.
 * 
 * @see #dragEnter(DropTargetEvent event)
 * @see #dragOperationChanged(DropTargetEvent event)
 */
private void setOriginalDropType(int type) {
	origDropType = type;
}

/*
 * Updates the current listener to one that can handle the drop.  There can be many listeners 
 * and each listener may be able to handle many TransferData types.  The first listener found 
 * that can handle a drop of one of the given TransferData types will be selected.
 */
private void updateCurrentListener(DropTargetEvent event) {
	int temp = event.detail;
	event.detail = getOriginalDropType();

	Iterator iter = listeners.iterator();
	while (iter.hasNext()) {
		TransferDropTargetListener listener = (TransferDropTargetListener)iter.next();
		if (listener.isEnabled(event)) {
			if (!setCurrentListener(listener))
				event.detail = temp;
			return;
		}
	}
	setCurrentListener(null);
	event.detail = DND.DROP_NONE;
}

}
