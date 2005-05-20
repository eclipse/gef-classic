/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/ 

package org.eclipse.draw2d.test;

import junit.framework.TestCase;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.Pattern;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.SWTGraphics;

public class AdvancedGraphicsTests extends TestCase {

static final int LINE[] = new int[] {5, 5, 20, 20, 35, 5, 50, 5};
static final int POLY[] = new int[] {5, 5, 45, 15, 20, 30, 20, 20, 45, 35, 5, 45};
private static final int PREVIEW_DELAY = 250;
private SWTGraphics g;

private Image image;
private GC imageGC;

private void assertImageEquality(int width, int height) {
	ImageData data = image.getImageData();
	int src, dst;
	PaletteData palette = data.palette;
	for (int y = 0; y < height; y++)
		for (int x = 0; x < width; x++) {
			src = data.getPixel(x, y);
			dst = data.getPixel(x, y + height);
			
			if (src != dst) {
				RGB rgb1 = palette.getRGB(src);
				RGB rgb2 = palette.getRGB(dst);
				//HACK, image operations seem to differ by as much as 2
				if (Math.abs(rgb1.red - rgb2.red) > 2
						| Math.abs(rgb1.green - rgb2.green) > 2
						| Math.abs(rgb1.blue - rgb2.blue) > 2)
				assertEquals("Discrepancy at coordinates <" + x +", " + y + ">",
						rgb1,
						rgb2);
			}
		}
}

private void displayImage() {
	final Shell shell = new Shell();
	shell.addPaintListener(new PaintListener() {
		public void paintControl(PaintEvent e) {
			e.gc.drawImage(image, 0, 0);
		}
	});
	shell.setBounds(100, 100, 800, 600);
	shell.open();
	Display d = shell.getDisplay();
	d.timerExec(PREVIEW_DELAY, new Runnable() {
		public void run() {
			if (!shell.isDisposed())
				shell.close();
		}	
	});
	while (!shell.isDisposed())
		while (!d.readAndDispatch())
			d.sleep();
	
}

private void performTestcase(Runnable painter, Runnable tests[]) {
	g.pushState();
	painter.run();
	for (int i = 0; i < tests.length; i++) {
		g.translate(100, 0);
		tests[i].run();
		g.pushState();
		painter.run();
	}
	
	for (int i = 0; i <= tests.length; i++) {
		g.popState();
		g.translate(0, 100);
		painter.run();
	}
	
	displayImage();
	assertImageEquality(100 * tests.length, 100);
}

protected void setUp() throws Exception {
	image = new Image(Display.getDefault(), 800, 600);
	imageGC = new GC(image);
	g = new SWTGraphics(imageGC);
}

protected void tearDown() throws Exception {
	g.dispose();
	imageGC.dispose();
	image.dispose();
}
/*
public void testPatterns() {
	
	class SetPattern implements Runnable {
		private final Pattern bg;
		private final Pattern fg;
		SetPattern (Pattern bg, Pattern fg) {
			this.bg = bg;
			this.fg = fg;
		}
		public void run() {
			g.setBackgroundPattern(bg);
			g.setForegroundPattern(fg);
		}
	};

	//$Workaround, force GDI + so that normal fillString is consistent
	g.setAntialias(SWT.OFF);
	
	//Initial state
	Font f = new Font(null, "Arial Black", 24, SWT.BOLD);
	g.setFont(f);
	g.setBackgroundColor(ColorConstants.yellow);
	g.pushState();
	
	Pattern gradient = new Pattern(null, 0, 0, 80, 40, ColorConstants.gray, ColorConstants.black);
	Pattern image = new Pattern(null, TestImages.depth_24);
	
	Runnable tests[] = new Runnable[1];
	tests[0] = new SetPattern(image, gradient);
	performTestcase(new Runnable() {
		public void run() {
			g.fillText("GEF", 0, 0);
		};
	}, tests);
	
	gradient.dispose();
	image.dispose();
	f.dispose();
}
*/
public void testAntialias() {
	class AntialiasSettings implements Runnable {
		private final Color color;
		private final int normal;
		private final int text;
		AntialiasSettings (int text, int normal, Color color) {
			this.text = text;
			this.normal = normal;
			this.color = color;
		}
		public void run() {
			g.setAntialias(normal);
			g.setTextAntialias(text);
			g.setForegroundColor(color);
		}
	};

	g.setLineWidth(9);

	/*
	 * $TODO workaround for default text antialias setting.
	 * Restoring the default text AA setting does not work. therefore, we will turn off
	 * TAA before running tests.
	 */
	g.setTextAntialias(SWT.ON);
	g.setAntialias(SWT.ON);
	g.pushState();

	Runnable tests[] = new Runnable[4];
	tests[0] = new AntialiasSettings(SWT.ON, SWT.ON, ColorConstants.red);
	tests[1] = new AntialiasSettings(SWT.OFF, SWT.OFF, ColorConstants.blue);
	tests[2] = new AntialiasSettings(SWT.DEFAULT, SWT.ON, ColorConstants.black);
	tests[3] = new AntialiasSettings(SWT.ON, SWT.DEFAULT, ColorConstants.darkGreen);
	performTestcase(new Runnable() {
		public void run() {
			g.drawPolyline(LINE);
			g.drawString("OWVO", 35, 20);
		};
	}, tests);
}

public void testFillRules() {
	
	class FillRules implements Runnable {
		private final int aa;
		private final int rule;
		FillRules (int rule, int aa) {
			this.rule = rule;
			this.aa = aa;
		}
		public void run() {
			g.setFillRule(rule);
			//$TODO 
			//g.setAntialias(aa);
		}
	};
    
    g.setAntialias(SWT.ON);
	g.setBackgroundColor(ColorConstants.red);
	g.pushState();

	Runnable tests[] = new Runnable[3];
	tests[0] = new FillRules(SWT.FILL_EVEN_ODD, SWT.ON);
	tests[1] = new FillRules(SWT.FILL_WINDING, SWT.OFF);
	tests[2] = new FillRules(SWT.FILL_EVEN_ODD, SWT.DEFAULT);
	performTestcase(new Runnable() {
		public void run() {
			g.fillPolygon(POLY);
		};
	}, tests);
}
/*
public void testInterpolation() {
	class InterpolationSettings implements Runnable {
		private final int level;
		InterpolationSettings (int level) {
			this.level = level;
		}
		public void run() {
			g.setInterpolation(level);
		}
	};

	g.setLineWidth(9);

    // $TODO workaround for default text antialias setting.
    // Restoring the default text AA setting does not work. therefore, we will turn off
    // TAA before running tests.
	g.setTextAntialias(SWT.OFF);
	g.setAntialias(SWT.OFF);
	g.pushState();

	Runnable tests[] = new Runnable[4];
	tests[0] = new InterpolationSettings(SWT.HIGH);
	tests[1] = new InterpolationSettings(SWT.LOW);
	tests[2] = new InterpolationSettings(SWT.NONE);
	tests[3] = new InterpolationSettings(SWT.DEFAULT);
	performTestcase(new Runnable() {
		public void run() {
			g.drawImage(TestImages.depth_24, 0, 0, 400, 400, 0, 0, 75, 75);
		};
	}, tests);
}
*/
public void testLineJoinCap() {
	
	class LineSettings implements Runnable {
		private final int cap;
		private final int dash;
		private final int join;
		LineSettings (int join, int cap, int dash) {
			this.join = join;
			this.cap = cap;
			this.dash = dash;
		}
		public void run() {
			g.setLineCap(cap);
			g.setLineJoin(join);
			g.setLineStyle(dash);
		}
	};
	
	g.setLineWidth(9);
	g.pushState();

	Runnable tests[] = new Runnable[3];
	tests[0] = new LineSettings(SWT.JOIN_ROUND, SWT.CAP_ROUND, SWT.LINE_DASH);
	tests[1] = new LineSettings(SWT.JOIN_BEVEL, SWT.CAP_FLAT, SWT.LINE_DOT);
	tests[2] = new LineSettings(SWT.JOIN_ROUND, SWT.CAP_SQUARE, SWT.LINE_SOLID);
	performTestcase(new Runnable() {
		public void run() {
			g.drawPolyline(LINE);
		};
	}, tests);
}

public void testLineJoinCapAA() {
	g.setAntialias(SWT.ON);
	testLineJoinCap();
}


}
