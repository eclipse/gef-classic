package org.eclipse.draw2d;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

/**
 * A listener interface for receiving {@link FocusEvent FocusEvents}.
 */
public interface FocusListener {
	
/**
 * Called when the listened to object has gained focus.
 * @param fe The FocusEvent object
 */
void focusGained(FocusEvent fe);

/**
 * Called when the listened to object has lost focus.
 * @param fe The FocusEvent object
 */
void focusLost(FocusEvent fe);

/**
 * An empty implementation of FocusListener for convenience.
 */
public class Stub 
	implements FocusListener
{
	/**
	 * @see org.eclipse.draw2d.FocusListener#focusGained(FocusEvent)
	 */
	public void focusGained(FocusEvent fe) { }
	/**
	 * @see org.eclipse.draw2d.FocusListener#focusLost(FocusEvent)
	 */
	public void focusLost(FocusEvent fe) { }
}

}