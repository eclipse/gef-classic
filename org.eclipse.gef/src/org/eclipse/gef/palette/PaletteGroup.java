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

public PaletteGroup(String label){
	super(label);
	setType(PALETTE_TYPE_GROUP);
}

public PaletteGroup(String label, List children){
	this(label);
	addAll(children);
}

}