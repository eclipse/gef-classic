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

package org.eclipse.gef.examples.text.model;

import org.eclipse.gef.examples.text.edit.TextualEditPart;

/**
 * Attempts to merge an element with the previous element which will accept it.
 * @since 3.1
 */
public class MergeWithPrevious extends MiniEdit {

private int index;
private Container container;
private TextRun run;

public MergeWithPrevious(TextualEditPart part) {
	run = (TextRun)part.getModel();
	container = run.getContainer();
	index = container.getChildren().indexOf(run);
	if (index == 0)
		return;
}

public void apply() {
	container.remove(run);
	TextRun previous = (TextRun)container.getChildren().get(index - 1);
	previous.insertText(run.getText(), previous.size());
}

public ModelLocation getResultingLocation() {
	TextRun previous = (TextRun)container.getChildren().get(index - 1);
	return new ModelLocation(previous, previous.size() - run.size());
}

public void rollback() {
}

public boolean isAllowed() {
	return index > 0;
}

}
