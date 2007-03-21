/*******************************************************************************
 * Copyright (c) 2004, 2007 Asim Ullah and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Asim Ullah - initial implementation [Sep 13, 2004]
 *******************************************************************************/
package org.eclipse.draw2d.examples.layouts;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Ellipse;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.GridData;
import org.eclipse.draw2d.GridLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseListener;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.RoundedRectangle;
import org.eclipse.draw2d.Shape;
import org.eclipse.draw2d.Triangle;
import org.eclipse.draw2d.examples.AbstractExample;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

/**
 * GridLayout Example that allows for the dynamic creation of child Shape
 * objects, as well as the modification of their layout properties (via
 * GridData). N.b You can mouse select a Shape to view it's details in the
 * GridData Group area. Also note when modifiying a value in a Text widget you
 * need to press RETURN after you enter the value to get the update. The Text
 * widget won't update the view by TABbing!
 */
public class GridLayoutExample extends AbstractExample {

	Figure container;
	Composite composite;
	GridLayout gridLayout;

	// for the GridData Group
	Text widthHint, heightHint, horizIndent, vertSpan, horizSpan;
	Combo vertAlign, horizAlign;
	Button grabVSpace, grabHSpace;

	// SWT layout references.
	org.eclipse.swt.layout.GridLayout _layout;
	org.eclipse.swt.layout.GridData _data;

	Shape selectedShape = null, prevSelectedShape = null;
	int shapeCount = 0;
	int colorCount = 0;
	Color colors[] = new Color[] { ColorConstants.blue, ColorConstants.red,
			ColorConstants.yellow, ColorConstants.gray, ColorConstants.green,
			ColorConstants.lightBlue, ColorConstants.cyan,
			ColorConstants.darkGreen, ColorConstants.orange };

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new GridLayoutExample().run();
	}

	/**
	 * @see org.eclipse.draw2d.examples.AbstractExample#getContents()
	 */
	protected IFigure getContents() {
		container = new Figure();
		container.setBorder(new LineBorder());

		gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		gridLayout.makeColumnsEqualWidth = false;

		container.setLayoutManager(gridLayout);
		container.addMouseListener(new MouseListener.Stub() {
			public void mousePressed(MouseEvent event) {
				if (selectedShape != null)
					prevSelectedShape.setLineWidth(1);
				selectedShape = prevSelectedShape = null;
				setEnableGridDataGroup(false);
			}
		});

		Shape shape = createShape();
		container.add(shape);
		container.add(createShape());

		return container;
	}

	protected Shape createShape() {
		Shape shape = null;
		if (shapeCount == 0)
			shape = new Ellipse();
		else if (shapeCount == 1)
			shape = new RectangleFigure();
		else if (shapeCount == 2)
			shape = new RoundedRectangle();
		else if (shapeCount == 3)
			shape = new RectangleFigure();
		else if (shapeCount == 4)
			shape = new Ellipse();
		else if (shapeCount == 5)
			shape = new Triangle();
		else if (shapeCount == 6)
			shape = new RoundedRectangle();
		else if (shapeCount == 7)
			shape = new Triangle();
		else
			shape = new RoundedRectangle();

		shape.setBackgroundColor(colors[colorCount]);
		shape.setSize(70, 70);

		shape.addMouseListener(new MouseListener.Stub() {
			public void mousePressed(MouseEvent event) {
				selectedShape = (Shape) event.getSource();
				setEnableGridDataGroup(true);
				selectedShape.setLineWidth(4);
				if (prevSelectedShape != null
						&& prevSelectedShape != selectedShape)
					prevSelectedShape.setLineWidth(1);

				GridData d = (GridData) gridLayout.getConstraint(selectedShape);
				populateGridDataGroup(d);

				prevSelectedShape = selectedShape;
			}
		});

		if (shapeCount == 8) // reset shapes and colors
			shapeCount = colorCount = 0;
		else {
			shapeCount++;
			colorCount++;
		}
		return shape;
	}

	protected Integer getEventValue(Event e, String id) {
		int val = 1;
		try {
			if (e.widget instanceof Text) {
				val = Integer.parseInt(((Text) e.widget).getText());
			}
		} catch (NumberFormatException ex) {
			System.out.println("Error: Invalid Number entered for " + id);
		}
		return new Integer(val);
	}

	protected Integer getEventValue(SelectionEvent e, String id) {
		int val = 5;
		try {
			if (e.widget instanceof Combo) {
				val = Integer.parseInt(((Combo) e.widget).getText());
			}
		} catch (NumberFormatException ex) {
			System.out.println("Error: Invalid Number entered for " + id);
		}
		return new Integer(val);
	}

	protected void updateView() {
		contents.revalidate();
		shell.pack();
	}

	protected void createColumnGroup() {
		// Columns section
		Group columnsGroup = new Group(composite, 0);
		columnsGroup.setText("Columns");

		_layout = new org.eclipse.swt.layout.GridLayout();
		_layout.numColumns = 2;
		columnsGroup.setLayout(_layout);

		_data = new org.eclipse.swt.layout.GridData(
				org.eclipse.swt.layout.GridData.VERTICAL_ALIGN_FILL);
		columnsGroup.setLayoutData(_data);
		Text numColumns = new Text(columnsGroup, SWT.BORDER);
		numColumns.setText("2");
		numColumns.addListener(SWT.DefaultSelection, new Listener() {
			public void handleEvent(Event e) {
				gridLayout.numColumns = getEventValue(e, "numColumns")
						.intValue();
				updateView();
			}
		});
		_data = new org.eclipse.swt.layout.GridData();
		_data.widthHint = 15;
		numColumns.setLayoutData(_data);
		new Label(columnsGroup, SWT.NONE).setText("numColumns");

		Button makeColumnsEqualWidth = new Button(columnsGroup, SWT.CHECK);
		makeColumnsEqualWidth.setText("makeColumnsEqualWidth");
		makeColumnsEqualWidth.setSelection(false);
		makeColumnsEqualWidth.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				gridLayout.makeColumnsEqualWidth = !gridLayout.makeColumnsEqualWidth;
				updateView();
			}
		});
		_data = new org.eclipse.swt.layout.GridData();
		_data.horizontalSpan = 2;
		_data.horizontalIndent = 14;
		makeColumnsEqualWidth.setLayoutData(_data);
	}

	protected void createMarginGroup() {
		// Margins and Spacing section

		Group marginGroup = new Group(composite, 0);
		marginGroup.setLayout(new FillLayout(SWT.VERTICAL));
		marginGroup.setText("Margins and Spacing");

		String[] marginValues = new String[] { "0", "3", "5", "10" };

		_data = new org.eclipse.swt.layout.GridData();
		_data.verticalSpan = 2;
		marginGroup.setLayoutData(_data);
		_layout = new org.eclipse.swt.layout.GridLayout();
		_layout.numColumns = 2;
		marginGroup.setLayout(_layout);
		new Label(marginGroup, SWT.NONE).setText("marginHeight");
		Combo marginHeight = new Combo(marginGroup, SWT.NONE);
		marginHeight.setItems(marginValues);
		marginHeight.select(2);
		_data = new org.eclipse.swt.layout.GridData();
		_data.widthHint = 60;
		marginHeight.setLayoutData(_data);

		marginHeight.addListener(SWT.DefaultSelection, new Listener() {
			public void handleEvent(Event e) {
				gridLayout.marginHeight = getEventValue(e, "marginHeight")
						.intValue();
				updateView();
			};
		});
		marginHeight.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				gridLayout.marginHeight = getEventValue(e, "marginHeight")
						.intValue();
				updateView();
			}
		});

		new Label(marginGroup, SWT.NONE).setText("marginWidth");
		Combo marginWidth = new Combo(marginGroup, SWT.NONE);
		marginWidth.setItems(marginValues);
		marginWidth.select(2);
		_data = new org.eclipse.swt.layout.GridData();
		_data.widthHint = 60;
		marginWidth.setLayoutData(_data);

		marginWidth.addListener(SWT.DefaultSelection, new Listener() {
			public void handleEvent(Event e) {
				gridLayout.marginWidth = getEventValue(e, "marginWidth")
						.intValue();
				updateView();
			};
		});
		marginWidth.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				gridLayout.marginWidth = getEventValue(e, "marginWidth")
						.intValue();
				updateView();
			}
		});

		new Label(marginGroup, SWT.NONE).setText("horizontalSpacing");
		Combo horizontalSpacing = new Combo(marginGroup, SWT.NONE);
		horizontalSpacing.setItems(marginValues);
		horizontalSpacing.select(2);
		_data = new org.eclipse.swt.layout.GridData();
		_data.widthHint = 60;
		horizontalSpacing.setLayoutData(_data);

		horizontalSpacing.addListener(SWT.DefaultSelection, new Listener() {
			public void handleEvent(Event e) {
				gridLayout.horizontalSpacing = getEventValue(e,
						"horizontalSpacing").intValue();
				updateView();
			};
		});
		horizontalSpacing.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				gridLayout.horizontalSpacing = getEventValue(e,
						"horizontalSpacing").intValue();
				updateView();
			}
		});

		new Label(marginGroup, SWT.NONE).setText("verticalSpacing");
		Combo verticalSpacing = new Combo(marginGroup, SWT.NONE);
		verticalSpacing.setItems(marginValues);
		verticalSpacing.select(2);
		_data = new org.eclipse.swt.layout.GridData();
		_data.widthHint = 60;
		verticalSpacing.setLayoutData(_data);

		verticalSpacing.addListener(SWT.DefaultSelection, new Listener() {
			public void handleEvent(Event e) {
				gridLayout.verticalSpacing = getEventValue(e, "verticalSpacing")
						.intValue();
				updateView();
			};
		});

		verticalSpacing.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				gridLayout.verticalSpacing = getEventValue(e, "verticalSpacing")
						.intValue();
				;
				updateView();
			}
		});

	}

	protected void createChildGroup() {
		Group childGroup;
		Button add, clear;

		childGroup = new Group(composite, SWT.NONE);
		childGroup.setText("Children");
		org.eclipse.swt.layout.GridLayout layout = new org.eclipse.swt.layout.GridLayout();
		layout.numColumns = 3;
		childGroup.setLayout(layout);
		org.eclipse.swt.layout.GridData data = new org.eclipse.swt.layout.GridData(
				org.eclipse.swt.layout.GridData.FILL_HORIZONTAL);
		data.horizontalSpan = 2;
		childGroup.setLayoutData(data);

		/* Controls for adding and removing children */
		add = new Button(childGroup, SWT.PUSH);
		add.setText("Add");
		add.setLayoutData(new org.eclipse.swt.layout.GridData(
				org.eclipse.swt.layout.GridData.FILL_HORIZONTAL));
		add.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				container.add(createShape());
				updateView();
			}
		});

		clear = new Button(childGroup, SWT.PUSH);
		clear.setText("Clear All");
		clear.setLayoutData(new org.eclipse.swt.layout.GridData(
				org.eclipse.swt.layout.GridData.FILL_HORIZONTAL));
		clear.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				container.removeAll();
				setEnableGridDataGroup(false);
				updateView();
			}
		});

	}

	protected void createGridDataGroup() {
		String[] alignValues = new String[] { "BEGINNING", "CENTER", "END",
				"FILL" };
		Group gridDataGroup;

		gridDataGroup = new Group(composite, SWT.NONE);
		gridDataGroup.setText("Selected Figure's GridData");

		_layout = new org.eclipse.swt.layout.GridLayout();
		_layout.numColumns = 2;
		gridDataGroup.setLayout(_layout);

		_data = new org.eclipse.swt.layout.GridData(
				org.eclipse.swt.layout.GridData.VERTICAL_ALIGN_FILL);
		gridDataGroup.setLayoutData(_data);

		// widthHint --------------------------------------
		widthHint = new Text(gridDataGroup, SWT.BORDER);
		widthHint.setText("-1");
		widthHint.addListener(SWT.DefaultSelection, new Listener() {
			public void handleEvent(Event e) {
				if (selectedShape == null)
					return;
				GridData d = (GridData) gridLayout.getConstraint(selectedShape);
				d.widthHint = getEventValue(e, "widthHint").intValue();
				updateView();
			}
		});
		_data = new org.eclipse.swt.layout.GridData();
		_data.widthHint = 35;
		widthHint.setLayoutData(_data);
		new Label(gridDataGroup, SWT.NONE).setText("widthHint");

		// heightHint ------------------------------------
		heightHint = new Text(gridDataGroup, SWT.BORDER);
		heightHint.setText("-1");
		heightHint.addListener(SWT.DefaultSelection, new Listener() {
			public void handleEvent(Event e) {
				if (selectedShape == null)
					return;
				GridData d = (GridData) gridLayout.getConstraint(selectedShape);
				d.heightHint = getEventValue(e, "heightHint").intValue();
				updateView();
			}
		});
		_data = new org.eclipse.swt.layout.GridData();
		_data.widthHint = 35;
		heightHint.setLayoutData(_data);
		new Label(gridDataGroup, SWT.NONE).setText("heightHint");

		// horizontalAlignment ------------------------------------
		horizAlign = new Combo(gridDataGroup, SWT.READ_ONLY);
		horizAlign.setItems(alignValues);
		horizAlign.select(0);
		_data = new org.eclipse.swt.layout.GridData();
		_data.widthHint = 60;
		horizAlign.setLayoutData(_data);

		horizAlign.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (selectedShape == null)
					return;
				GridData d = (GridData) gridLayout.getConstraint(selectedShape);
				String align = ((Combo) e.widget).getText();
				d.horizontalAlignment = findAlignment(align);
				updateView();
			}
		});
		new Label(gridDataGroup, SWT.NONE).setText("horizontalAlignment");

		// verticalAlignment ------------------------------------
		vertAlign = new Combo(gridDataGroup, SWT.READ_ONLY);
		vertAlign.setItems(alignValues);
		vertAlign.select(0);
		_data = new org.eclipse.swt.layout.GridData();
		_data.widthHint = 60;
		vertAlign.setLayoutData(_data);

		vertAlign.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (selectedShape == null)
					return;
				GridData d = (GridData) gridLayout.getConstraint(selectedShape);
				String align = ((Combo) e.widget).getText();
				d.verticalAlignment = findAlignment(align);
				updateView();
			}
		});
		new Label(gridDataGroup, SWT.NONE).setText("verticalAlignment");

		// horizontalIndent ------------------------------------
		horizIndent = new Text(gridDataGroup, SWT.BORDER);
		horizIndent.setText("0");
		horizIndent.addListener(SWT.DefaultSelection, new Listener() {
			public void handleEvent(Event e) {
				if (selectedShape == null)
					return;
				GridData d = (GridData) gridLayout.getConstraint(selectedShape);
				d.horizontalIndent = getEventValue(e, "horizontalIndent")
						.intValue();
				updateView();
			}
		});
		_data = new org.eclipse.swt.layout.GridData();
		_data.widthHint = 35;
		horizIndent.setLayoutData(_data);
		new Label(gridDataGroup, SWT.NONE).setText("horizontalIndent");

		// horizontalSpan ------------------------------------
		horizSpan = new Text(gridDataGroup, SWT.BORDER);
		horizSpan.setText("1");
		horizSpan.addListener(SWT.DefaultSelection, new Listener() {
			public void handleEvent(Event e) {
				if (selectedShape == null)
					return;
				GridData d = (GridData) gridLayout.getConstraint(selectedShape);
				d.horizontalSpan = getEventValue(e, "horizontalSpan")
						.intValue();
				updateView();
			}
		});
		_data = new org.eclipse.swt.layout.GridData();
		_data.widthHint = 35;
		horizSpan.setLayoutData(_data);
		new Label(gridDataGroup, SWT.NONE).setText("horizontalSpan");

		// verticalSpan ------------------------------------
		vertSpan = new Text(gridDataGroup, SWT.BORDER);
		vertSpan.setText("1");
		vertSpan.addListener(SWT.DefaultSelection, new Listener() {
			public void handleEvent(Event e) {
				if (selectedShape == null)
					return;
				GridData d = (GridData) gridLayout.getConstraint(selectedShape);
				d.verticalSpan = getEventValue(e, "verticalSpan").intValue();
				updateView();
			}
		});
		_data = new org.eclipse.swt.layout.GridData();
		_data.widthHint = 35;
		vertSpan.setLayoutData(_data);
		new Label(gridDataGroup, SWT.NONE).setText("verticalSpan");

		// grabExcessHorizontalSpace ------------------------------------

		grabHSpace = new Button(gridDataGroup, SWT.CHECK);
		grabHSpace.setText("grabExcessHorizontalSpace");
		grabHSpace.setSelection(false);
		grabHSpace.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (selectedShape == null)
					return;
				GridData d = (GridData) gridLayout.getConstraint(selectedShape);
				d.grabExcessHorizontalSpace = !d.grabExcessHorizontalSpace;
				updateView();
			}
		});
		_data = new org.eclipse.swt.layout.GridData();
		_data.horizontalSpan = 2;
		_data.horizontalIndent = 14;
		grabHSpace.setLayoutData(_data);

		// grabExcessVerticalSpace ------------------------------------

		grabVSpace = new Button(gridDataGroup, SWT.CHECK);
		grabVSpace.setText("grabExcessVerticalSpace");
		grabVSpace.setSelection(false);
		grabVSpace.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (selectedShape == null)
					return;
				GridData d = (GridData) gridLayout.getConstraint(selectedShape);
				d.grabExcessVerticalSpace = !d.grabExcessVerticalSpace;
				updateView();
			}
		});
		_data = new org.eclipse.swt.layout.GridData();
		_data.horizontalSpan = 2;
		_data.horizontalIndent = 14;
		grabVSpace.setLayoutData(_data);

		setEnableGridDataGroup(false);
	}

	protected void setEnableGridDataGroup(boolean enable) {
		widthHint.setEnabled(enable);
		heightHint.setEnabled(enable);
		horizIndent.setEnabled(enable);
		vertSpan.setEnabled(enable);
		horizSpan.setEnabled(enable);
		vertAlign.setEnabled(enable);
		horizAlign.setEnabled(enable);
		grabVSpace.setEnabled(enable);
		grabHSpace.setEnabled(enable);
	}

	protected void populateGridDataGroup(GridData d) {
		widthHint.setText(Integer.toString(d.widthHint));
		heightHint.setText(Integer.toString(d.heightHint));
		horizIndent.setText(Integer.toString(d.horizontalIndent));
		vertSpan.setText(Integer.toString(d.verticalSpan));
		horizSpan.setText(Integer.toString(d.horizontalSpan));
		vertAlign.select(findAlignment(d.verticalAlignment));
		horizAlign.select(findAlignment(d.horizontalAlignment));
		grabVSpace.setSelection(d.grabExcessVerticalSpace);
		grabHSpace.setSelection(d.grabExcessHorizontalSpace);
	}

	protected int findAlignment(int align) {
		switch (align) {
		case SWT.BEGINNING:
			return 0; // these are the index into the align combo
		case SWT.CENTER:
			return 1;
		case SWT.END:
			return 2;
		case SWT.FILL:
			return 3;
		default:
			break;
		}
		return -1;
	}

	protected int findAlignment(String align) {
		if (align.equals("BEGINNING")) {
			return SWT.BEGINNING;
		} else if (align.equals("CENTER")) {
			return SWT.CENTER;
		} else if (align.equals("END")) {
			return SWT.END;
		} else if (align.equals("FILL")) {
			return SWT.FILL;
		}
		return -1;
	}

	/**
	 * @see org.eclipse.draw2d.examples.AbstractExample#hookShell()
	 */
	protected void hookShell() {
		composite = new Composite(shell, 0);
		_data = new org.eclipse.swt.layout.GridData(
				org.eclipse.swt.layout.GridData.FILL_VERTICAL);
		_data.widthHint = 300;
		composite.setLayoutData(_data);
		composite.setLayout(new org.eclipse.swt.layout.GridLayout());

		createColumnGroup();
		createMarginGroup();
		createChildGroup();
		createGridDataGroup();
	}

}