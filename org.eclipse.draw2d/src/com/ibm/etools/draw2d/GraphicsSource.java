package com.ibm.etools.draw2d;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import com.ibm.etools.draw2d.geometry.Rectangle;

public interface GraphicsSource {

/**
 * returns an IGraphics for the Rectangular region requested
 */
Graphics getGraphics(Rectangle region);

/**
 * tells the GraphicsSource that you have finished using that region.
 */
void flushGraphics(Rectangle region);

}