package org.eclipse.gef.ui.palette;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.*;

class AnimatableFigure 
	extends Figure 
{

private AnimationModel animationModel = null;
protected boolean expanded = true;
private static final long delay = 150;

public AnimatableFigure(){}

private void animate(){
	animationModel = new AnimationModel(delay, expanded);
	animationModel.animationStarted();
	while(!animationModel.isFinished())
		this.step();
	step();
	animationModel=null;
}

/**
 * Should be called, after which the compoenents can be removed.
 */
public void collapse(){
	if (expanded == false)
		return;
	expanded = false;
	animate();
}

/** 
 * Should get called after adding all the new components.
 */
public void expand(){
	if (expanded == true)
		return;
	expanded = true;
	animate();
}

public Dimension getPreferredSize(int w, int h){
	if(animationModel == null){
		if (expanded)
			return super.getPreferredSize(w, h);
		else
			return getMinimumSize();
	}
	Dimension pref = super.getPreferredSize(w, h);
	Dimension min  = getMinimumSize();
	float scale = animationModel.getProgress();
	return pref.getScaled(scale).expand(min.getScaled(1.0f-scale));
}

public void setExpanded(boolean value){
	if (expanded == value)
		return;
	expanded = value;
	revalidate();
}

private void step(){
	revalidate();
	getUpdateManager().performUpdate();
}

}