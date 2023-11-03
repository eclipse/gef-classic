/*******************************************************************************
 * Copyright (c) 2005, 2023 IBM Corporation and others.
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
package org.eclipse.draw2d.examples.layouts;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Ellipse;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FlowLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.OrderedLayout;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.RoundedRectangle;
import org.eclipse.draw2d.Triangle;
import org.eclipse.draw2d.examples.AbstractExample;

/**
 * @author hudsonr Created on Apr 30, 2003
 */
public class FlowLayoutExample extends AbstractExample {

	FlowLayout layout;
	private Ellipse ellipse1;
	private RectangleFigure rect1;
	private RoundedRectangle rect2;
	private RectangleFigure rect3;
	private Ellipse ellipse2;
	private Triangle triangle1;
	private RoundedRectangle rect4;
	private RectangleFigure rect5;
	private Triangle triangle2;
	private Ellipse ellipse3;
	private RoundedRectangle rect6;

	public static void main(String[] args) {
		new FlowLayoutExample().run();
	}

	/**
	 * @see org.eclipse.draw2d.examples.AbstractExample#createContents()
	 */
	@Override
	protected IFigure createContents() {
		Figure container = new Figure();
		container.setBorder(new LineBorder());
		container.setLayoutManager(layout = new FlowLayout());

		ellipse1 = new Ellipse();
		ellipse1.setBackgroundColor(ColorConstants.blue);
		ellipse1.setSize(60, 40);
		container.add(ellipse1);

		rect1 = new RectangleFigure();
		rect1.setBackgroundColor(ColorConstants.red);
		rect1.setSize(30, 70);
		container.add(rect1);

		rect2 = new RoundedRectangle();
		rect2.setBackgroundColor(ColorConstants.yellow);
		rect2.setSize(90, 30);
		container.add(rect2);

		rect3 = new RectangleFigure();
		rect3.setBackgroundColor(ColorConstants.gray);
		rect3.setSize(50, 80);
		container.add(rect3);

		ellipse2 = new Ellipse();
		ellipse2.setBackgroundColor(ColorConstants.green);
		ellipse2.setSize(50, 50);
		container.add(ellipse2);

		triangle1 = new Triangle();
		triangle1.setBackgroundColor(ColorConstants.black);
		triangle1.setSize(50, 50);
		container.add(triangle1);

		rect4 = new RoundedRectangle();
		rect4.setBackgroundColor(ColorConstants.cyan);
		rect4.setSize(50, 50);
		container.add(rect4);

		rect5 = new RectangleFigure();
		rect5.setBackgroundColor(ColorConstants.darkGreen);
		rect5.setSize(50, 70);
		container.add(rect5);

		triangle2 = new Triangle();
		triangle2.setBackgroundColor(ColorConstants.orange);
		triangle2.setSize(50, 50);
		container.add(triangle2);

		ellipse3 = new Ellipse();
		ellipse3.setBackgroundColor(ColorConstants.red);
		ellipse3.setSize(50, 50);
		container.add(ellipse3);

		rect6 = new RoundedRectangle();
		rect6.setBackgroundColor(ColorConstants.yellow);
		rect6.setSize(50, 50);
		container.add(rect6);

		return container;
	}

	/**
	 * @see org.eclipse.draw2d.examples.AbstractExample#hookShell(Shell shell)
	 */
	@Override
	protected void hookShell(Shell shell) {
		Composite composite = new Composite(shell, 0);
		composite.setLayoutData(new GridData(GridData.FILL_VERTICAL));

		composite.setLayout(new GridLayout());

		final Button horizontal = new Button(composite, SWT.CHECK);
		horizontal.setText("Horizontal"); //$NON-NLS-1$
		horizontal.setSelection(true);
		horizontal.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				layout.setHorizontal(!layout.isHorizontal());
				resetShapes();
				getContents().revalidate();
				shell.layout(true);
			}
		});
		final Button stretch = new Button(composite, SWT.CHECK);
		stretch.setText("Stretch Minor Axis"); //$NON-NLS-1$
		stretch.setSelection(false);
		stretch.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				layout.setStretchMinorAxis(stretch.getSelection());
				resetShapes();
				getContents().revalidate();
				shell.layout(true);
			}
		});
		{
			Group major = new Group(composite, 0);
			major.setLayout(new FillLayout(SWT.VERTICAL));
			major.setText("Major Axis"); //$NON-NLS-1$

			Button left = new Button(major, SWT.RADIO);
			left.setText("Top/Left"); //$NON-NLS-1$
			left.setSelection(true);
			left.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					layout.setMajorAlignment(FlowLayout.ALIGN_TOPLEFT);
					getContents().revalidate();
				}
			});

			Button center = new Button(major, SWT.RADIO);
			center.setText("Middle/Center"); //$NON-NLS-1$
			center.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					layout.setMajorAlignment(FlowLayout.ALIGN_CENTER);
					getContents().revalidate();
				}
			});

			Button right = new Button(major, SWT.RADIO);
			right.setText("Buttom/Right"); //$NON-NLS-1$
			right.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					layout.setMajorAlignment(FlowLayout.ALIGN_BOTTOMRIGHT);
					getContents().revalidate();
				}
			});

			final Scale spacing = new Scale(major, 0);
			spacing.setMinimum(0);
			spacing.setMaximum(20);
			spacing.setSelection(5);
			spacing.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					layout.setMajorSpacing(spacing.getSelection());
					getContents().revalidate();
				}
			});
			Label spacingLabel = new Label(major, SWT.CENTER);
			spacingLabel.setText("Spacing"); //$NON-NLS-1$
		}

		{

			Group minor = new Group(composite, 0);
			minor.setLayout(new FillLayout(SWT.VERTICAL));
			minor.setText("minor axis"); //$NON-NLS-1$

			Button left = new Button(minor, SWT.RADIO);
			left.setText("Top/Left"); //$NON-NLS-1$
			left.setSelection(true);
			left.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					layout.setMinorAlignment(OrderedLayout.ALIGN_TOPLEFT);
					getContents().revalidate();
				}
			});

			Button center = new Button(minor, SWT.RADIO);
			center.setText("Middle/Center"); //$NON-NLS-1$
			center.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					layout.setMinorAlignment(FlowLayout.ALIGN_CENTER);
					getContents().revalidate();
				}
			});

			Button right = new Button(minor, SWT.RADIO);
			right.setText("Buttom/Right"); //$NON-NLS-1$
			right.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					layout.setMinorAlignment(OrderedLayout.ALIGN_BOTTOMRIGHT);
					getContents().revalidate();
				}
			});

			final Scale spacing = new Scale(minor, 0);
			spacing.setMinimum(0);
			spacing.setMaximum(20);
			spacing.setSelection(5);
			spacing.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					layout.setMinorSpacing(spacing.getSelection());
					getContents().revalidate();
				}
			});
			Label spacingLabel = new Label(minor, SWT.CENTER);
			spacingLabel.setText("Spacing"); //$NON-NLS-1$
		}
	}

	private void resetShapes() {
		ellipse1.setSize(60, 40);
		rect1.setSize(30, 70);
		rect2.setSize(90, 30);
		rect3.setSize(50, 80);
		ellipse2.setSize(50, 50);
		triangle1.setSize(50, 50);
		rect4.setSize(50, 50);
		rect5.setSize(50, 70);
		triangle2.setSize(50, 50);
		ellipse3.setSize(50, 50);
		rect6.setSize(50, 50);
	}
}
