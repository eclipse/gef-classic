/*******************************************************************************
 * Copyright 2005, CHISEL Group, University of Victoria, Victoria, BC, Canada.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     The Chisel Group, University of Victoria
 *******************************************************************************/
package org.eclipse.mylar.zest.core.internal.nestedgraphviewer.parts;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Shape;
import org.eclipse.mylar.zest.core.internal.graphviewer.parts.GraphConnectionEditPart;


/**
 * @author Chris Callendar
 */
public class NestedGraphConnectionEditPart extends GraphConnectionEditPart {

	/**
	 * 
	 */
	public NestedGraphConnectionEditPart() {
		super();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.mylar.zest.core.internal.springgraphviewer.parts.GraphConnectionEditPart#createFigure()
	 */
	protected IFigure createFigure() {
		
		Connection connection = (Connection)super.createFigure();
		// Now only directed edges have arrow endpoints - see ZestStyles.DIRECTED_GRAPH
		//connection.setTargetDecoration(new PolygonDecoration()); // arrow at target endpoint
		getCastedModel().changeLineColor(ColorConstants.darkBlue);
		connection.setForegroundColor(getCastedModel().getLineColor());
		if (connection instanceof Shape) {
			((Shape)connection).setLineWidth(getCastedModel().getLineWidth());
			((Shape)connection).setLineStyle(getCastedModel().getLineStyle());
		}
		return connection;
	}

}
