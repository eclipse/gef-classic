/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.draw2d;

public interface RoutingListener {

void invalidate(Connection connection);

void postRoute(Connection connection);

void remove(Connection connection);

/**
 * Called prior to routing occurring.  A listener may intercept routing by
 * returning <code>true</code>.  If intercepted, the connection's
 * <code>ConnectionRouter</code> will not perform routing.
 * @param connection the connection being routed
 * @return <code>true</code> if routing has been performed by the listener
 * @since 3.2
 */
boolean route(Connection connection);

void setConstraint(Connection connection, Object constraint);

class Stub implements RoutingListener {
	public void invalidate(Connection connection) { }
	public void postRoute(Connection connection) { }
	public void remove(Connection connection) { }
	public boolean route(Connection connection) {
		return false;
	}
	public void setConstraint(Connection connection, Object constraint) { }
}

}
