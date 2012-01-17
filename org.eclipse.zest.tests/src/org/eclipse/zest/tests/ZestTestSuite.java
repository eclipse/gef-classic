/*******************************************************************************
 * Copyright (c) 2000, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.zest.tests;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * The main test suite for Zest.
 * 
 * @author anyssen
 */
public class ZestTestSuite extends TestSuite {

	public static Test suite() {
		return new ZestTestSuite();
	}

	public ZestTestSuite() {
		addTest(new TestSuite(GraphTests.class));
		addTest(new TestSuite(GraphSelectionTests.class));
		addTest(new TestSuite(GraphViewerTests.class));
	}
}
