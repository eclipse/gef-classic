/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.draw2d;

import org.eclipse.draw2d.geometry.*;

public class ScrollPaneSolver {

public static int NEVER = 0;
public static int AUTOMATIC = 1;
public static int ALWAYS = 2;

static public class Result{
	public boolean showH;
	public boolean showV;
	public Rectangle viewportArea;
	public Insets insets;
}

public static Result solve(
	Rectangle $REFclientArea,
	Viewport viewport,
	int hVis, int vVis,
	int vBarWidth, int hBarHeight)
{

	Result result = new Result();

	result.insets = new Insets();
	result.insets.bottom = hBarHeight;
	result.insets.right  = vBarWidth;
	
	Dimension available  = $REFclientArea.getSize();
	Dimension guaranteed = new Dimension(available).shrink(
		    	(vVis == NEVER ? 0 : result.insets.right),
		    	(hVis == NEVER ? 0 : result.insets.bottom));
	int wHint = guaranteed.width;
	int hHint = guaranteed.height;

	Dimension preferred  = viewport.getPreferredSize(wHint, hHint).getCopy();
	
	//This was calling viewport.getMinimumSize(), but viewports minimum size was really small,
	//and wasn't a function of its contents.
	Dimension viewportMinSize = new Dimension(
		viewport.getInsets().getWidth(),
		viewport.getInsets().getHeight());
	if (viewport.getContents() != null)
		viewportMinSize.expand(viewport.getContents().getMinimumSize(wHint, hHint));

	//Adjust preferred size if tracking flags set.  Basically, tracking == "compress view until
	// its minimum size is reached".
	if (viewport.getContentsTracksHeight())
		preferred.height = viewportMinSize.height;
	if (viewport.getContentsTracksWidth())
		preferred.width = viewportMinSize.width;

	boolean none = available.contains(preferred),
	        both = !none && preferred.containsProper(guaranteed),
		  showV= both || preferred.height > available.height,
		  showH= both || preferred.width  > available.width;
	
	//Adjust for visibility override flags
	result.showV = !(vVis == NEVER) && (showV  || vVis == ALWAYS);
	result.showH = !(hVis == NEVER) && (showH  || hVis == ALWAYS);
	
	if (!result.showV)
		result.insets.right = 0;
	if (!result.showH)
		result.insets.bottom = 0;
	result.viewportArea = $REFclientArea.getCropped(result.insets);
	viewport.setBounds(result.viewportArea);
	return result;
}

}
