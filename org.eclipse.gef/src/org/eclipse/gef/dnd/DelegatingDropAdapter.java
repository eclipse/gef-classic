package org.eclipse.gef.dnd;
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

import org.eclipse.gef.GEF;

/**
 * A <code>DropTargetListener</code> that manages and delegates to a set of {@link
 * TransferDropTargetListener}s. Each <code>TransferDropTargetListener</code> can then be
 * implemented as if it were the DropTarget's only DropTargetListener.
 * <P>
 * On each DropTargetEvent, a <i>current</i> listener is obtained from the set of all
 * TransferDropTargetListers. The current listener is the first listener to return
 * <code>true</code> for {@link TransferDropTargetListener#isEnabled(DropTargetEvent)}.
 * The current listener is forwarded all <code>DropTargetEvents</code> until some other
 * listener becomes the current listener, or the Drop terminates.
 * <P>
 * As listeners are added and removed, the combined set of Transfers is updated to contain
 * the <code>Tranfer</code> from each listener. {@link #getTransferTypes()} provides the
 * merged transfers. This set of Transfers should be set on the SWT {@link
 * org.eclipse.swt.dnd.DropTarget}.
 */
public class DelegatingDropAdapter 
	implements DropTargetListener 
{

private List listeners = new ArrayList();
private TransferDropTargetListener currentListener;
private int origDropType;

/**
 * Adds the given TransferDropTargetListener.
 * @param listener the listener
 */
public void addDropTargetListener(TransferDropTargetListener listener) {
	listeners.add(listener);
}

/**
 * The cursor has entered the drop target boundaries. The current listener is updated, and
 * dragEnter() is forwarded to the current listener.
 * @see DropTargetListener#dragEnter(DropTargetEvent) for more details
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
 * The cursor has left the drop target boundaries. The event is forwarded to the current
 * listener.
 * @param event the Event
 * @see DropTargetListener#dragLeave(DropTargetEvent) for more details
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
 * The operation being performed has changed (usually due to the user changing the
 * selected key while dragging). Updates the current listener and forwards this event to
 * that listener.
 * @param event the Event
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
 * The cursor is moving over the drop target. Updates the current listener and forwards
 * this event to that listener.  If the current listener is <code>null</code>,
 * <code>event.detail</code> is set to <code>DND.DROP_NONE</code>, which causes the NOT
 * cursor to appear.
 * @param event the Event
 * @see DropTargetListener#dragOver(DropTargetEvent)
 */
public void dragOver(final DropTargetEvent event) {
	updateCurrentListener(event);
	if (getCurrentListener() != null) {
		Platform.run(new SafeRunnable() {
			public void run() throws Exception {
				getCurrentListener().dragOver(event);
			}
		});
	} else
		event.detail = DND.DROP_NONE;
}

/**
 * Forwards this event to the current listener, if it is not <code>null</code>. Sets the
 * current listener to <code>null</code> afterwards.
 * @param event the Event
 * @see DropTargetListener#drop(DropTargetEvent)
 */
public void drop(final DropTargetEvent event) {
	if (GEF.DebugDND)
		GEF.debug("Drop: " + toString()); //$NON-NLS-1$
	if (getCurrentListener() != null) {
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
 * @param event the Event
 * @see DropTargetListener#dropAccept(DropTargetEvent)
 */
public void dropAccept(final DropTargetEvent event) {
	if (GEF.DebugDND)
		GEF.debug("Drop Accept: " + toString()); //$NON-NLS-1$
	if (getCurrentListener() != null) {
		Platform.run(new SafeRunnable() {
			public void run() throws Exception {
				getCurrentListener().dropAccept(event);
			}
		});
	}
}

/**
 * Returns the current listener.
 */
private TransferDropTargetListener getCurrentListener() {
	return currentListener;
}

/**
 * Returns the original drop type.
 */
private int getOriginalDropType() {
	return origDropType;
}

/**
 * Adds the Transfer from each listener to an array and returns that array.
 * @return the merged Transfers from all listeners
 */
public Transfer[] getTransferTypes() {
	Transfer[] types = new Transfer[listeners.size()];
	for (int i = 0; i < listeners.size(); i++) {
		TransferDropTargetListener listener = (TransferDropTargetListener)listeners.get(i);
		types[i] = listener.getTransfer();
	}
	return types;
}

/**
 * Returns <code>true</code> if there are no listeners.
 * @return true if there are no listeners
 */
public boolean isEmpty() {
	return listeners.isEmpty();
}

/**
 * Removes the given <code>TransferDropTargetListener</code>.
 * @param listener the listener
 */
public void removeDropTargetListener(TransferDropTargetListener listener) {
	if (currentListener == listener)
		setCurrentListener(null);
	listeners.remove(listener);
}

/**
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

/**
 * Caches the original drop type.
 * 
 * @see #dragEnter(DropTargetEvent event)
 * @see #dragOperationChanged(DropTargetEvent event)
 */
private void setOriginalDropType(int type) {
	origDropType = type;
}

/**
 * Updates the current listener to one that can handle the drop.  There can be many listeners 
 * and each listener may be able to handle many TransferData types.  The first listener found 
 * that can handle a drop of one of the given TransferData types will be selected.
 */
private void updateCurrentListener(DropTargetEvent event) {
	int temp = event.detail;
	//Revert the detail to the "original" drop type that the User indicated.
	//this is necessary because the previous listener may have changed the detail to
	//something other than what the User indicated.
	event.detail = getOriginalDropType();

	Iterator iter = listeners.iterator();
	while (iter.hasNext()) {
		TransferDropTargetListener listener = (TransferDropTargetListener)iter.next();
		if (listener.isEnabled(event)) {
			//If the listener stays the same, undo the reverted detail that was done
			//At the beginning of this method
			if (!setCurrentListener(listener))
				event.detail = temp;
			return;
		}
	}
	setCurrentListener(null);
	event.detail = DND.DROP_NONE;
}

}
