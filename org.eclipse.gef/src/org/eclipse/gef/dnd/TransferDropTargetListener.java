package org.eclipse.gef.dnd;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.swt.dnd.*;

/**
 * A <code>DropTragetListener</code> that handles one type of SWT {@link Transfer}. The
 * purpose of a TransferDropTargetListener is to:
 * <UL>
 *   <LI>Determine enablement for a Drop operation. Enablement is often a function drop
 *   location, and/or criteria.
 *   <LI>When enabled, optionally showing feedback on the DropTarget.
 *   <LI>Performing the actualy Drop
 * </UL>
 * {@link DelegatingDropAdapter} allows these functions to be implemented separately for
 * unrelated types of Drags. DelegatingDropAdapter then combines the function of each
 * TransferDropTargetListener, while allowing them to be implemented as if they were the
 * only DragSourceListener.
 */
public interface TransferDropTargetListener 
	extends DropTargetListener
{

/**
 * Called when this listener becomes the <i>active</i> listener on the
 * DelegatingDropAdapter.
 * @see DelegatingDropAdapter
 */
void activate();

/**
 * Called when this listener is not longer the <i>active</i> listener on the
 * DelegatingDropAdapter.
 * 
 * 
 * @see DelegatingDropAdapter
 */
void deactivate();

/**
 * Returns the <code>Transfer</code> that this listener can handle.
 * @return the Transfer for this listener
 */
Transfer getTransfer();

/**
 * Called when the mouse hovers during drag and drop.
 * @param event The current DropTargetEvent */
void dragHover(DropTargetEvent event);

/**
 * Returns <code>true</code> if this listener can handle the drop
 * based on the given DropTargetEvent.
 * @param event the current DropTargetEvent
 * @return <code>true</code> if the listener is enabled.
 */
boolean isEnabled(DropTargetEvent event);

}