/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.examples.logicdesigner.dnd;

import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.dnd.TemplateTransferDropTargetListener;
import org.eclipse.gef.requests.CreationFactory;

import org.eclipse.gef.examples.logicdesigner.model.LogicElementFactory;

/**
 * This listener handles logic templates that are dropped onto
 * the logic editor.
 * 
 * @author Eric Bordeau
 */
public class LogicTemplateTransferDropTargetListener
	extends TemplateTransferDropTargetListener 
{

public LogicTemplateTransferDropTargetListener(EditPartViewer viewer) {
	super(viewer);
}

protected CreationFactory getFactory(Object template) {
	if (template instanceof String)
		return new LogicElementFactory((String)template);
	return null;
}

}
