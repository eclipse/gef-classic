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
package org.eclipse.draw2d.text;

public interface FlowContext {

/**
 * Adds the given box into the current line.
 * @param box the FlowBox to add */
void addToCurrentLine(FlowBox box);

/**
 * The current line should be committed if it is occupied, and then set to
 * <code>null</code>. Otherwise, do nothing.
 */
void endLine();

/**
 * Obtains the current line, creating a new line if there is no current line.
 * @return the current line */
LineBox getCurrentLine();

int getCurrentY();

/**
 * @return <code>true</code> if the current line contains any fragments */
boolean isCurrentLineOccupied();

}