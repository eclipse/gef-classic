/*****************************************************************************************
 * Copyright (c) 2004 IBM Corporation and others. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Common Public
 * License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors: IBM Corporation - initial API and implementation
 ****************************************************************************************/

package org.eclipse.draw2d;

/**
 * since 3.1
 */
public interface LayoutListener {

/**
 * @since 3.1
 * @param child
 * @param constraint
 */
void setConstraint(IFigure child, Object constraint);

/**
 * @since 3.1
 * @param child
 */
void remove(IFigure child);

/**
 * @since 3.1
 */
void invalidate();

/**
 * @since 3.1
 * @param container
 * @return
 */
boolean layout(IFigure container);

/**
 * Called after layout has occured.
 * @since 3.1
 * @param container
 */
void postLayout(IFigure container);

class Stub implements LayoutListener {

	/**
	 * @see LayoutListener#setConstraint(IFigure, java.lang.Object)
	 */
	public void setConstraint(IFigure child, Object constraint) { }

	/**
	 * @see LayoutListener#remove(IFigure)
	 */
	public void remove(IFigure child) { }

	/**
	 * @see LayoutListener#invalidate()
	 */
	public void invalidate() { }

	/**
	 * @see LayoutListener#layout(IFigure)
	 */
	public boolean layout(IFigure container) {
		return false;
	}

	/**
	 * @see LayoutListener#postLayout(IFigure)
	 */
	public void postLayout(IFigure container) { }
	
}

}