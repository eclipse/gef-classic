/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef;

import org.eclipse.ui.IEditorPart;

/**
 * A default implementation of {@link EditDomain}. An {@link IEditorPart} is required 
 * in the constructor, but it can be <code>null</code>.
 */
public class DefaultEditDomain
	extends EditDomain
{

private IEditorPart editorPart;

/**
 * Constructs a DefaultEditDomain with the specified IEditorPart
 * @param editorPart <code>null</code> or an IEditorPart
 */	
public DefaultEditDomain(IEditorPart editorPart) {
	setEditorPart(editorPart);
}

/** * @return the IEditorPart for this EditDomain */
public IEditorPart getEditorPart() {
	return editorPart;
}

/**
 * Sets the IEditorPart for this EditDomain.
 * @param editorPart the editor */
protected void setEditorPart(IEditorPart editorPart) {
	this.editorPart = editorPart;
}


}
