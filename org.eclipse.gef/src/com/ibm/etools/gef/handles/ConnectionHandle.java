package com.ibm.etools.gef.handles;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.widgets.Display;

import com.ibm.etools.draw2d.*;
import com.ibm.etools.draw2d.geometry.*;

import com.ibm.etools.gef.*;
import com.ibm.etools.gef.commands.*;
import com.ibm.etools.gef.tools.*;

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
 * Adds this as a {@link FigureListener} to the owner's {@link Figure}.
 */
public void addNotify() {
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
}

public void setFixed(boolean fixed) {
	this.fixed = fixed;
	if (fixed)
		setCursor(Cursors.NO);
	else
		setCursor(Cursors.CROSS);
}

}