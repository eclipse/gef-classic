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
void entrySelected(PaletteEvent event);

/**
 * There is a new default entry. This is necessary
 * so that the editor knows what the default tool is.
 * Whenever a palette is loaded all listeners are sent
 * this message with the new default entry. The editor
 * can then get its tool out of it. If there is no
 * default entry, then null is sent.
 *
 * This will also be sent whenever an add listener
 * is performed, but only for the listener that was
 * added. This way when they first attach they will
 * get notification of the default entry.
 *
 * The entry will be contained in the event.
 *
 */
//void newDefaultEntry(PaletteEvent event);

}
