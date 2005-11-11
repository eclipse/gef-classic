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

import java.util.Stack;

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
import org.eclipse.swt.graphics.Path;
import org.eclipse.swt.graphics.Pattern;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Resource;
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
private Path path1;
private Path path2;
private Stack resources = new Stack();

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
				//HACK, image operations seem to differ by as much as 4
				if (Math.abs(rgb1.red - rgb2.red) > 4
						| Math.abs(rgb1.green - rgb2.green) > 4
						| Math.abs(rgb1.blue - rgb2.blue) > 4)
				assertEquals("Discrepancy at coordinates <" + x +", " + y + ">",
						rgb1,
						rgb2);
			}
		}
}

private void displayImage() {
	final Shell shell = new Shell(SWT.DIALOG_TRIM);
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
	path1 = new Path(null);
	path1.moveTo(20, 5);
	path1.quadTo(40, 5, 50, 25);
	path1.quadTo(20, 25, 20, 45);
	path1.lineTo(0, 25);
	path1.close();
	
	path2 = new Path(null);
	path2.moveTo(15, 30);
	path2.cubicTo(50, 0, 50, 30, 20, 60);
	path2.close();

	image = new Image(Display.getDefault(), 800, 600);
	imageGC = new GC(image, SWT.NONE);
	g = new SWTGraphics(imageGC);
	
	resources.push(path1);
	resources.push(path2);
	resources.push(image);
	resources.push(imageGC);
}

protected void tearDown() throws Exception {
	g.dispose();
	while (!resources.isEmpty())
		((Resource)resources.pop()).dispose();
}

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
	}

	g.setLineWidth(9);
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
		}
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
			g.setAntialias(aa);
		}
	}
	g.setBackgroundColor(ColorConstants.red);
	g.pushState();

	Runnable tests[] = new Runnable[3];
	tests[0] = new FillRules(SWT.FILL_EVEN_ODD, SWT.ON);
	tests[1] = new FillRules(SWT.FILL_WINDING, SWT.OFF);
	tests[2] = new FillRules(SWT.FILL_EVEN_ODD, SWT.DEFAULT);
	performTestcase(new Runnable() {
		public void run() {
			g.fillPolygon(POLY);
		}
	}, tests);
}

//public void testInterpolation() {
//	class InterpolationSettings implements Runnable {
//		private final int level;
//		InterpolationSettings (int level) {
//			this.level = level;
//		}
//		public void run() {
//			g.setInterpolation(level);
//		}
//	}
//
//	g.pushState();
//
//	Runnable tests[] = new Runnable[4];
//	tests[0] = new InterpolationSettings(SWT.HIGH);
//	tests[1] = new InterpolationSettings(SWT.LOW);
//	tests[2] = new InterpolationSettings(SWT.NONE);
//	tests[3] = new InterpolationSettings(SWT.DEFAULT);
//	performTestcase(new Runnable() {
//		public void run() {
//			g.drawImage(TestImages.depth_24, 0, 0, 400, 400, 0, 0, 75, 75);
//		}
//	}, tests);
//}

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
	}
	
	g.setLineWidth(9);
	g.pushState();

	Runnable tests[] = new Runnable[3];
	tests[0] = new LineSettings(SWT.JOIN_ROUND, SWT.CAP_ROUND, SWT.LINE_DASH);
	tests[1] = new LineSettings(SWT.JOIN_BEVEL, SWT.CAP_FLAT, SWT.LINE_DOT);
	tests[2] = new LineSettings(SWT.JOIN_ROUND, SWT.CAP_SQUARE, SWT.LINE_SOLID);
	performTestcase(new Runnable() {
		public void run() {
			g.drawPolyline(LINE);
		}
	}, tests);
}

public void testLineJoinCapAA() {
	g.setAntialias(SWT.ON);
	testLineJoinCap();
}

//public void testPathDraw() {
//	
//	class PathSettings implements Runnable {
//		private final int antialias;
//		private final Color color;
//		private final int style;
//		private final int width;
//		PathSettings(int antialias, int width, int style, Color color) {
//			this.antialias = antialias;
//			this.width = width;
//			this.style = style;
//			this.color = color;
//		}
//		public void run() {
//			g.setAntialias(antialias);
//			g.setLineWidth(width);
//			g.setLineStyle(style);
//			g.setForegroundColor(color);
//		}
//	}
//	
//	g.setBackgroundColor(ColorConstants.darkBlue);
//	
//	Runnable tests[] = new Runnable[5];
//	tests[0] = new PathSettings(SWT.ON, 3, SWT.LINE_SOLID, ColorConstants.darkBlue);
//	tests[1] = new PathSettings(SWT.OFF, 0, SWT.LINE_DOT, ColorConstants.red);
//	tests[2] = new PathSettings(SWT.DEFAULT, 1, SWT.LINE_DOT, ColorConstants.darkBlue);
//	tests[3] = new PathSettings(SWT.DEFAULT, 2, SWT.LINE_DOT, ColorConstants.darkGreen);
//	tests[4] = new PathSettings(SWT.ON, 2, SWT.LINE_DASHDOTDOT, ColorConstants.black);
//	performTestcase(new Runnable() {
//		public void run() {
//			g.drawPath(path1);
//			g.drawPath(path2);
//		}
//	}, tests);
//	
//	path1.dispose();
//}

//public void testPathFill() {
//	
//	class PathSettings implements Runnable {
//		private final int antialias;
//		private final int alpha;
//		PathSettings(int antialias, int alpha) {
//			this.antialias = antialias;
//			this.alpha = alpha;
//		}
//		public void run() {
//			g.setAntialias(antialias);
//			g.setAlpha(alpha);
//		}
//	}
//	
//	g.setBackgroundColor(ColorConstants.darkBlue);
//	
//	Runnable tests[] = new Runnable[4];
//	tests[0] = new PathSettings(SWT.ON, 200);
//	tests[1] = new PathSettings(SWT.OFF, 100);
//	tests[2] = new PathSettings(SWT.DEFAULT, 200);
//	tests[3] = new PathSettings(SWT.ON, 150);
//	performTestcase(new Runnable() {
//		public void run() {
//			g.setFillRule(SWT.FILL_EVEN_ODD);
//			g.fillPath(path1);
//			g.fillPath(path2);
//		}
//	}, tests);
//	
//	path1.dispose();
//}

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
	}
	
	//Initial state
	Font f = new Font(null, "Helvetica", 50, SWT.BOLD);
	resources.push(f);
	g.setFont(f);
	g.setBackgroundColor(ColorConstants.yellow);
	g.pushState();
	
	Pattern gradient = new Pattern(null, 0, 0, 80, 40, ColorConstants.gray, ColorConstants.black);
	Pattern image = new Pattern(null, TestImages.depth_24);
	
	resources.push(gradient);
	resources.push(image);
	
	Runnable tests[] = new Runnable[1];
	tests[0] = new SetPattern(image, gradient);
	performTestcase(new Runnable() {
		public void run() {
			g.fillText("W", 0, 0);
		}
	}, tests);
	
}

}