/*******************************************************************************
 * Copyright (c) 2004, 2023 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.gef.examples.text.model.commands;

import org.eclipse.gef.examples.text.edit.TextEditPart;
import org.eclipse.gef.examples.text.model.Container;
import org.eclipse.gef.examples.text.model.ModelElement;
import org.eclipse.gef.examples.text.model.ModelLocation;
import org.eclipse.gef.examples.text.model.TextRun;

/**
 * Attempts to merge an element with the previous element which will accept it.
 * 
 * @since 3.1
 */
public class MergeWithPrevious extends MiniEdit {

	private final int index;
	private final Container container;
	private final TextRun run;

	public MergeWithPrevious(TextEditPart part) {
		run = (TextRun) part.getModel();
		container = run.getContainer();
		index = container.getChildren().indexOf(run);
	}

	private TextRun getPreviousTextRun() {
		ModelElement candidate = container.getChildren().get(index - 1);
		while (candidate instanceof Container cont) {
			candidate = cont.getChildren().get(cont.size() - 1);
		}
		return (TextRun) candidate;
	}

	@Override
	public void apply() {
		container.remove(run);
		TextRun previous = getPreviousTextRun();
		previous.insertText(run.getText(), previous.size());
	}

	@Override
	public boolean canApply() {
		return index > 0;
	}

	@Override
	public ModelLocation getResultingLocation() {
		TextRun previous = getPreviousTextRun();
		return new ModelLocation(previous, previous.size() - run.size());
	}

	@Override
	public void rollback() {
		throw new RuntimeException("not implemented"); //$NON-NLS-1$
	}

}
