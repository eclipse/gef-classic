/*****************************************************************************************
 * Copyright (c) 2004 IBM Corporation and others. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Common Public
 * License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors: IBM Corporation - initial API and implementation
 ****************************************************************************************/

package org.eclipse.gef.examples.text.model;


public class RemoveText extends MiniEdit {

int offset;

char chars[];

private final TextRun run;

public RemoveText(TextRun run, int begin, int end) {
	this.run = run;
	this.offset = begin;
	this.chars = run.getText().substring(offset, end).toCharArray();
}

public void apply() {
	run.removeRange(offset, chars.length);
}

public ModelLocation getResultingLocation() {
	return new ModelLocation(run, offset);
}

public void reapply() {
	apply();
}

public void rollback() {
	run.insertText(new String(chars), offset);
}

}