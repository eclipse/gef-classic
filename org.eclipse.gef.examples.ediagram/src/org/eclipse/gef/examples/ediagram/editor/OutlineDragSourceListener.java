/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.examples.ediagram.editor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.dnd.DragSourceEvent;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.dnd.AbstractTransferDragSourceListener;

/**
 * @author Pratik Shah
 * @since 3.1
 */
public class OutlineDragSourceListener 
	extends AbstractTransferDragSourceListener
{
	
private List transferData;

public OutlineDragSourceListener(EditPartViewer viewer) {
	super(viewer, OutlineToDiagramTransfer.getInstance());
}

public void dragFinished(DragSourceEvent event) {
	transferData = null;
	OutlineToDiagramTransfer.getInstance().setObject(null);
}

public void dragSetData(DragSourceEvent event) {
	event.data = getSelection();
}

public void dragStart(DragSourceEvent event) {
	List data = getSelection();
	if (data.isEmpty())
		event.doit = false;
	OutlineToDiagramTransfer.getInstance().setObject(data);
}

protected List getSelection() {
	if (transferData != null)
		return transferData;
	
	List selection = getViewer().getSelectedEditParts();
	transferData = new ArrayList();
	for (int i = 0; i < selection.size(); i++) {
		transferData.add(((EditPart)selection.get(i)).getModel());
	}
	return transferData;
}

}