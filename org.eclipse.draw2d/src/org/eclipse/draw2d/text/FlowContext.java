/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.draw2d.text;

/**
 * The context that a {@link FlowFigureLayout} uses to perform its layout.
 * 
 * <P>WARNING: This interface is not intended to be implemented by clients. It exists to
 * define the API between the layout and its context.
 */
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

/**
 * Returns the current Y value.
 * @return the current Y value
 */
int getCurrentY();

/**
 * @return <code>true</code> if the current line contains any fragments */
boolean isCurrentLineOccupied();

}