/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
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
package org.eclipse.draw2d.examples;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FlowLayout;
import org.eclipse.draw2d.GroupBoxBorder;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.ToolbarLayout;

/**
 * A factory for figures. Created on :Sep 26, 2002
 *
 * @author hudsonr
 * @since 2.0
 */
public final class ExampleUtil {

	static final Font FONT_LARGE = new Font(null, "Arial", 16, SWT.BOLD); //$NON-NLS-1$

	public static IFigure createFlowLayoutPanel() {
		Figure f = new Figure();

		f.setBorder(new GroupBoxBorder("FlowLayout")); //$NON-NLS-1$
		f.setLayoutManager(new FlowLayout());

		for (int i = 0; i < 5; i++) {
			f.add(new Label("Label " + i, ExampleImages.GEORGE)); //$NON-NLS-1$
		}

//	Label l = new Label("A really wide label is here");
//	l.setBorder(new LineBorder());
//	l.setFont(FONT_LARGE);
//	f.add(l);

		for (int i = 6; i < 10; i++) {
			f.add(new Label("Label " + i, ExampleImages.GEORGE)); //$NON-NLS-1$
		}

		return f;
	}

	public static IFigure createToolbarLayout() {
		Figure f = new Figure();
		f.setBorder(new GroupBoxBorder("Toolbar")); //$NON-NLS-1$
		f.setLayoutManager(new ToolbarLayout());
		f.add(createFlowLayoutPanel());
		f.add(createFlowLayoutPanel());
		return f;
	}

}
