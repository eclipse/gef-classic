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

import java.util.List;

import org.eclipse.gef.commands.Command;

/**
 * This is the interface to interact with the ruler/guide feature provided in GEF.
 * Clients wishing to utilize that feature should implement this interface, and set
 * its instances as a property on the primary graphical viewer (one for each ruler --
 * horizontal and vertical).  Use PROPERTY_HORIZONTAL_RULER and PROPERTY_VERTICAL_RULER
 * as the keys.
 * 
 * @author Pratik Shah
 * @since 3.0
 */
public interface RulerProvider {

/**
 * The following are properties that should be set on the graphical viewer.
 * PROPERTY_HORIZONTAL_RULER and PROPERTY_VERTICAL_RULER should have RulerProviders as
 * their values, whereas PROPERTY_RULER_VISIBILITY should have a Boolean value.
 */
public static final String PROPERTY_HORIZONTAL_RULER = "horizontal ruler"; //$NON-NLS-1$
public static final String PROPERTY_VERTICAL_RULER = "vertical ruler"; //$NON-NLS-1$
public static final String PROPERTY_RULER_VISIBILITY = "ruler$visibility"; //$NON-NLS-1$

/**
 * The following indicate the measurement system that a ruler should display.  Note that
 * this setting does not affect how a guide's position is interpreted (it is always
 * taken as pixels).
 */
public static final int UNIT_INCHES = 0;
public static final int UNIT_CENTIMETERS = 1;
public static final int UNIT_PIXELS = 2;

/**
 * 
 */
public Command getCreateGuideCommand(int position);
public Command getDeleteGuideCommand(Object guide);
public Command getMoveGuideCommand(Object guide, int positionDelta);
public List getGuides();
public int[] getGuidePositions();
public int getGuidePosition(Object guide);
public Object getRuler();
public int getUnit();
public void setUnit(int newUnit);
public void addRulerChangeListener(RulerChangeListener listener);
public void removeRulerChangeListener(RulerChangeListener listener);

}