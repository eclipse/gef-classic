/*******************************************************************************
 * Copyright (c) 2004, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.gef.examples.text;

import org.eclipse.core.runtime.Assert;
import org.eclipse.gef.examples.text.edit.TextEditPart;

/**
 * @since 3.1
 */
public class TextLocation {

public final int offset;

public final TextEditPart part;

/**
 * @since 3.1
 */
public TextLocation(TextEditPart part, int offset) {
	Assert.isNotNull(part);
	this.offset = offset;
	this.part = part;
}

/**
 * @see java.lang.Object#equals(java.lang.Object)
 */
public boolean equals(Object obj) {
	if (obj instanceof TextLocation) {
		TextLocation other = (TextLocation)obj;
		return other.offset == offset && other.part == part;
	}
	return false;
}

/**
 * @see java.lang.Object#hashCode()
 */
public int hashCode() {
	return part.hashCode() << 11 ^ offset;
}

}
