package com.ibm.etools.gef;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

/**
 * The listener interface for receiving basic events from an EditPart.
 * Listener interested in only one type of Event can extend the {@Stub Stub}
 * implementation rather than implementing the entire interface.
 */

public interface EditPartListener {

/**
 * Listeners interested in just a subset of Events can extend this stub implementation.
 * Also, extending the Stub will reduce the impact of new API on this interface.
 */
public class Stub implements EditPartListener{

	public void childAdded(EditPart child, int index){}

	public void partActivated(EditPart editpart){}

	public void partDeactivated(EditPart editpart){}

	public void removingChild(EditPart child, int index){}

	public void selectedStateChanged(EditPart part){}
};

/**
 * Called after a child EditPart has been added to its parent.
 */
void childAdded(EditPart child, int index);

/**
 * This method is currently never called by default.
 */
void partActivated(EditPart editpart);

/**
 * This method is currently never called by default.
 */
void partDeactivated(EditPart editpart);

/**
 * Called before a child EditPart is removed from its parent.
 */
void removingChild(EditPart child, int index);

/**
 * Called when the selected state of an EditPart has changed.
 * @see EditPart#SELECTED
 */
void selectedStateChanged(EditPart part);

}