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
