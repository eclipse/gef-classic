/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.draw2d;

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