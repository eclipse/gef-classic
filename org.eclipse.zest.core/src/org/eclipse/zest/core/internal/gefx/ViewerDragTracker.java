/*******************************************************************************
 * Copyright 2006, CHISEL Group, University of Victoria, Victoria, BC, Canada.
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
import org.eclipse.gef.tools.SelectEditPartTracker;
import org.eclipse.swt.events.MouseEvent;

/**
 * @author Ian Bull
 * 
 * This is our basic drag tracker that we use in our graph views.  
 * The mouse events are handled here.
 */
public class ViewerDragTracker extends SelectEditPartTracker {

	public ViewerDragTracker(EditPart owner) {
		super(owner);
	}
	
	/**
	 * A Mouse down event has happened on the canvas.  If it is mouse button 1, then 
	 * we can handle it, otherwise just ignore it.  This means that users can add
	 * context menus with mouse button 2.  
	 */
	public void mouseDown(MouseEvent me, EditPartViewer viewer) {
		if (me.button == 1 )  
			super.mouseDown(me, viewer);
		return;
	} 
	
}
