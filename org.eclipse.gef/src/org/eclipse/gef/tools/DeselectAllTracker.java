package org.eclipse.gef.tools;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.gef.EditPart;

public class DeselectAllTracker
	extends SelectEditPartTracker
{

public DeselectAllTracker(EditPart ep) {
	super(ep);
}

protected boolean handleButtonDown(int button){
	getCurrentViewer().deselectAll();
	return true;
}

}
