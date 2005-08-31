/*******************************************************************************
 * Copyright 2005, CHISEL Group, University of Victoria, Victoria, BC, Canada.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     The Chisel Group, University of Victoria
 *******************************************************************************/
package org.eclipse.mylar.zest.core.internal.viewers.trackers;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.tools.DragEditPartsTracker;

/**
 * The SingleSelectionTracker is similar to the org.eclipse.gef.internal.ui.palette.editparts.SingleSelectionTracker
 * class.
 * 
 * @author ccallendar
 */
public class SingleSelectionTracker extends DragEditPartsTracker {

	/**
	 * Initializes this drag tracker.
	 * @param source	Owner edit part.
	 */
	public SingleSelectionTracker(EditPart source) {
		super(source);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.tools.DragEditPartsTracker#getDebugName()
	 */
	protected String getDebugName() {
		return "SingleSelectionTracker";
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.tools.SelectEditPartTracker#performSelection()
	 */
	protected void performSelection() {
		if (hasSelectionOccurred())
			return;
		setFlag(FLAG_SELECTION_PERFORMED, true);
		getCurrentViewer().select(getSourceEditPart());
	}
		
}
