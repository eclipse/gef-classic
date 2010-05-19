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

package org.eclipse.gef.examples.text.edit;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.text.BlockFlow;
import org.eclipse.draw2d.text.TextFlow;
import org.eclipse.gef.examples.text.figures.Images;
import org.eclipse.gef.examples.text.figures.TreeItemBorder;

/**
 * @since 3.1
 */
public class ImportPart 
	extends TextFlowPart 
{

public ImportPart(Object model) {
	super(model);
}

protected IFigure createFigure() {
	TextFlow flow = new TextFlow();
	BlockFlow block = new BlockFlow();
	block.setBorder(new TreeItemBorder(Images.IMPORT));
	block.add(flow);
	return block;
}

TextFlow getTextFlow() {
	return (TextFlow)getFigure().getChildren().get(0);
}

}
