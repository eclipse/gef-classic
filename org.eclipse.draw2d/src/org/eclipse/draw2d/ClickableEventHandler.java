/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.draw2d;

class ClickableEventHandler
	extends MouseMotionListener.Stub
	implements
		MouseListener,
		FigureListener,
		ChangeListener,
		KeyListener,
		FocusListener
{

private MouseEvent lastEvent;

/**
 * @see org.eclipse.draw2d.FocusListener#focusLost(org.eclipse.draw2d.FocusEvent)
 */
public void focusLost(FocusEvent fe) {
	Clickable loser = (Clickable)fe.loser;
	loser.repaint();
	loser.getModel().setArmed(false);
	loser.getModel().setPressed(false);
}

/**
 * @see org.eclipse.draw2d.FocusListener#focusGained(org.eclipse.draw2d.FocusEvent)
 */
public void focusGained(FocusEvent fe) {
	Clickable clickable = (Clickable)fe.gainer;
	clickable.repaint();
}	

/**
 * @see org.eclipse.draw2d.FigureListener#figureMoved(org.eclipse.draw2d.IFigure)
 */
public void figureMoved(IFigure source) {
	if (lastEvent == null)
		return;
	mouseDragged(lastEvent);
}

/**
 * @see org.eclipse.draw2d.ChangeListener#handleStateChanged(org.eclipse.draw2d.ChangeEvent)
 */
public void handleStateChanged(ChangeEvent change) {
	Clickable clickable = (Clickable)change.getSource();
	if (change.getPropertyName() == ButtonModel.MOUSEOVER_PROPERTY
		&& !clickable.isRolloverEnabled())
		return;
	clickable.repaint();
}

/**
 * @see org.eclipse.draw2d.MouseListener#mouseDoubleClicked(org.eclipse.draw2d.MouseEvent)
 */
public void mouseDoubleClicked(MouseEvent me) { }

/**
 * @see org.eclipse.draw2d.MouseMotionListener#mouseDragged(org.eclipse.draw2d.MouseEvent)
 */
public void mouseDragged(MouseEvent me) {
	lastEvent = me;
	Clickable click = (Clickable)me.getSource();
	ButtonModel model = click.getModel();
	if (model.isPressed()) {
		boolean over = click.containsPoint(me.getLocation());
			model.setArmed(over);
			model.setMouseOver(over);
	}
}

/**
 * @see org.eclipse.draw2d.MouseMotionListener#mouseEntered(org.eclipse.draw2d.MouseEvent)
 */
public void mouseEntered(MouseEvent me) {
	Clickable click = (Clickable)me.getSource();
	click.getModel().setMouseOver(true);
	click.addFigureListener(this);
}

/**
 * @see org.eclipse.draw2d.MouseMotionListener#mouseExited(org.eclipse.draw2d.MouseEvent)
 */
public void mouseExited(MouseEvent me) {
	Clickable click = (Clickable)me.getSource();
	click.getModel().setMouseOver(false);
	click.removeFigureListener(this);
}

/**
 * @see org.eclipse.draw2d.MouseMotionListener#mouseMoved(org.eclipse.draw2d.MouseEvent)
 */
public void mouseMoved(MouseEvent me) { }

/**
 * @see org.eclipse.draw2d.MouseListener#mousePressed(org.eclipse.draw2d.MouseEvent)
 */
public void mousePressed(MouseEvent me) {
	if (me.button != 1)
		return;
	lastEvent = me;
	Clickable click = (Clickable)me.getSource();
	ButtonModel model = click.getModel();
	click.requestFocus();
	model.setArmed(true);
	model.setPressed(true);
	me.consume();
}

/**
 * @see org.eclipse.draw2d.MouseListener#mouseReleased(org.eclipse.draw2d.MouseEvent)
 */
public void mouseReleased(MouseEvent me) {
	if (me.button != 1)
		return;
	ButtonModel model = ((Clickable)me.getSource()).getModel();
	if (!model.isPressed())
		return;
	model.setPressed(false);
	model.setArmed(false);
	me.consume();
}

/**
 * @see org.eclipse.draw2d.KeyListener#keyPressed(org.eclipse.draw2d.KeyEvent)
 */
public void keyPressed(KeyEvent ke) {	
	ButtonModel model = ((Clickable)ke.getSource()).getModel();
	if (ke.character == ' ' || ke.character == '\r') {
		model.setPressed(true);
		model.setArmed(true);
	}
}

/**
 * @see org.eclipse.draw2d.KeyListener#keyReleased(org.eclipse.draw2d.KeyEvent)
 */
public void keyReleased(KeyEvent ke) {
	ButtonModel model = ((Clickable)ke.getSource()).getModel();
	if (ke.character == ' ' || ke.character == '\r') {
		model.setPressed(false);
		model.setArmed(false);
	}
}

}
