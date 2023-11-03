/*******************************************************************************
 * Copyright (c) 2010 IBM Corporation and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Research Group Software Construction,
 *     RWTH Aachen University, Germany - initial API and implementation
 */
package org.eclipse.draw2d;

/**
 * Interface to stereotype those figures that contain a ScrollPane with a nested
 * Viewport.
 *
 * @author Philip Ritzkopf
 * @author Alexander Nyssen
 *
 * @since 3.6
 */
public interface IScrollableFigure extends IFigure {

	/**
	 * Provides access to this figure's nested ScrollPane.
	 *
	 * @return the ScrollPane contained within this figure
	 */
	ScrollPane getScrollPane();
}