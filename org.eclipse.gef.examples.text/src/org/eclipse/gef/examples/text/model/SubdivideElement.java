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

/**
 * Divides a TextRun into itself and another TextRun.
 * @since 3.1
 */
public class SubdivideElement extends MiniEdit {


private final TextRun run;
private final int offset;
private TextRun inserted;

public SubdivideElement(TextRun run, int offset) {
	this.run = run;
	this.offset = offset;	
}

public void apply() {
	inserted = run.subdivideRun(offset);
	int index = run.getContainer().getChildren().indexOf(run);
	run.getContainer().add(inserted, index + 1);
}

public void reapply() {
	throw new RuntimeException("Need to implement");
}

public ModelLocation getResultingLocation() {
	return new ModelLocation(inserted, 0);
}

public void rollback() {
	inserted.getContainer().remove(inserted);
	run.insertText(inserted.getText(), run.size());
	inserted.setText("");
}

}
