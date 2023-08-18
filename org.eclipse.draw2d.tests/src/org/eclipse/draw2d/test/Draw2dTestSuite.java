/*******************************************************************************
 * Copyright (c) 2000, 2023 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Alexander Shatalin (Borland) - Contribution for Bug 238874
 *******************************************************************************/
package org.eclipse.draw2d.test;

import org.eclipse.draw2d.graph.test.DirectedGraphLayoutTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * The main test suite for Draw2d.
 * 
 * @author Eric Bordeau
 */
@RunWith(Suite.class) 
@Suite.SuiteClasses({
	ShortestPathRoutingTest.class,
	XYLayoutTest.class,
	TextFlowWrapTest.class,
	LocalOptimizerTest.class,
	AdvancedGraphicsTests.class,
	FlowBorderTests.class,
	GraphicsClipping.class,
	PaintDamageEraseTest.class,
	LayeredPaneTest.class,
	ConnectionEndPointMoveTest.class,
	ImageUtilitiesTest.class,
	LookAheadTest.class,
	TextualTests.class,
	PointTests.class,
	DimensionTests.class,
	PointListTests.class,
	PrecisionDimensionTest.class,
	PrecisionPointTest.class,
	PrecisionRectangleTest.class,
	ThumbnailTest.class,
	FigureUtilitiesTest.class,
	RectangleTest.class,
	ColorConstantTest.class,
	RayTest.class,
	VectorTest.class,
	StraightTest.class,
	RelativeBendpointTest.class,
	GeometryTest.class,
	ScalablePolygonShapeTest.class,
	LayerTest.class,
	ShapeTest.class,
	InsetsTest.class,
	DirectedGraphLayoutTest.class
})
public class Draw2dTestSuite {
}
