package org.eclipse.gef.dnd;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.swt.dnd.*;
import org.eclipse.gef.*;

/**
 * An abstract implementation of <code>TransferDragSourceListener</code>
 * associated with an {@link EditPartViewer}
 */
public abstract class AbstractTransferDragSourceListener
	implements TransferDragSourceListener
{

private EditPartViewer viewer;
private Transfer transfer;

/**
 * Constructs an AbstractTransferDragSourceListener for the given EditPartViewer.
 * @param viewer the EditPartViewer
 */
public AbstractTransferDragSourceListener(EditPartViewer viewer) {
	setViewer(viewer);
}

/**
 * Constructs an AbstractTransferDragSourceListener with the specified EditPartViewer and
 * Transfer.
 * @param viewer the EditPartViewer
 * @param xfer the Transfer
 */
public AbstractTransferDragSourceListener(EditPartViewer viewer, Transfer xfer) {
	setViewer(viewer);
	setTransfer(xfer);
}

/** 
 * @see DragSourceListener#dragFinished(DragSourceEvent)
 */
public void dragFinished(DragSourceEvent event) { }

/**
 * @see DragSourceListener#dragStart(DragSourceEvent)
 */
public void dragStart(DragSourceEvent event) { }

/**
 * @see TransferDragSourceListener#getTransfer()
 */
public Transfer getTransfer() {
	return transfer;
}

/**
 * Returns the <code>EditPartViewer</code>.
 * @return the EditPartViewer
 */
protected EditPartViewer getViewer() {
	return viewer;
}

/**
 * Sets the <code>Transfer</code> for this listener.
 * @param xfer the Transfer
 */
protected void setTransfer(Transfer xfer) {
	transfer = xfer;
}

/**
 * Sets the EditPartViewer for this listener.
 * @param viewer the EditPartViewer
 */
protected void setViewer(EditPartViewer viewer) {
	this.viewer = viewer;
}

}