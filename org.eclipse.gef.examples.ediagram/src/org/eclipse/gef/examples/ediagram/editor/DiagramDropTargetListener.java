/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.examples.ediagram.editor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTargetEvent;

import org.eclipse.emf.ecore.EReference;

import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.dnd.AbstractTransferDropTargetListener;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.gef.requests.CreationFactory;

import org.eclipse.gef.examples.ediagram.model.ModelFactory;
import org.eclipse.gef.examples.ediagram.outline.InheritanceModel;

/**
 * @author Pratik Shah
 * @since 3.1
 */
public class DiagramDropTargetListener 
	extends AbstractTransferDropTargetListener
{
	
private Factory factory;
	
public DiagramDropTargetListener(EditPartViewer viewer) {
	super(viewer, OutlineToDiagramTransfer.getInstance());
}

protected Request createTargetRequest() {
	CreateRequest req = new CreateRequest();
	List transfer = (List)((OutlineToDiagramTransfer)getTransfer()).getObject();
	req.getExtendedData().put(OutlineToDiagramTransfer.TYPE_NAME, transfer);
	if (factory == null)
		factory = new Factory();
	factory.setTransferredObjects(transfer);
	req.setFactory(factory);
	return req;
}

protected void handleDragOver() {
	getCurrentEvent().detail = DND.DROP_COPY;
	super.handleDragOver();
}

// Over-ridden so that the cursor can be updated to "disallow" when the command is 
// not executable
public boolean isEnabled(DropTargetEvent event) {
	boolean result = super.isEnabled(event);
	if (result) {
		updateTargetRequest();
		updateTargetEditPart();
		Command cmd = getCommand();
		result = cmd != null && cmd.canExecute();
	}
	return result;
}

protected void updateTargetRequest() {
	CreateRequest req = (CreateRequest)getTargetRequest();
	req.setLocation(getDropLocation());
}

protected static class Factory implements CreationFactory {
	private List transfer, result;
	public Object getNewObject() {
		if (result != null)
			return result;
		result = new ArrayList();
		for (Iterator iter = transfer.iterator(); iter.hasNext();) {
			Object element = iter.next();
			if (element instanceof EReference) {
				result.add(ModelFactory.eINSTANCE.createReferenceView());
			} else if (element instanceof InheritanceModel) {
				result.add(ModelFactory.eINSTANCE.createInheritanceView());
			} else {
				result.add(ModelFactory.eINSTANCE.createNamedElementView());
			}
		}
		return result;
	}
	public Object getObjectType() {
		return null;
	}
	public void setTransferredObjects(List transfer) {
		if (this.transfer != transfer) {
			this.transfer = transfer;
			result = null;
		}
	}
}

}
