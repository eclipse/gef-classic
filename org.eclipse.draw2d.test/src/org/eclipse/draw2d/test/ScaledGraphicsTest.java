package org.eclipse.draw2d.test;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;

import org.eclipse.draw2d.*;

public class ScaledGraphicsTest
	extends BaseTestCase
{

protected Image image;
protected GC gc;
protected Graphics dest;
protected ScaledGraphics zoom;

public ScaledGraphicsTest(String name) {
	super(name);
}

protected void setUp() throws Exception {
	image = new Image(null, 600,600);
	gc= new GC(image);
	dest = new SWTGraphics(gc);
	zoom = new ScaledGraphics(dest);
	zoom.setBackgroundColor(ColorConstants.red);
}

protected void tearDown() throws Exception {
	zoom.dispose();
	dest.dispose();
	gc.dispose();
	image.dispose();
}

public void testClipRect() {
}

public void testDrawEmptyArc() {
	zoom.drawArc(200, 200, 0, 100, 180, 180);
	zoom.drawArc(100, 100, 50, 50, 180, 0);
	zoom.drawArc(10, 10, -10, 10, 90, -540);
	Image expected = new Image(null, 600, 600);
	try {
		assertEquals(expected, image);
	} finally {
		expected.dispose();
	}
}

public void testDrawTwoPixelArc() {
}

public void testBugNum52414() {
}

public void testDrawZoomedArc() {
}

public void testFillEmptyArc() {
	zoom.fillArc(200, 200, 100, 100, 101, 0);
	zoom.fillArc(200, 200, 100, 0, 90, 180);
	zoom.fillArc(200, 200, -10, 90, 90, -540);
	zoom.fillArc(200, 200, 2, 2, 90, 180);
	
	Image expected = new Image(null, 600, 600);
	try {
		assertEquals(expected, image);
	} finally {
		expected.dispose();
	}
}

public void testFillOnePixelArc() {
}

public void testFillZoomedArc() {
}

/*
 * Test for void drawFocus(int, int, int, int)
 */
public void testDrawFocus() {
}

/*
 * Test for void drawImage(Image, int, int)
 */
public void testDrawImage() {
}

/*
 * Test for void drawImage(Image, int, int, int, int, int, int, int, int)
 */
public void testStretchImage() {
}

/*
 * Test for void drawLine(int, int, int, int)
 */
public void testDrawLine() {
}

/*
 * Test for void drawOval(int, int, int, int)
 */
public void testDrawOval() {
}

/*
 * Test for void fillOval(int, int, int, int)
 */
public void testFillOval() {
}

public void testDrawPolygon() {
}

public void testFillPolygon() {
}

public void testDrawPolyline() {
}

/*
 * Test for void drawRectangle(int, int, int, int)
 */
public void testDrawRectangle() {
}

/*
 * Test for void fillRectangle(int, int, int, int)
 */
public void testFillRectangle() {
}

public void testDrawRoundRectangle() {
}

public void testFillRoundRectangle() {
}

/*
 * Test for void drawText(String, int, int)
 */
public void testDrawTextString() {
}

/*
 * Test for void drawString(String, int, int)
 */
public void testDrawStringString() {
}

/*
 * Test for void fillString(String, int, int)
 */
public void testFillStringString() {
}

/*
 * Test for void fillText(String, int, int)
 */
public void testFillTextString() {
}

public void testGetAdvanceWidth() {
}

public void testGetBackgroundColor() {
}

public void testGetCharWidth() {
}

public void testGetClip() {
}

public void testGetFont() {
}

public void testGetFontMetrics() {
}

public void testGetForegroundColor() {
}

public void testGetLineStyle() {
}

public void testGetLineWidth() {
}

public void testGetStringExtent() {
}

public void testGetTextExtent() {
}

public void testGetXORMode() {
}

public void testPopState() {
}

public void testPushState() {
}

public void testRestoreState() {
}

public void testScale() {
}

public void testSetBackgroundColor() {
}

public void testSetClip() {
}

public void testSetFont() {
}

public void testSetForegroundColor() {
}

public void testSetLineStyle() {
}

public void testSetLineWidth() {
}

public void testSetXORMode() {
}

/*
 * Test for void translate(int, int)
 */
public void testTranslate() {
}

public void testScaledGraphics() {
}

public void testGetCachedFont() {
}

}
