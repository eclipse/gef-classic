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
import org.eclipse.swt.graphics.Region;
import org.eclipse.swt.graphics.Transform;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.draw2d.ColorConstants;

public class RegionTransforms extends AbstractSWTTransform {

	private Region region;

	public static void main(String[] args) {
		new RegionTransforms().runTransformTest();
	}

	@Override
	protected void performPaint(PaintEvent e) {
		GC gc = e.gc;
		Transform t = new Transform(gc.getDevice());
		t.rotate(-15);
		gc.setTransform(t);
		t.dispose();

		Region clipping = new Region();
		gc.setClipping(region);
		System.out.println("original " + region.getBounds()); //$NON-NLS-1$
		gc.getClipping(clipping);
		System.out.println("transformed " + clipping.getBounds()); //$NON-NLS-1$
		clipping.dispose();

		t = new Transform(gc.getDevice());
		gc.setTransform(t);
		t.dispose();
		gc.setBackground(ColorConstants.blue);
		gc.fillRectangle(e.x, e.y, e.width, e.height);

		t.dispose();
	}

	@Override
	protected Shell createShell(Display display) {
		Shell createShell = super.createShell(display);
		// region created and initialized here as it has to be done after display
		// creation
		region = new Region();
		region.add(10, 10, 100, 150);
		return createShell;
	}

}