package zoom;

import org.eclipse.draw2d.*;
import org.eclipse.swt.graphics.*;

import junit.framework.Assert;
import junit.framework.TestCase;

public class ZoomGraphicsTest
	extends TestCase
{

Image image = new Image(null, 600,600);
GC gc= new GC(image);
Graphics dest = new SWTGraphics(gc);
ZoomGraphics zoom = new ZoomGraphics(dest);

public ZoomGraphicsTest(String name) {
	super(name);
}

protected void setUp() throws Exception {
	super.setUp();
}

protected void tearDown() throws Exception {
	super.tearDown();
}

public void testClipRect() {
}

/*
 * Test for void drawArc(int, int, int, int, int, int)
 */
public void testDrawArc() {
}

/*
 * Test for void fillArc(int, int, int, int, int, int)
 */
public void testFillArc() {
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

public void testZoomGraphics() {
}

public void testGetCachedFont() {
}

}
