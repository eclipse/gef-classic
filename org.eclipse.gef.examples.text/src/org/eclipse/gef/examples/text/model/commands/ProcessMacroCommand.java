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

import org.eclipse.gef.examples.text.GraphicalTextViewer;
import org.eclipse.gef.examples.text.SelectionRange;
import org.eclipse.gef.examples.text.model.ModelElement;
import org.eclipse.gef.examples.text.model.ModelLocation;
import org.eclipse.gef.examples.text.model.TextRun;

/**
 * @since 3.1
 */
public class ProcessMacroCommand extends CompoundEditCommand {

/**
 * @since 3.1
 */
public ProcessMacroCommand(TextRun run, int begin, int end, ModelElement substitution, ModelLocation loc) {
	super("$$conversion");
	RemoveRange removal = new RemoveRange(run, begin, run, end);
	pendEdit(removal);
	SubdivideElement subdivide = new SubdivideElement(run, begin);
	pendEdit(subdivide);
	InsertModelElement insert = new InsertModelElement(run.getContainer(), run
			.getContainer().getChildren().indexOf(run) + 1, substitution, loc);
	pendEdit(insert);
}

/**
 * @see org.eclipse.gef.examples.text.TextCommand#getRedoSelectionRange(org.eclipse.gef.examples.text.GraphicalTextViewer)
 */
public SelectionRange getRedoSelectionRange(GraphicalTextViewer viewer) {
	return null;
}

/**
 * @see org.eclipse.gef.examples.text.TextCommand#getExecuteSelectionRange(org.eclipse.gef.examples.text.GraphicalTextViewer)
 */
public SelectionRange getExecuteSelectionRange(GraphicalTextViewer viewer) {
	return super.getExecuteSelectionRange(viewer);
}

/**
 * @see org.eclipse.gef.examples.text.TextCommand#getUndoSelectionRange(org.eclipse.gef.examples.text.GraphicalTextViewer)
 */
public SelectionRange getUndoSelectionRange(GraphicalTextViewer viewer) {
	return null;
}

}
