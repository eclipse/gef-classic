/*******************************************************************************
 * Copyright (c) 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Stephane Lizeray slizeray@ilog.fr - initial API and implementation
 *******************************************************************************/
package org.eclipse.draw2d.examples.path;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.draw2d.BorderLayout;
import org.eclipse.draw2d.ButtonBorder;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LightweightSystem;
import org.eclipse.draw2d.SchemeBorder;
import org.eclipse.draw2d.ScrollBar;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class PathExample {

	public static void main(String[] args) {
		Display d = new Display();
		final Shell shell = new Shell(d, SWT.SHELL_TRIM | SWT.NO_BACKGROUND
				| SWT.NO_REDRAW_RESIZE);
		shell.setSize(800, 800);
		LightweightSystem lws = new LightweightSystem(shell);
		Figure fig = new Figure();

		fig.setLayoutManager(new ToolbarLayout());

		final ScrollBar bar = new ScrollBar();
		final Label l = new Label("«Zoom»");

		l.setBorder(new SchemeBorder(ButtonBorder.SCHEMES.BUTTON_SCROLLBAR));
		bar.setThumb(l);
		bar.setHorizontal(true);
		bar.setMaximum(200);
		bar.setMinimum(0);
		bar.setExtent(25);
		fig.add(bar);

		final ZoomFigure zoomFigure = new ZoomFigure();
		zoomFigure.setPreferredSize(500, 800);

		fig.add(zoomFigure);

		zoomFigure.setLayoutManager(new BorderLayout());
		zoomFigure.setScale(1);

		PathFigure polyline = new PathFigure();
		float w = 50;
		float k = 50;
		polyline.addPoint(new Point(0 + k, 0 + k));
		polyline.addPoint(new Point(w + k, 0 + k));
		polyline.addPoint(new Point(w + k, w + k));
		polyline.addPoint(new Point(0 + k, w + k));
		polyline.setLineWidth(3);
		polyline.setBackgroundColor(ColorConstants.red);
		polyline.setClosed(true);

		Point c = polyline.getBounds().getCenter();
		polyline.setRotation(c.x(), c.y(), 45);
		zoomFigure.add(polyline, BorderLayout.CENTER);

		bar.addPropertyChangeListener("value", new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				float z = (bar.getValue() + 50) * 0.02f;
				zoomFigure.setScale(z);
			}
		});

		lws.setContents(fig);
		shell.open();
		while (!shell.isDisposed())
			while (!d.readAndDispatch())
				d.sleep();

	}
}
