/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.examples.ediagram.edit.parts;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.PolylineConnection;

import org.eclipse.gef.examples.ediagram.model.InheritanceView;

/**
 * @author Pratik Shah
 * @since 3.1
 */
public class InheritanceLinkEditPart 
	extends LinkEditPart
{

public InheritanceLinkEditPart(InheritanceView conn) {
	super(conn);
}

protected IFigure createFigure() {
	PolylineConnection conn = new PolylineConnection();
	PolygonDecoration decor = new PolygonDecoration();
	decor.setScale(14, 6);
	decor.setBackgroundColor(ColorConstants.white);
	conn.setTargetDecoration(decor);
	conn.setLineStyle(Graphics.LINE_DOT);
	return conn;
}

}