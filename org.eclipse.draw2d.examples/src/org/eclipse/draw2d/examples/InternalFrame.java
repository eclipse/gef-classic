package org.eclipse.draw2d.examples;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.graphics.Cursor;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.*;

public class InternalFrame 
	extends LabeledContainer 
{

static class InternalFrameBorder extends CompoundBorder {
	InternalFrameBorder(){
		TitleBarBorder titlebar = new TitleBarBorder();
		titlebar.setTextColor(ColorConstants.white);
		titlebar.setBackgroundColor(ColorConstants.darkGray);
		inner = new CompoundBorder(
			new LineBorder(FigureUtilities.mixColors(
				ColorConstants.buttonDarker,ColorConstants.button), 3),
			new SchemeBorder(SchemeBorder.SCHEMES.LOWERED)
		);
		outer = new CompoundBorder(
			new SchemeBorder(SchemeBorder.SCHEMES.RAISED),
			titlebar
		);
	}
}

private LabeledBorder labeledBorder;

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
	private int position = PositionConstants.NONE;

	public void mouseDoubleClicked(MouseEvent me){}

	public void mouseDragged(MouseEvent me){
		if(position == PositionConstants.NONE)
			return;
		int dx = 0, dy = 0, dw = 0, dh = 0;
		Rectangle rect = bounds.getCopy();
		Dimension diff = me.getLocation().getDifference(startLocation);
		if((position & PositionConstants.NORTH) == PositionConstants.NORTH){
			dy = diff.height;
			if(move)
				dx = diff.width;
			else
				dh = -diff.height;
		}
		if((position & PositionConstants.WEST) == PositionConstants.WEST){
			dx = diff.width;
			dx = rect.x + dx > rect.x + rect.width - 100 ? rect.width - 100 : dx;
			dw = -diff.width;
		}
		if((position & PositionConstants.EAST) == PositionConstants.EAST){
			dw = diff.width;
		}
		if((position & PositionConstants.SOUTH) == PositionConstants.SOUTH){
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
	
	public void mouseMoved(MouseEvent me){
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
			case PositionConstants.NONE:
				break;
			case PositionConstants.SOUTH_EAST:
				newCursor = Cursors.SIZESE;
				break;
			case PositionConstants.SOUTH:
				newCursor = Cursors.SIZES;
				break;
			case PositionConstants.EAST:
				newCursor = Cursors.SIZEE;
				break;
			case PositionConstants.NORTH_EAST:
				newCursor = Cursors.SIZENE;
				break;
			case PositionConstants.NORTH_WEST:
				newCursor = Cursors.SIZENW;
				break;
			case PositionConstants.SOUTH_WEST:
				newCursor = Cursors.SIZESW;
				break;
			case PositionConstants.NORTH:
				if(mouseLocation.y > (getBounds().y + getInsets().left))
					// Special case: MOVE and NOT RESIZE 
					move = true;
				else
					newCursor = Cursors.SIZEN;
				break;
			case PositionConstants.WEST:
				newCursor = Cursors.SIZEW;
				break;
		}
		setCursor(newCursor);
	}
	
	public void mousePressed(MouseEvent me){
		// Request focus
		requestFocus();
		// Bring this frame to the top
		// getParent().add(InternalFrame.this); 
		me.consume();
		
		startLocation = me.getLocation();
		bounds = getBounds().getCopy();
	}
	
	public void mouseReleased(MouseEvent me){
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