/*******************************************************************************
 * Copyright (c) 2005, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.draw2dl.examples.scrollpane;

import org.eclipse.draw2dl.*;
import org.eclipse.swt.graphics.Cursor;

import org.eclipse.draw2dl.geometry.Dimension;
import org.eclipse.draw2dl.geometry.Point;
import org.eclipse.draw2dl.geometry.Rectangle;

public class InternalFrame 
	extends LabeledContainer
{

static class InternalFrameBorder extends org.eclipse.draw2dl.CompoundBorder {
	InternalFrameBorder(){
		org.eclipse.draw2dl.TitleBarBorder titlebar = new TitleBarBorder();
		titlebar.setTextColor(org.eclipse.draw2dl.ColorConstants.white);
		titlebar.setBackgroundColor(org.eclipse.draw2dl.ColorConstants.darkGray);
		inner = new org.eclipse.draw2dl.CompoundBorder(
			new LineBorder(FigureUtilities.mixColors(
				org.eclipse.draw2dl.ColorConstants.buttonDarker, ColorConstants.button), 3),
			new org.eclipse.draw2dl.SchemeBorder(org.eclipse.draw2dl.SchemeBorder.SCHEMES.LOWERED)
		);
		outer = new CompoundBorder(
			new org.eclipse.draw2dl.SchemeBorder(SchemeBorder.SCHEMES.RAISED),
			titlebar
		);
	}
}

public InternalFrame(){
	super(new InternalFrameBorder());
	setLayoutManager(new StackLayout());
	setOpaque(true);
	setRequestFocusEnabled(true);
	addListeners();
}

public InternalFrame(String title){
	this();
	setLabel(title);
}

protected void addListeners(){
	DragTracker tracker = new DragTracker();
	addMouseMotionListener(tracker);
	addMouseListener(tracker);
}

private class DragTracker
	extends MouseMotionListener.Stub
	implements MouseListener
{
	private boolean move;
	private Point startLocation;
	private Rectangle bounds;
	private int position = org.eclipse.draw2dl.PositionConstants.NONE;

	public void mouseDoubleClicked(org.eclipse.draw2dl.MouseEvent me){}

	public void mouseDragged(org.eclipse.draw2dl.MouseEvent me){
		if(position == org.eclipse.draw2dl.PositionConstants.NONE)
			return;
		int dx = 0, dy = 0, dw = 0, dh = 0;
		Rectangle rect = bounds.getCopy();
		Dimension diff = me.getLocation().getDifference(startLocation);
		if((position & org.eclipse.draw2dl.PositionConstants.NORTH) == org.eclipse.draw2dl.PositionConstants.NORTH){
			dy = diff.height;
			if(move)
				dx = diff.width;
			else
				dh = -diff.height;
		}
		if((position & org.eclipse.draw2dl.PositionConstants.WEST) == org.eclipse.draw2dl.PositionConstants.WEST){
			dx = diff.width;
			dx = rect.x + dx > rect.x + rect.width - 100 ? rect.width - 100 : dx;
			dw = -diff.width;
		}
		if((position & org.eclipse.draw2dl.PositionConstants.EAST) == org.eclipse.draw2dl.PositionConstants.EAST){
			dw = diff.width;
		}
		if((position & org.eclipse.draw2dl.PositionConstants.SOUTH) == org.eclipse.draw2dl.PositionConstants.SOUTH){
			dh = diff.height;
		}
		rect.x += dx;
		rect.y += dy;
		rect.width += dw;
		rect.width = rect.width < 100 ? 100 : rect.width;
		rect.height += dh;
		rect.height = rect.height < 22 ? 22 : rect.height;
		setBounds(rect);
		revalidate();
	}
	
	public void mouseMoved(org.eclipse.draw2dl.MouseEvent me){
		Rectangle figBounds = getBounds();
		Point mouseLocation = me.getLocation();
		if (!getBounds().contains(mouseLocation))
			System.out.println("Error");
		if (!figBounds.contains(mouseLocation))
			return;
		position = getClientArea().getCopy().shrink(4, 4).getPosition(mouseLocation);
		move = false;
		Cursor newCursor = null;

		switch(position){
			case org.eclipse.draw2dl.PositionConstants.NONE:
				break;
			case org.eclipse.draw2dl.PositionConstants.SOUTH_EAST:
				newCursor = org.eclipse.draw2dl.Cursors.SIZESE;
				break;
			case org.eclipse.draw2dl.PositionConstants.SOUTH:
				newCursor = org.eclipse.draw2dl.Cursors.SIZES;
				break;
			case org.eclipse.draw2dl.PositionConstants.EAST:
				newCursor = org.eclipse.draw2dl.Cursors.SIZEE;
				break;
			case org.eclipse.draw2dl.PositionConstants.NORTH_EAST:
				newCursor = org.eclipse.draw2dl.Cursors.SIZENE;
				break;
			case org.eclipse.draw2dl.PositionConstants.NORTH_WEST:
				newCursor = org.eclipse.draw2dl.Cursors.SIZENW;
				break;
			case org.eclipse.draw2dl.PositionConstants.SOUTH_WEST:
				newCursor = org.eclipse.draw2dl.Cursors.SIZESW;
				break;
			case org.eclipse.draw2dl.PositionConstants.NORTH:
				if(mouseLocation.y > (getBounds().y + getInsets().left))
					// Special case: MOVE and NOT RESIZE 
					move = true;
				else
					newCursor = org.eclipse.draw2dl.Cursors.SIZEN;
				break;
			case org.eclipse.draw2dl.PositionConstants.WEST:
				newCursor = Cursors.SIZEW;
				break;
		}
		setCursor(newCursor);
	}
	
	public void mousePressed(org.eclipse.draw2dl.MouseEvent me){
		// Request focus
		requestFocus();
		// Bring this frame to the top
		// getParent().add(InternalFrame.this); 
		me.consume();
		
		startLocation = me.getLocation();
		bounds = getBounds().getCopy();
	}
	
	public void mouseReleased(org.eclipse.draw2dl.MouseEvent me){
		revalidate();
		startLocation = null;
		position = PositionConstants.NONE;
		move = false;
	}
	
	public void mouseExited(MouseEvent me){
		setCursor(null);
	}
}
	
}