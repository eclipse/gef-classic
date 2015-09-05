/*******************************************************************************
 * Copyright (c) 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.draw2d.test;

import junit.framework.TestCase;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Layer;
import org.eclipse.draw2d.geometry.Rectangle;

public class LayerTest extends TestCase {

	public void testContainsPointInLayer() {
		MyLayer layer = new MyLayer();
		layer.setBounds(new Rectangle(0, 0, 1000, 1000));

		MyLayer layer1 = new MyLayer();
		layer1.setBounds(new Rectangle(20, 20, 50, 50));
		layer.add(layer1);

		Figure figureOnLayer1 = new Figure();
		layer1.add(figureOnLayer1);
		// So effectively on 30,30
		figureOnLayer1.setBounds(new Rectangle(10, 10, 30, 30));
		MyLayer layer2 = new MyLayer();
		layer2.setBounds(new Rectangle(50, 50, 50, 50));
		layer.add(layer2);

		Figure figureOnLayer2 = new Figure();
		// So effectively on 60,60
		figureOnLayer2.setBounds(new Rectangle(10, 10, 30, 30));
		layer2.add(figureOnLayer2);

		// Is location of figureOnLayer2
		assertEquals(true, layer.containsPoint(60, 60));
	}

	public class MyLayer extends Layer {

		protected boolean useLocalCoordinates() {
			return true;
		}

	}
}
