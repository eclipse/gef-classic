package org.eclipse.draw2d.test;

import junit.framework.TestCase;

import org.eclipse.swt.widgets.Display;

public class ColorConstantTest extends TestCase {
	/**
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
	}

	/**
	 * @see TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void test_ColorConstantInit() {
		final Boolean result[] = new Boolean[2];
		result[0] = Boolean.FALSE;
		result[1] = Boolean.FALSE;
		
		Thread testThread = new Thread() {
			public void run() {
				try {
					Class.forName("org.eclipse.draw2d.ColorConstants");
					result[0] = Boolean.TRUE;
				} catch (Error e) {
					result[0] = Boolean.FALSE;
				} catch (Exception ex) {
					result[0] = Boolean.FALSE;
				}
				
				result[1] = Boolean.TRUE;
			}
		};
		
		testThread.start();
		
		while (!result[1].booleanValue()) {
            Display.getCurrent().readAndDispatch();
		}

		testThread.stop();
		
		assertTrue(result[0].booleanValue());

	}
}
