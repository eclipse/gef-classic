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

import org.eclipse.gef.ui.actions.GEFActionConstants;

import org.eclipse.gef.examples.text.model.Container;
import org.eclipse.gef.examples.text.model.InlineContainer;
import org.eclipse.gef.examples.text.model.ModelLocation;
import org.eclipse.gef.examples.text.model.TextRun;

/**
 * Applies a boolean style such as BOLD to a range in the model.
 * @since 3.1
 */
public class ApplyStyle extends MiniEdit {

private TextRun begin;
private int beginOffset;
private int endOffset;
private Container container;
private TextRun right;
private TextRun middle;
String[] keys;
Object[] values;

public ApplyStyle(ModelLocation start, ModelLocation end,
		String keys[], Object values[]) {
	begin = (TextRun)start.model;
	beginOffset = start.offset;
	endOffset = end.offset;
	this.keys = keys;
	this.values = values;
}

public void apply() {
	right = begin.subdivideRun(endOffset);
	middle = begin.subdivideRun(beginOffset); 
	container = new InlineContainer(Container.TYPE_INLINE);
	for (int i = 0; i < keys.length; i++) {
		String key = keys[i];
		if (key.equals(GEFActionConstants.STYLE_BOLD))
			container.getStyle().setBold(((Boolean)values[i]).booleanValue());
		else if (key.equals(GEFActionConstants.STYLE_FONT_SIZE))
			container.getStyle().setFontHeight(((Integer)values[i]).intValue());
		else if (key.equals(GEFActionConstants.STYLE_ITALIC))
			container.getStyle().setItalic(((Boolean)values[i]).booleanValue());
		else if (key.equals(GEFActionConstants.STYLE_UNDERLINE))
			container.getStyle().setUnderline(((Boolean)values[i]).booleanValue());
		else if (key.equals(GEFActionConstants.STYLE_FONT_NAME))
			container.getStyle().setFontFamily((String)values[i]);
	}
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
