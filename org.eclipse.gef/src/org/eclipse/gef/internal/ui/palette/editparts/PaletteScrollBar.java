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
package org.eclipse.gef.internal.ui.palette.editparts;

import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.Display;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.*;
import org.eclipse.draw2d.geometry.Rectangle;

public final class PaletteScrollBar 
	extends ScrollBar 
{

private static final ButtonBorder BORDER =
	new ButtonBorder(new ButtonBorder.ButtonScheme(
		new Color[] {ColorConstants.buttonLightest},
		new Color[] {ColorConstants.buttonDarker},
		new Color[] {ColorConstants.buttonDarker},
		new Color[] {ColorConstants.buttonDarker}
	));

private static final Border CONTRAST = new CompoundBorder(
	new MarginBorder(1, 0, 0, 0) {
		public void paint(IFigure figure, Graphics graphics, Insets insets) {
			if (!((Clickable)figure).getModel().isMouseOver())
				return;
			Rectangle r = getPaintRectangle(figure, insets);
			graphics.setForegroundColor(ColorConstants.button);
			graphics.drawLine(r.x, r.y, r.right(), r.y);
		}
	}, BORDER
);

public static final int BUTTON_HEIGHT = 12;
private static final int SCROLL_TIME = 200;

private static final Image TRANSPARENCY;

static {
	Display display = Display.getCurrent();
	PaletteData pData = new PaletteData(0xFF, 0xFF00, 0xFF0000);
	RGB rgb = ColorConstants.button.getRGB();
	int fillColor = pData.getPixel(rgb);
	ImageData iData = new ImageData(1, 1, 24, pData);
	iData.setPixel(0, 0, fillColor);
	iData.setAlpha(0, 0, 200);
	TRANSPARENCY = new Image(display, iData);
}

protected Label downLabel;

protected Label upLabel;

public PaletteScrollBar() {
	super();
}

protected Clickable createDefaultDownButton() {
	return createTransparentArrowButton();
}

protected Clickable createDefaultUpButton() {
	return createTransparentArrowButton();
}

private ArrowButton createTransparentArrowButton() {
	ArrowButton button = new ArrowButton() {
		protected void paintFigure(Graphics g) {
			if (!getModel().isMouseOver())
				g.drawImage(TRANSPARENCY, new Rectangle(0, 0, 1, 1), getBounds());
			else
				super.paintFigure(g);
		}
	};
	button.setRolloverEnabled(true);
	button.setBorder(BORDER);
	return button;
}

/**
 * @see org.eclipse.draw2d.Figure#findFigureAt(int, int, TreeSearch)
 */
public IFigure findFigureAt(int x, int y, TreeSearch search) {
	IFigure result = super.findFigureAt(x, y, search);
	if (result != this)
		return result;
	return null;
}

/**
 * @see org.eclipse.draw2d.Figure#getPreferredSize(int, int)
 */
public Dimension getPreferredSize(int wHint, int hHint) {
	return new Dimension(wHint, hHint);
}

protected void initialize() {
	super.initialize();
	setLayoutManager(new ScrollBarLayout(transposer) {
		protected Rectangle layoutButtons(ScrollBar scrollBar) {
			Rectangle bounds = transposer.t(scrollBar.getClientArea());
			Dimension buttonSize = new Dimension(bounds.width, BUTTON_HEIGHT);
		
			getButtonUp().setBounds(transposer.t(
				new Rectangle(bounds.getTopLeft(), buttonSize)));
			Rectangle r = new Rectangle (
				bounds.x, bounds.bottom() - buttonSize.height,
				buttonSize.width, buttonSize.height);
			getButtonDown().setBounds(transposer.t(r));
			if (scrollBar.getBackgroundColor() == ColorConstants.listBackground
			  && getButtonDown().getBorder() != CONTRAST)
			  	getButtonDown().setBorder(CONTRAST);
			Rectangle trackBounds = bounds.getCropped(
				new Insets(buttonSize.height, 0, buttonSize.height, 0));
			RangeModel model = scrollBar.getRangeModel();
			getButtonUp().setVisible(model.getValue() != model.getMinimum());
			getButtonDown().setVisible(
				model.getValue() != model.getMaximum() - model.getExtent());
			return trackBounds;
		}
	});
	setPageUp(null);
	setPageDown(null);
	setThumb(null);
	setOpaque(false);
}

/**
 * @see org.eclipse.draw2d.ScrollBar#stepDown()
 */
protected void stepDown() {
	timedStep(false);
}

/**
 * @see org.eclipse.draw2d.ScrollBar#stepUp()
 */
protected void stepUp() {
	timedStep(true);
}

protected void timedStep(boolean up) {
	int increment = Math.max(getExtent() * 1 / 2, getStepIncrement());
	int value = getValue();
	long startTime = System.currentTimeMillis();
	long elapsedTime = System.currentTimeMillis() - startTime;
	while (elapsedTime < SCROLL_TIME) {
		int step = (int)(increment * elapsedTime / SCROLL_TIME);
		step = up ? value - step : value + step;
		setValue(step);
		getUpdateManager().performUpdate();
		elapsedTime = System.currentTimeMillis() - startTime;
	}	
}

protected void updateDownLabel() {
	getButtonDown().setVisible(getValue() < (getMaximum() - getExtent()));
}

protected void updateUpLabel() {
	getButtonUp().setVisible(getValue() > getMinimum());
}

}