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
package org.eclipse.gef.palette;

import java.util.List;

public class PaletteGroup 
	extends PaletteContainer {

public static final String
	PALETTE_TYPE_GROUP = "Palette_Group";//$NON-NLS-1$

public PaletteGroup(String label){
	super(label, null, null, PALETTE_TYPE_GROUP);
	setUserModificationPermission(PERMISSION_NO_MODIFICATION);
}

public PaletteGroup(String label, List children){
	this(label);
	addAll(children);
}

}