/**
 * 
 */
package org.eclipse.draw2d.test;

import junit.framework.TestCase;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.Shape;
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
		Shape shape = new Shape() {

			protected void fillShape(Graphics graphics) {
				// NOTHING TO DO
			}

			protected void outlineShape(Graphics graphics) {
				// NOTHING TO DO
			}

		};
		shape.setLineAttributes(attributes);
		assertEquals(SWT.LINE_DASHDOT, shape.getLineStyle());
	}

	public void testLineWidthBackwardsCompatibility() {
		LineAttributes attributes = new LineAttributes(4);
		Shape shape = new Shape() {

			protected void fillShape(Graphics graphics) {
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
