/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.dnd;

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
 * Returns the <code>Transfer</code> that this listener can handle.
 * @return the Transfer for this listener
 */
Transfer getTransfer();

/**
 * Returns <code>true</code> if this listener can handle the drop
 * based on the given DropTargetEvent.
 * @param event the current DropTargetEvent
 * @return <code>true</code> if the listener is enabled.
 */
boolean isEnabled(DropTargetEvent event);

}