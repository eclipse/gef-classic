/*****************************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: IBM Corporation - initial API and implementation
 ****************************************************************************************/

package org.eclipse.gef.examples.text.model.commands;

import org.eclipse.jface.util.Assert;

import org.eclipse.gef.examples.text.model.ModelLocation;
import org.eclipse.gef.examples.text.model.TextRun;

/**
 * @since 3.1
 */
public class InsertString extends MiniEdit {

private String pending;

private char insertedChars[];

private final int offset;

private final TextRun run;

public InsertString(TextRun run, String c, int offset) {
	this.run = run;
	this.pending = c;
	this.offset = offset;
}

/**
 * @since 3.1
 * @param char1
 */
public void appendText(String append) {
	Assert.isTrue(pending == null);
	pending = append;
}

/**
 * @see org.eclipse.gef.commands.Command#execute()
 */
public void apply() {
	run.insertText(pending, offset);
	insertedChars = pending.toCharArray();
	pending = null;
}

public boolean canApply() {
	return pending != null;
}

/**
 * re-executes the command for the additional character added above.
 */
public void commitPending() {
	run.insertText(pending, offset + insertedChars.length);
	char old[] = insertedChars;
	insertedChars = new char[old.length + 1];
	System.arraycopy(old, 0, insertedChars, 0, old.length);
	insertedChars[insertedChars.length - 1] = pending.toCharArray()[0];
	pending = null;
}

public ModelLocation getResultingLocation() {
	return new ModelLocation(run, offset + insertedChars.length);
}

public void reapply() {
	run.insertText(new String(insertedChars), offset);
}

public void rollback() {
	run.removeRange(offset, insertedChars.length);
}

}