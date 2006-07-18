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
 * 
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
	
	//@tag taken : from java.awt.geom.Arc2D
	public boolean containsPoint(int x, int y) {
		// Normalize the coordinates compared to the ellipse
		// having a center at 0,0 and a radius of 0.5.
		Rectangle r = getArcBounds();
		double ellw = r.width;
		if (ellw <= 0.0) {
			return false;
		}
		double normx = (x - r.x) / ellw - 0.5;
		double ellh = r.height;
		if (ellh <= 0.0) {
			return false;
		}
		double normy = (y - r.y) / ellh - 0.5;
		double distSq = (normx * normx + normy * normy);
		if (distSq >= 0.25) {
			return false;
		}
		double angExt = Math.abs(getLength());
		if (angExt >= 360.0) {
			return true;
		}
		boolean inarc = containsAngle(-Math.toDegrees(Math.atan2(normy,
				normx)));
		return inarc;

		/*// CHORD and OPEN behave the same way
		if (inarc) {
			if (angExt >= 180.0) {
				return true;
			}
			// point must be outside the "pie triangle"
		} else {
			if (angExt <= 180.0) {
				return false;
			}
			// point must be inside the "pie triangle"
		}
		// The point is inside the pie triangle iff it is on the same
		// side of the line connecting the ends of the arc as the center.
		double angle = Math.toRadians(-getAngleStart());
		double x1 = Math.cos(angle);
		double y1 = Math.sin(angle);
		angle += Math.toRadians(-getAngleExtent());
		double x2 = Math.cos(angle);
		double y2 = Math.sin(angle);
		boolean inside = (Line2D.relativeCCW(x1, y1, x2, y2, 2*normx, 2*normy) *
				Line2D.relativeCCW(x1, y1, x2, y2, 0, 0) >= 0);
		return inarc ? !inside : inside;
		*/
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



