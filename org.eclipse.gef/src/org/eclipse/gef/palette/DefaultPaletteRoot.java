package org.eclipse.gef.palette;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.util.*;

public class DefaultPaletteRoot
	extends DefaultPaletteContainer
	implements PaletteRoot
{

public DefaultPaletteRoot(List children) {
	setType (PALETTE_TYPE_ROOT);
	addAll(children);
}

public String toString() {
	return "Palette Root"; //$NON-NLS-1$
}

}
