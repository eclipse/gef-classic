package com.ibm.etools.gef;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.swt.widgets.*;
import com.ibm.etools.gef.*;

public interface TreeEditPart
	extends EditPart
{

Widget getWidget();

void setWidget(Widget widget);

}
