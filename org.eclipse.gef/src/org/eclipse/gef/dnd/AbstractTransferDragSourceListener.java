/*******************************************************************************
 * Copyright (c) 2000, 2010 IBM Corporation and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.dnd;

import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.Transfer;

import org.eclipse.gef.EditPartViewer;

/**
 * An abstract implementation of <code>TransferDragSourceListener</code>
 * associated with an {@link EditPartViewer}
 */
public abstract class AbstractTransferDragSourceListener implements TransferDragSourceListener {

	private EditPartViewer viewer;
	private Transfer transfer;

	/**
	 * Constructs an AbstractTransferDragSourceListener for the given
	 * EditPartViewer.
	 *
	 * @param viewer the EditPartViewer
	 */
	public AbstractTransferDragSourceListener(EditPartViewer viewer) {
		setViewer(viewer);
	}

	/**
	 * Constructs an AbstractTransferDragSourceListener with the specified
	 * EditPartViewer and Transfer.
	 *
	 * @param viewer the EditPartViewer
	 * @param xfer   the Transfer
	 */
	public AbstractTransferDragSourceListener(EditPartViewer viewer, Transfer xfer) {
		setViewer(viewer);
		setTransfer(xfer);
	}

	/**
	 * @see org.eclipse.swt.dnd.DragSourceListener#dragFinished(DragSourceEvent)
	 */
	@Override
	public void dragFinished(DragSourceEvent event) {
	}

	/**
	 * @see org.eclipse.swt.dnd.DragSourceListener#dragStart(DragSourceEvent)
	 */
	@Override
	public void dragStart(DragSourceEvent event) {
	}

	/**
	 * @see TransferDragSourceListener#getTransfer()
	 */
	@Override
	public Transfer getTransfer() {
		return transfer;
	}

	/**
	 * Returns the <code>EditPartViewer</code>.
	 *
	 * @return the EditPartViewer
	 */
	protected EditPartViewer getViewer() {
		return viewer;
	}

	/**
	 * Sets the <code>Transfer</code> for this listener.
	 *
	 * @param xfer the Transfer
	 */
	protected void setTransfer(Transfer xfer) {
		transfer = xfer;
	}

	/**
	 * Sets the EditPartViewer for this listener.
	 *
	 * @param viewer the EditPartViewer
	 */
	protected void setViewer(EditPartViewer viewer) {
		this.viewer = viewer;
	}

}
