package org.eclipse.gef.palette;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.util.List;

import org.eclipse.swt.graphics.Image;

public class PaletteCategory 
	extends PaletteContainer {

public PaletteCategory(String label){
	this(label, (Image)null);
}

public PaletteCategory(String label, Image icon, List children){
	this(label, icon);
	addAll(children);
}

public PaletteCategory(String label, List children){
	this(label, null, children);
}

public PaletteCategory(String label,Image icon){
	super(label, icon);
	setType(PALETTE_TYPE_CATEGORY);
}
	
}