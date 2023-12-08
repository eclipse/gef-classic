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
package org.eclipse.gef;

import org.eclipse.swt.widgets.Widget;

/**
 * A Specialization of {@link EditPart} for use with
 * {@link org.eclipse.gef.ui.parts.TreeViewer}. The <i>visualpart</i> of a
 * TreeEditPart is a {@link org.eclipse.swt.widgets.TreeItem}.
 * <p>
 * IMPORTANT: This interface is not intended to be implemented by clients.
 * Clients should inherit from
 * {@link org.eclipse.gef.editparts.AbstractGraphicalEditPart}. New methods may
 * be added in the future.
 */

public interface TreeEditPart extends EditPart {

	/**
	 * Returns either a {@link org.eclipse.swt.widgets.Tree} or
	 * {@link org.eclipse.swt.widgets.TreeItem}.
	 *
	 * @return the Widget
	 */
	Widget getWidget();

	/**
	 * Set's the EditPart's widget. Because SWT <code>TreeItem</code> and
	 * <code>Tree</code> cannot be created without a parent, a TreeEditPart must
	 * rely on its parent providing its Widget.
	 *
	 * @param widget the Widget
	 */
	void setWidget(Widget widget);

}
