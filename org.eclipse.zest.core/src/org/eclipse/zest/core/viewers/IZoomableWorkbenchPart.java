/*******************************************************************************
 * Copyright 2005-2006, CHISEL Group, University of Victoria, Victoria, BC,
 *                      Canada.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors: The Chisel Group, University of Victoria
 *******************************************************************************/
package org.eclipse.zest.core.viewers;

/**
 * An interface that can be added to IWorkbenchParts based on ZEST views so that
 * zooming is supported.
 *
 * @author Del Myers
 *
 */
//@tag bug.156286-Zooming.fix : experimental
public interface IZoomableWorkbenchPart {
	/**
	 * Returns the viewer that is zoomable.
	 *
	 * @return the viewer that is zoomable.
	 */
	AbstractZoomableViewer getZoomableViewer();
}
