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

import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.RootEditPart;
import org.eclipse.gef.SharedCursors;
import org.eclipse.gef.commands.Command;
import org.eclipse.mylar.zest.core.internal.gefx.IPanningListener;
import org.eclipse.mylar.zest.core.internal.viewers.commands.PanningCommand;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.graphics.Cursor;



/**
 * Panning drag tracker (single selection only).
 * 
 * @author ccallendar
 */
public class PanningTracker extends SingleSelectionTracker {

	private IPanningListener panningListener;
	private boolean allowPanning = true;
	private boolean panningStarted = false;
	private Point lastLocation;

	/**
	 * PanningTracker constructor.
	 */
	public PanningTracker(RootEditPart source, IPanningListener panningListener, boolean allowPanning) {
		super(source);
		this.panningListener = panningListener;
		this.allowPanning = allowPanning;
		this.lastLocation = new Point(0, 0);
	}

	/**
	 * @see org.eclipse.gef.tools.AbstractTool#getDebugName()
	 */
	protected String getDebugName() {
		return "PanningTracker";
	}
	
	/**
	 * Gets the figure canvas. If the current viewer's control is not a figure canvas then null is returned.
	 * @return the figure canvas from the current viewer's control
	 */
	protected FigureCanvas getCanvas() {
		if (getCurrentViewer().getControl() instanceof FigureCanvas) {
			return (FigureCanvas) getCurrentViewer().getControl();
		}
		return null;
	}

	/**
	 * Returns the cursor used under normal conditions.
	 * If panning has started a HAND cursor is returned.
	 * @return the default cursor
	 */
	protected Cursor getDefaultCursor() {
		if (panningStarted) {
			return SharedCursors.HAND;
		}
		return super.getDefaultCursor();
	}

	
	/**
	 * Gets a {@link PanningCommand} if panning has started.  This is used 
	 * to have a HAND cursor instead of a disabled one.
	 * @see org.eclipse.gef.tools.DragEditPartsTracker#getCommand()
	 */
	protected Command getCommand() {
		if (panningStarted) {
			return new PanningCommand();
		}
		return super.getCommand();
	}
	
	/**
	 * Instead of giving the <b>total</b> drag distance this returns
	 * the distance since the last time handleDragStarted() or handleDrag()
	 * was called.  
	 */
	private Dimension getPanningDelta() {
		Dimension diff = getLocation().getDifference(lastLocation);
		return diff;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.gef.tools.AbstractTool#handleDrag()
	 */
	protected boolean handleDrag() {
		if (panningStarted && (getCanvas() != null)) {
			panningListener.panning(getPanningDelta().width, getPanningDelta().height);
			lastLocation = getLocation();
			return true;
		} else {
			return super.handleDrag();
		}
	}

	/**
	 * If panning is allowed then the source editpart viewer is signalled
	 * about the start of panning and the cursor is changed to a HAND cursor. 
	 * @see org.eclipse.gef.tools.AbstractTool#handleDragStarted()
	 */
	protected boolean handleDragStarted() {
		if (allowPanning) {
			panningListener.panningStart();
			panningStarted = true;
			lastLocation = getLocation();
			refreshCursor();
			return true;
		}
		return super.handleDragStarted();
	}
	
	/**
	 * Indicates that the panning has stopped if the button is 1.  
	 * @see org.eclipse.gef.tools.DragEditPartsTracker#handleButtonUp(int)
	 */
	protected boolean handleButtonUp(int button) {
		if (button == 1) {
			panningStarted = false;
			if (allowPanning) {
				panningListener.panningEnd();
			}
		}
		return super.handleButtonUp(button);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gef.tools.DragEditPartsTracker#handleKeyDown(org.eclipse.swt.events.KeyEvent)
	 */
	protected boolean handleKeyDown(KeyEvent e) {
		//System.out.println("PanningTracker: keyDown: " + e.toString());
		return super.handleKeyDown(e);
	}
		
	
}
