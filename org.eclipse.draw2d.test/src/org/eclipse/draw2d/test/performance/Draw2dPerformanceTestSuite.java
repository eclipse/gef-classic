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
package org.eclipse.draw2d.test.performance;

import junit.framework.Test;
import junit.framework.TestSuite;

public class Draw2dPerformanceTestSuite 
	extends TestSuite
{

public Draw2dPerformanceTestSuite() {
	addTest(new TestSuite(TextPerformanceTests.class));
	addTest(new TestSuite(GraphPerformanceTests.class));
}

public static Test suite() {
	return new Draw2dPerformanceTestSuite();
}

}