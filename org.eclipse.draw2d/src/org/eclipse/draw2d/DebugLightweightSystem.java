package org.eclipse.draw2d;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.draw2d.geometry.*;
import org.eclipse.swt.events.KeyEvent;

public class DebugLightweightSystem
	extends LightweightSystem
{

class TitleBarBorder extends org.eclipse.draw2d.TitleBarBorder {
	public void paint(IFigure figure, Graphics g, Insets insets) {
		g.setXORMode(true);
		super.paint(figure, g, insets);
	}
}

LayeredPane layers = new LayeredPane();
Layer primary = new Layer();
Layer debug = new Layer() {
	public boolean containsPoint(int x, int y) {
		return false;
	}
};

LabeledContainer cursorHighlight;
LabeledContainer mouseHighlight;

{
	getRootFigure().add(layers);
	layers.add(primary);
	primary.setLayoutManager(new StackLayout());
	layers.add(debug);

	cursorHighlight = createHighlight();
	mouseHighlight  = createHighlight();

	debug.add(cursorHighlight);
	debug.add(mouseHighlight);
}

public DebugLightweightSystem() {
}

public DebugLightweightSystem(Canvas c) {
	super(c);
}

LabeledContainer createHighlight() {
	TitleBarBorder titleBarBorder = new TitleBarBorder();
	titleBarBorder.setBackgroundColor(ColorConstants.orange);
	LabeledContainer c = new LabeledContainer(
		new CompoundBorder(
			new SimpleRaisedBorder(),
			titleBarBorder
		)
	);
	c.setOpaque(false);
	c.setVisible(false);
	return c;
}

void followCursorTarget() {
	IFigure f = getEventDispatcher().getCursorTarget();
	cursorHighlight.setLabel("Cursor: " + f);//$NON-NLS-1$
	if (f == getEventDispatcher().getMouseTarget())
		f = mouseHighlight;
	highlight(f, cursorHighlight);
}

void followMouseTarget() {
	IFigure f = getEventDispatcher().getMouseTarget();
	mouseHighlight.setLabel("Mouse: " + f);//$NON-NLS-1$
	highlight(f, mouseHighlight);
}

EventHandler internalCreateEventHandler() {
	return new DebugEventHandler();
}

void highlight(IFigure f, LabeledContainer c) {
	if (f == null || f.getParent() == null)
		c.setVisible(false);
	else {
		c.setVisible(true);
		Rectangle r = f.getBounds().getExpanded(c.getInsets());
		f.translateToAbsolute(r);
		c.setBounds(r);
	}
}


public void setContents(IFigure figure) {
	if (contents != null)
		primary.remove(contents);
	contents = figure;
	primary.add(contents);
}

protected class DebugEventHandler
	extends EventHandler 
{
		/**
		 * @see org.eclipse.swt.events.KeyListener#keyPressed(KeyEvent)
		 */
		public void keyPressed(KeyEvent e) {
			super.keyPressed(e);
			if (e.character == ' ')
				debug.setVisible(!debug.isVisible());
		}
		
		/**		 * @see org.eclipse.swt.events.MouseMoveListener#mouseMove(MouseEvent)		 */
		public void mouseMove(MouseEvent e) {
			super.mouseMove(e);
			followCursorTarget();
			followMouseTarget();
		}
}

}