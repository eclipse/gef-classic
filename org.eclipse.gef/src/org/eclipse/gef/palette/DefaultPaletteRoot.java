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

private List children = new ArrayList();

public DefaultPaletteRoot(List children) {
	super(null, children, PaletteRoot.PALETTE_TYPE_ROOT);
}

public String toString() {
	return "Palette Root"; //$NON-NLS-1$
}

}
