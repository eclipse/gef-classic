package org.eclipse.draw2d;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

/**
 * A Listener interface for receiving {@link ActionEvent}s.
 */
public interface ActionListener {
	
/**
 * Called when the action occurs.
 */
void actionPerformed(ActionEvent event);

}