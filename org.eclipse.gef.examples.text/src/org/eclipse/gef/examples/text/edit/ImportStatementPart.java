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

package org.eclipse.gef.examples.text.edit;

import org.eclipse.draw2d.IFigure;

import org.eclipse.gef.EditPart;

import org.eclipse.gef.examples.text.figures.Images;
import org.eclipse.gef.examples.text.figures.TreeItemBorder;

/**
 * @since 3.1
 */
public class ImportStatementPart extends TextFlowPart implements EditPart {

/**
 * @since 3.1
 * @param model
 */
public ImportStatementPart(Object model) {
	super(model);
}

protected IFigure createFigure() {
	IFigure f = super.createFigure();
	f.setBorder(new TreeItemBorder(Images.IMPORT));
	return f;
}

}
