package org.eclipse.draw2d.internal;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.swt.widgets.Display;

public class UITimer 
	extends Timer
{

protected void preformRun(){
	Display.getDefault().syncExec(getRunnable());
}

}