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
package org.eclipse.gef.util;

public class FlagSupport {

private int flags;

protected boolean getFlag(int flag){
	return (flags & flag) != 0;
}

protected void setFlag(int flag, boolean value){
	if (value)
		flags |= flag;
	else
		flags &= ~flag;
}

}