package org.eclipse.gef;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

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
