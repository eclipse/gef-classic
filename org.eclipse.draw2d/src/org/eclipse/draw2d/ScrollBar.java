package org.eclipse.draw2d;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.draw2d.geometry.*;
import org.eclipse.swt.graphics.Color;

/**
 * Provides for the scrollbars used by the
 * {@link ScrollPane}. A ScrollBar is made up of five essential
 * Figures: An 'Up' arrow button, a 'Down' arrow button, a 
 * draggable 'Thumb', a 'Pageup' button, and a 'Pagedown' button.
 * 
 */
public class ScrollBar
	extends Figure
	implements Orientable, PropertyChangeListener
{

private static final int ORIENTATION_FLAG = Figure.MAX_FLAG <<1;
protected static final int MAX_FLAG = ORIENTATION_FLAG;

private static final Color COLOR_TRACK = FigureUtilities.mixColors(
		ColorConstants.white,
		ColorConstants.button);

private RangeModel rangeModel = null;
private IFigure thumb;
private Clickable pageUp, pageDown;
private Clickable buttonUp, buttonDown;
protected ThumbDragger thumbDragger = new ThumbDragger();

private boolean isHorizontal = false;

private int pageIncrement = 50;
private int stepIncrement = 10;

final protected Transposer transposer = new Transposer();

{
	setRangeModel(new DefaultRangeModel());
}

/**
 * Constructs a ScrollBar. ScrollBar orientation
 * is vertical by default. Call setHorizontal(true)
 * to set horizontal orientation.
 * 
 * @since 2.0
 */
public ScrollBar() {
	initialize();
}

/**
 * Creates the default 'Up' ArrowButton for the ScrollBar.
 * 
 * @since 2.0
 */
protected Clickable createDefaultUpButton() {
	Button buttonUp = new ArrowButton();
	buttonUp.setBorder(new ButtonBorder(ButtonBorder.SCHEMES.BUTTON_SCROLLBAR));
	return buttonUp;
}

/**
 * Creates the default 'Down' ArrowButton for the ScrollBar.
 * 
 * @since 2.0
 */
protected Clickable createDefaultDownButton(){
	Button buttonDown = new ArrowButton();
	buttonDown.setBorder(new ButtonBorder(ButtonBorder.SCHEMES.BUTTON_SCROLLBAR));
	return buttonDown;
}

/**
 * Creates the pagedown Figure for the Scrollbar.
 * 
 * @since 2.0 
 */
protected Clickable createPageDown(){
	return createPageUp();
}

/**
 * Creates the pageup Figure for the Scrollbar.
 * 
 * @since 2.0 
 */
protected Clickable createPageUp(){
	final Clickable clickable = new Clickable();
	clickable.setOpaque(true);
	clickable.setBackgroundColor(COLOR_TRACK);
	clickable.setRequestFocusEnabled(false);
	clickable.setFocusTraversable(false);
	clickable.addChangeListener(new ChangeListener(){
		public void handleStateChanged(ChangeEvent evt){
			if (clickable.getModel().isArmed())
				clickable.setBackgroundColor(ColorConstants.black);
			else
				clickable.setBackgroundColor(COLOR_TRACK);
		}
	});
	return clickable;
}

/**
 * Creates the Scrollbar's "thumb", the draggable Figure
 * that indicates the Scrollbar's position.
 * 
 * @since 2.0
 */
protected IFigure createDefaultThumb() {
	Panel thumb = new Panel();
	thumb.setMinimumSize(new Dimension(6,6));
	thumb.setBackgroundColor(ColorConstants.button);

	thumb.setBorder(new SchemeBorder(SchemeBorder.SCHEMES.BUTTON_CONTRAST));
	return thumb;
}

protected IFigure getButtonUp() {return buttonUp;}
protected IFigure getButtonDown() {return buttonDown;}
public    int     getExtent() {return getRangeModel().getExtent();}
public    int     getMinimum() {return getRangeModel().getMinimum();}
public    int     getMaximum() {return getRangeModel().getMaximum();}
protected IFigure getPageDown(){return pageDown;}
public    int     getPageIncrement() {return pageIncrement;}
protected IFigure getPageUp(){return pageUp;}
public RangeModel getRangeModel(){return rangeModel;}
public    int     getStepIncrement() {return stepIncrement;}
protected IFigure getThumb(){return thumb;}
public    int     getValue() {return getRangeModel().getValue();}

protected int     getValueRange(){return getMaximum() - getExtent() - getMinimum();}

/**
 * Initilization of the ScrollBar. Sets the Scrollbar to have
 * a ScrollBarLayout with vertical orientation. 
 * Creates the Figures that make up the components of the
 * ScrollBar.
 * 
 * @since 2.0
 */
protected void initialize(){
	setLayoutManager(new ScrollBarLayout(transposer));
	setUpClickable(createDefaultUpButton());
	setDownClickable(createDefaultDownButton());
	setPageUp(createPageUp());
	setPageDown(createPageDown());
	setThumb(createDefaultThumb());
}

public    boolean isHorizontal() {return isHorizontal;}
private   void    pageDown() {setValue(getValue() + getPageIncrement()); }
private   void    pageUp() {setValue(getValue() - getPageIncrement());}

public void propertyChange(PropertyChangeEvent event){
	if( event.getSource() instanceof RangeModel){
		setEnabled(getRangeModel().isEnabled());
		if( RangeModel.PROPERTY_VALUE.equals(event.getPropertyName())){
			firePropertyChange("value", event.getOldValue(), event.getNewValue());//$NON-NLS-1$
			revalidate();
		}
		if(RangeModel.PROPERTY_MINIMUM.equals(event.getPropertyName())){
			firePropertyChange("value", event.getOldValue(), event.getNewValue());//$NON-NLS-1$
			revalidate();
		}
		if(RangeModel.PROPERTY_MAXIMUM.equals(event.getPropertyName())){
			firePropertyChange("value", event.getOldValue(), event.getNewValue());//$NON-NLS-1$
			revalidate();
		}
		if(RangeModel.PROPERTY_EXTENT.equals(event.getPropertyName())){
			firePropertyChange("value", event.getOldValue(), event.getNewValue());//$NON-NLS-1$
			revalidate();
		}
	}
}

public void revalidate(){
	//Override default revalidate to prevent going up the parent chain.
	//Reason for this is that preferred size never changes unless orientation changes.
	invalidate();
	getUpdateManager().addInvalidFigure(this);
}

public void setDirection(int direction){
	//Doesn't make sense for Scrollbar.
}

/**
 * Sets the Clickable that represents the down arrow of the
 * Scrollbar to <i>down</i>.
 * 
 * @since 2.0
 */
public void setDownClickable(Clickable down){
	if (buttonDown != null) {
		remove(buttonDown);
	}
	buttonDown = down;
	if (buttonDown != null) {
		if (buttonDown instanceof Orientable)
			((Orientable)buttonDown).setDirection(isHorizontal() ? Orientable.EAST : Orientable.SOUTH);
		buttonDown.setFiringMethod(Clickable.REPEAT_FIRING);
		buttonDown.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){stepDown();}
		});
		add(buttonDown, ScrollBarLayout.DOWN_ARROW);
	}
}

/**
 * Sets the Clickable that represents the up arrow of the
 * Scrollbar to <i>up</i>.
 * 
 * @since 2.0
 */
public void setUpClickable(Clickable up){
	if (buttonUp != null) {
		remove(buttonUp);
	}
	buttonUp = up;
	if (up != null){
		if (up instanceof Orientable)
			((Orientable)up).setDirection(isHorizontal() ? Orientable.WEST : Orientable.NORTH);
		buttonUp.setFiringMethod(Clickable.REPEAT_FIRING);
		buttonUp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {stepUp();}
		});
		add(buttonUp, ScrollBarLayout.UP_ARROW);
	}
}

/**
 * Sets enabled status of Scrollbar to <i>value</i>.
 * 
 * @since 2.0
 */
public void setEnabled(boolean value){
	if (isEnabled() == value)
		return;
	super.setEnabled(value);
	setChildrenEnabled(value);
	if(getThumb()!=null){
		getThumb().setVisible(value);
		revalidate();
	}
}

/**
 * Sets the extent of the Scrollbar to <i>ext</i>
 * 
 * @since 2.0
 */
public void setExtent(int ext) {
	if(getExtent() == ext) return;
	getRangeModel().setExtent(ext);
}

/**
 * Sets the orientation of the ScrollBar.
 * 
 * @param value If <i>true</i>, Scrollbar will 
 *               have horizontal orientation.
 *               If <i>false</i> ScrollBar will have
 *               vertical orientation.
 * 
 * @since 2.0
 */
final public void setHorizontal(boolean value){
	setOrientation(value ? HORIZONTAL : VERTICAL);
}

/**
 * The ScrollBars position is designated by integer values.
 * Sets the maximum position to <i>max</i>
 * 
 * @since 2.0
 */
public void setMaximum(int max) {
	if(getMaximum() == max) return;
	getRangeModel().setMaximum(max);
}

/**
 * The ScrollBars position is designated by integer values.
 * Sets the minimum position to <i>min</i>
 * 
 * @since 2.0
 */
public void setMinimum(int min) {
	if(getMinimum()== min) return;
	getRangeModel().setMinimum(min);
}

/**
 * Sets the orientation of the ScrollBar to the passed
 * value. 
 * 
 * @param value Can be either HORIZONTAL or VERTICAL
 *               as seen in {@link Orientable} 
 * @since 2.0
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
 * Sets the ScrollBar to scroll <i>increment</i> pixels
 * when its pageup or pagedown buttons are pressed. 
 * (Note that the pageup and pagedown buttons are NOT the 
 * arrow buttons, they are the figures between
 * the arrow buttons and the ScrollBar's thumb figure).
 * 
 * @since 2.0
 */
public void setPageIncrement(int increment) {pageIncrement = increment;}

/**
 * Sets the pagedown button to the passed Clickable.
 * The pagedown button is the figure between
 * the down arrow button and the ScrollBar's thumb figure.
 * 
 * @since 2.0
 */
public void setPageDown(Clickable down){
	if(pageDown != null)
		remove(pageDown);
	pageDown=down;
	if(pageDown != null){
		pageDown.setFiringMethod(Clickable.REPEAT_FIRING);
		pageDown.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event){
				pageDown();
			}
		});
		add(down,ScrollBarLayout.PAGE_DOWN);
	}
}

/**
 * Sets the pageup button to the passed Clickable.
 * The pageup button is the rectangular figure between
 * the down arrow button and the ScrollBar's thumb figure.
 * 
 * @since 2.0
 */
public void setPageUp(Clickable up){
	if(pageUp != null)
		remove(pageUp);
	pageUp = up;
	if(pageUp != null){
		pageUp.setFiringMethod(Clickable.REPEAT_FIRING);
		pageUp.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event){
				pageUp();
			}
		});
		add(pageUp,ScrollBarLayout.PAGE_UP);
	}
}

/**
 * Sets the ScrollBar's RangeModel to the passed value.
 * 
 * @since 2.0
 */
public void setRangeModel(RangeModel rangeModel){
	if(this.rangeModel != null)
		this.rangeModel.removePropertyChangeListener(this);
	this.rangeModel = rangeModel;
	rangeModel.addPropertyChangeListener(this);
}

/**
 * Sets the ScrollBar's step increment to the passed value.
 * The step increment indicates how many pixels the ScrollBar
 * will scroll when its up or down arrow button is pressed.
 * 
 * @since 2.0
 */
public void setStepIncrement(int increment) {stepIncrement = increment;}

/**
 * Sets the ScrollBar's thumb to the passed Figure.
 * The thumb is the draggable component of the ScrollBar
 * that indicates the ScrollBar's position.
 * 
 * @since 2.0
 */
public void setThumb(IFigure figure){
	if (thumb != null){
		thumb.removeMouseListener(thumbDragger);
		thumb.removeMouseMotionListener(thumbDragger);
		remove(thumb);
	}
	thumb = figure;
	if (thumb != null){
		thumb.addMouseListener(thumbDragger);
		thumb.addMouseMotionListener(thumbDragger);
		add(thumb,ScrollBarLayout.THUMB);
	}
}

/**
 * Sets the value of the Scrollbar to <i>v</i>
 * 
 * @since 2.0
 */
public void setValue(int v) {getRangeModel().setValue(v);}

/**
 * Causes the ScrollBar to scroll down (or right) by the 
 * value of its step increment.
 * 
 * @since 2.0
 */
private void stepDown() {setValue(getValue() + getStepIncrement());}

/**
 * Causes the ScrollBar to scroll up (or left) by the 
 * value of its step increment.
 * 
 * @since 2.0
 */
private void stepUp() {setValue(getValue() - getStepIncrement());}

class ThumbDragger
	extends MouseMotionListener.Stub
	implements MouseListener
{
	protected Point start;
	protected int dragRange;
	protected int revertValue;
	protected boolean armed;
	public ThumbDragger(){}
	
	public void mousePressed(MouseEvent me){
		armed = true;
		start = me.getLocation();
		Rectangle area = new Rectangle(transposer.t(getClientArea()));
		Dimension thumbSize = transposer.t(getThumb().getSize());
		if(getButtonUp()!=null)
			area.height-=transposer.t(getButtonUp().getSize()).height;
		if(getButtonDown()!=null)
			area.height-=transposer.t(getButtonDown().getSize()).height;
		Dimension sizeDifference = new Dimension(
			area.width, area.height-thumbSize.height);
		dragRange = sizeDifference.height;
		revertValue = getValue();
		me.consume();
	}
	
	public void mouseDragged(MouseEvent me){
		if (!armed) return;
		Dimension difference = transposer.t(me.getLocation().getDifference(start));
		int change = getValueRange() * difference.height / dragRange;
		setValue(revertValue + change);
		me.consume();
	}
	
	public void mouseReleased(MouseEvent me){
		if (!armed) return;
		armed = false;
		me.consume();
	}
	
	public void mouseDoubleClicked(MouseEvent me){}
	};

}