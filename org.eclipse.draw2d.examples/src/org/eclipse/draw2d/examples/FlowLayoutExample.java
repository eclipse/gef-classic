package org.eclipse.draw2d.examples;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.widgets.Button;

import org.eclipse.draw2d.*;

/**
 * @author hudsonr
 * Created on Apr 30, 2003
 */
public class FlowLayoutExample extends AbstractExample {

FlowLayout layout;

public static void main(String[] args) {
	new FlowLayoutExample().run();
}

/**
 * @see org.eclipse.draw2d.examples.AbstractExample#getContents()
 */
protected IFigure getContents() {
	Figure container = new Figure();
	container.setBorder(new LineBorder());
	container.setLayoutManager(layout = new FlowLayout());
	
	Shape shape;
	shape = new Ellipse();
	shape.setBackgroundColor(ColorConstants.blue);
	shape.setSize(60,40);
	container.add(shape);

	shape = new RectangleFigure();
	shape.setBackgroundColor(ColorConstants.red);
	shape.setSize(30,70);
	container.add(shape);

	shape = new RoundedRectangle();
	shape.setBackgroundColor(ColorConstants.yellow);
	shape.setSize(90,30);
	container.add(shape);

	shape = new RectangleFigure();
	shape.setBackgroundColor(ColorConstants.gray);
	shape.setSize(50,80);
	container.add(shape);

	shape = new Ellipse();
	shape.setBackgroundColor(ColorConstants.green);
	shape.setSize(50,50);
	container.add(shape);
	
	return container;
}

/**
 * @see org.eclipse.draw2d.examples.AbstractExample#hookShell()
 */
protected void hookShell() {
	Composite composite = new Composite(shell, 0);
	composite.setLayoutData(new GridData(GridData.FILL_VERTICAL));
	
	composite.setLayout(new GridLayout());
	
	final Button horizontal = new Button(composite, SWT.CHECK);
	horizontal.setText("Horizontal");
	horizontal.setSelection(true);
	horizontal.addSelectionListener(new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			layout.setHorizontal(!layout.isHorizontal());
			contents.revalidate();
			shell.layout(true);
		}
	});
	{
		Group major = new Group(composite, 0);
		major.setLayout(new FillLayout(SWT.VERTICAL));
		major.setText("Major Axis");
		
		Button left = new Button(major, SWT.RADIO);
		left.setText("Top/Left");
		left.setSelection(true);
		left.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				layout.setMajorAlignment(FlowLayout.ALIGN_LEFTTOP);
				contents.revalidate();
			}
		});
	
		Button center = new Button(major, SWT.RADIO);
		center.setText("Middle/Center");
		center.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				layout.setMajorAlignment(FlowLayout.ALIGN_CENTER);
				contents.revalidate();
			}
		});
	
		Button right = new Button(major, SWT.RADIO);
		right.setText("Buttom/Right");
		right.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				layout.setMajorAlignment(FlowLayout.ALIGN_RIGHTBOTTOM);
				contents.revalidate();
			}
		});
		
		final Scale spacing = new Scale(major, 0);
		spacing.setMinimum(0);
		spacing.setMaximum(20);
		spacing.setSelection(5);
		spacing.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				layout.setMajorSpacing(spacing.getSelection());
				contents.revalidate();
			}
		});
	}

	{

		Group minor = new Group(composite, 0);
		minor.setLayout(new FillLayout(SWT.VERTICAL));
		minor.setText("minor axis");
	
		Button left = new Button(minor, SWT.RADIO);
		left.setText("Top/Left");
		left.setSelection(true);
		left.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				layout.setMinorAlignment(FlowLayout.ALIGN_LEFTTOP);
				contents.revalidate();
			}
		});

		Button center = new Button(minor, SWT.RADIO);
		center.setText("Middle/Center");
		center.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				layout.setMinorAlignment(FlowLayout.ALIGN_CENTER);
				contents.revalidate();
			}
		});

		Button right = new Button(minor, SWT.RADIO);
		right.setText("Buttom/Right");
		right.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				layout.setMinorAlignment(FlowLayout.ALIGN_RIGHTBOTTOM);
				contents.revalidate();
			}
		});
	
		final Scale spacing = new Scale(minor, 0);
		spacing.setMinimum(0);
		spacing.setMaximum(20);
		spacing.setSelection(5);
		spacing.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				layout.setMinorSpacing(spacing.getSelection());
				contents.revalidate();
			}
		});	
	}
}

}
