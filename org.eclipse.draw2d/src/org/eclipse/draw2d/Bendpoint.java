package org.eclipse.draw2d;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.draw2d.geometry.Point;

/**
 * A point used by a bendable {@link org.eclipse.draw2d.Connection}.
 */
public interface Bendpoint {

/**
 * Returns the location of the bendpoint.  This may return the point by reference and
 * modifying it could produce unpredictable results.
 * 
 * @return the location of the bendpoint
 */
Point getLocation();

}