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

import org.eclipse.swt.widgets.Widget;

/**
 * A Specialization of {@link EditPart} for use with {@link TreeViewer}.  The <i>visual
 * part</i> of a TreeEditPart is a {@link TreeItem}.
 * <p>
 * IMPORTANT: This interface is not intended to be implemented by clients.  Clients should
 * inherit from {@link org.eclipse.gef.editparts.AbstractGraphicalEditPart}.  New methods
 * may be added in the future.
 */

public interface TreeEditPart
	extends EditPart
{

/**
 * Returns either a {@link Tree} or {@link TreeItem}.
 * @return the Widget */
Widget getWidget();

/**
 * Set's the EditPart's widget. Because SWT <code>TreeItem</code> and <code>Tree</code>
 * cannot be created without a parent, a TreeEditPart must rely on its parent
 * providing its Widget.
 * @param widget the Widget */
void setWidget(Widget widget);

}
