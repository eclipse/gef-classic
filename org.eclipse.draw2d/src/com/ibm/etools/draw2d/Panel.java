package com.ibm.etools.draw2d;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

/**
 * A General purpose Container.
 * This figure is opaque by default, and will fill its entire
 * bounds with either the background color that is set
 * on the figure, or the IGraphics current background color
 * if none has been set.  Opaque figures help to optimize painting.
 *
 * Note that the paintFigure() method in the superclass Figure
 * actually fills the bounds of this figure.
 */

public class Panel
	extends Figure
{
/**
 * Returns <code>true</code> as this is an opaque figure.
 *
 * @return  The opaque state of this figure.
 * @since 2.0
 */
public boolean isOpaque(){ return true; }
}