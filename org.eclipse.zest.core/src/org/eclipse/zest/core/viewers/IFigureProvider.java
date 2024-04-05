/*******************************************************************************
 * Copyright (c) 2005, 2024 IBM Corporation and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.zest.core.viewers;

import org.eclipse.jface.viewers.IBaseLabelProvider;

import org.eclipse.draw2d.IFigure;

/**
 * Allows a user to create a figure for an element in graph model. To use this
 * interface, it should be implemented and passed to
 * {@link GraphViewer#setLabelProvider(IBaseLabelProvider)}
 */
public interface IFigureProvider {

	/**
	 * Creates a custom figure for a graph model element
	 */
	public IFigure getFigure(Object element);
}
