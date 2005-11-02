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
package org.eclipse.mylar.zest.core.internal.timelineview.parts;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;

/**
 * 
 * @author Ian Bull
 *
 */
public class EndPointEditPart extends AbstractGraphicalEditPart {

	protected IFigure createFigure() {
		// TODO Auto-generated method stub
		RectangleFigure rf = new RectangleFigure();
		rf.setBackgroundColor( ColorConstants.black );
		rf.setForegroundColor( ColorConstants.black );
		rf.setSize(100,100);
		return new RectangleFigure();
	}

	protected void createEditPolicies() {
		// TODO Auto-generated method stub
		

	}

}
