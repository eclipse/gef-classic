/*******************************************************************************
 * Copyright (c) 2000, 2010 IBM Corporation and others.
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
package org.eclipse.gef.palette;

import org.eclipse.gef.tools.PanningSelectionTool;

/**
 * A ToolEntry for a {@link PanningSelectionTool}.
 *
 * @author msorens
 * @since 3.0
 */
public class PanningSelectionToolEntry extends SelectionToolEntry {

	/**
	 * Creates a new PanningSelectionToolEntry.
	 */
	public PanningSelectionToolEntry() {
		this(null);
	}

	/**
	 * Constructor for PanningSelectionToolEntry.
	 *
	 * @param label the label
	 */
	public PanningSelectionToolEntry(String label) {
		this(label, null);
	}

	/**
	 * Constructor for PanningSelectionToolEntry.
	 *
	 * @param label     the label
	 * @param shortDesc the description
	 */
	public PanningSelectionToolEntry(String label, String shortDesc) {
		super(label, shortDesc);
		setToolClass(PanningSelectionTool.class);
	}

}
