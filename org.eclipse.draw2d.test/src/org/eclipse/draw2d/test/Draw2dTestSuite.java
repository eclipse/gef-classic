/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.draw2d.test;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * The main test suite for Draw2d.
 * @author Eric Bordeau
 */
public class Draw2dTestSuite extends TestSuite {

public static Test suite() {
	return new Draw2dTestSuite();
}

/**
 * Constructs a new Draw2dTestSuite. Add any JUnit tests to the suite here.
 */
public Draw2dTestSuite() {
	addTest(new TestSuite(ShortestPathRoutingTest.class));
	addTest(new TestSuite(XYLayoutTest.class));
	addTest(new TestSuite(TextFlowWrapTest.class));
	addTest(new TestSuite(LocalOptimizerTest.class));
//	addTest(new TestSuite(CaretTests.class));
	addTest(new TestSuite(AdvancedGraphicsTests.class));
	addTest(new TestSuite(FlowBorderTests.class));
	addTest(new TestSuite(GraphicsClipping.class));
	addTest(new TestSuite(PaintDamageEraseTest.class));
	addTest(new TestSuite(LayeredPaneTest.class));
	addTest(new TestSuite(ConnectionEndPointMoveTest.class));
	addTest(new TestSuite(ImageUtilitiesTest.class));
	addTest(new TestSuite(LookAheadTest.class));
	addTest(new TestSuite(TextualTests.class));
	addTest(new TestSuite(PointListTests.class));
    addTest(new TestSuite(PrecisionDimensionTest.class));
    addTest(new TestSuite(PrecisionPointTest.class));
    addTest(new TestSuite(PrecisionRectangleTest.class));
    addTest(new TestSuite(ThumbnailTest.class));
    addTest(new TestSuite(FigureUtilitiesTest.class));
    addTest(new TestSuite(RectangleTest.class));
//    addTest(new TestSuite(ColorConstantTest.class));
    addTest(new TestSuite(RayTest.class));
}

}

