/*******************************************************************************
 * Copyright (c) 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.editpolicies;

import java.util.*;

import org.eclipse.swt.graphics.Color;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.Rectangle;

import org.eclipse.gef.*;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.gef.requests.ChangeBoundsRequest;

/**
 * @author Pratik Shah
 */
public class SnapFeedbackPolicy 
	extends GraphicalEditPolicy
{
	
private List figures = new ArrayList();
private ZoomManager zoomManager;
	
public void eraseTargetFeedback(Request request) {
	for (Iterator iter = figures.iterator(); iter.hasNext();) {
		IFigure fig = (IFigure)iter.next();
		if (fig.getParent() != null) {
			fig.getParent().remove(fig);
		}
	}
	figures.clear();		
}

protected void highlightGuide(Integer pos, Color color, boolean horizontal) {
	if (pos == null) {
		return;
	}
	int position = pos.intValue();
	
	if (zoomManager != null) {
		position = (int)Math.round(position * zoomManager.getZoom());
	}
	Rectangle diagramBounds = getLayer(LayerConstants.FEEDBACK_LAYER).getBounds();
	Rectangle figBounds = Rectangle.SINGLETON;
	figBounds.setBounds(diagramBounds);
	if (horizontal) {
		figBounds.height = 1;
		figBounds.y = position;
	} else {
		figBounds.x = position;
		figBounds.width = 1;
	}
	IFigure fig = new Figure();
	fig.setOpaque(true);
	fig.setBackgroundColor(color);
	addFeedback(fig);
	fig.setBounds(figBounds);
	figures.add(fig);
}

public void setHost(EditPart host) {
	super.setHost(host);
	zoomManager = (ZoomManager)getHost().getViewer()
			.getProperty(ZoomManager.class.toString());
}

public void showTargetFeedback(Request request) {
	if (request.getType().equals(REQ_MOVE)
			|| request.getType().equals(REQ_RESIZE)
			|| request.getType().equals(REQ_CLONE)) {
		eraseTargetFeedback(request);
		ChangeBoundsRequest req = (ChangeBoundsRequest)request;
		highlightGuide((Integer)req.getExtendedData().get(SnapToGuides.VERTICAL_GUIDE), 
				ColorConstants.red, false);
		highlightGuide((Integer)req.getExtendedData().get(SnapToGuides.HORIZONTAL_GUIDE), 
				ColorConstants.red, true);
		highlightGuide((Integer)req.getExtendedData().get(SnapToGeometry.VERTICAL_ANCHOR), 
				ColorConstants.blue, false);
		highlightGuide((Integer)req.getExtendedData().get(SnapToGeometry.HORIZONTAL_ANCHOR), 
				ColorConstants.blue, true);
	}
}

}