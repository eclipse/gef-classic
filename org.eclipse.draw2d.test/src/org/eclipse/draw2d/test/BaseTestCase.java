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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

import junit.framework.TestCase;

import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;

/**
 * @author Pratik Shah
 */
public abstract class BaseTestCase 
	extends TestCase 
{

protected static final Font TAHOMA = new Font(null, "Tahoma", 8, 0);//$NON-NLS-1$

public BaseTestCase() {
	super();
}

public BaseTestCase(String text) {
	super(text);
}
	
protected boolean callBooleanMethod(Object receiver, String method) {
	try {
		Method m = receiver.getClass().getMethod(method, null);
		Boolean result = (Boolean)m.invoke(receiver, null);
		return result.booleanValue();
	} catch (NoSuchMethodException exc) {
		fail(exc.getMessage());
	} catch (IllegalAccessException exc) {
		fail (exc.getMessage());
	}catch (InvocationTargetException exc) {
		fail (exc.getMessage());
	}
	return false;
}

public void assertEquals(Image expected, Image actual) {
	assertTrue("The given images did not match", 
			Arrays.equals(expected.getImageData().data, actual.getImageData().data));
}
	
}