package org.eclipse.gef.dnd;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.swt.dnd.*;

/**
 * A DragSourceListener that can handle one type of SWT {@link Transfer}. The purpose of a
 * TransferDragSourceListener is to:
 * <UL>
 *   <LI>Determine enablement for a Drag operation. Enablement is often a function of the
 *   current <I>Selection</I> and/or other criteria.
 *   <LI>Set data for a single type of Drag and Transfer.
 * </UL>
 * {@link DelegatingDragAdapter} allows these functions to be implemented separately for
 * unrelated types of Drags. DelegatingDragAdapter then combines the function of each
 * TransferDragSourceListener, while allowing them to be implemented as if they were the
 * only DragSourceListener.
 */
public interface TransferDragSourceListener 
	extends DragSourceListener
{

/**
 * Returns the Transfer that this listener works with.
 * @return the Transfer associated with this listener
 */
Transfer getTransfer();

}