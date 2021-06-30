/*******************************************************************************
 * Copyright (c) 2000, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.draw2dl;

class ClickableEventHandler extends MouseMotionListener.Stub implements
    MouseListener, FigureListener, ChangeListener, KeyListener,
    FocusListener {

	private org.eclipse.draw2dl.MouseEvent lastEvent;

	public void focusLost(org.eclipse.draw2dl.FocusEvent fe) {
		org.eclipse.draw2dl.Clickable loser = (org.eclipse.draw2dl.Clickable) fe.loser;
		loser.repaint();
		loser.getModel().setArmed(false);
		loser.getModel().setPressed(false);
	}

	public void focusGained(FocusEvent fe) {
		org.eclipse.draw2dl.Clickable clickable = (org.eclipse.draw2dl.Clickable) fe.gainer;
		clickable.repaint();
	}

	public void figureMoved(IFigure source) {
		if (lastEvent == null)
			return;
		mouseDragged(lastEvent);
	}

	public void handleStateChanged(ChangeEvent change) {
		org.eclipse.draw2dl.Clickable clickable = (org.eclipse.draw2dl.Clickable) change.getSource();
		if (change.getPropertyName() == org.eclipse.draw2dl.ButtonModel.MOUSEOVER_PROPERTY
				&& !clickable.isRolloverEnabled())
			return;
		clickable.repaint();
	}

	public void mouseDoubleClicked(org.eclipse.draw2dl.MouseEvent me) {
	}

	public void mouseDragged(org.eclipse.draw2dl.MouseEvent me) {
		lastEvent = me;
		org.eclipse.draw2dl.Clickable click = (org.eclipse.draw2dl.Clickable) me.getSource();
		org.eclipse.draw2dl.ButtonModel model = click.getModel();
		if (model.isPressed()) {
			boolean over = click.containsPoint(me.getLocation());
			model.setArmed(over);
			model.setMouseOver(over);
		}
	}

	public void mouseEntered(org.eclipse.draw2dl.MouseEvent me) {
		org.eclipse.draw2dl.Clickable click = (org.eclipse.draw2dl.Clickable) me.getSource();
		click.getModel().setMouseOver(true);
		click.addFigureListener(this);
	}

	public void mouseExited(org.eclipse.draw2dl.MouseEvent me) {
		org.eclipse.draw2dl.Clickable click = (org.eclipse.draw2dl.Clickable) me.getSource();
		click.getModel().setMouseOver(false);
		click.removeFigureListener(this);
	}

	public void mouseMoved(org.eclipse.draw2dl.MouseEvent me) {
	}

	public void mousePressed(org.eclipse.draw2dl.MouseEvent me) {
		if (me.button != 1)
			return;
		lastEvent = me;
		org.eclipse.draw2dl.Clickable click = (org.eclipse.draw2dl.Clickable) me.getSource();
		org.eclipse.draw2dl.ButtonModel model = click.getModel();
		click.requestFocus();
		model.setArmed(true);
		model.setPressed(true);
		me.consume();
	}

	public void mouseReleased(MouseEvent me) {
		if (me.button != 1)
			return;
		org.eclipse.draw2dl.ButtonModel model = ((org.eclipse.draw2dl.Clickable) me.getSource()).getModel();
		if (!model.isPressed())
			return;
		model.setPressed(false);
		model.setArmed(false);
		me.consume();
	}

	public void keyPressed(org.eclipse.draw2dl.KeyEvent ke) {
		org.eclipse.draw2dl.ButtonModel model = ((org.eclipse.draw2dl.Clickable) ke.getSource()).getModel();
		if (ke.character == ' ' || ke.character == '\r') {
			model.setPressed(true);
			model.setArmed(true);
		}
	}

	public void keyReleased(KeyEvent ke) {
		ButtonModel model = ((Clickable) ke.getSource()).getModel();
		if (ke.character == ' ' || ke.character == '\r') {
			model.setPressed(false);
			model.setArmed(false);
		}
	}

}
