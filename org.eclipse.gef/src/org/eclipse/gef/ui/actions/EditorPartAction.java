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
package org.eclipse.gef.ui.actions;

import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPart;

import org.eclipse.gef.Disposable;

/**
 * @author hudsonr
 */
public abstract class EditorPartAction
	extends WorkbenchPartAction
	implements Disposable, UpdateAction
{

/**
 * @param editor The editor to be associated with this action.
 */
public EditorPartAction(IEditorPart editor) {
	super(editor);
}

/**
 * Used internally to avoid deprecation warnings in GEF subclasses.
 * @param part the part
 */
EditorPartAction(IWorkbenchPart part) {
	super(part);
}

/**
 * Returns the editor associated with this action.
 * @return the Editor part
 */
protected IEditorPart getEditorPart() {
	return (IEditorPart)getWorkbenchPart();
}

/**
 * Sets the editor.
 * @param part the editorpart
 */
protected void setEditorPart(IEditorPart part) {
	setWorkbenchPart(part);
}

}