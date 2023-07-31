/*******************************************************************************
 * Copyright (c) 2005, 2023 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package swt.transforms;

import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Transform;

import org.eclipse.draw2d.ColorConstants;

public class RotatedClipRect extends AbstractSWTTransform {

	private float angle = 4;

	public static void main(String[] args) {
		new RotatedClipRect().runTransformTest();
	}

	@Override
	protected void performPaint(PaintEvent e) {
		GC gc = e.gc;
		Transform t = new Transform(gc.getDevice());
		t.translate(60, 60);
		t.rotate(angle);
		gc.setTransform(t);
		gc.setClipping(-20, -20, 40, 40);

		gc.setBackground(ColorConstants.blue);
		gc.setForeground(ColorConstants.yellow);
		gc.fillGradientRectangle(-100, -100, 200, 200, true);
		angle += 0.3;
	}

}