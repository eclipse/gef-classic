package com.ibm.etools.gef.ui.palette;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import com.ibm.etools.gef.palette.*;

import com.ibm.etools.draw2d.*;
import com.ibm.etools.draw2d.geometry.*;

import java.util.*;

/**
 * Holds the count information, and notifies
 * interested figures of changes in animation.
 * Created by a root, which loops through the
 * animation process. 
 */
class AnimationModel {

private long startTime = new Date().getTime();
private long duration = 0;

private boolean ascending;

/**
 * Default constructor taking in number of
 * milliseconds the animation should take.
 */
public AnimationModel(long duration, boolean ascending){
	this.duration = duration;
	this.ascending = ascending;
}

/**
 * Called to notify the start of the animation process.
 * Notifies all listeners to get ready for animation start.
 */
public void animationStarted(){
	startTime = new Date().getTime();
}

/**
 * Returns (0.0<=value<=1.0), of current position
 */
public float getProgress(){
	long presentTime = new Date().getTime();
	float elapsed = (presentTime-startTime);
	float progress = Math.min(1.0f, elapsed/duration);
	if (!ascending)
		return 1.0f - progress;
	return progress;
}

public boolean isFinished(){
	return (new Date().getTime() - startTime) > duration;
}

}