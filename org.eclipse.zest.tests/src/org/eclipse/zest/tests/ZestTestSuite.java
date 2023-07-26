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

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * The main test suite for Zest.
 * 
 * @author anyssen
 */
@RunWith(Suite.class) 
@Suite.SuiteClasses({
	GraphTests.class,
	GraphSelectionTests.class,
	GraphViewerTests.class
})
public class ZestTestSuite {
}
