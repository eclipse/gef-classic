/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.draw2d.test;

import java.util.Arrays;

import junit.framework.TestCase;

import org.eclipse.swt.graphics.Image;

/**
 * @author Pratik Shah
 */
public abstract class BaseTestCase 
	extends TestCase 
{
	
public BaseTestCase() {
	super();
}

public BaseTestCase(String text) {
	super(text);
}
	
public void assertEquals(Image expected, Image actual) {
	assertTrue("The given images did not match", 
			Arrays.equals(expected.getImageData().data, actual.getImageData().data));
}
	
}