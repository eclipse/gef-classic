/*******************************************************************************
 * Copyright (c) 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Research Group Software Construction,
 *     RWTH Aachen University, Germany - initial API and implementation
 */
package org.eclipse.draw2d;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.draw2d.geometry.Rectangle;

/**
 * A {@link ScrollPane} with transparent {@link ScrollBar}s.
 * 
 * @author Alexander Nyssen
 * @author Philip Ritzkopf
 *
 * @since 3.6
 */
public class PuristicScrollPane extends ScrollPane {

	/**
	 * A {@link ScrollBar} with no thumb and non-opaque buttons.
	 * 
	 * @author Alexander Nyssen
	 * @author Philip Ritzkopf
	 */
	public class PuristicScrollBar extends ScrollBar {

		/**
		 * Instantiates a new transparent scroll bar.
		 * 
		 * @param isHorizontal
		 *            whether this scroll bar is used as a horizontal one.
		 */
		public PuristicScrollBar(boolean isHorizontal) {
			super();
			setHorizontal(isHorizontal);
		}

		/**
		 * @see org.eclipse.draw2d.ScrollBar#createDefaultDownButton()
		 */
		protected Clickable createDefaultDownButton() {
			Clickable buttonDown = super.createDefaultDownButton();
			buttonDown.setBorder(null);
			buttonDown.setOpaque(false);
			return buttonDown;
		}

		/**
		 * @see org.eclipse.draw2d.ScrollBar#createDefaultThumb()
		 */
		protected IFigure createDefaultThumb() {
			return null;
		}

		/**
		 * @see org.eclipse.draw2d.ScrollBar#createDefaultUpButton()
		 */
		protected Clickable createDefaultUpButton() {
			Clickable buttonUp = super.createDefaultUpButton();
			buttonUp.setBorder(null);
			buttonUp.setOpaque(false);
			return buttonUp;
		}

		/**
		 * @see org.eclipse.draw2d.ScrollBar#createPageDown()
		 */
		protected Clickable createPageDown() {
			return null;
		}

		/**
		 * @see org.eclipse.draw2d.ScrollBar#createPageUp()
		 */
		protected Clickable createPageUp() {
			return null;
		}

		/**
		 * @see PropertyChangeListener#propertyChange(java.beans.
		 *      PropertyChangeEvent )
		 */
		public void propertyChange(PropertyChangeEvent event) {
			if (event.getSource() instanceof RangeModel) {
				getButtonDown().setVisible(
						getValue() != getMaximum() - getExtent());
				getButtonUp().setVisible(getValue() != getMinimum());
			}
			super.propertyChange(event);
		}
	}

	/**
	 * @see org.eclipse.draw2d.ScrollPane#createVerticalScrollBar()
	 */
	protected void createVerticalScrollBar() {
		PuristicScrollBar verticalScrollBar = new PuristicScrollBar(false);
		setVerticalScrollBar(verticalScrollBar);
	}

	/**
	 * @see org.eclipse.draw2d.ScrollPane#createHorizontalScrollBar()
	 */
	protected void createHorizontalScrollBar() {
		PuristicScrollBar horizontalScrollBar = new PuristicScrollBar(true);
		setHorizontalScrollBar(horizontalScrollBar);
	}

	/**
	 * @see org.eclipse.draw2d.Figure#paintChildren(org.eclipse.draw2d.Graphics)
	 */
	protected void paintChildren(Graphics graphics) {
		IFigure child;
		// don't clip scroll bar area (as there is no thumb)
		Rectangle clip = Rectangle.SINGLETON;
		for (int i = 0; i < getChildren().size(); i++) {
			child = (IFigure) getChildren().get(i);
			if (child.isVisible() && child.intersects(graphics.getClip(clip))) {
				graphics.clipRect(getBounds());
				child.paint(graphics);
				graphics.restoreState();
			}
		}
	}

	/**
	 * @see org.eclipse.draw2d.Figure#invalidate()
	 */
	public void invalidate() {
		// ensure scroll bar area is marked dirty as well.
		getUpdateManager().addDirtyRegion(this, this.getBounds());
		super.invalidate();
	}
}