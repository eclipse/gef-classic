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

package org.eclipse.gef.examples.text.model.commands;

import org.eclipse.gef.examples.text.model.Container;
import org.eclipse.gef.examples.text.model.InlineContainer;
import org.eclipse.gef.examples.text.model.ModelLocation;
import org.eclipse.gef.examples.text.model.TextRun;

/**
 * Applies a boolean style such as BOLD to a range in the model.
 * @since 3.1
 */
public class ApplyBooleanStyle extends MiniEdit {

private TextRun begin;
private int beginOffset;
private int endOffset;
private Container container;
private TextRun right;
private TextRun middle;

public ApplyBooleanStyle(ModelLocation start, ModelLocation end,
		String keys[], Object values[]) {
	begin = (TextRun)start.model;
	beginOffset = start.offset;
	endOffset = end.offset;
}

public void apply() {
	right = begin.subdivideRun(endOffset);
	middle = begin.subdivideRun(beginOffset); 
	container = new InlineContainer(Container.TYPE_INLINE);
	container.getStyle().setBold(true);
	container.add(middle);
	int index = begin.getContainer().getChildren().indexOf(begin) + 1;
	begin.getContainer().add(right, index);
	begin.getContainer().add(container, index);
}

public boolean canApply() {
	return true;
}

public ModelLocation getResultingLocation() {
	return new ModelLocation(middle, middle.size());
}

public void rollback() {
	container.getContainer().remove(container);
	right.getContainer().remove(right);
	begin.insertText(middle.getText(), begin.size());
	begin.insertText(right.getText(), begin.size());
}

}
