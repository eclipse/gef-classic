package com.ibm.etools.draw2d;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import com.ibm.etools.draw2d.geometry.*;

public class ScrollPaneSolver {

public static int NEVER = 0;
public static int AUTOMATIC = 1;
public static int ALWAYS = 2;

static public class Result{
	public boolean showH;
	public boolean showV;
}

public static Result solve(
	Rectangle $REFclientArea,
	Viewport viewport,
	int hVis, int vVis,
	int vBarWidth, int hBarHeight)
{

	Result result = new Result();

	Insets insets = new Insets();
	insets.bottom = hBarHeight;
	insets.right  = vBarWidth;
	
	Dimension preferred  = viewport.getPreferredSize().getCopy();
	Dimension available  = $REFclientArea.getSize();
	Dimension guaranteed = new Dimension(available).shrink(
		    	(vVis == NEVER ? 0 : insets.right),
		    	(hVis == NEVER ? 0 : insets.bottom));
	
	//Adjust preferred size if tracking flags set
	Dimension viewportMinSize = viewport.getMinimumSize();

	if(viewport.getContentsTracksHeight()){
		preferred.height = viewportMinSize.height;
	}
	if(viewport.getContentsTracksWidth()){
		preferred.width = viewportMinSize.width;
	}

	boolean none = available.contains(preferred),
	        both = !none && preferred.containsProper(guaranteed),
		  showV= both || preferred.height > available.height,
		  showH= both || preferred.width  > available.width;
	
	//Adjust for visibility override flags
	result.showV = !(vVis == NEVER) && (showV  || vVis == ALWAYS);
	result.showH = !(hVis == NEVER) && (showH  || hVis == ALWAYS);
	
	if (!result.showV) insets.right = 0;
	if (!result.showH) insets.bottom = 0;
	Rectangle viewportArea = $REFclientArea.getCropped(insets);

	viewport.setBounds(viewportArea);
	return result;
}

}
