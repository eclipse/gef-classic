package org.eclipse.draw2d;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.util.Iterator;

import org.eclipse.draw2d.util.Timer;

/**
 * A model for buttons.
 */
public class ButtonModel {

public static final String
         ENABLED_PROPERTY = "enabled", //$NON-NLS-1$
         PRESSED_PROPERTY = "pressed", //$NON-NLS-1$
        SELECTED_PROPERTY = "selected", //$NON-NLS-1$
ROLLOVER_ENABLED_PROPERTY = "rollover enabled", //$NON-NLS-1$
       MOUSEOVER_PROPERTY = "mouseover"; //$NON-NLS-1$

/**
 * @deprecated This property will soon disappear since it is simply defined by
 * (isPressed() & isRollover())
 */
public static final String ARMED_PROPERTY = "armed";  //$NON-NLS-1$

protected static final int
           ARMED_FLAG = 1,
         PRESSED_FLAG = 2,
       MOUSEOVER_FLAG = 4,
        SELECTED_FLAG = 8,
         ENABLED_FLAG = 16,
ROLLOVER_ENABLED_FLAG = 32,
             MAX_FLAG = ROLLOVER_ENABLED_FLAG;

private int state = ENABLED_FLAG;
private Object data;

public static final int
	DEFAULT_FIRING_BEHAVIOR = 0,
	REPEAT_FIRING_BEHAVIOR  = 1;

protected String actionName;

protected ButtonGroup group = null;

private EventListenerList listeners = new EventListenerList();

protected ButtonStateTransitionListener firingBehavior;{
	installFiringBehavior();
}

/**
 * Registers the given listener as an ActionListener.
 * 
 * @since 2.0
 */
public void addActionListener(ActionListener listener){
	if (listener == null)
		throw new IllegalArgumentException();
	listeners.addListener(ActionListener.class, listener);
}

/**
 * Registers the given listener as a ChangeListener.
 * 
 * @since 2.0
 */
public void addChangeListener(ChangeListener listener){
	if (listener == null)
		throw new IllegalArgumentException();
	listeners.addListener(ChangeListener.class, listener);
}

/**
 * Registers the given listener as a ButtonStateTransitionListener.
 * 
 * @since 2.0
 */
public void addStateTransitionListener(ButtonStateTransitionListener listener){
	if (listener == null)
		throw new IllegalArgumentException();
	listeners.addListener(ButtonStateTransitionListener.class, listener);
}

/**
 * Notifies any ActionListeners on this ButtonModel that an action
 * has been performed.
 * 
 * @since 2.0
 */
protected void fireActionPerformed(){
	Iterator iter = listeners.getListeners(ActionListener.class);
	ActionEvent action = new ActionEvent(this);
	while (iter.hasNext())
		((ActionListener)iter.next()).
			actionPerformed(action);
}

/**
 * Notifies any listening ButtonStateTransitionListener that the
 * pressed state of this button has been cancelled.
 * 
 * @since 2.0
 */
protected void fireCanceled(){
	Iterator iter = listeners.getListeners(ButtonStateTransitionListener.class);
	while(iter.hasNext())
		((ButtonStateTransitionListener)iter.next()).
			canceled();
}

/**
 * Notifies any listening ButtonStateTransitionListener that this
 * button has been pressed.
 * 
 * @since 2.0
 */
protected void firePressed(){
	Iterator iter = listeners.getListeners(ButtonStateTransitionListener.class);
	while(iter.hasNext())
		((ButtonStateTransitionListener)iter.next()).
			pressed();
}

/** 
 * Notifies any listening ButtonStateTransitionListener that this
 * button has been released.
 * 
 * @since 2.0
 */
protected void fireReleased(){
	Iterator iter = listeners.getListeners(ButtonStateTransitionListener.class);
	while(iter.hasNext())
		((ButtonStateTransitionListener)iter.next()).
			released();
}

/**
 * Notifies any listening ButtonStateTransitionListeners that this 
 * button has resumed activity.
 * 
 * @since 2.0
 */
protected void fireResume(){
	Iterator iter = listeners.getListeners(ButtonStateTransitionListener.class);
	while(iter.hasNext())
		((ButtonStateTransitionListener)iter.next()).
			resume();
}

/**
 * Notifies any listening ChangeListeners that this button's 
 * state has changed.
 * 
 * @since 2.0
 */
protected void fireStateChanged(String property){
	Iterator iter = listeners.getListeners(ChangeListener.class);
	ChangeEvent change = new ChangeEvent(this, property);
	while(iter.hasNext())
		((ChangeListener)iter.next()).
			handleStateChanged(change);
}

/**
 * Notifies any listening ButtonStateTransitionListeners that this 
 * button has suspended activity.
 * 
 * @since 2.0
 */
protected void fireSuspend(){
	Iterator iter = listeners.getListeners(ButtonStateTransitionListener.class);
	while(iter.hasNext())
		((ButtonStateTransitionListener)iter.next()).
			suspend();
}

boolean getFlag(int which){
	return (state & which) != 0;
}

/**
 * Returns the group to which this model belongs.
 * 
 * @since 2.0
 */
public ButtonGroup getGroup(){return group;}

/**
 * Returns an object representing user data.
 * 
 * @since 2.0
 */
public Object getUserData(){
	return data;
}

/**
 * Sets the firing behavior for this button.
 * 
 * @since 2.0
 */
protected void installFiringBehavior(){
	setFiringBehavior(DEFAULT_FIRING_BEHAVIOR);
}

/**
 * Returns <code>true</code> if this button is armed.
 * If a button is armed, it will fire an ActionPerformed
 * when released.
 * 
 * @since 2.0
 */
public boolean isArmed() {
	return (state & ARMED_FLAG) != 0;
}

/**
 * Returns <code>true</code> if this button is enabled.
 * 
 * @since 2.0
 */
public boolean isEnabled() {
	return (state & ENABLED_FLAG) != 0;
}

/**
 * Returns <code>true</code> if the mouse is over this button.
 * 
 * @since 2.0
 */
public boolean isMouseOver() {
	return (state & MOUSEOVER_FLAG) != 0;
}

/**
 * Returns <code>true</code> if this button is pressed.
 * 
 * @since 2.0
 */
public boolean isPressed() {
	return (state & PRESSED_FLAG) != 0;
}

/**
 * Returns the selection state of this model. If this model
 * belongs to any group, the group is queried for selection 
 * state, else the flags are used.
 *
 * @return  The selection state of this model.
 * @since 2.0
 */
public boolean isSelected()   {
	if( group==null){
		return (state & SELECTED_FLAG) != 0;
	}else{
		return group.isSelected(this);
	}
}

/**
 * Removes the given ActionListener.
 * 
 * @since 2.0
 */
public void removeActionListener(ActionListener listener){
	listeners.removeListener(ActionListener.class, listener);
}

/**
 * Removes the given ChangeListener.
 * 
 * @since 2.0
 */
public void removeChangeListener(ChangeListener listener){
	listeners.removeListener(ChangeListener.class, listener);
}

/**
 * Removes the given ButtonStateTransitionListener.
 * 
 * @since 2.0
 */
public void removeStateTransitionListener(ButtonStateTransitionListener listener){
	listeners.removeListener(ButtonStateTransitionListener.class, listener);
}

/**
 * Sets this button to be armed.
 * If a button is armed, it will fire an ActionPerformed
 * when released.
 *
 * @since 2.0
 */
public void setArmed(boolean value){
	if (isArmed() == value)
		return;
	if (!isEnabled())
		return;
	setFlag(ARMED_FLAG, value);
	fireStateChanged(ARMED_PROPERTY);
}

/**
 * Sets this button to be enabled.
 * 
 * @since 2.0
 */
public void setEnabled(boolean value){
	if (isEnabled() == value)
		return;
	if(!value){
		setMouseOver(value);
		setArmed(value);
		setPressed(value);
	}
	setFlag(ENABLED_FLAG, value);
	fireStateChanged(ENABLED_PROPERTY);
}

/**
 * Sets the firing behavior for this button.
 * 
 * @param type DEFAULT_FIRING_BEHAVIOR is the default
 *         behavior. Action performed events are not fired
 *         until mouse button is released.
 *         REPEAT_FIRING_BEHAVIOR causes action performed events
 *         to fire repeatedly until the mouse button is released.
 * 
 * @since 2.0
 *          
 */
public void setFiringBehavior(int type){
	if (firingBehavior != null)
		removeStateTransitionListener(firingBehavior);
	switch (type){
		case REPEAT_FIRING_BEHAVIOR:
			firingBehavior = new RepeatFiringBehavior();
			break;
		default:
			firingBehavior = new DefaultFiringBehavior();
	}
	addStateTransitionListener(firingBehavior);
}

void setFlag(int flag, boolean value){
	if (value)
		state |= flag;
	else
		state &= ~flag;
}

/**
 * Sets the ButtonGroup to which this model belongs to. Adds this
 * model as a listener to the group.
 *
 * @param bg  Group to which this model belongs to.
 * @since 2.0
 */
public void setGroup( ButtonGroup bg ){
	if (group == bg)
		return;
	if(group != null)
		group.remove(this);
	group = bg;
	if (group != null)
		group.add(this);
}

/**
 * Sets the mouseover property of this button.
 * 
 * @since 2.0
 */
public void setMouseOver(boolean value){
	if (isMouseOver() == value)
		return;
	if (isPressed())
		if (value)
			fireResume();
		else
			fireSuspend();
	setFlag(MOUSEOVER_FLAG, value);
	fireStateChanged(MOUSEOVER_PROPERTY);
}

/**
 * Sets the pressed property of this button.
 * 
 * @since 2.0
 */
protected void setPressed(boolean value){
	if (isPressed() == value)
		return;
	setFlag(PRESSED_FLAG, value);
	if (value)
		firePressed();
	else {
		if (isArmed())
			fireReleased();
		else
			fireCanceled();
	}
	fireStateChanged(PRESSED_PROPERTY);
}

/**
 * Sets this button to be selected.
 * 
 * @since 2.0
 */
public void setSelected(boolean value){
	if (group == null){
		if (isSelected() == value)
			return;
	} else {
		group.setSelected(this, value);
		if (getFlag(SELECTED_FLAG) == isSelected())
			return;
	}
	setFlag(SELECTED_FLAG, value);
	fireStateChanged(SELECTED_PROPERTY);
}

/**
 * Sets user data.
 * 
 * @since 2.0
 */
public void setUserData(Object data){
	this.data = data;
}

class DefaultFiringBehavior
	extends ButtonStateTransitionListener
{
	public void released(){fireActionPerformed();}
}

class RepeatFiringBehavior
	extends ButtonStateTransitionListener
{
	protected static final int
		INITIAL_DELAY = 250,
		STEP_DELAY = 40;
	
	protected int
		stepDelay = INITIAL_DELAY,
		initialDelay = STEP_DELAY;
	
	protected Timer timer;
	
	private Runnable runAction = new Runnable(){
		public void run(){
			org.eclipse.swt.widgets.Display.getDefault().syncExec(new Runnable(){
				public void run(){
					if (!isEnabled())
						timer.cancel();
					fireActionPerformed();
				}
			});
		}
	};

	public void pressed(){
		fireActionPerformed();
		if (!isEnabled())
			return;
		timer = new Timer();
		timer.scheduleRepeatedly(runAction, INITIAL_DELAY, STEP_DELAY);
	}

	public void canceled(){
		suspend();
	}	
	public void released(){
		suspend();
	}
	
	public void resume(){
		timer = new Timer();
		timer.scheduleRepeatedly(runAction, STEP_DELAY, STEP_DELAY);
	}
	
	public void suspend(){
		if (timer == null) return;
		timer.cancel();
		timer = null;
	}
} 

}
