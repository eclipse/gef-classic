package org.eclipse.gef.tools;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.gef.*;

/**
 * Specializes selection to do nothing, the native Tree provides selection for free.
 * @author hudsonr
 */
public class DragTreeItemsTracker
	extends SelectEditPartTracker
{

public DragTreeItemsTracker(EditPart sourceEditPart){
	super(sourceEditPart);
}

protected String getDebugName(){
	return "Tree Tracker: " + getCommandName();//$NON-NLS-1$
}

protected void performSelection() { }

}
