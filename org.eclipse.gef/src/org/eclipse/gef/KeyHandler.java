package org.eclipse.gef;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.util.*;

import org.eclipse.jface.action.IAction;
import org.eclipse.swt.events.KeyEvent;

/**
 * The KeyHandler should handle all normal keystrokes on an <code>EditPartViewer</code>. 
 * <I>Normal</i> is simply defined as keystrokes which are not associated with an
 * Accelerator on the Menu. The KeyHandler will be forwarded KeyEvents by the active Tool,
 * which is usually the SelectionTool. The Tool may be in a state where keystrokes should
 * not be processed, in which case it will not forward the keystrokes.  For this reason,
 * it is important to always handle KeyEvents by using a KeyHandler.
 * <P>
 * KeyHandlers can be chained by calling {@link #setParent(KeyHandler)}. If this
 * KeyHandler does not handle the keystroke, it will pass the keystroke to its
 * <i>parent</i> KeyHandler.
 * <P>
 * KeyHandlers can be implemented using two stragegies. One is to map {@link KeyStroke
 * KeyStrokes} to {@link org.eclipse.jface.action.IAction Actions} using the {@link
 * #put(KeyStroke, IAction)} and {@link #remove(KeyStroke)} API. The other is to subclass
 * KeyHandler, and override various methods. A combination of the two is also useful.
 * @since 2.0 */
public class KeyHandler {

private Map actions;
private KeyHandler parent;

/**
 * Processes a <i>key pressed</i> event. This method is called by the Tool whenever a key
 * is pressed, and the Tool is in the proper state.
 * @param event the KeyEvent * @return <code>true</code> if KeyEvent was handled in some way */
public boolean keyPressed(KeyEvent event) {
	if (performStroke(new KeyStroke(event, true)))
		return true;
	return parent != null && parent.keyPressed(event);
}

/**
 * Processes a <i>key released</i> event. This method is called by the Tool whenever a key
 * is released, and the Tool is in the proper state.
 * @param event the KeyEvent
 * @return <code>true</code> if KeyEvent was handled in some way
 */
public boolean keyReleased(KeyEvent event) {
	if (performStroke(new KeyStroke(event, false)))
		return true;
	return parent != null && parent.keyReleased(event);
}

private final boolean performStroke(KeyStroke key) {
	if (actions == null)
		return false;
	IAction action = (IAction)actions.get(key);
	if (action == null)
		return false;
	if (action.isEnabled())
		action.run();
	return true;
}

/**
 * Maps a specified <code>KeyStroke</code> to an <code>IAction</code>. When a KeyEvent
 * occurs matching the given KeyStroke, the Action will be <code>run()</code> iff it is
 * enabled.
 * @param keystroke the KeyStroke * @param action the Action to run */
public void put(KeyStroke keystroke, IAction action) {
	if (actions == null)
		actions = new HashMap();
	actions.put(keystroke, action);
}

/**
 * Removed a mapped <code>IAction</code> for the specified <code>KeyStroke</code>.
 * @param keystroke the KeyStroke to be unmapped
 */
public void remove(KeyStroke keystroke) {
	if (actions != null)
		actions.remove(keystroke);
}

/**
 * Sets a <i>parent</i> <code>KeyHandler</code> to which this KeyHandler will forward
 * un-consumed KeyEvents. This KeyHandler will first attempt to handle KeyEvents. If it
 * does not recognize a given KeyEvent, that event is passed to its <i>parent</i>.
 * @param parent the <i>parent</i> KeyHandler * @return <code>this</code> for convenience */
public KeyHandler setParent(KeyHandler parent) {
	this.parent = parent;
	return this;
}

}