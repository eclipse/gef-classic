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

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Ellipse;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FlowLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.RoundedRectangle;
import org.eclipse.draw2d.Shape;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.examples.AbstractExample;

/**
 * @author hudsonr
 * Created on Apr 30, 2003
 */
public class ToolbarLayoutExample extends AbstractExample {

ToolbarLayout layout;

Shape ellipse, rect, roundRect, ellipse2, rect2;

public static void main(String[] args) {
	new ToolbarLayoutExample().run();
}

/**
 * @see org.eclipse.draw2d.examples.AbstractExample#getContents()
 */
protected IFigure getContents() {
	Figure container = new Figure();
	container.setBorder(new LineBorder());
	container.setLayoutManager(layout = new ToolbarLayout(true));
	
	ellipse = new Ellipse();
	ellipse.setBackgroundColor(ColorConstants.blue);
	ellipse.setSize(60,40);
	container.add(ellipse);

	rect = new RectangleFigure();
	rect.setBackgroundColor(ColorConstants.red);
	rect.setSize(30,70);
	container.add(rect);

	roundRect = new RoundedRectangle();
	roundRect.setBackgroundColor(ColorConstants.yellow);
	roundRect.setSize(90,30);
	container.add(roundRect);

	rect2 = new RectangleFigure();
	rect2.setBackgroundColor(ColorConstants.gray);
	rect2.setSize(50,80);
	container.add(rect2);

	ellipse2 = new Ellipse();
	ellipse2.setBackgroundColor(ColorConstants.green);
	ellipse2.setSize(50,50);
	container.add(ellipse2);
	
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
			layout.setVertical(layout.isHorizontal());
			if (layout.getStretchMinorAxis())
				resetShapes();
			contents.revalidate();
			shell.layout(true);
		}
	});

	final Button stretch = new Button(composite, SWT.CHECK);
		stretch.setText("Stretch Minor Axis");
		stretch.setSelection(false);
		stretch.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				layout.setStretchMinorAxis(!layout.getStretchMinorAxis());
				resetShapes();
				contents.revalidate();
				shell.layout(true);
			}
		});
	{
		Group major = new Group(composite, 0);
		major.setLayout(new FillLayout(SWT.VERTICAL));
		major.setText("Minor Axis");
		
		Button left = new Button(major, SWT.RADIO);
		left.setText("Top/Left");
		left.setSelection(true);
		left.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				layout.setMinorAlignment(FlowLayout.ALIGN_LEFTTOP);
				contents.revalidate();
			}
		});
	
		Button center = new Button(major, SWT.RADIO);
		center.setText("Middle/Center");
		center.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				layout.setMinorAlignment(FlowLayout.ALIGN_CENTER);
				contents.revalidate();
			}
		});
	
		Button right = new Button(major, SWT.RADIO);
		right.setText("Buttom/Right");
		right.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				layout.setMinorAlignment(FlowLayout.ALIGN_RIGHTBOTTOM);
				contents.revalidate();
			}
		});
		
		final Scale spacing = new Scale(major, 0);
		spacing.setMinimum(0);
		spacing.setMaximum(20);
		spacing.setSelection(5);
		spacing.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				layout.setSpacing(spacing.getSelection());
				contents.revalidate();
			}
		});
		Label spacingLabel = new Label(major, SWT.CENTER);
		spacingLabel.setText("Spacing");

	}
}

private void resetShapes() {
	rect.setSize(30,70);
	rect2.setSize(50,80);
	roundRect.setSize(90,30);
	ellipse.setSize(60,40);
	ellipse2.setSize(50,50);
}

}
