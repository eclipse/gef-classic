package org.eclipse.gef.dnd;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.swt.dnd.*;

/**
 * A DragSourceListener that can handle one type of Transfer.
 */
public interface TransferDragSourceListener 
	extends DragSourceListener
{

/**
 * Returns the Transfer that this listener can handle.
 */
Transfer getTransfer();

}