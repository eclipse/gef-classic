package org.eclipse.draw2d.parts;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.*;

/**
 * Relocates a {@link Dock} figure to the bottom right corner
 * of the Dock's host figure.
 */
public final class DockLocator 
	implements Locator
{

/**
 * Sets the Dock's location.
 */
public void relocate(IFigure fig){
	Dock dock = (Dock)fig;
	IFigure port = dock.getHost();
	Dimension prefSize = dock.getPreferredSize();
	Rectangle portBounds = port.getBounds();
	Point newDockLocation = new Point(portBounds.x + portBounds.width - prefSize.width,
							portBounds.y + portBounds.height - prefSize.height);
	dock.setLocation(newDockLocation);
	dock.setSize(prefSize);
	dock.repaint();
	return;
}

}