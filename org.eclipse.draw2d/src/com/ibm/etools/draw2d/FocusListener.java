package com.ibm.etools.draw2d;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

/**
 * A listener interface for receiving {@link FocusEvent}s.
 */
public interface FocusListener {
	
/**
 * Called when the listened to object has gained
 * focus.
 */
void focusGained(FocusEvent fe);

/**
 * Called when the listened to object has lost
 * focus.
 */
void focusLost(FocusEvent fe);

public class Stub 
	implements FocusListener
{
	public void focusGained(FocusEvent fe){}
	public void focusLost(FocusEvent fe){}
}

}