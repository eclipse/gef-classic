package org.eclipse.gef.dnd;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.swt.dnd.*;

/**
 * A DropTargetListener that can handle one type of Transfer.
 */
public interface TransferDropTargetListener 
	extends DropTargetListener
{

/**
 * Called when this listener becomes enabled.
 * 
 * @see DelegatingDropAdapter
 */
void activate();

/**
 * Called when this listener becomes disabled. 
 * 
 * @see DelegatingDropAdapter
 */
void deactivate();

/**
 * Returns the Transfer that this listener can handle.
 */
Transfer getTransfer();

/**
 * Returns <code>true</code> if this listener can handle the drop
 * based on the given DropTargetEvent.  
 */
boolean isEnabled(DropTargetEvent event);

}