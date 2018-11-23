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

package org.eclipse.gef.examples.text.model.commands;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.Assert;
import org.eclipse.gef.examples.text.AppendableCommand;
import org.eclipse.gef.examples.text.GraphicalTextViewer;
import org.eclipse.gef.examples.text.SelectionRange;
import org.eclipse.gef.examples.text.edit.TextEditPart;
import org.eclipse.gef.examples.text.model.ModelLocation;

/**
 * @since 3.1
 */
public class CompoundEditCommand extends ExampleTextCommand implements
		AppendableCommand {

	private ModelLocation beginLocation;

	List edits = new ArrayList();

	private ModelLocation endLocation;

	ArrayList pending;

	/**
	 * @since 3.1
	 * @param label
	 */
	public CompoundEditCommand(String label) {
		super(label);
	}

	/**
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	public void execute() {
		executePending();
	}

	public boolean canExecute() {
		return canExecutePending();
	}

	public boolean canExecutePending() {
		if (pending == null || pending.size() == 0)
			return false;
		for (int i = 0; i < pending.size(); i++) {
			MiniEdit edit = (MiniEdit) pending.get(i);
			if (edit == null || !edit.canApply())
				return false;
		}
		return true;
	}

	public void executePending() {
		Assert.isNotNull(pending);
		for (int i = 0; i < pending.size(); i++) {
			MiniEdit edit = (MiniEdit) pending.get(i);
			edit.apply();
		}
		edits.addAll(pending);
		pending = null;
	}

	public void flushPending() {
		pending = null;
	}

	public SelectionRange getExecuteSelectionRange(GraphicalTextViewer viewer) {
		ModelLocation loc = ((MiniEdit) edits.get(edits.size() - 1))
				.getResultingLocation();
		if (loc == null)
			return getUndoSelectionRange(viewer);
		return new SelectionRange(lookupModel(viewer, loc.model), loc.offset);
	}

	public SelectionRange getRedoSelectionRange(GraphicalTextViewer viewer) {
		return getExecuteSelectionRange(viewer);
	}

	public SelectionRange getUndoSelectionRange(GraphicalTextViewer viewer) {
		TextEditPart begin = lookupModel(viewer, beginLocation.model);
		if (endLocation == null)
			return new SelectionRange(begin, beginLocation.offset);
		TextEditPart end = lookupModel(viewer, endLocation.model);
		return new SelectionRange(begin, beginLocation.offset, end,
				endLocation.offset);
	}

	public void pendEdit(MiniEdit edit) {
		if (pending == null)
			pending = new ArrayList(2);
		pending.add(edit);
	}

	/**
	 * @see org.eclipse.gef.commands.Command#redo()
	 */
	public void redo() {
		for (int i = 0; i < edits.size(); i++) {
			MiniEdit edit = (MiniEdit) edits.get(i);
			edit.reapply();
		}
	}

	public void setEndLocation(ModelLocation endLocation) {
		this.endLocation = endLocation;
	}

	public void setBeginLocation(ModelLocation startLocation) {
		this.beginLocation = startLocation;
	}

	/**
	 * @see org.eclipse.gef.commands.Command#undo()
	 */
	public void undo() {
		for (int i = edits.size() - 1; i >= 0; i--) {
			MiniEdit edit = (MiniEdit) edits.get(i);
			edit.rollback();
		}
	}

}
