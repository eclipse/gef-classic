package org.eclipse.gef.palette;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.util.List;

import org.eclipse.swt.graphics.Image;

public class DefaultPaletteCategory 
	extends DefaultPaletteContainer {

public DefaultPaletteCategory(String label){
	super(label, PaletteContainer.PALETTE_TYPE_CATEGORY);
}

public DefaultPaletteCategory(String label, List children){
	super(label, children, PaletteContainer.PALETTE_TYPE_CATEGORY);
}

public DefaultPaletteCategory(String label,Image icon){
	super(label,PaletteContainer.PALETTE_TYPE_CATEGORY, icon, icon);
}
	
}