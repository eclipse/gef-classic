package org.eclipse.draw2d.parts;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.util.Date;

/**
 * Holds the count information, and notifies
 * interested figures of changes in animation.
 * Created by a root, which loops through the
 * animation process. 
 */
class AnimationModel{

protected long startTime = new Date().getTime();
protected long endTime = new Date().getTime();
protected long numberOfMilliSeconds = 0;

/**
 * Default constructor taking in number of
 * milliseconds the animation should take.
 */
public AnimationModel( long numberOfMilliSeconds ){
	this.numberOfMilliSeconds = numberOfMilliSeconds;
}

/**
 * Called to notify the start of the animation process.
 * Notifies all listeners to get ready for animation start.
 */
public void start(){
	startTime = new Date().getTime();
	endTime = startTime+numberOfMilliSeconds;
}

/**
 * Returns (0.0<=value<=1.0), of current position
 */
public float getValue(){
	long presentTime = new Date().getTime();
	if( presentTime > endTime ) return (float)1.0;
	long timePassed = (presentTime-startTime);
	float progress = ((float)timePassed)/((float)numberOfMilliSeconds);
	return progress;
}

}


