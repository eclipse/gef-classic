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
package org.eclipse.gef.tools;

import org.eclipse.gef.*;

/**
 * Specializes selection to do nothing, the native Tree provides selection for free.
 * @author hudsonr
 */
public class DragTreeItemsTracker
	extends SelectEditPartTracker
{

public DragTreeItemsTracker(EditPart sourceEditPart) {
	super(sourceEditPart);
}

protected String getDebugName() {
	return "Tree Tracker: " + getCommandName();//$NON-NLS-1$
}

protected void performSelection() { }

}
