package com.ibm.etools.draw2d;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

/**
 * Layer designed specifically to handle the presence of
 * connections. This is done due to the necessity of 
 * having a router for the connections added.
 */
public class ConnectionLayer
	extends FreeformLayer
{

protected ConnectionRouter connectionRouter;

/**
 * Adds the given figure with the given contraint at the given index.
 * If the figure is a {@link Connection}, its connection router is set.
 *
 * @param figure  Figure being added.
 * @param constraint  Constraint of the figure being added.
 * @param index  Index where the figure is to be added.
 * @since 2.0 
 */
public void add(IFigure figure, Object constraint, int index) {
	super.add(figure, constraint, index);
	
	// If the connection layout manager is set, then every
	// figure added should use this layout manager.
	if (figure instanceof Connection && getConnectionRouter() != null){
		((Connection)figure).setConnectionRouter(getConnectionRouter());
	}
}

/**
 * Returns the connection router being used by this layer.
 *
 * @return  Connection router being used by this layer.
 * @see  #setConnectionRouter(ConnectionRouter)
 * @since 2.0
 */
public ConnectionRouter getConnectionRouter() {
	return connectionRouter;
}

public void remove(IFigure figure){
	if (figure instanceof Connection)
		((Connection)figure).setConnectionRouter(null);
	super.remove(figure);
}

/**
 * Sets the connection router for this layer. The set router is
 * added to all the child connections of this figure.
 *
 * @param router  Connection router of this figure.
 * @see  #getConnectionRouter()
 * @since 2.0
 */
public void setConnectionRouter(ConnectionRouter router) {
	connectionRouter = router;
	FigureIterator iter = new FigureIterator(this);
	IFigure figure;
	while (iter.hasNext()){
		figure = iter.nextFigure();
		if (figure instanceof Connection)
			((Connection)figure).setConnectionRouter(router);
	}
}

}