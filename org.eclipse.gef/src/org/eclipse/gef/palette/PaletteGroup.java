package org.eclipse.gef.palette;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

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