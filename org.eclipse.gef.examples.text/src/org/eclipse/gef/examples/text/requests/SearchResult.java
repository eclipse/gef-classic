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
package org.eclipse.gef.examples.text.requests;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.gef.examples.text.TextLocation;

/**
 * Used to store results of a CaretRequest.
 * @author Pratik Shah
 * @since 3.2
 */
public class SearchResult
{
	
public boolean trailing;
public TextLocation location;
public Dimension proximity = new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
public boolean bestMatchFound;

}