/*****************************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: IBM Corporation - initial API and implementation
 ****************************************************************************************/

package org.eclipse.gef.examples.text;

import org.eclipse.gef.Request;

/**
 * @since 3.1
 */
public class SelectionRangeRequest extends Request {

private final SelectionRange range;

/**
 * @since 3.1
 * @param type
 */
public SelectionRangeRequest(Object type, SelectionRange range) {
	super(type);
	this.range = range;
}

/**
 * @return Returns the range.
 */
public SelectionRange getSelectionRange() {
	return range;
}

}