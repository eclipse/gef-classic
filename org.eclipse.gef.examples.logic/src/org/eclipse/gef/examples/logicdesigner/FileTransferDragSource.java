package org.eclipse.gef.examples.logicdesigner;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.swt.dnd.*;

import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.examples.logicdesigner.edit.LogicLabelEditPart;

public class FileTransferDragSource
	extends org.eclipse.gef.dnd.AbstractTransferDragSourceListener
{

public FileTransferDragSource(EditPartViewer viewer) {
	super(viewer, TextTransfer.getInstance());
}

public FileTransferDragSource(EditPartViewer viewer, Transfer xfer) {
	super(viewer, xfer);
}

public void dragSetData(DragSourceEvent event) {
	event.data = "Some text"; //$NON-NLS-1$
}

public void dragStart(DragSourceEvent event) {
	if (getViewer().getSelectedEditParts().get(0) instanceof LogicLabelEditPart)
		return;
	event.doit = false;
}

}
