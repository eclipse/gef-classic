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

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.Shape;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * A shape representation of an arc, so that arcs can be used in editparts.
 * @tag bug(114452)
 * @author Del Myers
 */
public class Arc extends Shape {
	private int offset;
	private int length;
	 /**
     * The closure type for an arc closed by drawing straight line
     * segments from the start of the arc segment to the center
     * of the full ellipse and from that point to the end of the arc segment.
     */
    public final static int PIE = 2;
    /** 
     * There are times when the clipping bounds and the bounds of the arc will be different
     * allow for this.
     */
    private Rectangle arcBounds;
	
	public Arc() {
		super();
		setBounds(new Rectangle(0,0,0,0));
		arcBounds = new Rectangle(0,0,0,0);
	}
	
	
	public Arc(Rectangle r, int offset, int length) {
		super();
		setBounds(r);
		arcBounds = new Rectangle(r);
		this.offset = offset;
		this.length = length;
	}
	
	public Arc(int x, int y, int width, int height, int offset, int length) {
		this (new Rectangle(x, y, width, height), offset, length);
	}
	
	
	protected void fillShape(Graphics graphics) {
		//graphics.setBackgroundColor(PlatformUI.getWorkbench().getDisplay().getSystemColor(SWT.COLOR_BLUE));
		graphics.fillArc(getArcBounds(), offset, length);
		/*for (int i = 0; i < 1000; i++) {
			int x = (int)(Math.random()*getBounds().width+getBounds().x);
			int y = (int)(Math.random()*getBounds().height+getBounds().x);
			if (containsPoint(x,y)) {
				graphics.drawPoint(x, y);
			}
		}*/
	}

	protected void outlineShape(Graphics graphics) {
		graphics.drawArc(getArcBounds(), offset, length);
	}
	
	public int getOffset() {
		return offset;
	}
	
	
	public void setOffset(int offset) {
		this.offset = offset;
	}
	
	public int getLength() {
		return length;
	}
	
	public void setLength(int length) {
		this.length = length;
	}
	
	public boolean containsPoint(int x, int y) {
		Rectangle r = getArcBounds();
		double boundsWidth = r.width;
		double boundsHeight = r.height;
		if (boundsWidth <= 0.0) {
			return false;
		}
		else if (boundsHeight <= 0.0) {
			return false;
		}
		double normx = (x - r.x) / boundsWidth - 0.5;
		double normy = (y - r.y) / boundsHeight - 0.5;
		double distanceSquared = (normx * normx + normy * normy);
		
		if (distanceSquared >= 0.25) {
			return false;
		}
		double angleExtent = Math.abs(getLength());
		if (angleExtent >= 360.0) {
			return true;
		}
		boolean inArc = containsAngle(-Math.toDegrees(Math.atan2(normy,	normx)));
		return inArc;
	}
	
	public boolean containsAngle(double angle) {
		double angExt = getLength();
		boolean backwards = (angExt < 0.0);
		if (backwards) {
			angExt = -angExt;
		}
		if (angExt >= 360.0) {
			return true;
		}
		angle = normalizeDegrees(angle) - normalizeDegrees(getOffset());
		if (backwards) {
			angle = -angle;
		}
		if (angle < 0.0) {
			angle += 360.0;
		}
		return (angle >= 0.0) && (angle < angExt);
	}
	
	/*
	 * Normalizes the specified angle into the range -180 to 180.
	 */
	static double normalizeDegrees(double angle) {
		if (angle > 180.0) {
			if (angle <= (180.0 + 360.0)) {
				angle = angle - 360.0;
			} else {
				angle = Math.IEEEremainder(angle, 360.0);
				// IEEEremainder can return -180 here for some input values...
				if (angle == -180.0) {
					angle = 180.0;
				}
			}
		} else if (angle <= -180.0) {
			if (angle > (-180.0 - 360.0)) {
				angle = angle + 360.0;
			} else {
				angle = Math.IEEEremainder(angle, 360.0);
				// IEEEremainder can return -180 here for some input values...
				if (angle == -180.0) {
					angle = 180.0;
				}
			}
		}
		return angle;
	}
	
	public Rectangle getArcBounds() {
		return arcBounds;
	}
	
	protected void setArcBounds(Rectangle bounds) {
		this.arcBounds = bounds;
	}

}



