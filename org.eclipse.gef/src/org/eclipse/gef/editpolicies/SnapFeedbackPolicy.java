/*******************************************************************************
 * Copyright (c) 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.editpolicies;

import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.Display;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.*;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

import org.eclipse.gef.*;

/**
 * @author Pratik Shah
 */
public class SnapFeedbackPolicy 
	extends GraphicalEditPolicy
{

IFigure guide[] = new IFigure[6];
Integer location[] = new Integer[6];

public void eraseTargetFeedback(Request request) {
	for (int i = 0; i < guide.length; i++) {
		if (guide[i] != null)
			removeFeedback(guide[i]);
		guide[i] = null;
		location[i] = null;
	}
}

static class FadeIn extends Figure {
	int opacity = 0;
	static final int FRAMES = 6;
	Image image;
	static int count;
	FadeIn(Color bg) {
		setBackgroundColor(bg);
		super.setOpaque(true);
	}
	
	/**
	 * @see org.eclipse.draw2d.Figure#getBackgroundColor()
	 */
	public Color getLocalBackgroundColor() {
		return FigureUtilities.mixColors(
				super.getLocalBackgroundColor(),
				getParent().getBackgroundColor(),
				(double)opacity / FRAMES);
	}
	
	/**
	 * @see org.eclipse.draw2d.Figure#paintFigure(org.eclipse.draw2d.Graphics)
	 */
	protected void paintFigure(Graphics graphics) {
		if (opacity != FRAMES) {
			if (image != null) {
				image.dispose();
				count--;
				image = null;
			}
			if (opacity != FRAMES - 1) {
				Display display = Display.getCurrent();
				PaletteData pData = new PaletteData(0xFF, 0xFF00, 0xFF0000);
				int fillColor = pData.getPixel(getLocalBackgroundColor().getRGB());
				ImageData iData = new ImageData(1, 1, 24, pData);
				iData.setPixel(0, 0, fillColor);
				iData.setAlpha(0, 0, 255 * opacity / FRAMES);
				image = new Image(display, iData);
				count++;
			}
			Display.getCurrent().timerExec(100, new Runnable() {
				public void run() {
					opacity = Math.min(FRAMES, opacity + 1);
					repaint();
				}
			});
		}
		Rectangle r = getBounds();
		if (image != null)
			graphics.drawImage(image, 0, 0, 1, 1, r.x, r.y, r.width, r.height);
		else
			super.paintFigure(graphics);
	}
	/**
	 * @see org.eclipse.draw2d.Figure#removeNotify()
	 */
	public void removeNotify() {
		if (image != null) {
			image.dispose();
			count--;
			image = null;
		}
	}
}

void highlightGuide(Integer pos, Color color, int offset) {
	if (pos == null) {
		if (guide[offset] != null) {
			removeFeedback(guide[offset]);
			guide[offset] = null;
		}
		location[offset] = pos;
		return;
	}
	
	//pos is an integer relative to target's client area.
	//translate pos to absolute, and then make it relative to fig.
	int position = pos.intValue();
	Point loc = new PrecisionPoint(position, position);
	IFigure contentPane = ((GraphicalEditPart)getHost()).getContentPane();
	contentPane.translateToParent(loc);
	contentPane.translateToAbsolute(loc);
	
	if (location[offset] == null || !location[offset].equals(pos)) {
		location[offset] = pos;		
		if (guide[offset] != null) {
			removeFeedback(guide[offset]);
			guide[offset] = null;
		}

		IFigure fig = new FadeIn(color);
		guide[offset] = fig;
		addFeedback(fig);
		fig.translateToRelative(loc);
		position = offset == 0 ? loc.x : loc.y;
	
		Rectangle figBounds = getLayer(LayerConstants.FEEDBACK_LAYER)
			.getBounds().getCopy();
		if ((offset % 2) == 1) {
			figBounds.height = 1;
			figBounds.y = position;
		} else {
			figBounds.x = position;
			figBounds.width = 1;
		}
		fig.setBounds(figBounds);
	}
}

public void showTargetFeedback(Request req) {
	if (req.getType().equals(REQ_MOVE)
			|| req.getType().equals(REQ_RESIZE)
			|| req.getType().equals(REQ_CLONE)
			|| req.getType().equals(REQ_CREATE)) {
//		eraseTargetFeedback(req);
		Integer value;
		value = (Integer)req.getExtendedData().get(SnapToGeometry.PROPERTY_WEST_ANCHOR);
		highlightGuide(value, ColorConstants.blue, 0);
		
		value = (Integer)req.getExtendedData().get(SnapToGeometry.PROPERTY_NORTH_ANCHOR);
		highlightGuide(value, ColorConstants.blue, 1);
		
		value = (Integer)req.getExtendedData().get(SnapToGeometry.PROPERTY_EAST_ANCHOR);
		highlightGuide(value, ColorConstants.blue, 2);
		
		value = (Integer)req.getExtendedData().get(SnapToGeometry.PROPERTY_SOUTH_ANCHOR);
		highlightGuide(value, ColorConstants.blue, 3);

		value = (Integer)req.getExtendedData().get(SnapToGuides.PROPERTY_VERTICAL_GUIDE);
		highlightGuide(value, ColorConstants.red, 4);
		
		value = (Integer)req.getExtendedData().get(SnapToGuides.PROPERTY_HORIZONTAL_GUIDE);
		highlightGuide(value, ColorConstants.red, 5);
	}
}

}