package org.eclipse.draw2d;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */


/**
 * A generic state listener
 */
public interface ChangeListener {

/**
 * Called when the listened to object's state has changed.
 */
void handleStateChanged(ChangeEvent event);

}