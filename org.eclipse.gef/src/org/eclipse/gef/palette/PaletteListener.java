package org.eclipse.gef.palette;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.util.*;

public interface PaletteListener
	extends EventListener
{

/**
 * A palette entry was selected.
 * @param event org.eclipse.gef.palette.PaletteEvent
 */
void modeChanged();

}
