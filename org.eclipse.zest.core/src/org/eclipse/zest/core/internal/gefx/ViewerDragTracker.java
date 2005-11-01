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
package org.eclipse.mylar.zest.core.internal.gefx;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.tools.DragEditPartsTracker;
import org.eclipse.swt.events.MouseEvent;

/**
 * @author Ian Bull
 */
public class ViewerDragTracker extends DragEditPartsTracker {

	public ViewerDragTracker(EditPart owner) {
		super(owner);
	}
	
	protected void performSelection() {
		super.performSelection();
	}
	
	public void mouseUp(MouseEvent me, EditPartViewer viewer) {
		super.mouseUp(me, viewer);
	}

}
