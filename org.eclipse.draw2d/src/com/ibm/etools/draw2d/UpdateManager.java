package com.ibm.etools.draw2d;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import com.ibm.etools.draw2d.geometry.*;

/**
 * Update managers handle the job of repainting and laying
 * out figures.  A desirable implementation is one that batches
 * work to be done and collapses any redundant work.
 * Update managers may contain 0 or more nested update managers.
 * Some optimizations can only be performed after all requests have
 * been batched.  For this reason, an UpdateManager should call
 * performUpdate() on its nested UpdateManagers prior to doing its
 * own update.  During the nested updates, new requests may be added.
 */

public abstract class UpdateManager {

/**
 * Adds the dirty region defined by the coordinates on the IFigure
 * <b>figure</b>.  The update manager should repaint the dirty
 * region in a timely fashion.
 */
public abstract void addDirtyRegion(IFigure figure, int x, int y, int w, int h);
public void addDirtyRegion(IFigure figure, Rectangle rect){
	addDirtyRegion(
		figure,
		rect.x, rect.y,
		rect.width, rect.height
	);
}

/**
 * The receiver should call validate() on the IFigure
 * <b>figure</b> in a timely fashion.
 */
public abstract void addInvalidFigure(IFigure figure);

public abstract void performUpdate();
public abstract void performUpdate(Rectangle exposed);

public abstract void setGraphicsSource(GraphicsSource gs);
public abstract void setRoot(IFigure figure);
}