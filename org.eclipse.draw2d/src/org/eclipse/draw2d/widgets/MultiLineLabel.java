/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.draw2d.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.accessibility.*;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Composite;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.text.FlowPage;
import org.eclipse.draw2d.text.TextFlow;

/**
 * A widget for displaying a multi-line string. The label will have a vertical or
 * horizontal scrollbar when needed. Unlike the platform Label, this label is focusable
 * and accessible to screen-readers.
 * 
 * @author hudsonr
 */
public final class MultiLineLabel extends FigureCanvas {

private TextFlow textFlow;

class FocusableViewport extends Viewport {
	FocusableViewport() {
		super(true);
	}
	
	{
		setFocusTraversable(true);
		setBorder(new MarginBorder(2));
	}
	
	public void handleFocusGained(FocusEvent event) {
		super.handleFocusGained(event);
		repaint();
	}

	public void handleFocusLost(FocusEvent event) {
		super.handleFocusLost(event);
		repaint();
	}

	protected void paintBorder(Graphics graphics) {
		super.paintBorder(graphics);
		if (hasFocus()) {
			graphics.setForegroundColor(ColorConstants.black);
			graphics.setBackgroundColor(ColorConstants.white);
			graphics.drawFocus(getBounds().getResized(-1,-1));
		}
	}
}

/**
 * @param parent
 */
public MultiLineLabel(Composite parent) {
	super(parent);
	setViewport(new FocusableViewport());
	FlowPage page = new FlowPage();
	textFlow = new TextFlow();
	page.add(textFlow);
	setContents(page);
	getViewport().setContentsTracksWidth(true);
	addAccessibility();
}

private void addAccessibility() {
	getAccessible().addAccessibleControlListener(new AccessibleControlAdapter() {
		public void getRole(AccessibleControlEvent e) {
			e.detail = ACC.ROLE_LABEL;
		}
		public void getState(AccessibleControlEvent e) {
			e.detail = ACC.STATE_READONLY;
		}
	});
	getAccessible().addAccessibleListener(new AccessibleAdapter() {
		public void getName(AccessibleEvent e) {
			e.result = getText();
		}
	});
	addKeyListener(new KeyAdapter() {
		public void keyPressed(KeyEvent e) {
			Point p = getViewport().getViewLocation();
			int dy = getFont().getFontData()[0].getHeight();
			int dx = dy * 3 / 2;
			if (e.keyCode == SWT.ARROW_DOWN) {
				scrollToY(p.y + dy / 2);
				scrollToY(p.y + dy);
				scrollToY(p.y + dy * 3 / 2);
				scrollToY(p.y + dy * 2);
			}
			if (e.keyCode == SWT.ARROW_UP) {
				scrollToY(p.y - dy / 2);
				scrollToY(p.y - dy);
				scrollToY(p.y - dy * 3 / 2);
				scrollToY(p.y - dy * 2);
			}
			if (e.keyCode == SWT.ARROW_RIGHT) {
				scrollToX(p.x + dx);
				scrollToX(p.x + dx * 2);
				scrollToX(p.x + dx * 3);
			}
			if (e.keyCode == SWT.ARROW_LEFT) {
				scrollToX(p.x - dx);
				scrollToX(p.x - dx * 2);
				scrollToX(p.x - dx * 3);
			}
		}
	});
}

public String getText() {
	return textFlow.getText();
}

/**
 * @see org.eclipse.swt.widgets.Canvas#setFont(org.eclipse.swt.graphics.Font)
 */
public void setFont(Font font) {
	super.setFont(font);
	textFlow.revalidate();
}

public void setText(String text) {
	textFlow.setText(text);
}

}
