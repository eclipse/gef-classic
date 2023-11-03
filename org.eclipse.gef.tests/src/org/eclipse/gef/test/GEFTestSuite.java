/*******************************************************************************
 * Copyright (c) 2000, 2010 IBM Corporation and others.
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
package org.eclipse.gef.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * The main test suite for GEF.
 *
 * @author Eric Bordeau
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
	PaletteCustomizerTest.class,
	ToolUtilitiesTest.class,
	DragEditPartsTrackerTest.class,
	CommandStackTest.class
})
public class GEFTestSuite {
}
