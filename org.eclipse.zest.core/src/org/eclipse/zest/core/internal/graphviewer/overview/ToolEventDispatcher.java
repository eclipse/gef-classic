/*******************************************************************************
 * Copyright 2005-2006, CHISEL Group, University of Victoria, Victoria, BC, Canada.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     The Chisel Group, University of Victoria
 *******************************************************************************/
package org.eclipse.mylar.zest.core.internal.graphviewer.overview;

import org.eclipse.draw2d.InputEvent;
import org.eclipse.draw2d.SWTEventDispatcher;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.Tool;
import org.eclipse.gef.tools.SelectionTool;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.widgets.Event;

/**
 * An event dispatcher that simply delegates events to tools.
 * @author Del Myers
 *
 */
public class ToolEventDispatcher extends SWTEventDispatcher {
	private EditPartViewer viewer;
	private Tool tool;

	public ToolEventDispatcher(EditPartViewer viewer) {
		this.viewer = viewer;
		setTool(new SelectionTool());
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.SWTEventDispatcher#dispatchFocusGained(org.eclipse.swt.events.FocusEvent)
	 */
	public void dispatchFocusGained(FocusEvent e) {
		tool.focusGained(e, viewer);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.SWTEventDispatcher#dispatchFocusLost(org.eclipse.swt.events.FocusEvent)
	 */
	public void dispatchFocusLost(FocusEvent e) {
		tool.focusLost(e, viewer);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.SWTEventDispatcher#dispatchKeyPressed(org.eclipse.swt.events.KeyEvent)
	 */
	public void dispatchKeyPressed(KeyEvent e) {
		tool.keyDown(e, viewer);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.SWTEventDispatcher#dispatchKeyReleased(org.eclipse.swt.events.KeyEvent)
	 */
	public void dispatchKeyReleased(KeyEvent e) {
		tool.keyUp(e, viewer);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.SWTEventDispatcher#dispatchKeyTraversed(org.eclipse.swt.events.TraverseEvent)
	 */
	public void dispatchKeyTraversed(TraverseEvent e) {
		tool.keyTraversed(e, viewer);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.SWTEventDispatcher#dispatchMouseDoubleClicked(org.eclipse.swt.events.MouseEvent)
	 */
	public void dispatchMouseDoubleClicked(MouseEvent me) {
		tool.mouseDoubleClick(me, viewer);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.SWTEventDispatcher#dispatchMouseEntered(org.eclipse.swt.events.MouseEvent)
	 */
	public void dispatchMouseEntered(MouseEvent me) {
		tool.viewerEntered(me, viewer);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.SWTEventDispatcher#dispatchMouseExited(org.eclipse.swt.events.MouseEvent)
	 */
	public void dispatchMouseExited(MouseEvent me) {
		tool.viewerExited(me, viewer);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.SWTEventDispatcher#dispatchMouseHover(org.eclipse.swt.events.MouseEvent)
	 */
	public void dispatchMouseHover(MouseEvent me) {
		tool.mouseHover(me, viewer);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.SWTEventDispatcher#dispatchMouseMoved(org.eclipse.swt.events.MouseEvent)
	 */
	public void dispatchMouseMoved(MouseEvent me) {
		if ((me.stateMask & InputEvent.ANY_BUTTON) != 0)
			tool.mouseDrag(me, viewer);
		else
			tool.mouseMove(me, viewer);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.SWTEventDispatcher#dispatchMousePressed(org.eclipse.swt.events.MouseEvent)
	 */
	public void dispatchMousePressed(MouseEvent me) {
		tool.mouseDown(me, viewer);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.SWTEventDispatcher#dispatchMouseReleased(org.eclipse.swt.events.MouseEvent)
	 */
	public void dispatchMouseReleased(MouseEvent me) {
		tool.mouseUp(me, viewer);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.EventDispatcher#dispatchMouseWheelScrolled(org.eclipse.swt.widgets.Event)
	 */
	public void dispatchMouseWheelScrolled(Event event) {
		tool.mouseWheelScrolled(event, viewer);
	}
	
	public void setTool(Tool tool) {
		this.tool = tool;
	}
	
}
