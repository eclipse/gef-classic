/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.draw2d;

/**
 * Layer designed specifically to handle the presence of connections. This is done due to
 * the necessity of having a router for the connections added.
 */
public class ConnectionLayer
	extends FreeformLayer
{

/**
 * The ConnectionRouter used to route all connections on this layer.
 */
protected ConnectionRouter connectionRouter;

/**
 * Adds the given figure with the given contraint at the given index. If the figure is a
 * {@link Connection}, its {@link ConnectionRouter} is set.
 *
 * @param figure  Figure being added
 * @param constraint  Constraint of the figure being added
 * @param index  Index where the figure is to be added
 * @since 2.0 
 */
public void add(IFigure figure, Object constraint, int index) {
	super.add(figure, constraint, index);
	
	// If the connection layout manager is set, then every
	// figure added should use this layout manager.
	if (figure instanceof Connection && getConnectionRouter() != null)
		((Connection)figure).setConnectionRouter(getConnectionRouter());
}

/**
 * Returns the ConnectionRouter being used by this layer.
 *
 * @return  ConnectionRouter being used by this layer
 * @since 2.0
 */
public ConnectionRouter getConnectionRouter() {
	return connectionRouter;
}

/**
 * Removes the figure from this Layer.  If the figure is a {@link Connection}, that
 * Connection's {@link ConnectionRouter} is set to <code>null</code>.
 * 
 * @param figure The figure to remove
 */
public void remove(IFigure figure) {
	if (figure instanceof Connection)
		((Connection)figure).setConnectionRouter(null);
	super.remove(figure);
}

/**
 * Sets the ConnectionRouter for this layer. This router is set as the ConnectionRouter
 * for all the child connections of this Layer.
 *
 * @param router  The ConnectionRouter to set for this Layer
 * @since 2.0
 */
public void setConnectionRouter(ConnectionRouter router) {
	connectionRouter = router;
	FigureIterator iter = new FigureIterator(this);
	IFigure figure;
	while (iter.hasNext()) {
		figure = iter.nextFigure();
		if (figure instanceof Connection)
			((Connection)figure).setConnectionRouter(router);
	}
}

}