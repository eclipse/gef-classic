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
package org.eclipse.gef.handles;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.Cursors;

/**
 * A handle used on a {@link Connection}.
 */
abstract public class ConnectionHandle
	extends SquareHandle
	implements PropertyChangeListener
{

private boolean fixed = false;

/**
 * Creates a new ConnectionHandle.
 */
public ConnectionHandle() {
	setCursor(Cursors.CROSS);
}

public ConnectionHandle(boolean fixed) {
	setFixed(fixed);
	if (fixed)
		setCursor(Cursors.NO);
	else
		setCursor(Cursors.CROSS);
}

/**
 * Adds this as a {@link org.eclipse.draw2d.FigureListener} to the 
 * owner's {@link org.eclipse.draw2d.Figure}.
 */
public void addNotify() {
	super.addNotify();
	getConnection().addPropertyChangeListener(Connection.PROPERTY_POINTS, this);
}

/**
 * Returns the Connection this handle is on.
 */
public Connection getConnection() {
	return (Connection)getOwnerFigure();
}

protected boolean isFixed() {
	return fixed;
}

public void propertyChange(PropertyChangeEvent evt) {
	if (evt.getPropertyName().equals(Connection.PROPERTY_POINTS))
		revalidate();
}

public void removeNotify(){
	getConnection().removePropertyChangeListener(Connection.PROPERTY_POINTS, this);
	super.removeNotify();
}

public void setFixed(boolean fixed) {
	this.fixed = fixed;
	if (fixed)
		setCursor(Cursors.NO);
	else
		setCursor(Cursors.CROSS);
}

}