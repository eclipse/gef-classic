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
package org.eclipse.gef.examples.logicdesigner.actions;

import org.eclipse.ui.IEditorPart;

import org.eclipse.draw2d.geometry.Point;

import org.eclipse.gef.requests.CreationFactory;

import org.eclipse.gef.examples.logicdesigner.model.LogicElementFactory;

/**
 * @author Eric Bordeau
 */
public class LogicPasteTemplateAction extends PasteTemplateAction {

/**
 * Constructor for LogicPasteTemplateAction.
 * @param editor
 */
public LogicPasteTemplateAction(IEditorPart editor) {
	super(editor);
}

/**
 * @see org.eclipse.gef.ui.actions.PasteTemplateAction#getFactory(java.lang.Object)
 */
protected CreationFactory getFactory(Object template) {
	return new LogicElementFactory((String)template);
}

/**
 * 
 * @see org.eclipse.gef.examples.logicdesigner.actions.PasteTemplateAction#getPasteLocation()
 */
protected Point getPasteLocation() {
	return new Point(10, 10);
}

}
