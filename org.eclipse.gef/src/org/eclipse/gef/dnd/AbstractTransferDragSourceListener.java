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
 * An abstract implementation of TransferDragSourceListener that adds a reference to 
 * the {@link EditPartViewer} that contains the {@link DragSource} widget.
 */
abstract public class AbstractTransferDragSourceListener
	implements TransferDragSourceListener
{

private EditPartViewer viewer;
private Transfer transfer;

/**
 * Creates a new AbstractTransferDragSourceListener with the given EditPartViewer.
 */
public AbstractTransferDragSourceListener(EditPartViewer viewer) {
	setViewer(viewer);
}

/**
 * Creates a new AbstractTransferDragSourceListener with the given EditPartViewer 
 * and Transfer.
 */
public AbstractTransferDragSourceListener(EditPartViewer viewer, Transfer xfer) {
	setViewer(viewer);
	setTransfer(xfer);
}

/** 
 * @see DragSourceListener#dragFinished(DragSourceEvent)
 */
public void dragFinished(DragSourceEvent event) {}

/**
 * @see DragSourceListener#dragStart(DragSourceEvent)
 */
public void dragStart(DragSourceEvent event) {}

/**
 * Returns the Transfer that this listener can handle.
 * 
 * @see TransferDragSourceListener#getTransfer()
 */
public Transfer getTransfer() {
	return transfer;
}

/**
 * Returns the EditPartViewer that is the source of the drag.
 */
protected EditPartViewer getViewer() {
	return viewer;
}

/**
 * Sets the Transfer that this listener can handle.
 */
public void setTransfer(Transfer xfer) {
	transfer = xfer;
}

/**
 * Sets the EditPartViewer that is the source of the drag.
 */
protected void setViewer(EditPartViewer viewer) {
	this.viewer = viewer;
}

}