/*******************************************************************************
 * Copyright 2005-2006, CHISEL Group, University of Victoria, Victoria, BC, Canada.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     The Chisel Group, University of Victoria
 *******************************************************************************/
package org.eclipse.mylar.zest.core.internal.gefx;

/**
 * An experimental interface used for publishing property changes on Zest graphical edit
 * part viewers.
 * @author Del Myers
 *
 */
//@tag zest.experimental
public interface IZestViewerProperties {
	/**
	 * Allows viewer listeners to know when the contents of the viewer has changed.
	 */
	//@tag zest.experimental.contents.
	static final String GRAPH_VIEWER_CONTENTS = "zest.viewer.contents"; //$NON-NLS-1$
	
}