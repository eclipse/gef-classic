/*******************************************************************************
 * Copyright (c) 2024 Patrick Ziegler and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Patrick Ziegler - initial API and implementation
 *******************************************************************************/

package org.eclipse.gef.test.swtbot;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Test suite containing all tests that require the SWTBot. These tests must
 * <b>NOT</b> be executed within the UI thread.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
	FlowDiagramTests.class,
	LogicDiagramTests.class,
	ShapesDiagramTests.class
})
public class SWTBotTestSuite {
}
