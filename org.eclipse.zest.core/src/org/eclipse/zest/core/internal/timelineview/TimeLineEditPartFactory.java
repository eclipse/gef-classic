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
package org.eclipse.mylar.zest.core.internal.timelineview;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;
import org.eclipse.mylar.zest.core.internal.timelineview.model.TimeLineDiagram;
import org.eclipse.mylar.zest.core.internal.timelineview.model.TimeTaskPoint;
import org.eclipse.mylar.zest.core.internal.timelineview.parts.EndPointEditPart;
import org.eclipse.mylar.zest.core.internal.timelineview.parts.TimeLineDiagramEditPart;


/**
 * 
 * @author Ian Bull
 *
 */
public class TimeLineEditPartFactory implements EditPartFactory {

	public EditPart createEditPart(EditPart context, Object model) {

		if ( model instanceof TimeTaskPoint ) {
			return new EndPointEditPart();
		}
		else if ( model instanceof TimeLineDiagram ) {
			EditPart ep = new TimeLineDiagramEditPart();
			ep.setModel( model );
			return ep;
		}
		return null;
	}

}
