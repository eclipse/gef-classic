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
package org.eclipse.gef.rulers;

import java.util.*;

import org.eclipse.swt.accessibility.AccessibleControlEvent;
import org.eclipse.swt.accessibility.AccessibleEvent;

import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.UnexecutableCommand;
import org.eclipse.gef.internal.GEFMessages;

/**
 * This class provides an interface to interact with the ruler/guide feature provided in 
 * GEF.  A <code>RulerProvider</code> represents a ruler (and the guides contained within),
 * and provides the necessary information about them.
 * <p>
 * Clients wishing to utilize this GEF feature should do the following:
 * <ul>
 * 		<li>Extend this class and override the necessary methods.  Also provide a 
 * 			notification mechanism to notify <code>RulerChangeListener</code>s of 
 * 			changes in ruler properties.</li>
 * 		<li>Set instances of that class as properties on the diagram's graphical viewer
 * 			(PROPERTY_HORIZONTAL_RULER and/or PROPERTY_VERTICAL_RULER)</li>
 * 		<li>Set PROPERTY_RULER_VISIBILITY to be <code>true</code> on the graphical viewer.</li>
 * </ul>
 * 
 * @author Pratik Shah
 * @since 3.0
 */
public abstract class RulerProvider {

/**
 * The following are properties that should be set on the graphical viewer.
 * PROPERTY_HORIZONTAL_RULER and PROPERTY_VERTICAL_RULER should have RulerProviders as
 * their values, whereas PROPERTY_RULER_VISIBILITY should have a Boolean value.
 */
public static final String PROPERTY_HORIZONTAL_RULER = "horizontal ruler"; //$NON-NLS-1$
public static final String PROPERTY_RULER_VISIBILITY = "ruler$visibility"; //$NON-NLS-1$
public static final String PROPERTY_VERTICAL_RULER = "vertical ruler"; //$NON-NLS-1$

/**
 * The following indicate the measurement system that a ruler should display.  Note that
 * this setting does not affect how a guide's position is interpreted (it is always
 * taken as pixels).
 */
public static final int UNIT_CENTIMETERS = 1;
public static final int UNIT_INCHES = 0;
public static final int UNIT_PIXELS = 2;

/**
 * A list of <code>RulerChangeListener</code>s that have to be notified of changes in
 * ruler/guide properties.
 */
protected List listeners = new ArrayList();

/**
 * The given listener will be 
 */
public void addRulerChangeListener(RulerChangeListener listener) {
	if (!listeners.contains(listener)) {
		listeners.add(listener);
	}
}

/**
 * @see	org.eclipse.swt.accessibility.AccessibleAdapter#getDescription(org.eclipse.swt.accessibility.AccessibleEvent)
 * @param	e		AccessibleEvent
 * @param	guide	The guide whose accessibility information is requested
 * @return			A default description of guides
 */
public void getAccGuideDescription(AccessibleEvent e, Object guide) {
	e.result = GEFMessages.Guide_Desc;
}

/**
 * @see	org.eclipse.swt.accessibility.AccessibleAdapter#getName(org.eclipse.swt.accessibility.AccessibleEvent)
 * @param	e		AccessibleEvent
 * @param	guide	The guide whose accessibility information is requested
 * @return			A default name for guides
 */
public void getAccGuideName(AccessibleEvent e, Object guide) {
	e.result = GEFMessages.Guide_Label; 
}

/**
 * @see	org.eclipse.swt.accessibility.AccessibleControlAdapter#getValue(org.eclipse.swt.accessibility.AccessibleControlEvent)
 * @param	e		AccessibleEvent
 * @param	guide	The guide whose accessibility information is requested
 * @return			The guide's position
 */
public void getAccGuideValue(AccessibleControlEvent e, Object guide) {
	e.result = "" + getGuidePosition(guide); //$NON-NLS-1$
}

/**
 * Returns a List of model objects that are attached to the given guide. 
 * 
 * @param guide the guide to which the model parts are attached.
 * @return list of attached model objects
 */
public List getAttachedModelObjects(Object guide) {
	return Collections.EMPTY_LIST;
}

/**
 * Returns a List of EditParts that are attached to the given guide.
 * 
 * @param guide the guide to which the EditParts are attached.
 * @param viewer the GraphicalViewer in which these EditParts are shown.
 * @return list of attached edit parts
 */
public List getAttachedEditParts(Object guide, GraphicalViewer viewer) {
	List attachedModelObjects = getAttachedModelObjects(guide);
	List attachedEditParts = new ArrayList(attachedModelObjects.size());
	Iterator i = attachedModelObjects.iterator();
	
	while (i.hasNext())
		attachedEditParts.add(viewer.getEditPartRegistry().get(i.next()));
	
	return attachedEditParts;
}

/**
 * Clients should override this method to return a Command to create a new guide at the
 * given position.
 * 
 * @param	position	The pixel position where the new guide is to be created
 * @return	UnexecutableCommand
 */
public Command getCreateGuideCommand(int position) {
	return UnexecutableCommand.INSTANCE;
}

/**
 * Clients should override this method to return a Command to delete the given guide.
 * 
 * @param	guide	The guide that is to be deleted
 * @return	UnexecutableCommand
 */
public Command getDeleteGuideCommand(Object guide) {
	return UnexecutableCommand.INSTANCE;
}

/**
 * Clients should override this method to return a Command to move the given guide by
 * the given amount.
 * 
 * @param	guide			The guide that is to be moved
 * @param	positionDelta	The amount by which the guide is to be moved
 * @return	UnexecutableCommand 
 */
public Command getMoveGuideCommand(Object guide, int positionDelta) {
	return UnexecutableCommand.INSTANCE;
}

/**
 * Clients should override this method to return a list of all the guides set on this
 * ruler.
 * 
 * @return	an empty list
 */
public List getGuides() {
	return Collections.EMPTY_LIST;
}

/**
 * Clients should override this method to return an array of all the positions of all the 
 * guides on this ruler.
 * 
 * @return	an empty array
 */
public int[] getGuidePositions() {
	return new int[0];
}

/**
 * Clients should override this method to return the position (in pixels) of the given 
 * guide.
 * 
 * @param	guide	The guide whose position is to be determined
 * @return	<code>Integer.MIN_VALUE</code>
 */
public int getGuidePosition(Object guide) {
	return Integer.MIN_VALUE;
}

/**
 * Clients should override this method to return a model representation of the ruler.
 * 
 * @return	<code>null</code> 
 */
public Object getRuler() {
	return null;
}

/**
 * Clients should override this method to return the units that the ruler should display:
 * one of UNIT_INCHES, UNIT_CENTIMETERS, UNIT_PIXELS.
 * 
 * @return	UNIT_INCHES
 */
public int getUnit() {
	return UNIT_INCHES;
}

/**
 * The given listener will not be notified of changes in the ruler anymore.
 */
public void removeRulerChangeListener(RulerChangeListener listener) {
	listeners.remove(listener);
}

/**
 * This method will be invoked when the user requests that the ruler display a different
 * measurement.  The default implementation ignores the user's request.
 */
public void setUnit(int newUnit) {
}

}