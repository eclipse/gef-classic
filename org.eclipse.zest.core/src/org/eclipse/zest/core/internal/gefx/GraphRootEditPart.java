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

import org.eclipse.draw2d.ConnectionLayer;
import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.FreeformLayeredPane;
import org.eclipse.draw2d.LayeredPane;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.Request;
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;
import org.eclipse.mylar.zest.core.internal.graphviewer.parts.GraphEditPart;
import org.eclipse.mylar.zest.core.internal.viewers.trackers.PanningTracker;
import org.eclipse.mylar.zest.core.internal.viewers.trackers.SingleSelectionTracker;



/**
 * Extends {@link org.eclipse.gef.editparts.ScalableFreeformRootEditPart ScalableFreeformRootEditPart} 
 * to give the option of using a {@link SingleSelectionTracker SingleSelectionTracker} 
 * instead of a marquee drag tracker.  A PanningDragTracker will be used if the background is 
 * dragged and marquee selection is not 
 * 
 * @author Chris Callendar
 */
public class GraphRootEditPart extends ScalableFreeformRootEditPart implements ZestRootEditPart {


	
	private IPanningListener panningListener;
	private boolean allowMarqueeSelection;
	private boolean allowPanning;
	protected GraphEditPart graphEditPart = null;
	
	public GraphRootEditPart() {
		this(null, false, false);
	}
	
	/**
	 * Initializes this root edit part.
	 * @param panningListener the listener to be notified of panning events (dragging the canvas)
	 * @param allowMarqueeSelection if marquee selection is allowed - multiple node selection
	 * @param allowPanning if panning is allowed.  Only one of panning OR marquee selection is allowed.
	 */
	public GraphRootEditPart(IPanningListener panningListener, boolean allowMarqueeSelection, boolean allowPanning) {
		super();
		this.panningListener = panningListener;
		this.allowMarqueeSelection = allowMarqueeSelection;
		this.allowPanning = allowPanning;
	}
	
	protected LayeredPane createPrintableLayers() {
		FreeformLayeredPane layeredPane = new FreeformLayeredPane();
		
		layeredPane.add(new ConnectionLayer(), CONNECTION_LAYER);
		layeredPane.add(new FreeformLayer(), PRIMARY_LAYER);
		layeredPane.add(new ConnectionLayer(), CONNECTION_FEEDBACK_LAYER);
		
		return layeredPane;
	}
	
	/**
	 * Returns a drag tracker.  If panning is allowed then a PanningTracker is returned.  
	 * Otherwise either a {@link SingleSelectionTracker} (marqueeSelection disabled)
	 * or a {@link org.eclipse.gef.tools.MarqueeDragTracker} is returned. 
	 * @see org.eclipse.gef.editparts.ScalableRootEditPart#getDragTracker(org.eclipse.gef.Request)
	 */
	public DragTracker getDragTracker(Request req) {
		if (allowPanning && (panningListener != null)) {
			return new PanningTracker(this, panningListener, allowPanning);
		} else if (!allowMarqueeSelection) {
			return new SingleSelectionTracker(this);
		}
		return super.getDragTracker(req);
	}

	/**
	 * Sets the main edit part for the model. You should be able to 
	 * fire changes off here and see the effect
	 */
	public void setModelRootEditPart(Object modelRootEditPart) {
		this.graphEditPart = (GraphEditPart) modelRootEditPart;
	}
	
}
