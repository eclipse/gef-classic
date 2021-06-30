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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.swt.graphics.Color;

import org.eclipse.draw2dl.geometry.Dimension;
import org.eclipse.draw2dl.geometry.Point;
import org.eclipse.draw2dl.geometry.Rectangle;
import org.eclipse.draw2dl.geometry.Transposer;

/**
 * Provides for the scrollbars used by the {@link ScrollPane}. A ScrollBar is
 * made up of five essential Figures: An 'Up' arrow button, a 'Down' arrow
 * button, a draggable 'Thumb', a 'Pageup' button, and a 'Pagedown' button.
 */
public class ScrollBar extends org.eclipse.draw2dl.Figure implements org.eclipse.draw2dl.Orientable,
		PropertyChangeListener {

	private static final int ORIENTATION_FLAG = org.eclipse.draw2dl.Figure.MAX_FLAG << 1;
	/** @see Figure#MAX_FLAG */
	protected static final int MAX_FLAG = ORIENTATION_FLAG;

	private static final Color COLOR_TRACK = FigureUtilities.mixColors(
			org.eclipse.draw2dl.ColorConstants.white, org.eclipse.draw2dl.ColorConstants.button);

	private org.eclipse.draw2dl.RangeModel rangeModel = null;
	private org.eclipse.draw2dl.IFigure thumb;
	private org.eclipse.draw2dl.Clickable pageUp, pageDown;
	private org.eclipse.draw2dl.Clickable buttonUp, buttonDown;
	/**
	 * Listens to mouse events on the scrollbar to take care of scrolling.
	 */
	protected ThumbDragger thumbDragger = new ThumbDragger();

	private boolean isHorizontal = false;

	private int pageIncrement = 50;
	private int stepIncrement = 10;

	/**
	 * Transposes from vertical to horizontal if needed.
	 */
	protected final Transposer transposer = new Transposer();

	{
		setRangeModel(new DefaultRangeModel());
	}

	/**
	 * Constructs a ScrollBar. ScrollBar orientation is vertical by default.
	 * Call {@link #setHorizontal(boolean)} with <code>true</code> to set
	 * horizontal orientation.
	 * 
	 * @since 2.0
	 */
	public ScrollBar() {
		initialize();
	}

	/**
	 * Creates the default 'Up' ArrowButton for the ScrollBar.
	 * 
	 * @return the up button
	 * @since 2.0
	 */
	protected org.eclipse.draw2dl.Clickable createDefaultUpButton() {
		org.eclipse.draw2dl.Button buttonUp = new org.eclipse.draw2dl.ArrowButton();
		buttonUp.setBorder(new org.eclipse.draw2dl.ButtonBorder(
				org.eclipse.draw2dl.ButtonBorder.SCHEMES.BUTTON_SCROLLBAR));
		return buttonUp;
	}

	/**
	 * Creates the default 'Down' ArrowButton for the ScrollBar.
	 * 
	 * @return the down button
	 * @since 2.0
	 */
	protected org.eclipse.draw2dl.Clickable createDefaultDownButton() {
		Button buttonDown = new ArrowButton();
		buttonDown.setBorder(new org.eclipse.draw2dl.ButtonBorder(
				ButtonBorder.SCHEMES.BUTTON_SCROLLBAR));
		return buttonDown;
	}

	/**
	 * Creates the pagedown Figure for the Scrollbar.
	 * 
	 * @return the page down figure
	 * @since 2.0
	 */
	protected org.eclipse.draw2dl.Clickable createPageDown() {
		return createPageUp();
	}

	/**
	 * Creates the pageup Figure for the Scrollbar.
	 * 
	 * @return the page up figure
	 * @since 2.0
	 */
	protected org.eclipse.draw2dl.Clickable createPageUp() {
		final org.eclipse.draw2dl.Clickable clickable = new org.eclipse.draw2dl.Clickable();
		clickable.setOpaque(true);
		clickable.setBackgroundColor(COLOR_TRACK);
		clickable.setRequestFocusEnabled(false);
		clickable.setFocusTraversable(false);
		clickable.addChangeListener(new ChangeListener() {
			public void handleStateChanged(ChangeEvent evt) {
				if (clickable.getModel().isArmed())
					clickable.setBackgroundColor(org.eclipse.draw2dl.ColorConstants.black);
				else
					clickable.setBackgroundColor(COLOR_TRACK);
			}
		});
		return clickable;
	}

	/**
	 * Creates the Scrollbar's "thumb", the draggable Figure that indicates the
	 * Scrollbar's position.
	 * 
	 * @return the thumb figure
	 * @since 2.0
	 */
	protected org.eclipse.draw2dl.IFigure createDefaultThumb() {
		org.eclipse.draw2dl.Panel thumb = new Panel();
		thumb.setMinimumSize(new Dimension(6, 6));
		thumb.setBackgroundColor(ColorConstants.button);

		thumb.setBorder(new org.eclipse.draw2dl.SchemeBorder(SchemeBorder.SCHEMES.BUTTON_CONTRAST));
		return thumb;
	}

	/**
	 * Returns the figure used as the up button.
	 * 
	 * @return the up button
	 */
	protected org.eclipse.draw2dl.IFigure getButtonUp() {
		// TODO: The set method takes a Clickable while the get method returns
		// an IFigure.
		// Change the get method to return Clickable (since that's what it's
		// typed as).
		return buttonUp;
	}

	/**
	 * Returns the figure used as the down button.
	 * 
	 * @return the down button
	 */
	protected org.eclipse.draw2dl.IFigure getButtonDown() {
		// TODO: The set method takes a Clickable while the get method returns
		// an IFigure.
		// Change the get method to return Clickable (since that's what it's
		// typed as).
		return buttonDown;
	}

	/**
	 * Returns the extent.
	 * 
	 * @return the extent
	 * @see org.eclipse.draw2dl.RangeModel#getExtent()
	 */
	public int getExtent() {
		return getRangeModel().getExtent();
	}

	/**
	 * Returns the minumum value.
	 * 
	 * @return the minimum
	 * @see org.eclipse.draw2dl.RangeModel#getMinimum()
	 */
	public int getMinimum() {
		return getRangeModel().getMinimum();
	}

	/**
	 * Returns the maximum value.
	 * 
	 * @return the maximum
	 * @see org.eclipse.draw2dl.RangeModel#getMaximum()
	 */
	public int getMaximum() {
		return getRangeModel().getMaximum();
	}

	/**
	 * Returns the figure used for page down.
	 * 
	 * @return the page down figure
	 */
	protected org.eclipse.draw2dl.IFigure getPageDown() {
		// TODO: The set method takes a Clickable while the get method returns
		// an IFigure.
		// Change the get method to return Clickable (since that's what it's
		// typed as).
		return pageDown;
	}

	/**
	 * Returns the the amound the scrollbar will move when the page up or page
	 * down areas are pressed.
	 * 
	 * @return the page increment
	 */
	public int getPageIncrement() {
		return pageIncrement;
	}

	/**
	 * Returns the figure used for page up.
	 * 
	 * @return the page up figure
	 */
	protected org.eclipse.draw2dl.IFigure getPageUp() {
		// TODO: The set method takes a Clickable while the get method returns
		// an IFigure.
		// Change the get method to return Clickable (since that's what it's
		// typed as).
		return pageUp;
	}

	/**
	 * Returns the range model for this scrollbar.
	 * 
	 * @return the range model
	 */
	public org.eclipse.draw2dl.RangeModel getRangeModel() {
		return rangeModel;
	}

	/**
	 * Returns the amount the scrollbar will move when the up or down arrow
	 * buttons are pressed.
	 * 
	 * @return the step increment
	 */
	public int getStepIncrement() {
		return stepIncrement;
	}

	/**
	 * Returns the figure used as the scrollbar's thumb.
	 * 
	 * @return the thumb figure
	 */
	protected org.eclipse.draw2dl.IFigure getThumb() {
		return thumb;
	}

	/**
	 * Returns the current scroll position of the scrollbar.
	 * 
	 * @return the current value
	 * @see org.eclipse.draw2dl.RangeModel#getValue()
	 */
	public int getValue() {
		return getRangeModel().getValue();
	}

	/**
	 * Returns the size of the range of allowable values.
	 * 
	 * @return the value range
	 */
	protected int getValueRange() {
		return getMaximum() - getExtent() - getMinimum();
	}

	/**
	 * Initilization of the ScrollBar. Sets the Scrollbar to have a
	 * ScrollBarLayout with vertical orientation. Creates the Figures that make
	 * up the components of the ScrollBar.
	 * 
	 * @since 2.0
	 */
	protected void initialize() {
		setLayoutManager(new org.eclipse.draw2dl.ScrollBarLayout(transposer));
		setUpClickable(createDefaultUpButton());
		setDownClickable(createDefaultDownButton());
		setPageUp(createPageUp());
		setPageDown(createPageDown());
		setThumb(createDefaultThumb());
	}

	/**
	 * Returns <code>true</code> if this scrollbar is orientated horizontally,
	 * <code>false</code> otherwise.
	 * 
	 * @return whether this scrollbar is horizontal
	 */
	public boolean isHorizontal() {
		return isHorizontal;
	}

	private void pageDown() {
		setValue(getValue() + getPageIncrement());
	}

	private void pageUp() {
		setValue(getValue() - getPageIncrement());
	}

	/**
	 * @see PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	public void propertyChange(PropertyChangeEvent event) {
		if (event.getSource() instanceof org.eclipse.draw2dl.RangeModel) {
			setEnabled(getRangeModel().isEnabled());
			if (org.eclipse.draw2dl.RangeModel.PROPERTY_VALUE.equals(event.getPropertyName())) {
				firePropertyChange("value", event.getOldValue(), //$NON-NLS-1$
						event.getNewValue());
				revalidate();
			}
			if (org.eclipse.draw2dl.RangeModel.PROPERTY_MINIMUM.equals(event.getPropertyName())) {
				firePropertyChange("value", event.getOldValue(), //$NON-NLS-1$
						event.getNewValue());
				revalidate();
			}
			if (org.eclipse.draw2dl.RangeModel.PROPERTY_MAXIMUM.equals(event.getPropertyName())) {
				firePropertyChange("value", event.getOldValue(), //$NON-NLS-1$
						event.getNewValue());
				revalidate();
			}
			if (org.eclipse.draw2dl.RangeModel.PROPERTY_EXTENT.equals(event.getPropertyName())) {
				firePropertyChange("value", event.getOldValue(), //$NON-NLS-1$
						event.getNewValue());
				revalidate();
			}
		}
	}

	/**
	 * @see org.eclipse.draw2dl.IFigure#revalidate()
	 */
	public void revalidate() {
		// Override default revalidate to prevent going up the parent chain.
		// Reason for this
		// is that preferred size never changes unless orientation changes.
		invalidate();
		getUpdateManager().addInvalidFigure(this);
	}

	/**
	 * Does nothing because this doesn't make sense for a scrollbar.
	 * 
	 * @see org.eclipse.draw2dl.Orientable#setDirection(int)
	 */
	public void setDirection(int direction) {
		// Doesn't make sense for Scrollbar.
	}

	/**
	 * Sets the Clickable that represents the down arrow of the Scrollbar to
	 * <i>down</i>.
	 * 
	 * @param down
	 *            the down button
	 * @since 2.0
	 */
	public void setDownClickable(org.eclipse.draw2dl.Clickable down) {
		if (buttonDown != null) {
			remove(buttonDown);
		}
		buttonDown = down;
		if (buttonDown != null) {
			if (buttonDown instanceof org.eclipse.draw2dl.Orientable)
				((org.eclipse.draw2dl.Orientable) buttonDown)
						.setDirection(isHorizontal() ? org.eclipse.draw2dl.Orientable.EAST
								: org.eclipse.draw2dl.Orientable.SOUTH);
			buttonDown.setFiringMethod(org.eclipse.draw2dl.Clickable.REPEAT_FIRING);
			buttonDown.addActionListener(new org.eclipse.draw2dl.ActionListener() {
				public void actionPerformed(org.eclipse.draw2dl.ActionEvent e) {
					stepDown();
				}
			});
			add(buttonDown, org.eclipse.draw2dl.ScrollBarLayout.DOWN_ARROW);
		}
	}

	/**
	 * Sets the Clickable that represents the up arrow of the Scrollbar to
	 * <i>up</i>.
	 * 
	 * @param up
	 *            the up button
	 * @since 2.0
	 */
	public void setUpClickable(org.eclipse.draw2dl.Clickable up) {
		if (buttonUp != null) {
			remove(buttonUp);
		}
		buttonUp = up;
		if (up != null) {
			if (up instanceof org.eclipse.draw2dl.Orientable)
				((org.eclipse.draw2dl.Orientable) up).setDirection(isHorizontal() ? org.eclipse.draw2dl.Orientable.WEST
						: org.eclipse.draw2dl.Orientable.NORTH);
			buttonUp.setFiringMethod(org.eclipse.draw2dl.Clickable.REPEAT_FIRING);
			buttonUp.addActionListener(new org.eclipse.draw2dl.ActionListener() {
				public void actionPerformed(org.eclipse.draw2dl.ActionEvent e) {
					stepUp();
				}
			});
			add(buttonUp, org.eclipse.draw2dl.ScrollBarLayout.UP_ARROW);
		}
	}

	/**
	 * @see org.eclipse.draw2dl.IFigure#setEnabled(boolean)
	 */
	public void setEnabled(boolean value) {
		if (isEnabled() == value)
			return;
		super.setEnabled(value);
		setChildrenEnabled(value);
		if (getThumb() != null) {
			getThumb().setVisible(value);
			revalidate();
		}
	}

	/**
	 * Sets the extent of the Scrollbar to <i>ext</i>
	 * 
	 * @param ext
	 *            the extent
	 * @since 2.0
	 */
	public void setExtent(int ext) {
		if (getExtent() == ext)
			return;
		getRangeModel().setExtent(ext);
	}

	/**
	 * Sets the orientation of the ScrollBar. If <code>true</code>, the
	 * Scrollbar will have a horizontal orientation. If <code>false</code>, the
	 * scrollBar will have a vertical orientation.
	 * 
	 * @param value
	 *            <code>true</code> if the scrollbar should be horizontal
	 * @since 2.0
	 */
	public final void setHorizontal(boolean value) {
		setOrientation(value ? HORIZONTAL : VERTICAL);
	}

	/**
	 * Sets the maximum position to <i>max</i>.
	 * 
	 * @param max
	 *            the maximum position
	 * @since 2.0
	 */
	public void setMaximum(int max) {
		if (getMaximum() == max)
			return;
		getRangeModel().setMaximum(max);
	}

	/**
	 * Sets the minimum position to <i>min</i>.
	 * 
	 * @param min
	 *            the minumum position
	 * @since 2.0
	 */
	public void setMinimum(int min) {
		if (getMinimum() == min)
			return;
		getRangeModel().setMinimum(min);
	}

	/**
	 * @see Orientable#setOrientation(int)
	 */
	public void setOrientation(int value) {
		if ((value == HORIZONTAL) == isHorizontal())
			return;
		isHorizontal = value == HORIZONTAL;
		transposer.setEnabled(isHorizontal);

		setChildrenOrientation(value);
		super.revalidate();
	}

	/**
	 * Sets the ScrollBar to scroll <i>increment</i> pixels when its pageup or
	 * pagedown buttons are pressed. (Note that the pageup and pagedown buttons
	 * are <b>NOT</b> the arrow buttons, they are the figures between the arrow
	 * buttons and the ScrollBar's thumb figure).
	 * 
	 * @param increment
	 *            the new page increment
	 * @since 2.0
	 */
	public void setPageIncrement(int increment) {
		pageIncrement = increment;
	}

	/**
	 * Sets the pagedown button to the passed Clickable. The pagedown button is
	 * the figure between the down arrow button and the ScrollBar's thumb
	 * figure.
	 * 
	 * @param down
	 *            the page down figure
	 * @since 2.0
	 */
	public void setPageDown(org.eclipse.draw2dl.Clickable down) {
		if (pageDown != null)
			remove(pageDown);
		pageDown = down;
		if (pageDown != null) {
			pageDown.setFiringMethod(org.eclipse.draw2dl.Clickable.REPEAT_FIRING);
			pageDown.addActionListener(new org.eclipse.draw2dl.ActionListener() {
				public void actionPerformed(org.eclipse.draw2dl.ActionEvent event) {
					pageDown();
				}
			});
			add(down, org.eclipse.draw2dl.ScrollBarLayout.PAGE_DOWN);
		}
	}

	/**
	 * Sets the pageup button to the passed Clickable. The pageup button is the
	 * rectangular figure between the down arrow button and the ScrollBar's
	 * thumb figure.
	 * 
	 * @param up
	 *            the page up figure
	 * @since 2.0
	 */
	public void setPageUp(org.eclipse.draw2dl.Clickable up) {
		if (pageUp != null)
			remove(pageUp);
		pageUp = up;
		if (pageUp != null) {
			pageUp.setFiringMethod(Clickable.REPEAT_FIRING);
			pageUp.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					pageUp();
				}
			});
			add(pageUp, org.eclipse.draw2dl.ScrollBarLayout.PAGE_UP);
		}
	}

	/**
	 * Sets the ScrollBar's RangeModel to the passed value.
	 * 
	 * @param rangeModel
	 *            the new range model
	 * @since 2.0
	 */
	public void setRangeModel(RangeModel rangeModel) {
		if (this.rangeModel != null)
			this.rangeModel.removePropertyChangeListener(this);
		this.rangeModel = rangeModel;
		rangeModel.addPropertyChangeListener(this);
	}

	/**
	 * Sets the ScrollBar's step increment to the passed value. The step
	 * increment indicates how many pixels the ScrollBar will scroll when its up
	 * or down arrow button is pressed.
	 * 
	 * @param increment
	 *            the new step increment
	 * @since 2.0
	 */
	public void setStepIncrement(int increment) {
		stepIncrement = increment;
	}

	/**
	 * Sets the ScrollBar's thumb to the passed Figure. The thumb is the
	 * draggable component of the ScrollBar that indicates the ScrollBar's
	 * position.
	 * 
	 * @param figure
	 *            the thumb figure
	 * @since 2.0
	 */
	public void setThumb(IFigure figure) {
		if (thumb != null) {
			thumb.removeMouseListener(thumbDragger);
			thumb.removeMouseMotionListener(thumbDragger);
			remove(thumb);
		}
		thumb = figure;
		if (thumb != null) {
			thumb.addMouseListener(thumbDragger);
			thumb.addMouseMotionListener(thumbDragger);
			add(thumb, ScrollBarLayout.THUMB);
		}
	}

	/**
	 * Sets the value of the Scrollbar to <i>v</i>
	 * 
	 * @param v
	 *            the new value
	 * @since 2.0
	 */
	public void setValue(int v) {
		getRangeModel().setValue(v);
	}

	/**
	 * Causes the ScrollBar to scroll down (or right) by the value of its step
	 * increment.
	 * 
	 * @since 2.0
	 */
	protected void stepDown() {
		setValue(getValue() + getStepIncrement());
	}

	/**
	 * Causes the ScrollBar to scroll up (or left) by the value of its step
	 * increment.
	 * 
	 * @since 2.0
	 */
	protected void stepUp() {
		setValue(getValue() - getStepIncrement());
	}

	/**
	 * @since 3.6
	 */
	protected class ThumbDragger extends MouseMotionListener.Stub implements
        MouseListener {
		protected Point start;
		protected int dragRange;
		protected int revertValue;
		protected boolean armed;

		public ThumbDragger() {
		}

		public void mousePressed(org.eclipse.draw2dl.MouseEvent me) {
			armed = true;
			start = me.getLocation();
			Rectangle area = new Rectangle(transposer.t(getClientArea()));
			Dimension thumbSize = transposer.t(getThumb().getSize());
			if (getButtonUp() != null)
				area.height -= transposer.t(getButtonUp().getSize()).height;
			if (getButtonDown() != null)
				area.height -= transposer.t(getButtonDown().getSize()).height;
			Dimension sizeDifference = new Dimension(area.width, area.height
					- thumbSize.height);
			dragRange = sizeDifference.height;
			revertValue = getValue();
			me.consume();
		}

		public void mouseDragged(org.eclipse.draw2dl.MouseEvent me) {
			if (!armed)
				return;
			Dimension difference = transposer.t(me.getLocation().getDifference(
					start));
			int change = getValueRange() * difference.height / dragRange;
			setValue(revertValue + change);
			me.consume();
		}

		public void mouseReleased(org.eclipse.draw2dl.MouseEvent me) {
			if (!armed)
				return;
			armed = false;
			me.consume();
		}

		public void mouseDoubleClicked(MouseEvent me) {
		}
	}

}
