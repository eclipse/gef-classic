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
package org.eclipse.gef.test.performance;

import junit.framework.Test;
import junit.framework.TestSuite;

public class GEFPerformanceTestSuite 
	extends TestSuite
{

public GEFPerformanceTestSuite() {
	addTest(new TestSuite(LogicExampleTests.class));
}

public static Test suite() {
	return new GEFPerformanceTestSuite();
}

}