package org.eclipse.gef;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.swt.widgets.*;
import org.eclipse.gef.*;

public interface TreeEditPart
	extends EditPart
{

Widget getWidget();

void setWidget(Widget widget);

}
