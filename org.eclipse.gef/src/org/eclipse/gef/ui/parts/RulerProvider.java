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
package org.eclipse.gef.ui.parts;

import java.util.List;

import org.eclipse.gef.commands.Command;

/**
 * @author Pratik Shah
 */
public interface RulerProvider {
	
public static final String HORIZONTAL = "horizontal ruler"; //$NON-NLS-1$
public static final String VERTICAL = "vertical ruler"; //$NON-NLS-1$
	
public static final int UNIT_INCHES = 0;
public static final int UNIT_CENTIMETERS = 1;
public static final int UNIT_PIXELS = 2;
	
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
