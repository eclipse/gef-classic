package org.eclipse.draw2d.test;

import junit.framework.TestCase;

import org.eclipse.draw2d.Ellipse;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.parts.Thumbnail;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

public class ThumbnailTest extends TestCase {
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

	class TestThumbnail extends Thumbnail {
		public Image getThumbnailImage() {
			return super.getThumbnailImage();
		}
	}

	public void test_Thumbnail() {
		TestThumbnail thumb = new TestThumbnail();
		IFigure fig = new Ellipse();
		fig.setBounds(new Rectangle(0, 0, 100, 100));
		fig.setFont(Display.getDefault().getSystemFont());
		thumb.setSource(fig);
		thumb.setBounds(new Rectangle(0, 0, 100, 100));
		Image img = thumb.getThumbnailImage();
		assertTrue(img != null);
	}

	public void test_EmptyThumbnail() {
		TestThumbnail thumb = new TestThumbnail();
		IFigure fig = new Ellipse();
		fig.setBounds(new Rectangle(0, 0, 100, 100));
		fig.setFont(Display.getDefault().getSystemFont());
		thumb.setSource(fig);
		thumb.setBounds(new Rectangle(0, 0, 100, 100));
		Image img = thumb.getThumbnailImage();
		assertTrue(img != null);

		fig.setBounds(new Rectangle(0, 0, 0, 0));
		fig.revalidate();
		img = thumb.getThumbnailImage();

		assertTrue(img == null);
	}

}
