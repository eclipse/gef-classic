package com.ibm.etools.draw2d;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.util.*;

import org.eclipse.swt.graphics.Image;
import com.ibm.etools.draw2d.geometry.*;
import com.ibm.etools.draw2d.util.Timer;

/**
 * Clickable - that which responds to mouse clicks in some way
 * (determined by a ClickBehavior) and fires action events.
 * No visual appearance of feedback is offered. Depends on a
 * model holder and an event handler which understands the model
 * and updates the model accordingly. {@link ButtonModel} is used
 * by default. Any figure can be set as contents to a Clickable.
 * Clickable->EventHandler->Model->ModelObserver->Listeners of actions.
 */

public class Clickable
	extends Figure
{

static private final int
	ROLLOVER_ENABLED_FLAG = Figure.MAX_FLAG << 1,
	STYLE_BUTTON_FLAG = Figure.MAX_FLAG << 2,
	STYLE_TOGGLE_FLAG = Figure.MAX_FLAG << 3,
	MAX_FLAG = STYLE_TOGGLE_FLAG;

public static int 
	STYLE_BUTTON = STYLE_BUTTON_FLAG,
	STYLE_TOGGLE = STYLE_TOGGLE_FLAG;
	
/**
 * Observers the model for action and state changes.
 *
 * @see ActionListener
 * @see ChangeListener 
 */
static interface ModelObserver
	extends ActionListener, ChangeListener
{}

private ClickableEventHandler eventHandler;

private ButtonModel model;

private ModelObserver modelObserver;

{
	init();
	setRequestFocusEnabled(true);
	setFocusTraversable(true);
}

/**
 * Constructs a Clickable with no contents.
 */
public Clickable(){}

/**
 * Constructs a Clickable whose contents are provided 
 * as input. The content figure occupies the entire
 * region of the Clickable.
 */
public Clickable(IFigure contents){this(contents, 0);}

/**
 * Constructs a Clickable whose contents are provided 
 * as input. The content figure occupies the entire
 * region of the Clickable.
 * 
 * @param style Integer constants STYLE_BUTTON 
 *               or STYLE_TOGGLE.
 */
public Clickable(IFigure contents, int style){
	setContents(contents);
	setStyle(style);
}

public static final int
	DEFAULT_FIRING = 0,
	REPEAT_FIRING  = 1;

/**
 * Adds the given listener to the list of action listeners
 * of this Figure. Listener is called whenever an action is
 * performed.
 *
 * @param listener  Action listener to be added.
 * @see  #removeActionListener(ActionListener)
 * @since 2.0
 */
public void addActionListener(ActionListener listener){
	addListener(ActionListener.class, listener);
}

/**
 * Adds the given listener to the list of state change listeners
 * of this figure. A ChangeListener is informed if there is any
 * state change in the model requiring action by the listener. 
 *
 * @param listener  Change listener to be added.
 * @see  #removeChangeListener(ChangeListener)
 * @since 2.0
 */
public void addChangeListener(ChangeListener listener){
	addListener(ChangeListener.class, listener);
}

/**
 * Returns a newly created {@link ButtonModel} as the default model
 * to be used by this Clickable.
 *
 * @return  Model to be used by default.
 * @see  ButtonModel
 * @since 2.0
 */
protected ButtonModel createDefaultModel(){
	if( isStyle(STYLE_TOGGLE) )
		return new ToggleModel();
	else
		return new ButtonModel();
}

/**
 * Returns a newly created event handler for this Clickable
 * and its model.
 *
 * @return  Event handler.
 * @since 2.0
 */
protected ClickableEventHandler createEventHandler(){
	return new ClickableEventHandler();
}

/**
 * Returns a newly created model observer which listens to the 
 * model, and fires any action or state changes. A ModelObserver
 * holds both an action listener and a state change listener.
 *
 * @return  Newly created model observer.
 * @since 2.0
 */
protected ModelObserver createModelObserver(){
	return new ModelObserver(){
		public void actionPerformed(ActionEvent action){
			fireActionPerformed();
		}
		public void handleStateChanged(ChangeEvent change){
			fireStateChanged(change);
		}
	};
}

/**
 * Fires an action performed event.
 * 
 * @since 2.0
 */
public void doClick(){
	fireActionPerformed();
}

/**
 * Called when there has been an action performed by this
 * Clickable, which is determined by the model. Notifies 
 * all ActionListener type listeners of an action performed.
 * 
 * @since 2.0
 */
protected void fireActionPerformed(){
	ActionEvent action = new ActionEvent(this);
	Iterator listeners = getListeners(ActionListener.class);
	while (listeners.hasNext())
		((ActionListener)listeners.next()).  //Leave newline for debug stepping
			actionPerformed(action);
}

/**
 * Called when there has been a change of state in the model
 * of this clickable. Notifies all ChangeListener type listeners 
 * of the state change.
 * 
 * @since 2.0
 */
protected void fireStateChanged(ChangeEvent modelChange){
	ChangeEvent change = new ChangeEvent(this, modelChange.getPropertyName());
	Iterator listeners = getListeners(ChangeListener.class);
	while (listeners.hasNext())
		((ChangeListener)listeners.next()).  //Leave newline for debug stepping
			handleStateChanged(change);
}

/**
 * Returns the behavior model used by this Clickable.
 *
 * @return  Model used by this Clickable.
 * @since 2.0
 */
public ButtonModel getModel(){
	return model;
}

/**
 * Adds the given clickable event handler to this clickable.
 * A clickable event handler should be a MouseListener,
 * MouseMotionListener, ChangeListener, KeyListener, 
 * and FocusListener.
 *
 * @param handler  The new event handler.
 * @since 2.0
 */
protected void hookEventHandler(ClickableEventHandler handler){
	if (handler == null) return;
	addMouseListener(handler);
	addMouseMotionListener(handler);
	addChangeListener(handler);
	addKeyListener(handler);
	addFocusListener(handler);
}

/**
 * Sets model to be used by this Clickable.
 *
 * @param model   Model to be used.
 * @since 2.0
 */
protected void hookModel(ButtonModel model){
}

/**
 * Initializes this Clickable by setting a default model
 * and adding a clickable event handler for that model.
 * 
 * @since 2.0
 */
protected void init(){
	setModel(createDefaultModel());
	setEventHandler(createEventHandler());
}

/**
 * Returns whether rollover feedback is enabled or not.
 * 
 * @return   Rollover enabled on this clickable or not.
 * @see  #setRolloverEnabled(boolean)
 * @since 2.0
 */
public boolean isRolloverEnabled(){
	return (flags & ROLLOVER_ENABLED_FLAG) != 0;
}

/**
 * Returns whether this Clickable is in a selected state
 * or not. The model is the one which holds all this state
 * based information.
 *
 * @return  Selected state of this clickable.
 * @see  #setSelected(boolean)
 * @since 2.0
 */
public boolean isSelected(){
	return getModel().isSelected();
}

/**
 * Returns <code>true</code> if this Clickable's style is the 
 * same as the passed style.
 * 
 * @since 2.0
 */
public boolean isStyle(int style){
	return ((style & flags)==style);
}

protected void paintBorder(Graphics graphics){
	super.paintBorder(graphics);
	if(hasFocus()){
		graphics.setBackgroundColor(ColorConstants.buttonDarkest);
		graphics.setForegroundColor(ColorConstants.buttonLightest);

		Rectangle borderBounds = getClientArea();
		graphics.drawFocus(borderBounds.x,borderBounds.y,
								borderBounds.width-1, borderBounds.height-1);
	}
}

/**
 * Paints the area of this figure excluded by the borders.
 * Induces a (1,1) pixel shift in the painting if the 
 * mouse is armed, giving it the pressed appearance. 
 *
 * @param g  Graphics handle for painting.
 * @since 2.0
 */
protected void paintClientArea(Graphics g){
	if ( isStyle(STYLE_BUTTON) && 
			(getModel().isArmed() || 
			 getModel().isSelected())){
		g.translate(1,1);
		super.paintClientArea(g);
		g.translate(-1,-1);
	} else 
		super.paintClientArea(g);
}

/**
 * Removes the given listener from the list of ActionListener's
 * of this Clickable.
 * 
 * @param listener  Listener to be removed from this figure.
 * @see  #addActionListener(ActionListener)
 * @since 2.0
 */
public void removeActionListener(ActionListener listener){
	removeListener(ActionListener.class, listener);
}

/**
 * Removes the given listener from the list of ChangeListener's
 * of this clickable.
 * 
 * @param listener  Listener to be removed from this figure.
 * @see  #addChangeListener(ChangeListener)
 * @since 2.0
 */
public void removeChangeListener(ChangeListener listener){
	removeListener(ChangeListener.class, listener);
}

/**
 * Sets the Figure which is the contents of this Clickable. This
 * Figure occupies the entire clickable region.
 *
 * @param contents   Contents of the clickable.
 * @since 2.0
 */
protected void setContents(IFigure contents){
	setLayoutManager(new StackLayout());
	if(getChildren().size() > 0)
		remove((IFigure)getChildren().get(0));
	add(contents);
}

public void setEnabled(boolean value){
	if (isEnabled() == value) return;
	super.setEnabled(value);
	getModel().setEnabled(value);
	setChildrenEnabled(value);
}

/**
 * Sets the event handler which interacts with the model to
 * determine the behavior of this Clickable.
 *
 * @param h  Event handler for this clickable.
 * @since 2.0
 */
public void setEventHandler(ClickableEventHandler h){
	if (eventHandler != null) unhookEventHandler(eventHandler);
	eventHandler = h;
	if (eventHandler != null) hookEventHandler(eventHandler);
}

/**
 * Determines how this clickable is to fire notifications to its
 * listeners. In the default firing method, an action is performed 
 * every time the mouse is released. In the repeat firing method,
 * firing starts as soon as it is pressed on this clickable, and
 * keeps firing at prefixed intervals till the mouse is released.
 *
 * @param type  Type of firing. Can be any of the firing behavior
 *              values defined in the {@link ButtonModel model}.
 * @since 2.0
 */
public void setFiringMethod(int type){
	getModel().setFiringBehavior(type);
}

/**
 * Sets the model to be used by this clickable for its state and
 * behavior determination. This clickable removes any observers
 * from the previous model before adding new ones to the new model.
 *
 * @param model  New model of this Clickable.
 * @since 2.0
 */
public void setModel(ButtonModel model){
	if (this.model != null){
		this.model.removeChangeListener(modelObserver);
		this.model.removeActionListener(modelObserver);
		modelObserver = null;
	}
	this.model = model;
	if(model != null){
		modelObserver = createModelObserver();
		model.addActionListener(modelObserver);
		model.addChangeListener(modelObserver);
	}
}

/**
 * Enables or disables roll over feedback of this figure,
 * repainting it. Generally used in conjunction with the 
 * model to determine if feedback is to be shown. 
 *
 * @param value  Rollover state to be set.
 * @see  #isRolloverEnabled()
 * @since 2.0
 */
public void setRolloverEnabled(boolean value){
	if (isRolloverEnabled() == value)
		return;
	setFlag(ROLLOVER_ENABLED_FLAG, value);
	repaint();
}

/**
 * Sets the selected state of this Clickable. Since the model
 * is reposnsible for all state based information, it is 
 * informed of the state change. Extending classes can choose
 * selection information, if they do not represent any selection.
 *
 * @param value  New selected state of this clickable.
 * @see  #isSelected()
 * @since 2.0
 */
public void setSelected(boolean value) {
	getModel().setSelected(value);
}

/**
 * Sets this Clickable's style to the passed value.
 *
 * @param style Integer constants STYLE_BUTTON 
 *               or STYLE_TOGGLE.
 * @since 2.0
 */
public void setStyle( int style ){
	if(style==STYLE_BUTTON){
		setFlag(STYLE_BUTTON_FLAG, ((style & STYLE_BUTTON) == STYLE_BUTTON));
		if(!(getBorder() instanceof ButtonBorder))
			setBorder(new ButtonBorder());
		setOpaque(true);
	}
	if(style==STYLE_TOGGLE){
		setFlag(STYLE_TOGGLE_FLAG, ((style & STYLE_TOGGLE) == STYLE_TOGGLE));
		setModel(createDefaultModel());
	}
}

/**
 * Removes the given event handler containing listeners 
 * from this Clickable.
 *
 * @param handler  Event handler to be removed from this figure.
 * @since 2.0
 */
protected void unhookEventHandler(ClickableEventHandler handler){
	if (handler == null) return;
	removeMouseListener(handler);
	removeMouseMotionListener(handler);
	removeChangeListener(handler);
}

}