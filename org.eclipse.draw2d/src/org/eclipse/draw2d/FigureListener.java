package org.eclipse.draw2d;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

/**
 * A listener interface for receiving notification
 * that an IFigure has moved.
 */
public interface FigureListener {

/**
 * Called when the given IFigure has moved.
 */
void figureMoved(IFigure source);

}