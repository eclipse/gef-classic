/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
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
import org.eclipse.gef.examples.text.model.ModelLocation;
import org.eclipse.gef.examples.text.model.Style;

public class ApplyAlignment
	extends MiniEdit
{

private int alignment = -1;
private int oldAlignment = -1;
private Style style;

public ApplyAlignment(Container c, String styleID, Object value) {
	style = c.getStyle();
	alignment = ((Integer)value).intValue();
}

public boolean canApply() {
	return alignment != -1;
}

public void apply() {
	oldAlignment = style.getAlignment(); 
	style.setAlignment(alignment);
}

public ModelLocation getResultingLocation() {
	return null;
}

public void rollback() {
	style.setAlignment(oldAlignment);
}

}