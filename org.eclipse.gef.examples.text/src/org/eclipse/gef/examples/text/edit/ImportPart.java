/*******************************************************************************
 * Copyright (c) 2004, 2010 IBM Corporation and others.
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
import org.eclipse.gef.examples.text.model.TextRun;

/**
 * @since 3.1
 */
public class ImportPart extends TextFlowPart {

	public ImportPart(TextRun model) {
		super(model);
	}

	@Override
	protected IFigure createFigure() {
		TextFlow flow = new TextFlow();
		BlockFlow block = new BlockFlow();
		block.setBorder(new TreeItemBorder(Images.IMPORT));
		block.add(flow);
		return block;
	}

	@Override
	TextFlow getTextFlow() {
		return (TextFlow) getFigure().getChildren().get(0);
	}

}
