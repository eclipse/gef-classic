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
package org.eclipse.gef;

import org.eclipse.swt.accessibility.*;

import org.eclipse.draw2d.AccessibleBase;

/**
 * This class provides Accessibility support for {@link org.eclipse.gef.EditPart
 * EditParts}. EditParts are the unit of selection in GEF.  When selection changes,
 * Accessibility clients are notified.  These clients then query the EditPartViewer for
 * various accessibility information.
 * <P>EditParts must provide AccessibileEditPart adapters in order to work
 * with screen-readers, screen magnifiers, and other accessibility tools.  EditParts
 * should override {@link
 * org.eclipse.gef.editparts.AbstractEditPart#getAccessibleEditPart()}.
 * @author hudsonr
 */
public abstract class AccessibleEditPart
	extends AccessibleBase
{

/**
 * @see AccessibleControlAdapter#getChildCount(AccessibleControlEvent)
 * @param e AccessibleControlEvent
 */
public abstract void getChildCount(AccessibleControlEvent e);

/**
 * @see AccessibleControlAdapter#getChildren(AccessibleControlEvent)
 * @param e AccessibleControlEvent
 */
public abstract void getChildren(AccessibleControlEvent e);

/**
 * @see AccessibleControlAdapter#getDefaultAction(AccessibleControlEvent)
 * @param e AccessibleControlEvent
 */
public void getDefaultAction(AccessibleControlEvent e) { }

/**
 * @see AccessibleAdapter#getDescription(AccessibleEvent)
 * @param e AccessibleEvent
 */
public void getDescription(AccessibleEvent e) { }

/**
 * @see AccessibleAdapter#getKeyboardShortcut(AccessibleEvent)
 * @param e AccessibleEvent
 */
public void getKeyboardShortcut(AccessibleEvent e) { }

/**
 * @see AccessibleAdapter#getHelp(AccessibleEvent)
 * @param e AccessibleEvent
 */
public void getHelp(AccessibleEvent e) { }

/**
 * @see AccessibleAdapter#getName(AccessibleEvent)
 * @param e AccessibleEvent
 */
public abstract void getName(AccessibleEvent e);

/**
 * @see AccessibleControlAdapter#getLocation(AccessibleControlEvent)
 * @param e AccessibleControlEvent
 */
public abstract void getLocation(AccessibleControlEvent e);

/**
 * @see AccessibleControlAdapter#getRole(AccessibleControlEvent)
 * @param e AccessibleControlEvent
 */
public void getRole(AccessibleControlEvent e) { }

/**
 * @see AccessibleControlAdapter#getState(AccessibleControlEvent)
 * @param e AccessibleControlEvent
 */
public abstract void getState(AccessibleControlEvent e);

/**
 * @see AccessibleControlAdapter#getValue(AccessibleControlEvent)
 * @param e AccessibleControlEvent
 */
public void getValue(AccessibleControlEvent e) { }

}