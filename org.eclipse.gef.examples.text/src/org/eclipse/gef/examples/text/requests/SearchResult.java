/*******************************************************************************
 * Copyright (c) 2005, 2010 IBM Corporation and others.
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
package org.eclipse.gef.examples.text.requests;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.gef.examples.text.TextLocation;

/**
 * Used to store results of a CaretRequest.
 * 
 * @author Pratik Shah
 * @since 3.2
 */
public class SearchResult {

	public boolean trailing;
	public TextLocation location;
	public Dimension proximity = new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
	public boolean bestMatchFound;

}