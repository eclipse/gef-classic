/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.examples.flow.dnd;

import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.dnd.TemplateTransferDropTargetListener;
import org.eclipse.gef.requests.CreationFactory;

import org.eclipse.gef.examples.flow.model.FlowElementFactory;

/**
 * Provides a listener for dropping templates onto the flow editor.
 * @author Daniel Lee
 */
public class FlowTemplateTransferDropTargetListener
	extends TemplateTransferDropTargetListener 
{

/**
 * Creates a new FlowTemplateTransferDropTargetListener associated with the given
 * viewer.
 * @param viewer the viewer
 */
public FlowTemplateTransferDropTargetListener(EditPartViewer viewer) {
	super(viewer);
}

/**
 * @see org.eclipse.gef.dnd.TemplateTransferDropTargetListener#getFactory(java.lang.Object)
 */
protected CreationFactory getFactory(Object template) {
	return new FlowElementFactory(template);
}

}
