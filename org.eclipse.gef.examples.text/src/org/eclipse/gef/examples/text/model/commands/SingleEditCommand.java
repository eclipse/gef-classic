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

import org.eclipse.gef.examples.text.GraphicalTextViewer;
import org.eclipse.gef.examples.text.SelectionRange;
import org.eclipse.gef.examples.text.TextLocation;
import org.eclipse.gef.examples.text.model.ModelLocation;

/**
 * @since 3.1
 */
public class SingleEditCommand extends ExampleTextCommand {

	private final MiniEdit edit;
	private final ModelLocation end;
	private final ModelLocation start;

	/**
	 * @param label
	 * @since 3.1
	 */
	public SingleEditCommand(MiniEdit edit, ModelLocation start, ModelLocation end) {
		super(""); //$NON-NLS-1$
		this.edit = edit;
		this.start = start;
		this.end = end;
	}

	@Override
	public void execute() {
		edit.apply();
	}

	@Override
	public SelectionRange getExecuteSelectionRange(GraphicalTextViewer viewer) {
		ModelLocation loc = edit.getResultingLocation();
		if (loc != null)
			return new SelectionRange(lookupModel(viewer, loc.model), loc.offset);
		return getUndoSelectionRange(viewer);
	}

	@Override
	public SelectionRange getRedoSelectionRange(GraphicalTextViewer viewer) {
		return getExecuteSelectionRange(viewer);
	}

	@Override
	public SelectionRange getUndoSelectionRange(GraphicalTextViewer viewer) {
		TextLocation startLoc = new TextLocation(lookupModel(viewer, start.model), start.offset);
		TextLocation endLoc = new TextLocation(lookupModel(viewer, end.model), end.offset);
		return new SelectionRange(startLoc, endLoc);
	}

	@Override
	public void redo() {
		edit.reapply();
	}

	@Override
	public void undo() {
		edit.rollback();
	}

}