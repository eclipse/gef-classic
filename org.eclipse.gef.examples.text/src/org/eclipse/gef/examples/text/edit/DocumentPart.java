/*****************************************************************************************
 * Copyright (c) 2004 IBM Corporation and others. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Common Public
 * License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors: IBM Corporation - initial API and implementation
 ****************************************************************************************/

package org.eclipse.gef.examples.text.edit;

import org.eclipse.gef.editparts.AbstractEditPart;

/**
 * @since 3.1
 */
public class DocumentPart extends CompoundTextualEditPart {

/**
 * @since 3.1
 */
public DocumentPart(Object model) {
	super(model);
}

/**
 * @see AbstractEditPart#createEditPolicies()
 */
protected void createEditPolicies() {
	installEditPolicy("Text Editing", new BlockEditPolicy());
}

}