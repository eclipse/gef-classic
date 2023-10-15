/*******************************************************************************
 * Copyright (c) 2005, 2023 IBM Corporation and others.
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
package swt.transforms;

import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Transform;

public class FontScaling extends AbstractSWTTransform {

	private float value = 1;

	public static void main(String[] args) {
		new FontScaling().runTransformTest();
	}

	@Override
	protected void performPaint(PaintEvent e) {
		GC gc = e.gc;
		Transform t = new Transform(gc.getDevice());
		// t.translate(60, 60);
		t.scale(value, value * 1.15f);
		gc.setTransform(t);
		String s = "Test string XYZ"; //$NON-NLS-1$
		Point d = gc.stringExtent(s);
		gc.drawRectangle(0, 0, d.x - 1, d.y - 1);
		gc.drawString(s, 0, 0, true);
		value += 0.003;
	}

}