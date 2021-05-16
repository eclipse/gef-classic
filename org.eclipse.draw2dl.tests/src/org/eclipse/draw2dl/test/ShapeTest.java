/**
 * 
 */
package org.eclipse.draw2dl.test;

import junit.framework.TestCase;

import org.eclipse.draw2dl.Graphics;
import org.eclipse.draw2dl.Shape;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.LineAttributes;

/**
 * @author nyssen
 * 
 */
public class ShapeTest extends TestCase {

	/**
	 * Test case to demonstate bug #297223
	 */
	public void testLineStyleBackwardsCompatibility() {
		LineAttributes attributes = new LineAttributes(1);
		attributes.style = SWT.LINE_DASHDOT;
		org.eclipse.draw2dl.Shape shape = new org.eclipse.draw2dl.Shape() {

			protected void fillShape(org.eclipse.draw2dl.Graphics graphics) {
				// NOTHING TO DO
			}

			protected void outlineShape(org.eclipse.draw2dl.Graphics graphics) {
				// NOTHING TO DO
			}

		};
		shape.setLineAttributes(attributes);
		assertEquals(SWT.LINE_DASHDOT, shape.getLineStyle());
	}

	public void testLineWidthBackwardsCompatibility() {
		LineAttributes attributes = new LineAttributes(4);
		org.eclipse.draw2dl.Shape shape = new Shape() {

			protected void fillShape(org.eclipse.draw2dl.Graphics graphics) {
				// NOTHING TO DO
			}

			protected void outlineShape(Graphics graphics) {
				// NOTHING TO DO
			}

		};
		shape.setLineAttributes(attributes);
		assertEquals(4, shape.getLineWidth());
	}
}
