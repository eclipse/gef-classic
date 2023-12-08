/*******************************************************************************
 * Copyright (c) 2000, 2022 IBM Corporation and others.
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
package org.eclipse.gef.examples.logicdesigner.dnd;

import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;

import org.eclipse.gef.EditPartViewer;

import org.eclipse.gef.examples.logicdesigner.edit.LogicLabelEditPart;

public class FileTransferDragSourceListener extends org.eclipse.gef.dnd.AbstractTransferDragSourceListener {

	public FileTransferDragSourceListener(EditPartViewer viewer) {
		super(viewer, TextTransfer.getInstance());
	}

	public FileTransferDragSourceListener(EditPartViewer viewer, Transfer xfer) {
		super(viewer, xfer);
	}

	@Override
	public void dragSetData(DragSourceEvent event) {
		event.data = "Some text"; //$NON-NLS-1$
	}

	@Override
	public void dragStart(DragSourceEvent event) {
		if (getViewer().getSelectedEditParts().get(0) instanceof LogicLabelEditPart)
			return;
		event.doit = false;
	}

}
