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
import org.eclipse.mylar.zest.core.DebugPrint;
import org.eclipse.swt.events.MouseEvent;

/**
 * @author irbull
 */
public class ViewerDragTracker extends DragEditPartsTracker {

	public ViewerDragTracker(EditPart owner) {
		super(owner);
		// TODO Auto-generated constructor stub
	}
	
	protected void performSelection() {
		// TODO Auto-generated method stub
		DebugPrint.println("Perform selection called");
		super.performSelection();
	}
	
	public void mouseUp(MouseEvent me, EditPartViewer viewer) {
		// TODO Auto-generated method stub
		super.mouseUp(me, viewer);
		//viewer.deselect( getSourceEditPart() );
	}

}
