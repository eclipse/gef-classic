/*******************************************************************************
 * Copyright (c) 2000, 2010 IBM Corporation and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.dnd;

import org.eclipse.swt.dnd.Transfer;

/**
 * A <code>DropTragetListener</code> that handles one type of SWT
 * {@link Transfer}. The purpose of a TransferDropTargetListener is to:
 * <UL>
 * <LI>Determine enablement for a Drop operation. Enablement is often a function
 * drop location, and/or criteria.
 * <LI>When enabled, optionally showing feedback on the DropTarget.
 * <LI>Performing the actualy Drop
 * </UL>
 * {@link DelegatingDropAdapter} allows these functions to be implemented
 * separately for unrelated types of Drags. DelegatingDropAdapter then combines
 * the function of each TransferDropTargetListener, while allowing them to be
 * implemented as if they were the only DragSourceListener.
 *
 * @deprecated use org.eclipse.jface.util.TransferDropTargetListener instead
 */
public interface TransferDropTargetListener extends org.eclipse.jface.util.TransferDropTargetListener {

}
