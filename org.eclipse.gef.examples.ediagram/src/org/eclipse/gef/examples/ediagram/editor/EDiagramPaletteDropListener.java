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

import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.dnd.TemplateTransferDropTargetListener;
import org.eclipse.gef.requests.CreationFactory;

/**
 * @author Pratik Shah
 * @since 3.1
 */
public class EDiagramPaletteDropListener
	extends TemplateTransferDropTargetListener
{
	
public EDiagramPaletteDropListener(EditPartViewer viewer) {
	super(viewer);
}

protected CreationFactory getFactory(Object template) {
	return (CreationFactory)template;
}

}