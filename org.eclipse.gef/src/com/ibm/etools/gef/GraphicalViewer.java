package com.ibm.etools.gef;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import com.ibm.etools.draw2d.*;
import com.ibm.etools.draw2d.geometry.*;


/**
 * Specializes EditPartViewer adding the ability to hit-test {@link Handle}s.
 */
public interface GraphicalViewer
	extends EditPartViewer
{

/**
 * Finds the view handle at the point specified by x,y.  If
 * there is not a handle at this position, null is returned.
 * @return Handle The handle at the point or null if no handle is found
 * @param p Point the location on the View
 */
Handle findHandleAt(Point p);

}
