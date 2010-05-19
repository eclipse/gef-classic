/*******************************************************************************
 * Copyright (c) 2004, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.gef.examples.text.model.commands;

import org.eclipse.gef.examples.text.model.Container;
import org.eclipse.gef.examples.text.model.ModelElement;
import org.eclipse.gef.examples.text.model.ModelLocation;

public class InsertModelElement extends MiniEdit {

	private final Container parent;
	private final int offset;
	private final ModelElement child;
	private final ModelLocation location;

	public InsertModelElement(Container parent, int offset, ModelElement child,
			ModelLocation location) {
		this.parent = parent;
		this.offset = offset;
		this.child = child;
		this.location = location;
	}

	public void apply() {
		parent.add(child, offset);
	}

	public boolean canApply() {
		return true;
	}

	public ModelLocation getResultingLocation() {
		return location;
	}

	public void rollback() {
		parent.remove(child);
	}

}
